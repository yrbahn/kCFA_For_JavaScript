package javascriptparser;

import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.tools.ToolErrorReporter;

import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

public class ParserForJavaScript {
	private Parser p;
	
	public ParserForJavaScript(){
		CompilerEnvirons env = new CompilerEnvirons();
		Context ctx = Context.enter();
		ctx.initStandardObjects();
		ErrorReporter errors = new ToolErrorReporter(true);
		env = new CompilerEnvirons();
		env.initFromContext(ctx);
		env.setAllowMemberExprAsFunctionName(true);
		p = new Parser(env, errors);
		
	}
		
	public AstRoot parser(File inFile) throws Exception{
		FileReader scriptIn = new FileReader(inFile);
		AstRoot ast = p.parse(scriptIn, inFile.getAbsolutePath(), 1);
		return ast;
	}
	
	public static void main(String[] args){
		try {
			File inFile = null;
			inFile = new File("/Users/yrbahn/Documents/workspace/test.js");
			
			ParserForJavaScript pfj = new ParserForJavaScript();
			AstRoot tree = pfj.parser(inFile);
			GenIJSVistor v = new GenIJSVistor();
			tree.visit(v);
			v.genIJSAST();
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}