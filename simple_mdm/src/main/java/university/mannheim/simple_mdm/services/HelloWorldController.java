package university.mannheim.simple_mdm.services;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class HelloWorldController for client communication.
 * 
 * @author Simple MDM@University Mannheim
 * @version 22.09.2015
 */
@RestController
@RequestMapping("/hello")
public class HelloWorldController {
	
	/**
	 * Method retrieveMachineUsage
	 * 
	 * @param machineNumber
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@RequestMapping(value = { "/say" }, method = { RequestMethod.GET }, params = { "name" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public String sayHello(@RequestParam("name") String name) {

		// return to client
		return "Hello " + name;
	}

}
