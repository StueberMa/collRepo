package university.mannheim.comp_search.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

/**
 * Class to analyze Java soruce code.
 * 
 * @author Maximilian Stüber
 * @version 29.09.2015
 */
public class JavaAnalyzer extends Analyzer {

	/**
	 * Constructor
	 */
	public JavaAnalyzer() {
		
	}

	/**
	 * Method createComponents
	 * 
	 * @param fieldName
	 * @return
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		
		// declaratiom
		Tokenizer source = null;
		TokenStream result = null;
		
		// initialize
		source = new WhitespaceTokenizer();
	    result = new LowerCaseFilter(source);
	 
		return new TokenStreamComponents(source, result);
	}
}
