package university.mannheim.comp_search.sample;

/**
 * Class Bus.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class Bus extends Object implements Vehicle{

	// attributes
	private int numOfSeats;
	private int numOfWheels;

	/**
	 * Constructor
	 * 
	 * @param numOfSeats
	 * @param numOfWheels
	 */
	public Bus(int numOfSeats, int numOfWheels) {
		this.numOfSeats = numOfSeats;
		this.numOfWheels = numOfWheels;
	}

	/**
	 * Method to retrieve the number of seats.
	 * 
	 * @return
	 */
	@Override
	public int getNumOfSeats() {
		return this.numOfSeats;
	}

	/**
	 * Method to retrieve the number of wheels.
	 * 
	 * @return
	 */
	@Override
	public int getNumOfWheels() {
		return this.numOfWheels;
	}

	/**
	 * Method to retrieve the vehicle type.
	 * 
	 * @return
	 */
	@Override
	public String getVehicleType() {
		return "Bus";
	}
}
