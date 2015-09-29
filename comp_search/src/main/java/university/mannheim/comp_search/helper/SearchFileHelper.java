package university.mannheim.comp_search.helper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for Apache Lucene Searching.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class SearchFileHelper {

	// constants
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchFileHelper.class.getSimpleName());

	// attributes
	private QueryParser parser = null;
	private IndexSearcher searcher = null;

	/**
	 * Constants
	 */
	private SearchFileHelper() {
		
		try {
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(ConstantsHelper.INDEX_PATH))));
			parser = new QueryParser(ConstantsHelper.FIELD_DECLARATION, new PerFieldAnalyzerWrapper(new StandardAnalyzer(), ConstantsHelper.ANALYZER_PER_FIELD));
		} catch (IOException e) {
			LOGGER.error("Internal Error: I/O issue");
		}
	}

	/**
	 * Method executeQuery
	 * 
	 * @param queryString
	 * @return
	 */
	public List<Document> executeQuery(String queryString) {

		// declaration
		List<Document> results = null;
		ScoreDoc[] hits = null;
		Query query = null;

		// initialize
		results = new ArrayList<Document>();

		try {
			// parse query
			query = parser.parse(queryString);
			System.out.println("\nSearching for: " + query.toString(ConstantsHelper.FIELD_DECLARATION));
			
			// search for results
			searcher.search(query, ConstantsHelper.NUM_RESULTS);
			hits = searcher.search(query, ConstantsHelper.NUM_RESULTS).scoreDocs;

			// build results
			for (int i = 0; i < hits.length; i++) {
				results.add(searcher.doc(hits[i].doc));
			}
			
		} catch (ParseException e) {
			LOGGER.error("Internal Error: Not able to parse query");
		} catch (IOException e) {
			LOGGER.error("Internal Error: I/O issue");
		}

		return results;
	}
	
	/**
	 * Method getSearchFileHelper
	 * 
	 * @return
	 */
	public synchronized static SearchFileHelper getSearchFileHelper() {
		
		return new SearchFileHelper();
	}

}
