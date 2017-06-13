package visitor;

import syntaxtree.*;

public class PrintVisitor implements Visitor {

	@Override
	public void visit(Program n) {
		// TODO Auto-generated method stub
		System.out.println("Program");System.out.flush();
		n.getBody().accept(this);
		
	}

	@Override
	public void visit(Body n) {
		System.out.println("Body Begin");System.out.flush();
		n.getDeclList().accept(this);
		n.getStmtList().accept(this);
		System.out.println("Body End");System.out.flush();
	}

	public void visit(DeclList n){
		System.out.print("VarList:");System.out.flush();
		for(int i=0; i < n.size(); i++){
			System.out.print(n.elementAt(i));
			System.out.print(", ");
		}
		System.out.println("");
	}
	
	public void visit(StmtList n){
		for(int i=0; i < n.size(); i++){
			n.elementAt(i).accept(this);
		}
	}
	
	public void visit(ExpList n){
		System.out.print("(");
		for(int i=0; i < n.size(); i++){
			n.elementAt(i).accept(this);
			System.out.print(",");
		}
		System.out.print(")");

	}
	
	public void visit(EmptyStatement n){
		
	}
	
	@Override
	public void visit(AssignStatement n) {
		n.getLeft().accept(this);
		n.getRight().accept(this);
		System.out.print("\n");System.out.flush();
	}


	@Override
	public void visit(GotoStatement n) {
		// TODO Auto-generated method stub
		System.out.print("Goto ");System.out.flush();
		System.out.print(n.getGotoLabel());System.out.flush();
		System.out.print("\n");	System.out.flush();
	}

	@Override
	public void visit(ReturnStatement n) {
		// TODO Auto-generated method stub
		System.out.print("Return ");System.out.flush();
		n.getExpr().accept(this);
		System.out.print("\n");System.out.flush();
	}

	@Override
	public void visit(IfStatement n) {
		// TODO Auto-generated method stub
		System.out.print("IF ");System.out.flush();
		n.getCondition().accept(this);
		System.out.print("Goto ");
		System.out.print(n.getIfLabel());
		System.out.print("\n");
	}

	@Override
	public void visit(TryStatement n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ThrowStatement n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Function n) {
		// TODO Auto-generated method stub
		System.out.print("Fun :" );
		n.getArgList().accept(this);System.out.println("");
		n.getBody().accept(this);
	}

	@Override
	public void visit(ConditionalExpression n) {
		// TODO Auto-generated method stub
		n.getExpr1().accept(this);
		System.out.print(" ? ");
		n.getExpr2().accept(this);
		System.out.print(" :");
		n.getExpr3().accept(this);
	}

	@Override
	public void visit(BinaryOperator n) {
		// TODO Auto-generated method stub
		n.getLeft().accept(this);
		System.out.print(n.getOP());
		n.getRight().accept(this);
	}

	@Override
	public void visit(NumberLiteral n) {
		// TODO Auto-generated method stub
		System.out.print(n);
	}

	@Override
	public void visit(NullLiteral n) {
		// TODO Auto-generated method stub
		System.out.print("NULL");
	}

	@Override
	public void visit(ThisLiteral n) {
		// TODO Auto-generated method stub
		System.out.print("this");
		
	}

	@Override
	public void visit(BooleanLiteral n) {
		// TODO Auto-generated method stub
		System.out.print(n);
	}

	@Override
	public void visit(Selection n) {
		// TODO Auto-generated method stub
		n.getTarget().accept(this);
		n.getElement().accept(this);
	}

	@Override
	public void visit(StringLiteral n) {
		// TODO Auto-generated method stub
		System.out.print("StringLiteral("+n.str+")");

	}

	@Override
	public void visit(Identifier n) {
		// TODO Auto-generated method stub
		System.out.print(n);
	}

	@Override
	public void visit(NewExpression n) {
		// TODO Auto-generated method stub
		System.out.print("New ");
		System.out.print(n.getVariable());
		n.getExpList().accept(this);
	}
	
	public void visit(FieldExpression n) {
		// TODO Auto-generated method stub
		System.out.print("[");
		n.getExpr().accept(this);
		System.out.print("]");
	}

	@Override
	public void visit(CallExpression n) {
		// TODO Auto-generated method stub
		System.out.print(".Call");
		n.getExpList().accept(this);
	}
	
	public void visit(UnaryOperator n){
		System.out.print("!");
		n.getExpr().accept(this);
	}
	
	
}