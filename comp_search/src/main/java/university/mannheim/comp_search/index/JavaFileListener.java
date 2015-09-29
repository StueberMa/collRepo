package university.mannheim.comp_search.index;

import java.util.List;

import university.mannheim.comp_search.JavaBaseListener;
import university.mannheim.comp_search.JavaParser;
import university.mannheim.comp_search.JavaParser.ClassOrInterfaceModifierContext;
import university.mannheim.comp_search.JavaParser.ConstructorDeclarationContext;
import university.mannheim.comp_search.JavaParser.FieldDeclarationContext;
import university.mannheim.comp_search.JavaParser.FormalParameterContext;
import university.mannheim.comp_search.JavaParser.FormalParameterListContext;
import university.mannheim.comp_search.JavaParser.FormalParametersContext;
import university.mannheim.comp_search.JavaParser.ImportDeclarationContext;
import university.mannheim.comp_search.JavaParser.InterfaceDeclarationContext;
import university.mannheim.comp_search.JavaParser.InterfaceMethodDeclarationContext;
import university.mannheim.comp_search.JavaParser.PackageDeclarationContext;
import university.mannheim.comp_search.JavaParser.QualifiedNameContext;
import university.mannheim.comp_search.JavaParser.StatementExpressionContext;
import university.mannheim.comp_search.JavaParser.TypeContext;
import university.mannheim.comp_search.JavaParser.TypeListContext;
import university.mannheim.comp_search.JavaParser.VariableDeclaratorIdContext;
import university.mannheim.comp_search.JavaParser.VariableDeclaratorsContext;
import university.mannheim.comp_search.helper.ConstantsHelper;
import university.mannheim.comp_search.helper.IndexFileHelper;

/**
 * Listener for parsed java files.
 * 
 * @author Maximilian Stüber
 * @version 21.09.2015
 */
public class JavaFileListener extends JavaBaseListener {

	// attributes
	private JavaParser parser;
	private IndexFileHelper writer;

	/**
	 * Constructor
	 * 
	 * @param parser
	 * @param writer
	 */
	public JavaFileListener(JavaParser parser, IndexFileHelper writer) {
		this.parser = parser;
		this.writer = writer;
	}

	/**
	 * Method enterClassDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {

		// declaration
		String content = "";

		// consider: modifier
		content = parser.getTokenStream().getText(
				ctx.getParent().getRuleContext(ClassOrInterfaceModifierContext.class, 0));

		// consider: identifier
		content = content + " class " + ctx.getToken(JavaParser.Identifier, 0).getText();

		// consider: extends statement
		if (ctx.getToken(JavaParser.EXTENDS, 0) != null)
			content = content + " extends " + ctx.getRuleContext(TypeContext.class, 0).getText();

		// consider: implements statement
		if (ctx.getToken(JavaParser.IMPLEMENTS, 0) != null) {
			content = content + " implements";

			for (TypeContext type : ctx.getRuleContext(TypeListContext.class, 0).getRuleContexts(TypeContext.class)) {
				content = content + " " + type.getText();
			}
		}

		// add to doc.
		writer.addField(ConstantsHelper.FIELD_DECLARATION, ConstantsHelper.TYPE_TEXT, content);
	}

	/**
	 * Method enterConstructorDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterConstructorDeclaration(ConstructorDeclarationContext ctx) {

		// declaration
		List<FormalParameterContext> params = null;
		String content = "";

		// consider: identifier
		content = ctx.getToken(JavaParser.Identifier, 0).getText();

		// consider: params
		content = content + "(";

		if (ctx.getRuleContext(FormalParametersContext.class, 0).getRuleContext(FormalParameterListContext.class, 0) != null) {
			params = ctx.getRuleContext(FormalParametersContext.class, 0)
					.getRuleContext(FormalParameterListContext.class, 0).getRuleContexts(FormalParameterContext.class);

			for (FormalParameterContext param : params) {
				content = content + param.getRuleContext(TypeContext.class, 0).getText() + " "
						+ param.getRuleContext(VariableDeclaratorIdContext.class, 0).getText() + ", ";
			}

			content = content.substring(0, content.length() - 2);
		}

		content = content + ")";

		// add to doc.
		writer.addField(ConstantsHelper.FIELD_DECLARATION, ConstantsHelper.TYPE_TEXT, content);
	}

	/**
	 * Method enterFieldDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterFieldDeclaration(FieldDeclarationContext ctx) {

		// declaration
		String content = "";

		// consider: type
		content = ctx.getRuleContext(TypeContext.class, 0).getText();

		// consider: identifier
		content = content + " " + ctx.getRuleContext(VariableDeclaratorsContext.class, 0).getText();

		// add to doc.
		writer.addField(ConstantsHelper.FIELD_DECLARATION, ConstantsHelper.TYPE_TEXT, content);
	}

	/**
	 * Method enterImportDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterImportDeclaration(ImportDeclarationContext ctx) {

		// declaration
		String content = "";

		// consider: import
		content = "import " + ctx.getRuleContext(QualifiedNameContext.class, 0).getText();

		// add to doc.
		writer.addField(ConstantsHelper.FIELD_DECLARATION, ConstantsHelper.TYPE_TEXT, content);
	}

	/**
	 * Method enterInterfaceDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx) {

		// declaration
		String content = "";

		// consider: modifier
		content = parser.getTokenStream().getText(
				ctx.getParent().getRuleContext(ClassOrInterfaceModifierContext.class, 0));

		// consider: identifier
		content = content + " interface " + ctx.getToken(JavaParser.Identifier, 0).getText();

		// consider: extends statement
		if (ctx.getToken(JavaParser.EXTENDS, 0) != null) {
			content = content + " extends " + ctx.getRuleContext(TypeContext.class, 0).getText();
		}

		// add to doc.
		writer.addField(ConstantsHelper.FIELD_DECLARATION, ConstantsHelper.TYPE_TEXT, content);
	}

	/**
	 * Method enterStatementExpression
	 * 
	 * @param ctx
	 */
	@Override
	public void enterStatementExpression(StatementExpressionContext ctx) {

		// add to doc.
		writer.addField(ConstantsHelper.FIELD_BODY, ConstantsHelper.TYPE_TEXT, ctx.getText());

	}

