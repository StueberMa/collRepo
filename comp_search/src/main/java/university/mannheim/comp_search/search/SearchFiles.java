package university.mannheim.comp_search.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.lucene.document.Document;

import university.mannheim.comp_search.helper.ConstantsHelper;
import university.mannheim.comp_search.helper.SearchFileHelper;

/**
 * Search for indexed files.
 * 
 * @author Maximilian Stüber
 * @version 20.09.2015
 */
public class SearchFiles {

	/**
	 * Main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// declaration
		BufferedReader in = null;
		SearchFileHelper searcher = null;
		List<Document> results = null;
		Document doc = null;
		String query = null;

		// initalize index
		System.out.println("**** Application start up ****");
		searcher = new SearchFileHelper();

		// initialize query input
		in = new BufferedReader(new InputStreamReader(System.in));

		// print infos
		System.out.println("Fields: name, language, declaration (default), body, comment");
		System.out.println("Syntax:" + "\tlanguage:\"java\" AND Vehicle\t– Complex Search\n"
				+ "\tVe??cle\t\t\t\t- Wildcard Search\n" + "\tVehikle~0.5\t\t\t– Fuzzy Searches\n"
				+ "\t\"void String[]\"~5\t\t- Proximity Searches");

		while (true) {

			// get user input
			System.out.println("\nEnter query (ENTER for exit):");
			query = in.readLine();

			// exit if no input
			if (query == null || query.equals("")) {
				System.out.println("**** Application terminated ****");
				break;
			}

			// parse query
			results = searcher.executeQuery(query);

			// output result
			System.out.println(results.size() + " total matching documents");
			
			for (int i = 0; i < results.size(); i++) {
				doc = results.get(i);
				System.out.println((i + 1) + ". " + doc.get(ConstantsHelper.FIELD_NAME));
			}
		}
	}
}
