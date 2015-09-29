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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import university.mannheim.comp_search.JavaLexer;
import university.mannheim.comp_search.JavaParser;
import university.mannheim.comp_search.helper.ConstantsHelper;
import university.mannheim.comp_search.helper.IndexFileHelper;

/**
 * Index single text file.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class IndexFileTask implements Runnable {

	// attributes
	private File file;

	// constants
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexFileTask.class.getSimpleName());

	/**
	 * Constructor
	 * 
	 * @param file
	 */
	public IndexFileTask(File file) {
		this.file = file;
	}

	/**
	 * Method run
	 */
	@Override
	public void run() {

		// declaration
		IndexFileHelper writer = null;
		String value = "";
		
		JavaFileListener extractor = null;
		JavaParser parser = null;
		ParseTree tree = null;
		ParseTreeWalker walker = null;
	
		// initialize
		writer = new IndexFileHelper();

		// print start message
		LOGGER.info("Start processing (" + file.getName() + ")");

		try {
			// add name
			value = file.getName().toLowerCase();
			writer.addField(ConstantsHelper.FIELD_NAME, ConstantsHelper.TYPE_STRING, value);

			// add type
			value = FilenameUtils.getExtension(file.getName()).toLowerCase();
			writer.addField(ConstantsHelper.FIELD_LANGUAGE, ConstantsHelper.TYPE_STRING, value);

			// add declarations
			try {
				parser = new JavaParser(new CommonTokenStream(
						 	new JavaLexer(new ANTLRInputStream(new FileInputStream(file)))));
				tree = parser.compilationUnit();
				walker = new ParseTreeWalker();
				
				// debug: print tree
				System.out.println(tree.toStringTree(parser));
				
				// process tree
				extractor = new JavaFileListener(parser, writer);
				walker.walk(extractor, tree);
			} catch (IOException e) {
				LOGGER.error("Internal Error: I/O issue");
			}
			
			// add comments
			for(int i = 0; i < parser.getTokenStream().size(); i++) {
				if(parser.getTokenStream().get(i).getChannel() == JavaLexer.COMMENTS_CHANNEL)
					value = value + parser.getTokenStream().get(i).getText();
			}
			
			value = StringUtils.removePattern(value, "(\\*|/)");
			value = StringUtils.normalizeSpace(value);
			writer.addField(ConstantsHelper.FIELD_COMMENT, ConstantsHelper.TYPE_STRING, value);
			
			// flush to index
			writer.flush();
			
		} catch (Exception e) {
			LOGGER.error("Internal Error: I/O issue");
			return;
		}

		// print complete message
		LOGGER.info("Finished processing (" + file.getName() + ")");
	}
}
