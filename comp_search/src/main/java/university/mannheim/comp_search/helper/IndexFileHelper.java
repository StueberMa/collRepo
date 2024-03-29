package university.mannheim.comp_search.helper;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for Apache Lucene Indexing.
 * 
 * @author Maximilian Stüber
 * @version 29.09.2015
 */
public class IndexFileHelper {

	// constants
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexFileHelper.class.getSimpleName());

	// singleton
	private static IndexWriter writer;
	private static int activeProcesses = 0;

	// attributes
	private Document doc;

	/**
	 * Constructor
	 */
	private IndexFileHelper() {

		// declaration
		IndexWriterConfig iwc = null;
		Directory dir = null;

		// initialize: document
		doc = new Document();
		
		// writer already initialized
		if (writer != null && writer.isOpen())
			return;

		// initialize: writer
		try {
			dir = FSDirectory.open(Paths.get(ConstantsHelper.INDEX_PATH));

			iwc = new IndexWriterConfig(new PerFieldAnalyzerWrapper(new StandardAnalyzer(),
					ConstantsHelper.ANALYZER_PER_FIELD));
			iwc.setOpenMode(OpenMode.CREATE);

			writer = new IndexWriter(dir, iwc);
		} catch (IOException e) {
			LOGGER.error("Internal Error: I/O issue");
		}
	}

	/**
	 * Method addField
	 * 
	 * @param name
	 * @param type
	 * @param value
	 */
	public void addField(String name, int type, String value) {

		// declaration
		Field field = null;

		// add field to doc.
		switch (type) {
			case ConstantsHelper.TYPE_STRING:
				field = new StringField(name, value, Field.Store.YES);
				doc.add(field);
				break;
	
			case ConstantsHelper.TYPE_TEXT:
				field = new TextField(name, value, Field.Store.YES);
				doc.add(field);
				break;
		}
	}
	
	/**
	 * Method getDoc
	 * 
	 * @return
	 */
	public Document getDoc() {
		return doc;
	}

	/**
	 * Method flush
	 */
	public synchronized static void flush(Document doc) {
		
		try {
			writer.addDocument(doc);
			writer.commit();
			activeProcesses--;
			
			// close writer
			if(activeProcesses == 0)
				writer.close();
			
		} catch (IOException e) {
			LOGGER.error("Internal Error: I/O issue");
		}
	}

	/**
	 * Method getIndexFileHelper
	 * 
	 * @return
	 */
	public synchronized static IndexFileHelper getIndexFileHelper() {
		
		// count active processes
		activeProcesses++;
		
		return new IndexFileHelper();
	}
}
