package university.mannheim.comp_search.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import university.mannheim.comp_search.JavaLexer;
import university.mannheim.comp_search.JavaParser;

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
		JavaParser parser = null;
		ParseTree tree = null;
		ParseTreeWalker walker = null;
		Document doc = null;
		Field field = null;
		String value = "";

		// initialize
		doc = new Document();

		// print start message
		LOGGER.info("Start processing (" + file.getName() + ")");

		try {
			// add name
			value = file.getName().toLowerCase();
			field = new StringField("name", value, Field.Store.YES);
			doc.add(field);

			// add type
			value = FilenameUtils.getExtension(file.getName()).toLowerCase();
			field = new StringField("language", value, Field.Store.YES);
			doc.add(field);

			// add declarations
			try {
				parser = new JavaParser(new CommonTokenStream(
						 	new JavaLexer(new ANTLRInputStream(new FileInputStream(file)))));
				tree = parser.compilationUnit();
				walker = new ParseTreeWalker();
				
				// debug: print tree
				System.out.println(tree.toStringTree(parser));
				
				// process tree
				JavaFileListener extractor = new JavaFileListener(parser, doc);
				walker.walk(extractor, tree);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// add comments
			for(int i = 0; i < parser.getTokenStream().size(); i++) {
				if(parser.getTokenStream().get(i).getChannel() == JavaLexer.COMMENTS_CHANNEL)
					value = value + parser.getTokenStream().get(i).getText();
			}
			
			value = StringUtils.removePattern(value, "(\\*|/)");
			value = StringUtils.normalizeSpace(value);
		
			field = new TextField("comment", value, Field.Store.YES);
			doc.add(field);

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