	/**
	 * Method enterMethodDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterInterfaceMethodDeclaration(InterfaceMethodDeclarationContext ctx) {

		// declaration
		List<FormalParameterContext> params = null;
		String content = "";

		// consider: type
		content = "void";

		if (ctx.getRuleContext(TypeContext.class, 0) != null) {
			content = ctx.getRuleContext(TypeContext.class, 0).getText();
		}

		// consider: identifier
		content = content + " " + ctx.getToken(JavaParser.Identifier, 0);

		// consider: params
		content = content + "(";

		if (ctx.getRuleContext(FormalParametersContext.class, 0).getRuleContext(FormalParameterListContext.class, 0) != null) {
			params = ctx.getRuleContext(FormalParametersContext.class, 0)
					.getRuleContext(FormalParameterListContext.class, 0).getRuleContexts(FormalParameterContext.class);

			for (FormalParameterContext param : params) {
				content = content + param.getRuleContext(TypeContext.class, 0).getText() + " "
						+ param.getRuleContext(VariableDeclaratorIdContext.class, 0).getText() + ", ";
			}

			content = content.substring(0, content.length() - 2);
		}

		content = content + ")";

		// add to doc.
		writer.addField(ConstantsHelper.FIELD_DECLARATION, ConstantsHelper.TYPE_TEXT, content);
	}

	/**
	 * Method enterMethodDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {

		// declaration
		List<FormalParameterContext> params = null;
		String content = "";

		// consaider: type
		content = "void";

		if (ctx.getRuleContext(TypeContext.class, 0) != null) {
			content = ctx.getRuleContext(TypeContext.class, 0).getText();
		}

		// consider: identifier
		content = content + " " + ctx.getToken(JavaParser.Identifier, 0);

		// consider: params
		content = content + "(";

		if (ctx.getRuleContext(FormalParametersContext.class, 0).getRuleContext(FormalParameterListContext.class, 0) != null) {
			params = ctx.getRuleContext(FormalParametersContext.class, 0)
					.getRuleContext(FormalParameterListContext.class, 0).getRuleContexts(FormalParameterContext.class);

			for (FormalParameterContext param : params) {
				content = content + param.getRuleContext(TypeContext.class, 0).getText() + " "
						+ param.getRuleContext(VariableDeclaratorIdContext.class, 0).getText() + ", ";
			}

			content = content.substring(0, content.length() - 2);
		}

		content = content + ")";

		// add to doc.
		writer.addField(ConstantsHelper.FIELD_DECLARATION, ConstantsHelper.TYPE_TEXT, content);
	}

	/**
	 * Method enterPackageDeclaration
	 * 
	 * @param ctx
	 */
	@Override
	public void enterPackageDeclaration(PackageDeclarationContext ctx) {

		// declaration
		String content = "";

		// ensure: package declaration
		if (ctx.getRuleContext(QualifiedNameContext.class, 0) == null)
			return;

		// consider: package
		content = "package " + ctx.getRuleContext(QualifiedNameContext.class, 0).getText();

		// add to doc.
		writer.addField(ConstantsHelper.FIELD_DECLARATION, ConstantsHelper.TYPE_TEXT, content);
	}
}
