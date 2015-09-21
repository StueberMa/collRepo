package university.mannheim.comp_search.index;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Interval;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;

import university.mannheim.comp_search.JavaBaseListener;
import university.mannheim.comp_search.JavaParser;
import university.mannheim.comp_search.JavaParser.FormalParametersContext;
import university.mannheim.comp_search.JavaParser.InterfaceDeclarationContext;
import university.mannheim.comp_search.JavaParser.InterfaceMethodDeclarationContext;
import university.mannheim.comp_search.JavaParser.TypeContext;

/**
 * Listener for parsed java files.
 * 
 * @author Maximilian Stüber
 * @version 21.09.2015
 */
public class JavaFileListener extends JavaBaseListener {

	// attributes
	private JavaParser parser;
	private Document doc;

	/**
	 * Constructor
	 * 
	 * @param parser
	 */
	public JavaFileListener(JavaParser parser, Document doc) {
		this.parser = parser;
		this.doc = doc;
	}

	/**
	 * Method enterClassDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
		addContent("class " + ctx.getToken(JavaParser.Identifier, 0).getText());
	}

	/**
	 * Method enterInterfaceDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx) {
		addContent("Interface " + ctx.Identifier().getText());
	}

	/**
	 * Method enterMethodDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {

		// declration
		TokenStream tokens = null;
		String type = "";
		String args = "";

		// initialization
		type = "void";
		tokens = parser.getTokenStream();

		// get type AND args.
		if (ctx.type() != null) {
			type = tokens.getText(ctx.getRuleContext(TypeContext.class, 0));
		}

		args = tokens.getText(ctx.getRuleContext(FormalParametersContext.class, 0));

		// add to doc.
		addContent(type + " " + ctx.getToken(JavaParser.Identifier, 0) + args);
	}

	/**
	 * Method enterMethodDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterInterfaceMethodDeclaration(InterfaceMethodDeclarationContext ctx) {
		// declration
		TokenStream tokens = null;
		String type = "";
		String args = "";

		// initialization
		type = "void";
		tokens = parser.getTokenStream();

		// get type AND args.
		if (ctx.type() != null) {
			type = tokens.getText(ctx.getRuleContext(TypeContext.class, 0));
		}

		args = tokens.getText(ctx.getRuleContext(FormalParametersContext.class, 0));

		// add to doc.
		addContent(type + " " + ctx.getToken(JavaParser.Identifier, 0) + args);
	}

	/**
	 * Method addContent
	 * 
	 * @param stream
	 * @param interval
	 */
	@SuppressWarnings("unused")
	private void addContent(TokenStream stream, Interval interval) {

		// declaration
		TextField textField = null;
		String text = "";

		// conncatenate text
		for (int i = interval.a; i < interval.b; i++) {
			text = text + " " + stream.get(i).getText();
		}
		text.trim();

		// add (actual) content
		textField = new TextField("content", text, Store.NO);
		doc.add(textField);
	}

	/**
	 * Method addContent
	 * 
	 * @param text
	 */
	private void addContent(String text) {

		// declaration
		TextField textField = null;

		// add (actual) content
		textField = new TextField("content", text, Store.NO);
		doc.add(textField);
	}
}
