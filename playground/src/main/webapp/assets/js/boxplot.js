(function() {
	
	d3.boxplot = function(id, width, height, stats, points, showLabels) {
		
		// declaration
		var padding = 20,
			midline = height / 2,
		    minVal = Infinity,
		    maxVal = -Infinity;
	    
	    // get min/max values
	    if(points) {
	    	minVal = Math.min.apply(Math, points);
	    	maxVal = Math.max.apply(Math, points);
	    }
	    
	    if(minVal > stats.lowerWhisker)
	    	minVal = stats.lowerWhisker;
	    
	    if(maxVal < stats.upperWhisker)
	    	maxVal = stats.upperWhisker;
	    
	    
		//initialize scale
		var scale = d3.scale.linear()
					   .domain([minVal, maxVal])
		               .range([padding, width - padding]);  
		
		// compute iqr size
		var iqr = scale(stats.q3Val) - scale(stats.q1Val);
		
		// get svg container
		var svg = d3.select("#" + id);
		
		// draw box plot
		// verical line for lowerWhisker
		svg.append("line")
		    .attr("x1", scale(stats.lowerWhisker))
		    .attr("x2", scale(stats.lowerWhisker))
		    .attr("y1", midline - 10)
		    .attr("y2", midline + 10);

		// vertical line for upperWhisker
		svg.append("line")  
		    .attr("x1", scale(stats.upperWhisker))
		    .attr("x2", scale(stats.upperWhisker))
		    .attr("y1", midline - 10)
		    .attr("y2", midline + 10);
		
		// horizontal line from lowerWhisker to upperWhisker
		svg.append("line")
			.attr("class", "center")
		    .attr("x1",  scale(stats.lowerWhisker))
		    .attr("x2",  scale(stats.upperWhisker))
		    .attr("y1", midline)
		    .attr("y2", midline);
		
		// rectangle for iqr
		svg.append("rect")    
		    .attr("x", scale(stats.q1Val))
		    .attr("y", midline - 10)
		    .attr("width", iqr)
		    .attr("height", 20);
		
		//draw vertical line at median
		svg.append("line")
		    .attr("x1", scale(stats.medianVal))
		    .attr("x2", scale(stats.medianVal))
		    .attr("y1", midline - 10)
		    .attr("y2", midline + 10);
		
		// draw labels
		if(showLabels) {
			
			// lower whisker
			svg.append("text")
			    .attr("x", scale(stats.lowerWhisker))
			    .attr("y", midline - 20)
			    .attr("text-anchor", "middle")
			    .text(stats.lowerWhisker);
			
			// upper whisker
			svg.append("text")
			    .attr("x", scale(stats.upperWhisker))
			    .attr("y", midline - 20)
			    .attr("text-anchor", "middle")
			    .text(stats.upperWhisker);
			
			// 1st quantile
			svg.append("text")
			    .attr("x", scale(stats.q1Val))
			    .attr("y", midline + 30)
			    .attr("text-anchor", "middle")
			    .text(stats.q1Val);
			
			// 3rd quantile
			svg.append("text")
			    .attr("x", scale(stats.q3Val))
			    .attr("y", midline + 30)
			    .attr("text-anchor", "middle")
			    .text(stats.q3Val);
			
			// median
			svg.append("text")
			    .attr("x", scale(stats.medianVal))
			    .attr("y", midline - 20)
			    .attr("text-anchor", "middle")
			    .text(stats.medianVal);
		}
		
		// draw points
		if(points) {
			
			// define tooltip
			var div = d3.select("body").append("div")	
			    .attr("class", "tooltip")				
			    .style("opacity", 0);
			
			svg.selectAll("circle")
		    	.data(points)     
		    	.enter()
		    	.append("circle")
		    	.attr("r", 6)
		    	.attr("class", function(d) {
		    		if (d < stats.lowerWhisker || d > stats.upperWhisker)
		    			return "outlier";
				    else 
				        return "point";
				     })     
		    	.attr("cy", midline) 
		    	.attr("cx", function(d) {
		    		return scale(d);   
		    	})
		    	.on("mouseover", function(d) {		
		            div.transition()		
		                .duration(200)		
		                .style("opacity", .9);		
		            div.html("value: " + d)	
		                .style("left", (d3.event.pageX + 5) + "px")		
		                .style("top", (d3.event.pageY - 30) + "px");	
		            })					
		        .on("mouseout", function(d) {		
		            div.transition()		
		                .duration(500)		
		                .style("opacity", 0);	
		        });
		}
	};
	
})();