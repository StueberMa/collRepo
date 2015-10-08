package university.mannheim.sandbox;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Sample.
 * 
 * @author Maximilian Stüber
 * @version 06.10.2015
 */
public class Sample {

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String value = "";
		SampleParser parser = null;
		ParseTree tree = null;

		// init
		value = "30 + 10";

		// add declarations
		parser = new SampleParser(new CommonTokenStream(new SampleLexer(new ANTLRInputStream(value))));
		tree = parser.arithmeticExpr();

		// debug: print tree
		System.out.println(tree.toStringTree(parser));

	}

}
