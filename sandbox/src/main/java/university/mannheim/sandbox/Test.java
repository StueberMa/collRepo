package university.mannheim.sandbox;

/**
 * Class Test.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class Test {
	
	/**
	 * Main Method
	 * 
	 * @param args
	 */
	public static void main (String[] args) {
		
		// delcaration
		String dir = "";
		
		// determine current dir.
		dir = System.getProperty("user.dir");
		
		// output
		System.out.println(dir);
	}
}
