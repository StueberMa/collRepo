package university.mannheim.comp_search.index;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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
	private static final int TIMEOUT_IN_MINUTES = 999;
	private static final Logger LOGGER = LoggerFactory.getLogger(FileCrawler.class.getSimpleName());
	private static final String INPUT_PATH = System.getProperty("user.dir") + "/../data";
	private static final String INDEX_PATH = System.getProperty("user.dir") + "/../index";
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
		Directory dir = null;
		Analyzer analyzer = null;
		IndexWriterConfig iwc = null;
		IndexWriter writer = null;
		ExecutorService executor = null;
		Runnable task = null;

		// initialize indexer
		dir = FSDirectory.open(Paths.get(INDEX_PATH));
		analyzer = new StandardAnalyzer();

		iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);

		writer = new IndexWriter(dir, iwc);

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
			task = new IndexFileTask(file, writer);
			executor.submit(task);
		}

		// terminate
		executor.shutdown();
		executor.awaitTermination(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
		writer.close();
	}
}
