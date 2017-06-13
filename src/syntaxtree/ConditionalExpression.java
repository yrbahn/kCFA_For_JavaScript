package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.*;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("all")
public class ConditionalExpression extends Expression {
	private Expression expr1, expr2, expr3;
	
	public ConditionalExpression(Expression e1, Expression e2, Expression e3){
		expr1 = e1;
		expr2 = e2;
		expr3 = e3;
	}
	
	public Expression getExpr1(){
		return expr1;
	}

	public Expression getExpr2(){
		return expr2;
	}

	public Expression getExpr3(){
		return expr3;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (expr1 == null) ? 0 : expr1.hashCode());
		result = PRIME * result + ( (expr2 == null) ? 0 : expr2.hashCode());
		result = PRIME * result + ( (expr3 == null) ? 0 : expr3.hashCode());
		
		return result;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		ConditionalExpression ce = (ConditionalExpression) o;
		if (!expr1.equals(ce.getExpr1())) return false;
		if (!expr2.equals(ce.getExpr2())) return false;
		return expr2.equals(ce.getExpr3());
	}
	
	public String toString(){
		return "<" +expr1.toString() + " ? " + expr2.toString() + " : " + expr3.toString() + " >";
	}
	
	
	public SortedSet<Value> eval(Env e, Store s){
		SortedSet<Value> result = new TreeSet<Value>();
		SortedSet<Value> temp;
		temp = expr2.eval(e, s);
		if ( temp == null ) return null;
		result.addAll(temp);
		temp = expr3.eval(e, s);
		if ( temp == null) return null;
		result.addAll(temp);
		return result;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
		
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}
	
	
}