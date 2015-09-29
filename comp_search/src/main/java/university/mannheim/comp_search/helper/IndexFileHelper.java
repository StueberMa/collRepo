package university.mannheim.comp_search.helper;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
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
 * @version 20.09.2015
 */
public class IndexFileHelper {

	// constants
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexFileHelper.class.getSimpleName());

	// singleton
	private static IndexWriter writer;
	
	// attributes
	private Document doc;

	/**
	 * Constructor
	 */
	public IndexFileHelper() {
		doc = new Document();
		initIndexWriter();
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
		switch(type) {
			case ConstantsHelper.TYPE_STRING:
				field = new StringField(name, value, Field.Store.YES);
				doc.add(field);
				break;
				
			case ConstantsHelper.TYPE_TEXT:
				field = new TextField(name, value, Field.Store.NO);
				doc.add(field);
				break;
		}
	}
	
	/**
	 * Method flush
	 */
	public void flush() {
		try {
			writer.addDocument(doc);
		} catch (IOException e) {
			LOGGER.error("Internal Error: I/O issue");
		}
	}

	/**
	 * Method initIndexWriter
	 */
	private synchronized static void initIndexWriter() {

		// declaration
		Analyzer analyzer = null;
		IndexWriterConfig iwc = null;
		Directory dir = null;

		// already initialized
		if (writer != null)
			return;

		// initialize indexer
		try {
			dir = FSDirectory.open(Paths.get(ConstantsHelper.INDEX_PATH));
			analyzer = new StandardAnalyzer();

			iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);

			writer = new IndexWriter(dir, iwc);
		} catch (IOException e) {
			LOGGER.error("Internal Error: I/O issue");
		}
	}

}
