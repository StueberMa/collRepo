package university.mannheim.comp_search.helper;

import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

/**
 * Class for Constants.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class ConstantsHelper {

	public static final String FIELD_NAME = "name";
	public static final String FIELD_LANGUAGE = "language";
	public static final String FIELD_DECLARATION = "declaration";
	public static final String FIELD_BODY = "body";
	public static final String FIELD_COMMENT = "comment";

	public static final int TYPE_STRING = 1;
	public static final int TYPE_TEXT = 2;

	public static final int NUM_RESULTS = 50;

	public static final String INDEX_PATH = System.getProperty("user.dir") + "/../index";
	public static final HashMap<String, Analyzer> ANALYZER_PER_FIELD = new HashMap<String, Analyzer>() {
		private static final long serialVersionUID = 1L;
		{
			put(ConstantsHelper.FIELD_DECLARATION, new WhitespaceAnalyzer());
			put(ConstantsHelper.FIELD_BODY, new WhitespaceAnalyzer());
			put(ConstantsHelper.FIELD_COMMENT, new EnglishAnalyzer());
		}
	};

}
