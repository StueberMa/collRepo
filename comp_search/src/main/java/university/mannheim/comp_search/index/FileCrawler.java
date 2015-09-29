package university.mannheim.comp_search.index;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to index files in spec. directory.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class FileCrawler {

	// constants
	private static final int WORKERPOOL_SIZE = 10;
	private static final Logger LOGGER = LoggerFactory.getLogger(FileCrawler.class.getSimpleName());
	private static final String INPUT_PATH = System.getProperty("user.dir") + "/../data";
	private static final String[] SUPP_TYPES = { "java" };

	/**
	 * Main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// declaration
		File directory = null;
		File[] allFiles = null;
		ExecutorService executor = null;
		Runnable task = null;

		// get files from directory
		LOGGER.info("Scanning directory " + INPUT_PATH + " for files");
		directory = new File(INPUT_PATH);
		allFiles = directory.listFiles();

		// process operation in parallel
		executor = Executors.newFixedThreadPool(WORKERPOOL_SIZE);

		// add files to queue
		for (File file : allFiles) {

			// filter not supported types
			if (!ArrayUtils.contains(SUPP_TYPES, FilenameUtils.getExtension(file.getName())))
				continue;

			// call worker
			task = new IndexFileTask(file);
			executor.submit(task);
		}

		// terminate
		executor.shutdown();
	}
}
