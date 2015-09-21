package university.mannheim.comp_search.index;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Index single text file.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class IndexFileTask implements Runnable {

	// attributes
	private File file;
	private IndexWriter writer;

	// constants
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexFileTask.class.getSimpleName());

	/**
	 * Constructor
	 * 
	 * @param file
	 * @param writer
	 */
	public IndexFileTask(File file, IndexWriter writer) {
		this.file = file;
		this.writer = writer;
	}

	/**
	 * Method run
	 */
	@Override
	public void run() {

		// declaration
		Document doc = null;
		Field nameField = null;
		Field typeField = null;
		Field textField = null;

		// initialize
		doc = new Document();

		// print start message
		LOGGER.info("Start processing (" + file.getName() + ")");

		try {
			// add name
			nameField = new StringField("file_name", file.getName(), Field.Store.YES);
			doc.add(nameField);
			
			// add type
			typeField = new StringField("file_type", FilenameUtils.getExtension(file.getName()), Field.Store.YES);
			doc.add(typeField);
			
			// add (actual) content
			textField = new TextField("content", new FileReader(file));
			doc.add(textField);

			// add document
			writer.addDocument(doc);
		} catch (Exception e) {
			LOGGER.error("Internal Error: I/O issue");
			return;
		}

		// print complete message
		LOGGER.info("Finished processing (" + file.getName() + ")");
	}
}
