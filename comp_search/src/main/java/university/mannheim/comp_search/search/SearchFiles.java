package university.mannheim.comp_search.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 * Search for indexed files.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class SearchFiles {

	// constants
	private static final String INDEX_PATH = System.getProperty("user.dir") + "/../index";
	private static final String SEARCH_FIELD = "file_content";
	private static final int NUM_RESULTS = 50;

	/**
	 * Main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// declaration
		BufferedReader in = null;
		IndexReader reader = null;
		IndexSearcher searcher = null;
		QueryParser parser = null;
		Query query = null;
		TopDocs results = null;
		ScoreDoc[] hits = null;
		Document doc = null;

		String queryString = null;

		// initalize index
		System.out.println("**** Application start up ****");
		reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_PATH)));
		searcher = new IndexSearcher(reader);
		parser = new QueryParser(SEARCH_FIELD, new StandardAnalyzer());

		// initialize query input
		in = new BufferedReader(new InputStreamReader(System.in));
		
		// print infos
		System.out.println("Fields: file_name, file_type, file_content (default)");
		System.out.println("Syntax:" 
				+ "\tfile_type:\"java\" AND Vehicle\t– Complex Search\n"
				+ "\tVe??cle\t\t\t\t- Wildcard Search\n"
				+ "\tVehikle~0.5\t\t\t– Fuzzy Searches\n"
				+ "\t\"void String[]\"~5\t\t- Proximity Searches");

		while (true) {

			// get user input
			System.out.println("\nEnter query (ENTER for exit):");
			queryString = in.readLine();

			// exit if no input
			if (queryString == null || queryString.equals("")) {
				System.out.println("**** Application terminated ****");
				break;
			}

			// parse query
			query = parser.parse(queryString);
			System.out.println("\nSearching for: " + query.toString(SEARCH_FIELD));

			// search for results
			searcher.search(query, NUM_RESULTS);
			results = searcher.search(query, NUM_RESULTS);
			hits = results.scoreDocs;

			// print results
			System.out.println(results.totalHits + " total matching documents");

			for (int i = 0; i < hits.length; i++) {
				doc = searcher.doc(hits[i].doc);
				System.out.println((i + 1) + ". " + doc.get("file_name"));
			}
		}

		// close
		reader.close();
	}
}
