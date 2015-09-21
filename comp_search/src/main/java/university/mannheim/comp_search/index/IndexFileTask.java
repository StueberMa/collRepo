package university.mannheim.comp_search.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
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
		Document doc = null;
		Field nameField = null;
		Field typeField = null;

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

			// add actual content
			parseContent(file, doc);

			// add document
			writer.addDocument(doc);
		} catch (Exception e) {
			LOGGER.error("Internal Error: I/O issue");
			return;
		}

		// print complete message
		LOGGER.info("Finished processing (" + file.getName() + ")");
	}

	/**
	 * Method parseContent
	 * 
	 * @param file
	 * @param doc
	 */
	private void parseContent(File file, Document doc) {

		// declaration
		JavaParser parser = null;
		ParseTree tree = null;
		ParseTreeWalker walker = null;

		try {
			parser = new JavaParser(new CommonTokenStream(
					 	new JavaLexer(new ANTLRInputStream(new FileInputStream(file)))));
			tree = parser.compilationUnit();
			walker = new ParseTreeWalker();
			
			// debug: print tree
			//System.out.println(tree.toStringTree(parser));
			
			// process tree
			JavaFileListener extractor = new JavaFileListener(parser, doc);
			walker.walk(extractor, tree);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
