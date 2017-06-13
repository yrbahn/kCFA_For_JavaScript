import kcfa.KCFA;
import kcfa.State;
import syntaxtree.Program;
import java.util.Set;
import java.io.*;

import org.mozilla.javascript.ast.*;

import javascriptparser.ParserForJavaScript;
import javascriptparser.GenIJSVistor;


public class Main{
	public static void main(String[] args){
		try {
			//Program prog = new IntermediateJavaScriptParser(System.in).Program();
			
			File inFile = new File("/Users/yrbahn/Documents/workspace/test.js");
			
			// Parsing a javascript file and then getting a AST for javascript
			ParserForJavaScript parserForJavascript = new ParserForJavaScript();
			AstRoot javascriptAST = parserForJavascript.parser(inFile);
			
			// create a visitor which generates a AST for Intermediate Javascript.
			GenIJSVistor generatorIJS = new GenIJSVistor();
			javascriptAST.visit(generatorIJS);
			
			// getting a AST for IJS
			Program prog = generatorIJS.genIJSAST();

			// Starting a kCFA analysis.
			KCFA.setK(1);
			State initialState = KCFA.makeInitalState(prog);
			Set<State> setState = KCFA.explore(initialState);
			if (setState == null){
				System.err.println("Program aborts!");
			} else {
				System.out.println("=======Store summarize======");
				System.out.println(KCFA.summarize(setState));
			}
		} 
		catch (ParseException e){
			System.out.println(e);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}