package university.mannheim.comp_search.fulltext_index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class DataAdaptor
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class DataAdaptor {
	
	// constants
	private static final int WORKERPOOL_SIZE = 10;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataAdaptor.class.getSimpleName());
	private static final String INPUT_PATH = System.getProperty("user.dir") + "/../data";
	private static final String INDEX_PATH = System.getProperty("user.dir") + "/../index";
	
}
