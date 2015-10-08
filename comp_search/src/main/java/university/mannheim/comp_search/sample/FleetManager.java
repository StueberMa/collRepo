package university.mannheim.comp_search.sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage diverse vehicles.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class FleetManager {
	
	/**
	 * Main Method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// declaration
		List<Vehicle> fleet = null;
		
		// initialization
		fleet = new ArrayList<Vehicle>();
		
		// create car fleet
		fleet.add(new Car(5, 4));
		fleet.add(new Car(4, 4));
		fleet.add(new Truck(2, 8));
		fleet.add(new Bus(52, 12));
		
		// print car fleet
		for(Vehicle v : fleet) {
			System.out.println("Seats: " + v.getNumOfSeats() + "\tWheels: " + v.getNumOfWheels() + "\tType: " + v.getVehicleType());
		}
	}
}
