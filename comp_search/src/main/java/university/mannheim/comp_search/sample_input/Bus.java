package university.mannheim.comp_search.sample_input;

/**
 * Class Bus.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class Bus implements Vehicle {

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
	 * Method getNumOfSeats
	 */
	@Override
	public int getNumOfSeats() {
		return this.numOfSeats;
	}

	/**
	 * Method getNumOfWheels
	 */
	@Override
	public int getNumOfWheels() {
		return this.numOfWheels;
	}

	/**
	 * Method getVehicleType
	 */
	@Override
	public String getVehicleType() {
		return "Bus";
	}
}