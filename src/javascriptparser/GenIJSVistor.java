package javascriptparser;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.*;

public class GenIJSVistor implements NodeVisitor {
	private StringBuffer out;
	private Integer tmpVariableN;
	private syntaxtree.Program prog;
	private Map<String,syntaxtree.NumberLiteral> labelMap;
	private int statementLabel = 1;
	
	public GenIJSVistor(){
		out = new StringBuffer("");
		tmpVariableN = 0;
		labelMap = new HashMap<String, syntaxtree.NumberLiteral>();
	}
	
	public syntaxtree.Identifier genTempVar(){
		tmpVariableN++;
		return new syntaxtree.Identifier("_tmpvar_"+tmpVariableN);
	}
	
	/* JavaScript -> Intermediate JavaScript Representation */
	public syntaxtree.Node genIRNode(AstNode n){
		int nType = n.getType();
		
		if (nType == Token.SCRIPT){
			prog = new syntaxtree.Program();
			syntaxtree.Body b = new syntaxtree.Body();
			syntaxtree.StmtList  stmtList = new syntaxtree.StmtList();
			syntaxtree.DeclList  declList = new syntaxtree.DeclList(syntaxtree.DeclList.VAR_LIST);
		
			Node child = n.getFirstChild();
			while(child != null){
		
				syntaxtree.Node tNode;
				tNode = genIRNode((AstNode) child);
				if ( tNode instanceof syntaxtree.Identifier){ // Declaration
					declList.addElement((syntaxtree.Identifier) tNode);
				} else if (tNode instanceof syntaxtree.NodeList){
					syntaxtree.NodeList nList = (syntaxtree.NodeList) tNode;
					syntaxtree.Node fNode = nList.elementAt(0);
					if (fNode instanceof syntaxtree.DeclList){
						declList.union((syntaxtree.DeclList) fNode);
						nList.removeElementFirst();
					}
					stmtList.union(nList);

				} else if (tNode instanceof syntaxtree.Statement) { //Statement
					syntaxtree.Statement statement = (syntaxtree.Statement) tNode;
					stmtList.addElement(statement);
				}
				child = child.getNext();
			}
			
			b.setDeclList(declList);
			b.setStmtList(stmtList);
			prog.setBody(b);
			return prog;
			
		} if (nType == Token.EXPR_RESULT) {
			ExpressionStatement eStmt = (ExpressionStatement) n;
			syntaxtree.Node node = null;
			
			AstNode eNode = eStmt.getExpression();
			node = genIRNode(eNode);			
			if (node instanceof syntaxtree.Expression){
				syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();
				assign.setLeft(genTempVar());
				assign.setRight((syntaxtree.Expression) node);
				return assign;
			} else if (node instanceof syntaxtree.NodeList) {
				syntaxtree.NodeList nList = (syntaxtree.NodeList) node;
				syntaxtree.Node tNode = nList.getLastNode();
				if (tNode instanceof syntaxtree.Expression){
					syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();
					assign.setLeft(genTempVar());
					assign.setRight((syntaxtree.Expression)tNode);
					nList.removeLastElement();
					nList.addElement(assign);
				} 
			}
				
			return node;
			
		} if (nType == Token.EXPR_VOID) {
			AstNode eNode = null;
			syntaxtree.Node tempNode;
			boolean isLabeledStmt = false;
			syntaxtree.NumberLiteral stmtLabel=null;

			if ( n instanceof LabeledStatement){
				LabeledStatement labelStmt = (LabeledStatement) n;
				stmtLabel = new syntaxtree.NumberLiteral();

				for (Label l : labelStmt.getLabels()) {
		
					labelMap.put(l.getName(), stmtLabel);
				}
				isLabeledStmt = true;
				eNode = labelStmt.getStatement();
				labelStmt.getStatement();
				
			} else {
				ExpressionStatement eStmt = (ExpressionStatement) n;
				eNode = eStmt.getExpression();
			}
			
			tempNode = genIRNode(eNode);					
			if (tempNode instanceof syntaxtree.Expression){
				syntaxtree.AssignStatement assign;
				assign = new syntaxtree.AssignStatement();
				assign.setLeft(genTempVar());
				assign.setRight((syntaxtree.Expression) tempNode);
				if (isLabeledStmt){
					stmtLabel.setValue(assign.getLabel());
				}
				return assign;
			} else if (tempNode instanceof syntaxtree.NodeList) {
				syntaxtree.NodeList nList = (syntaxtree.NodeList) tempNode;
				syntaxtree.Node tNode = nList.getLastNode();
				if (tNode instanceof syntaxtree.Expression){
					syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();
					assign.setLeft(genTempVar());
					assign.setRight((syntaxtree.Expression)tNode);
					nList.removeLastElement();
					nList.addElement(assign);
					if (isLabeledStmt){
						stmtLabel.setValue(assign.getLabel());
					}
				}
				
			}
						
			return tempNode;
			
		} else if ( nType == Token.BLOCK ) {
			syntaxtree.NodeList nList = new syntaxtree.NodeList();
			Node tNode;
			if ( n instanceof Scope){
				tNode = (Scope) n;
			} else {
				tNode = (Block) n;
			}
			for (Node child : tNode){
				syntaxtree.Node tNode2 = genIRNode((AstNode) child);
				nList.addElement(tNode2);
			}
			return nList;
			
		} else if (nType == Token.NEW){
			NewExpression ne = (NewExpression) n;
			
			syntaxtree.NewExpression newNE = new syntaxtree.NewExpression();
			syntaxtree.Node tNode = genIRNode(ne.getTarget());
			if (tNode instanceof syntaxtree.Identifier){
				newNE.setVariable((syntaxtree.Identifier) tNode);
				newNE.setExpList(convertExpList(ne.getArguments()));
				syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();
				assign.setRight(genTempVar());
				assign.setRight(newNE);
				return assign;
			} else{
				System.out.println("New Error");
			}
			
			
		} else if (nType == Token.FUNCTION){
			FunctionNode f = (FunctionNode) n;
			//syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();
			syntaxtree.Function newF = new syntaxtree.Function();
			syntaxtree.Body  body = new syntaxtree.Body();
			syntaxtree.DeclList argList = new syntaxtree.DeclList(syntaxtree.DeclList.PRAMETER_LIST);
			syntaxtree.DeclList declList = new syntaxtree.DeclList(syntaxtree.DeclList.VAR_LIST);
			syntaxtree.NodeList nList  = null;
			syntaxtree.StmtList sList = new syntaxtree.StmtList();
			
			syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();
			
			for (AstNode an : f.getParams()){
				syntaxtree.Node arg = genIRNode(an);
				if ( arg instanceof syntaxtree.Identifier){
					argList.addElement((syntaxtree.Identifier) arg);
				}
			}
			
			syntaxtree.Node tBody = genIRNode(f.getBody());
			if (tBody instanceof syntaxtree.NodeList){
				nList = (syntaxtree.NodeList) tBody;
				//sList = new syntaxtree.StmtList(nList);
				for(Iterator<syntaxtree.Node> ptr = nList.iterator();ptr.hasNext();){
					syntaxtree.Node temp = ptr.next();
					if (temp instanceof syntaxtree.DeclList){
						declList.union((syntaxtree.DeclList) temp);
						System.out.println("DDDD:"+ temp);
						//ptr.remove();
					} else if (temp instanceof syntaxtree.Statement){
						sList.addElement((syntaxtree.Statement) temp);
					}
				}
				
			} else if (tBody instanceof syntaxtree.Statement){ //Statement
				sList.addElement((syntaxtree.Statement) tBody);
			}
			
			
			body.setStmtList(sList);
			body.setDeclList(declList);
			newF.setBody(body);
			newF.setArgList(argList);
			
			AstNode fname = f.getFunctionName();
			if ( fname == null){
				assign.setLeft(genTempVar());
			} else {
				assign.setLeft((syntaxtree.Expression) genIRNode(fname));
			}
			assign.setRight(newF);			
			return assign;
			
		} else if (nType == Token.ASSIGN ){
		
			Assignment tAssign = (Assignment) n;
			//System.out.println(n);
			AstNode left;
			AstNode right;
				
			left  = tAssign.getLeft();
			right = tAssign.getRight();
			
			//System.out.println(left);
			syntaxtree.Node leftNode = genIRNode(left);
			if (leftNode instanceof syntaxtree.Expression){	
				syntaxtree.Expression leftExpr = (syntaxtree.Expression) leftNode;
				
				syntaxtree.Node rightNode = genIRNode(right);
				if (rightNode instanceof syntaxtree.Expression){
					syntaxtree.Expression rightExpr = (syntaxtree.Expression) rightNode;
					syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();

					assign.setLeft(leftExpr);
					assign.setRight(rightExpr);
					System.out.println("AAA:"+assign);
					return assign;
					
				} else if ( rightNode instanceof syntaxtree.NodeList){
					syntaxtree.NodeList  nList = (syntaxtree.NodeList) rightNode;
					syntaxtree.Node lastNode = nList.getLastNode();
					if(lastNode instanceof syntaxtree.AssignStatement){
						syntaxtree.AssignStatement assignNode = (syntaxtree.AssignStatement) lastNode;
						assignNode.setLeft(leftExpr);
						return rightNode;
					} else if ( lastNode instanceof syntaxtree.Expression){
						syntaxtree.Expression e = (syntaxtree.Expression) lastNode;
						syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();
						assign.setLeft(leftExpr);
						assign.setRight(e);
						nList.removeLastElement();
						nList.addElement(assign);
						return nList;
					} else {
						System.err.println("Error! #1");
					}
				} else if ( rightNode instanceof syntaxtree.AssignStatement) {
					syntaxtree.AssignStatement assignNode = (syntaxtree.AssignStatement) rightNode;
					assignNode.setLeft(leftExpr);
					return rightNode;
				} else {
					System.err.println("!!Error");
				}
				
			} else {
				System.err.println("Error!: Should be an expression!");
			}
			
		} else if (nType == Token.ASSIGN_ADD){ // a += b; -> a = a + b;
			Assignment tAssign = (Assignment) n;
			
			AstNode left  = tAssign.getLeft();
			AstNode right = tAssign.getRight();
			syntaxtree.BinaryOperator binOperation = new syntaxtree.BinaryOperator();
			binOperation.setOp(syntaxtree.BinaryOperator.BIN_AND);
			
			syntaxtree.Node leftNode = genIRNode(left);
			if (leftNode instanceof syntaxtree.Expression){	
				syntaxtree.Expression leftExpr = (syntaxtree.Expression) leftNode;
				
				syntaxtree.Node rightNode = genIRNode(right);
				if (rightNode instanceof syntaxtree.Expression){
					syntaxtree.Expression rightExpr = (syntaxtree.Expression) rightNode;
					
					binOperation.setLeft(leftExpr);
					binOperation.setRight(rightExpr);
					syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();

					assign.setLeft(leftExpr);
					assign.setRight(binOperation);
					return assign;
					
				} else if ( rightNode instanceof syntaxtree.NodeList){
					syntaxtree.NodeList  nList = (syntaxtree.NodeList) rightNode;
					syntaxtree.Node lastNode = nList.getLastNode();
					if(lastNode instanceof syntaxtree.AssignStatement){
						syntaxtree.AssignStatement assignNode = (syntaxtree.AssignStatement) lastNode;
						binOperation.setLeft(leftExpr);
						binOperation.setRight(assignNode.getRight());
						assignNode.setLeft(leftExpr);
						assignNode.setRight(binOperation);
						return rightNode;
					} else {
						System.err.println("Error! #1");
					}
				} else if ( rightNode instanceof syntaxtree.AssignStatement) {
					syntaxtree.AssignStatement assignNode = (syntaxtree.AssignStatement) rightNode;
					binOperation.setLeft(leftExpr);
					binOperation.setRight(assignNode.getRight());
					assignNode.setRight(binOperation);
					assignNode.setLeft(leftExpr);
					return rightNode;
				} else {
					System.err.println(">>Error");
				}

				
			} else {
				System.err.println("Error!: Should be an expression!");
			}
			
		} else if (nType == Token.VAR) {
			//System.out.println(n.getClass());
			syntaxtree.NodeList nList = new syntaxtree.NodeList();
			if (n instanceof VariableDeclaration){
				VariableDeclaration var = (VariableDeclaration) n;
				VariableInitializer vInit  = null;
				syntaxtree.DeclList dList = new syntaxtree.DeclList(syntaxtree.DeclList.VAR_LIST);
			
				// Find a initialization
				for(AstNode node:var.getVariables()){
					if (((VariableInitializer) node).getInitializer() != null){
						vInit = (VariableInitializer) node;
					}
				}
				
				for(AstNode node:var.getVariables()){
					//System.out.println("debug:"+node.getClass());
					VariableInitializer tVInit = (VariableInitializer) node;
					syntaxtree.Identifier ident = (syntaxtree.Identifier) genIRNode(tVInit.getTarget());
					dList.addElement(ident);
					
					if ( vInit != null){
						//System.out.println("Not NULL");
						syntaxtree.Node tNode = genIRNode(vInit.getInitializer());
						if (tNode instanceof syntaxtree.Expression){
							syntaxtree.AssignStatement as = new syntaxtree.AssignStatement();
							as.setLeft(ident);
							as.setRight((syntaxtree.Expression) tNode);
							nList.addElement(as);
						} else if (tNode instanceof syntaxtree.NodeList){
							syntaxtree.NodeList tNList = (syntaxtree.NodeList) tNode;
							syntaxtree.Node tNode2 = tNList.getLastNode();
							if (tNode2 instanceof syntaxtree.Selection){
								syntaxtree.AssignStatement as = new syntaxtree.AssignStatement();
								as.setLeft(ident);
								as.setRight((syntaxtree.Selection) tNode2);
								tNList.removeLastElement();
								tNList.addElement(as);
								
							} else if ( tNode2 instanceof syntaxtree.AssignStatement){
								((syntaxtree.AssignStatement) tNode2).setLeft(ident);
							}
							
							nList.union(tNList);
							
						} else if (tNode instanceof syntaxtree.AssignStatement){ // Assignment
							syntaxtree.AssignStatement as2 = (syntaxtree.AssignStatement) tNode;
							as2.setLeft(ident);
							nList.addElement(as2);
						} 
					} else {
						System.out.println("NULL");
					}
				}
				nList.addElementFirst(dList);
			}
			return nList;
			
		} else if (nType == Token.CALL){
	
			FunctionCall call = (FunctionCall) n;
		
			syntaxtree.AssignStatement assign = new syntaxtree.AssignStatement();
			
			//syntaxtree.Selection sel = new syntaxtree.Selection();
			syntaxtree.CallExpression callExpr = new syntaxtree.CallExpression();
			callExpr.setExpList(convertExpList(call.getArguments()));
			
			AstNode target = call.getTarget();
			//syntaxtree.Expression tg = (syntaxtree.Expression) genIRNode(target);
			//callExpr.setTarget(tg);
			syntaxtree.Node tNode = genIRNode(target);
			if (target.getType() == Token.NAME){
				callExpr.setTarget((syntaxtree.Expression) tNode);
				callExpr.getExpList().insertElementAt(new syntaxtree.ThisLiteral(),0);
				System.out.println("D:::"+callExpr);
				assign.setLeft(genTempVar());
				assign.setRight(callExpr);	
				return assign;
			} else {
				
				if (tNode instanceof syntaxtree.Selection){	
					syntaxtree.Selection sel = (syntaxtree.Selection) tNode;
					callExpr.setTarget(sel.getElement());
					callExpr.getExpList().insertElementAt(sel.getTarget(),0);
					assign.setLeft(genTempVar());
					assign.setRight(callExpr);	
					return assign;
				
				} else if ( tNode instanceof syntaxtree.NodeList){
					
					syntaxtree.NodeList nList = (syntaxtree.NodeList) tNode;
					syntaxtree.Node tNode2 = nList.getLastNode();
					if (tNode2 instanceof syntaxtree.Selection){
						syntaxtree.Selection sel = (syntaxtree.Selection) tNode2;
						callExpr.setTarget(sel.getElement());
						callExpr.getExpList().insertElementAt(sel.getTarget(), 0);
						nList.removeLastElement();
						assign.setLeft(genTempVar());
						assign.setRight(callExpr);
					} else if (tNode2 instanceof syntaxtree.AssignStatement){
		
						callExpr.setTarget(((syntaxtree.AssignStatement) tNode2).getLeft());
						assign.setLeft(genTempVar());
						assign.setRight(callExpr);
					}
				
					nList.addElement(assign);
			
					return nList;
				
				} else if ( tNode instanceof syntaxtree.AssignStatement){ // Assignment
					syntaxtree.NodeList nList = new syntaxtree.NodeList();
					nList.addElement(tNode);
					
					callExpr.setTarget(((syntaxtree.AssignStatement) tNode).getLeft());
					assign.setLeft(genTempVar());
					assign.setRight(callExpr);
				
					nList.addElement(assign);
				
					return nList;
				}
			}
			
		} else if (nType == Token.GETELEM) { // NEED TO MODIFY
			ElementGet eg = (ElementGet) n;
			AstNode target = eg.getTarget();
			AstNode ele    = eg.getElement();
		
			syntaxtree.Selection sel = new syntaxtree.Selection();
			sel.set((syntaxtree.Expression) genIRNode(target), (syntaxtree.Expression) genIRNode(ele)); 
			
			return sel;
			
		} else if (nType == Token.GETPROP){
			System.out.println(n);
			PropertyGet pg = (PropertyGet) n;
			syntaxtree.Expression e1= null;
			syntaxtree.Expression e2= null;
			syntaxtree.NodeList nList = null;
			
			AstNode target = pg.getTarget();
			AstNode property = pg.getProperty();
			
			syntaxtree.Selection sel = new syntaxtree.Selection();
			syntaxtree.Node tNode = genIRNode(target);
			if (tNode instanceof syntaxtree.Expression){
				syntaxtree.Node propertyNode = genIRNode(property);
				if (propertyNode instanceof syntaxtree.Expression){
					e1 = (syntaxtree.Expression) tNode;
					e2 = (syntaxtree.Expression) propertyNode;
					sel.set(e1,e2);
					return sel;
				} else {
					System.err.println("ERROR GETPROP#!");
				}
				
			} else if (tNode instanceof syntaxtree.NodeList){
				nList = (syntaxtree.NodeList) tNode;
				//System.out.println(node);
				syntaxtree.Node tLNode = nList.getLastNode();
				if (tLNode instanceof syntaxtree.Selection) {
					e1 = (syntaxtree.Selection) tNode;
					nList.removeLastElement();
						
				} else if (tNode instanceof syntaxtree.AssignStatement){
					syntaxtree.AssignStatement assign = (syntaxtree.AssignStatement) tNode;
					e1 = assign.getLeft();
				} else {
						System.err.println("Error GETPROP");
				}
			
			} else if (tNode instanceof syntaxtree.AssignStatement){ // Assignment
				syntaxtree.AssignStatement assign = (syntaxtree.AssignStatement) tNode;
				e1 = assign.getLeft();
				nList = new syntaxtree.NodeList();
				nList.addElement(assign);
			} else {
				System.err.println("Error2 GETPROP!");
			}
		
			syntaxtree.Node propertyNode = genIRNode(property);
			if (propertyNode instanceof syntaxtree.Expression)
			{			
				e2 = (syntaxtree.Expression) genIRNode(property);
				sel.set(e1,e2);
				nList.addElement(sel);		
				return nList;
		
			} else{
				System.err.println("ERROR GETPROP");
			}		
			
		} else if (nType == Token.IF) {
			IfStatement ifStmt = (IfStatement) n;
			syntaxtree.NodeList nList = null;
			syntaxtree.IfStatement newIfStmt = new syntaxtree.IfStatement();
			AstNode cond = ifStmt.getCondition();
			syntaxtree.Node temp;
			
			if (cond != null){
				syntaxtree.Expression condition = (syntaxtree.Expression) genIRNode(cond);
				newIfStmt.setCondition(new syntaxtree.UnaryOperator(syntaxtree.UnaryOperator.UNARY_NOT, condition));
			} else{
				System.out.println("IF] No cond");
			}
			
			AstNode thenPart  = ifStmt.getThenPart();
			if (thenPart != null){
				syntaxtree.Node tNode = genIRNode(ifStmt.getThenPart());
				if (tNode instanceof syntaxtree.NodeList){
					nList = (syntaxtree.NodeList) tNode;
				} else if (tNode instanceof syntaxtree.Statement){
					nList = new syntaxtree.NodeList();
					nList.addElement(tNode);
				} else if (tNode instanceof syntaxtree.Expression){
					System.out.println("IF] then expression");
				}
			} else {
				System.out.println("IF] ThenPart No");
			}
			
			AstNode elsePart = ifStmt.getElsePart();
			if (elsePart != null ){
			/*	if (elsePart instanceof IfStatement){
					syntaxtree.Node newElsePart= genIRNode(elsePart);
					syntaxtree.Statement lastStmt = (syntaxtree.Statement) nList.getLastNode(); 
					nList.addElement(newElsePart);
					//temp = nList.getLastNode();
					newIfStmt.setIfLabel(lastStmt.getLabel()+1);
				} else { //Else*/
				
				syntaxtree.GotoStatement gotoStmt = new syntaxtree.GotoStatement();
				syntaxtree.Node newElsePart= genIRNode(elsePart);
				nList.addElement(gotoStmt);
				newIfStmt.setIfLabel(gotoStmt.getLabel()+1);
				nList.addElement(newElsePart);						
				syntaxtree.Statement empty = new syntaxtree.EmptyStatement();
				nList.addElement(empty);
				gotoStmt.setGotoLabel(empty.getLabel()+1);
				
				
			} else {
				//System.out.println("NO ELSE");
				if (nList == null){
					nList = new syntaxtree.NodeList();
				} 
				syntaxtree.Statement empty = new syntaxtree.EmptyStatement();
				nList.addElement(empty);
				newIfStmt.setIfLabel(empty.getLabel());
			}
			
			if (nList != null ){
				nList.addElementFirst(newIfStmt);
				return nList;
			}
			
			return newIfStmt;
			
		} else if (nType == Token.WHILE){
			WhileLoop wl = (WhileLoop) n;
			syntaxtree.NumberLiteral breakLabel = null;
			syntaxtree.IfStatement ifStmt = new syntaxtree.IfStatement();
			syntaxtree.Node conditional = genIRNode(wl.getCondition());
			if ( conditional instanceof syntaxtree.Expression  ){
				syntaxtree.UnaryOperator notConditional = new syntaxtree.UnaryOperator
											(syntaxtree.UnaryOperator.UNARY_NOT, (syntaxtree.Expression) conditional);
					
				ifStmt.setCondition(notConditional);
			}
			
			syntaxtree.NodeList nList = null;
			syntaxtree.Node tBody = genIRNode(wl.getBody());
			if (tBody instanceof syntaxtree.NodeList){
				for(syntaxtree.Node tNode:(syntaxtree.NodeList) tBody){
					if (tNode instanceof syntaxtree.GotoStatement ){// Check BREAK, CONTINUE 
						syntaxtree.GotoStatement gotoStmt = (syntaxtree.GotoStatement) tNode;
						if (gotoStmt.getGotoLabel() == -1){
							if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.BREAK){
								breakLabel = new syntaxtree.NumberLiteral();
								gotoStmt.setGotoLabel(breakLabel);
							} else if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.CONTINUE){
								gotoStmt.setGotoLabel(ifStmt.getLabel());				
							}
						}
					}
				}
				nList = (syntaxtree.NodeList) tBody;
			} else if (tBody instanceof syntaxtree.GotoStatement){ // Check BREAK, CONTINUE 
				syntaxtree.GotoStatement gotoStmt = (syntaxtree.GotoStatement) tBody;
				if (gotoStmt.getGotoLabel() == -1){
					if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.BREAK){
						breakLabel = new syntaxtree.NumberLiteral();
						gotoStmt.setGotoLabel(breakLabel);
					} else if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.CONTINUE){
						gotoStmt.setGotoLabel(ifStmt.getLabel());				
					}
				}
			} else {
				nList = new syntaxtree.NodeList();
				nList.addElement(tBody);
			}
			
			syntaxtree.GotoStatement goTo = new syntaxtree.GotoStatement();
			goTo.setGotoLabel(ifStmt.getLabel());
			nList.addElement(goTo);
			syntaxtree.Statement emptyStmt = new syntaxtree.EmptyStatement();
			nList.addElement(emptyStmt);
			ifStmt.setIfLabel(emptyStmt.getLabel());
			nList.addElementFirst(ifStmt);
			
			if (breakLabel != null)
				breakLabel.setValue(emptyStmt.getLabel());
			
			return nList;
			
		} else if (nType == Token.DO){
			DoLoop wl = (DoLoop) n;
			syntaxtree.NumberLiteral breakLabel= null;
			syntaxtree.NodeList nList = null;
			syntaxtree.Node tBody = genIRNode(wl.getBody());
			if (tBody instanceof syntaxtree.NodeList){
				nList = (syntaxtree.NodeList) tBody;
			} else {
				nList = new syntaxtree.NodeList();
				nList.addElement(tBody);
			}
			
			syntaxtree.IfStatement ifStmt = new syntaxtree.IfStatement();
			nList.addElement(ifStmt);
			
			syntaxtree.Node conditional = genIRNode(wl.getCondition());
			if ( conditional instanceof syntaxtree.Expression  ){
				syntaxtree.UnaryOperator notConditional = new syntaxtree.UnaryOperator
											(syntaxtree.UnaryOperator.UNARY_NOT, (syntaxtree.Expression) conditional);
					
				ifStmt.setCondition(notConditional);
			}
			
			tBody = genIRNode(wl.getBody());
			if (tBody instanceof syntaxtree.NodeList){
				for(syntaxtree.Node tNode:(syntaxtree.NodeList) tBody){
					if (tNode instanceof syntaxtree.GotoStatement ){// Check BREAK, CONTINUE 
						syntaxtree.GotoStatement gotoStmt = (syntaxtree.GotoStatement) tNode;
						if (gotoStmt.getGotoLabel() == -1){
							if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.BREAK){
								breakLabel = new syntaxtree.NumberLiteral();
								gotoStmt.setGotoLabel(breakLabel);
							} else if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.CONTINUE){
								gotoStmt.setGotoLabel(ifStmt.getLabel());				
							}
						}
					}
					nList.addElement(tNode);
				}
			} else if (tBody instanceof syntaxtree.GotoStatement){ // Check BREAK, CONTINUE 
				syntaxtree.GotoStatement gotoStmt = (syntaxtree.GotoStatement) tBody;
				if (gotoStmt.getGotoLabel() == -1){
					if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.BREAK){
						breakLabel = new syntaxtree.NumberLiteral();
						gotoStmt.setGotoLabel(breakLabel);
					} else if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.CONTINUE){
						gotoStmt.setGotoLabel(ifStmt.getLabel());				
					}
				}
			} else if (tBody instanceof syntaxtree.Statement) {
				nList = new syntaxtree.NodeList();
				nList.addElement(tBody);
			}
		
			syntaxtree.GotoStatement goTo = new syntaxtree.GotoStatement();
			goTo.setGotoLabel(ifStmt.getLabel());
			nList.addElement(goTo);
			syntaxtree.Statement emptyStmt = new syntaxtree.EmptyStatement();
			nList.addElement(emptyStmt);
			ifStmt.setIfLabel(emptyStmt.getLabel());
			
			return nList;
		} else if (nType  == Token.SWITCH ){
			SwitchStatement ss = (SwitchStatement) n;
			syntaxtree.NodeList nList = new syntaxtree.NodeList();
			syntaxtree.NumberLiteral lastLabel = new syntaxtree.NumberLiteral();
			syntaxtree.Node temp;
			
			syntaxtree.Node expr = genIRNode(ss.getExpression());
			if (expr instanceof syntaxtree.Expression){
				syntaxtree.Expression sExpr = (syntaxtree.Expression) expr;
				for( SwitchCase sc : ss.getCases()){
					AstNode ex = sc.getExpression();
					syntaxtree.Node tNode = genIRNode(ex);
					if ( tNode instanceof  syntaxtree.Expression ){
						syntaxtree.Expression caseExpr = (syntaxtree.Expression) tNode;
						syntaxtree.BinaryOperator bo = new syntaxtree.BinaryOperator(syntaxtree.BinaryOperator.BIN_NEQ, sExpr, caseExpr);
						syntaxtree.IfStatement ifStmt = new syntaxtree.IfStatement();
						ifStmt.setCondition(bo);
						
						nList.addElement(ifStmt);
						for( AstNode stmt : sc.getStatements() ){
							syntaxtree.Statement s = (syntaxtree.Statement) genIRNode(stmt);
							if (stmt.getType() == Token.BREAK){ // Break;
								syntaxtree.GotoStatement goTo = (syntaxtree.GotoStatement) s;
								goTo.setGotoLabel(lastLabel);
							} 	
							nList.addElement(s);
						}
						
						temp = nList.getLastNode();
						if ( temp instanceof syntaxtree.Statement){
							ifStmt.setIfLabel(((syntaxtree.Statement)temp).getLabel()+1);
						} else {
							System.err.println("Erorr");
						}
					}
				}
				
				temp = nList.getLastNode();
				if ( temp instanceof syntaxtree.Statement){
					lastLabel.setValue(((syntaxtree.Statement)temp).getLabel()+1);
				} else {
					System.err.println("Erorr");
				}

			}
			
			return nList;
			
		} else if (nType == Token.FOR){
			if ( n instanceof ForInLoop){
				System.out.println("Not yet implemented..");
	
			} else if ( n instanceof ForLoop){
				ForLoop fl = (ForLoop) n;
				syntaxtree.NumberLiteral breakLabel = null;
				
				syntaxtree.NodeList nList = new syntaxtree.NodeList();
				nList.addElement(genIRNode(fl.getInitializer()));

				syntaxtree.IfStatement ifStmt = new syntaxtree.IfStatement();
				syntaxtree.Node conditional = genIRNode(fl.getCondition());
				if ( conditional instanceof syntaxtree.Expression  ){
					syntaxtree.UnaryOperator notConditional = new syntaxtree.UnaryOperator
												(syntaxtree.UnaryOperator.UNARY_NOT, (syntaxtree.Expression) conditional);
						
					ifStmt.setCondition(notConditional);
				}
				
				
				nList.addElement(ifStmt);
				
				syntaxtree.Node tNode = genIRNode(fl.getBody());
				//System.out.println("D: "+tNode);
				if (tNode instanceof syntaxtree.GotoStatement){ // Check BREAK, CONTINUE 
					syntaxtree.GotoStatement gotoStmt = (syntaxtree.GotoStatement) tNode;
					if (gotoStmt.getGotoLabel() == -1){
						if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.BREAK){
							breakLabel = new syntaxtree.NumberLiteral();
							gotoStmt.setGotoLabel(breakLabel);
						} else if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.CONTINUE){
							gotoStmt.setGotoLabel(ifStmt.getLabel());				
						}
					}
				} else if (tNode instanceof syntaxtree.NodeList) {
					for(syntaxtree.Node tNode2: (syntaxtree.NodeList)tNode){
						if (tNode2 instanceof syntaxtree.GotoStatement ){// Check BREAK, CONTINUE 
							syntaxtree.GotoStatement gotoStmt = (syntaxtree.GotoStatement) tNode2;
							if (gotoStmt.getGotoLabel() == -1){
								if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.BREAK){
									breakLabel = new syntaxtree.NumberLiteral();
									gotoStmt.setGotoLabel(breakLabel);
								} else if (gotoStmt.getGotoType() == syntaxtree.GotoStatement.CONTINUE){
									gotoStmt.setGotoLabel(ifStmt.getLabel());				
								}
							}
						}							
					}
					nList.union((syntaxtree.NodeList) tNode);
				} else { 
					nList.addElement(tNode);
				}
				
				syntaxtree.Statement incStmt = null;
				syntaxtree.Node incNode = genIRNode(fl.getIncrement());
				if (incNode instanceof syntaxtree.UnaryOperator){
					syntaxtree.UnaryOperator unary = (syntaxtree.UnaryOperator) incNode;
					incStmt = new syntaxtree.AssignStatement(unary.getExpr(),unary);
				
				} else if (incNode instanceof syntaxtree.Statement) {
					incStmt = (syntaxtree.Statement) incNode;
				}
				
				nList.addElement(incStmt);
				
				
				syntaxtree.GotoStatement goTo = new syntaxtree.GotoStatement();
				goTo.setGotoLabel(ifStmt.getLabel());
				nList.addElement(goTo);
				syntaxtree.Statement emptyStmt = new syntaxtree.EmptyStatement();
				nList.addElement(emptyStmt);
				ifStmt.setIfLabel(emptyStmt.getLabel());
				if (breakLabel != null)
					breakLabel.setValue(emptyStmt.getLabel());		
				return nList;
			}
				
		} else if (nType == Token.TRY){
			TryStatement tryStmt = (TryStatement) n;
			syntaxtree.TryStatement nTryStmt = new syntaxtree.TryStatement();
			syntaxtree.Node tNode = genIRNode(tryStmt.getTryBlock());
			//nTryStmt.setTryBlock((syntaxtree.StmtList) tNode);
			//...
			
			return nTryStmt;
			
		} else if (nType == Token.RETURN){
			ReturnStatement rS = (ReturnStatement) n;
			syntaxtree.ReturnStatement newRS = new syntaxtree.ReturnStatement();
			newRS.setExpr((syntaxtree.Expression) genIRNode(rS.getReturnValue()));
			return newRS;

		} else if (nType == Token.INC || nType == Token.DEC || nType == Token.NEG){
			UnaryExpression uExpr = (UnaryExpression) n;
			syntaxtree.UnaryOperator unary = new syntaxtree.UnaryOperator();
			switch(nType){
				case Token.INC:
					unary.setOP(syntaxtree.UnaryOperator.UNARY_INC);
					break;
				case Token.DEC:
					unary.setOP(syntaxtree.UnaryOperator.UNARY_DEC);
					break;
				case Token.NEG:
					unary.setOP(syntaxtree.UnaryOperator.UNARY_NEG);
					break;
			}
			
			unary.setIsPostfix(uExpr.isPostfix());
			syntaxtree.Node tNode = genIRNode(uExpr.getOperand());
			if (tNode instanceof syntaxtree.Expression){
				unary.setExpr((syntaxtree.Expression) tNode);
				return unary;
			} else if (tNode instanceof syntaxtree.NodeList){
				syntaxtree.NodeList nList = (syntaxtree.NodeList) tNode;
				syntaxtree.Node t2Node = nList.getLastNode();
				if (t2Node instanceof syntaxtree.AssignStatement){
					unary.setExpr(((syntaxtree.AssignStatement) t2Node).getLeft());
					nList.addElement(unary);
				} else if (t2Node instanceof syntaxtree.Expression){
					unary.setExpr((syntaxtree.Expression) t2Node);
					nList.removeLastElement();
					nList.addElement(unary);
				}
				return nList;
			} else if ( tNode instanceof syntaxtree.AssignStatement){
				System.err.println("EEE1");
			} else {
				System.err.println("EEE");
			}
			
			return unary;
	
		} else if (nType >= Token.BITOR && nType <= Token.MOD || nType == Token.ADD || nType == Token.OR){ //InfixExpression
	
			InfixExpression infix = (InfixExpression) n;
			syntaxtree.BinaryOperator bo = new syntaxtree.BinaryOperator();
			
			switch(nType){
				case Token.BITOR:
					bo.setOp(syntaxtree.BinaryOperator.BIN_BITOR);
					break;
				case Token.BITXOR:
					bo.setOp(syntaxtree.BinaryOperator.BIN_BITXOR);
					break;
				case Token.BITAND:
					bo.setOp(syntaxtree.BinaryOperator.BIN_BITAND);
					break;
				case Token.EQ:
					bo.setOp(syntaxtree.BinaryOperator.BIN_EQ);
					break;
				case Token.NE:
					bo.setOp(syntaxtree.BinaryOperator.BIN_NEQ);
					break;
				case Token.LT:
					bo.setOp(syntaxtree.BinaryOperator.BIN_LT);
					break;
				case Token.LE:
					bo.setOp(syntaxtree.BinaryOperator.BIN_LE);
					break;
				case Token.GT:
					bo.setOp(syntaxtree.BinaryOperator.BIN_GT);
					break;
				case Token.GE:
					bo.setOp(syntaxtree.BinaryOperator.BIN_GE);
					break;
				case Token.LSH:
					bo.setOp(syntaxtree.BinaryOperator.BIN_LSH);
					break;
				case Token.RSH:
					bo.setOp(syntaxtree.BinaryOperator.BIN_RSH);
					break;
				case Token.URSH:
					bo.setOp(syntaxtree.BinaryOperator.BIN_URSH);
					break;
				case Token.ADD:
					bo.setOp(syntaxtree.BinaryOperator.BIN_PLUS);
					break;
				case Token.SUB:
					bo.setOp(syntaxtree.BinaryOperator.BIN_MINUS);
					break;
				case Token.MUL:
					bo.setOp(syntaxtree.BinaryOperator.BIN_TIMES);
					break;
				case Token.DIV:
					bo.setOp(syntaxtree.BinaryOperator.BIN_DIVIDE);
					break;
				case Token.MOD:
					bo.setOp(syntaxtree.BinaryOperator.BIN_MOD);
					break;
				case Token.AND:
					bo.setOp(syntaxtree.BinaryOperator.BIN_AND);
					break;
				case Token.OR:
					bo.setOp(syntaxtree.BinaryOperator.BIN_OR);
					break;
			}
			
			syntaxtree.Node left   = genIRNode(infix.getLeft());
			syntaxtree.Node right  = genIRNode(infix.getRight());
			if ( left instanceof syntaxtree.Expression && right instanceof syntaxtree.Expression){
				bo.setLeft((syntaxtree.Expression) left);
				bo.setRight((syntaxtree.Expression) right);
				return bo;
			}
			
			// Left Part
			syntaxtree.NodeList nList=null;
			if ( left instanceof syntaxtree.NodeList){
				nList = (syntaxtree.NodeList) left;
				syntaxtree.Node tLeft = nList.getLastNode();
				if ( tLeft instanceof syntaxtree.Expression){
					syntaxtree.Expression tExpr = (syntaxtree.Expression) tLeft;
					bo.setLeft(tExpr);
					nList.removeLastElement();
					nList.addElement(bo);
				} else if (tLeft instanceof syntaxtree.AssignStatement){
					syntaxtree.AssignStatement assign = (syntaxtree.AssignStatement) tLeft;
					bo.setLeft(assign.getLeft());
					nList.addElement(assign);
					nList.addElement(bo);
					System.out.println(nList);
				} else {
					System.out.println("??Error");
				}
				
			} else if (left instanceof syntaxtree.AssignStatement){
				nList = new syntaxtree.NodeList();
				nList.addElement(left);
				bo.setLeft(((syntaxtree.AssignStatement) left).getLeft());
			} else if (left instanceof syntaxtree.Expression){
				bo.setLeft((syntaxtree.Expression) left);			
			}
			
			// Right Part
			if ( right instanceof syntaxtree.NodeList){
				syntaxtree.NodeList nList2 = (syntaxtree.NodeList) right;
				if ( nList != null ) 
					nList.union(nList2);
				else 
					nList = nList2;
				
				syntaxtree.Node tRight = nList.getLastNode();
				if ( tRight instanceof syntaxtree.Expression){
					syntaxtree.Expression tExpr = (syntaxtree.Expression) tRight;
					bo.setRight(tExpr);
					nList.removeLastElement();
					nList.addElement(bo);
				} else if (tRight instanceof syntaxtree.AssignStatement){
					syntaxtree.AssignStatement assign = (syntaxtree.AssignStatement) tRight;
					bo.setRight(assign.getLeft());
					nList.addElement(assign);
					nList.addElement(bo);
					System.out.println(nList);
				} else {
					System.out.println("??Error");
				}
				
			} else if (right instanceof syntaxtree.AssignStatement){
				if (nList == null)
					nList = new syntaxtree.NodeList();
				nList.addElement(right);
				bo.setRight(((syntaxtree.AssignStatement) right).getLeft());
				nList.addElement(bo);
			} else if (right instanceof syntaxtree.Expression){
				bo.setRight((syntaxtree.Expression) right);	
				nList.addElement(bo);
			}
			

			return nList;
			
		} else if (nType == Token.NAME){
			Name name = (Name) n;
			return new syntaxtree.Identifier(name.getIdentifier());
		} else if (nType == Token.NUMBER){
			NumberLiteral num = (NumberLiteral) n;
			System.out.println("NUM:"+num.toSource());
			syntaxtree.NumberLiteral number = new syntaxtree.NumberLiteral(num.getValue());
			return number;
		} else if (nType == Token.STRING){
			StringLiteral str = (StringLiteral) n;
			syntaxtree.StringLiteral string = new syntaxtree.StringLiteral(str.getValue());
			return string;		
		} else  if (nType == Token.EMPTY){
			System.out.println("EMPTY");
			return new syntaxtree.EmptyStatement();
		} else if ( nType == Token.BREAK ){
			syntaxtree.GotoStatement gotoStmt = new syntaxtree.GotoStatement();
			gotoStmt.setGotoType(syntaxtree.GotoStatement.BREAK);
			return gotoStmt;
		} else if (nType == Token.NULL){
			return new syntaxtree.NullLiteral();
		} else if (nType == Token.CONTINUE){
			syntaxtree.GotoStatement gotoStmt = new syntaxtree.GotoStatement();
			gotoStmt.setGotoType(syntaxtree.GotoStatement.CONTINUE);
			return gotoStmt;	
		} else if (nType == Token.TRUE ){
			return new syntaxtree.BooleanLiteral(true);
		} else if ( nType == Token.FALSE){
			return new syntaxtree.BooleanLiteral(false);
		} else if (nType == Token.LP){
			ParenthesizedExpression pExpr = (ParenthesizedExpression) n;
			return genIRNode(pExpr.getExpression());
		} else if (nType == Token.THIS){
			return new syntaxtree.ThisLiteral();
		} else {
			System.out.println("else:" +n.getClass() + ":" + n);		
		}
		return null;
		
	}

	public boolean visit(AstNode astNode) {
		System.out.println("Generating....1");
		//System.out.println("[" + astNode.getClass()+ "]"+ astNode.toString() + ": " + astNode.toSource());
		genIRNode(astNode);
		System.out.println("Complete!");
		return false;
		
    }
		
	private <T extends AstNode> syntaxtree.ExpList convertExpList(List<T> items){
        syntaxtree.ExpList expList = new syntaxtree.ExpList();
        
        for (AstNode item : items) {
        	syntaxtree.Expression e = (syntaxtree.Expression) genIRNode(item);
        	expList.addElement(e);
        }
        
        return expList;
	}
	
	public syntaxtree.Program genIJSAST(){ // generate a AST for Intermediate JavaScript.
		System.out.println("Generating....2");
		System.out.println(prog);
		return prog;
	}
}