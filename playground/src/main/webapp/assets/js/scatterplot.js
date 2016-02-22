(function() {
	
	d3.scatterplot = function(id, width, heigth, points, name) {
		
		// declaration
		var margin = {top: 20, right: 20, bottom: 30, left: 40},
		    padding = 20,
		    minX = Infinity,
		    maxX = -Infinity,
		    minY = Infinity,
		    maxY = -Infinity;
	    
		// adjust width & heigth
		width = width - margin.left - margin.right;
		heigth = heigth - margin.top - margin.bottom;
		
	    // get min/max values
	    if(points) {
	    	minX = Math.min.apply(Math, points.map(function(o){return o[name.x];}));
	    	maxX = Math.max.apply(Math, points.map(function(o){return o[name.x];}));
	    	minY = Math.min.apply(Math, points.map(function(o){return o[name.y];}));
	    	maxY = Math.max.apply(Math, points.map(function(o){return o[name.y];}));
	    }
	    
	    // initialize color
	    var color = d3.scale.category10();
	    
	    //initialize scale
	    var xScale = d3.scale.linear()
	    	.domain([minX, maxX])
	    	.range([0, width]); 
	    
		var yScale = d3.scale.linear()
			.domain([minY, maxY])
			.range([heigth, 0]);  
		
		// initialize axis
		var xAxis = d3.svg.axis()
		    .scale(xScale)
		    .orient("bottom");

		var yAxis = d3.svg.axis()
		    .scale(yScale)
		    .orient("left");
		
		// get SVG container AND add graph area
		var svg = d3.select("#" + id)
			.append("g")
    		.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

		// draw axis
		svg.append("g")
	      .attr("class", "axis")
	      .attr("width", width)
	      .attr("height", heigth)
	      .attr("transform", "translate(0," + heigth + ")")
	      .call(xAxis)
	    .append("text")
	      .attr("class", "label")
	      .attr("x", width)
	      .attr("y", -6)
	      .style("text-anchor", "end")
	      .text(name.x);

		svg.append("g")
	      .attr("class", "axis")
	      .call(yAxis)
	    .append("text")
	      .attr("class", "label")
	      .attr("transform", "rotate(-90)")
	      .attr("y", 6)
	      .attr("dy", ".71em")
	      .style("text-anchor", "end")
	      .text(name.y)
	      
	    // define tooltip
	    var div = d3.select("#tooltip");
	    
		// draw points
	    svg.selectAll(".dot")
	      .data(points)
	    .enter().append("circle")
	      .attr("class", "dot")
	      .attr("r", 3.5)
	      .attr("cx", function(d) { return xScale(d[name.x]); })
	      .attr("cy", function(d) { return yScale(d[name.y]); })
	      .style("fill", function(d) { if(name.color) return color(d[name.color]); return "black"; })
	      .on("mouseover", function(d) {		
	    	  div.transition()		
                .duration(200)		
                .style("opacity", .9);		
	    	  div.html("x: " + d[name.x] + "<br>" + "y: " + d[name.y])
                .style("left", (d3.event.pageX + 5) + "px")		
                .style("top", (d3.event.pageY - 30) + "px");	
            })					
        .on("mouseout", function(d) {		
            div.transition()		
                .duration(500)		
                .style("opacity", 0);	
        });;	     
	      
	    // draw legend
		var legend = svg.selectAll(".legend")
	      .data(color.domain())
	      .enter().append("g")
	      .attr("class", "legend")
	      .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });

		legend.append("rect")
	      .attr("x", width - 18)
	      .attr("width", 18)
	      .attr("height", 18)
	      .style("fill", color);

		legend.append("text")
	      .attr("x", width - 24)
	      .attr("y", 9)
	      .attr("dy", ".35em")
	      .style("text-anchor", "end")
	      .text(function(d) { return d; });
	};
	
})();