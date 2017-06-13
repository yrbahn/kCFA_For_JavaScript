package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.Store;
import data.Env;
import data.Value;
import java.util.SortedSet;

@SuppressWarnings("all")
public class FieldExpression extends Expression {
	private Expression expr;
	
	public FieldExpression(Expression e){
		expr = e;
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		return expr.eval(e,s);
	}
		
	public Expression getExpr(){
		return expr;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (expr == null) ? 0 : expr.hashCode());
		return result;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		FieldExpression f = (FieldExpression) o;
		return expr.equals(f.getExpr());
	}
	
	public String toString(){
		return "[" + expr.toString() + " ]";
	}
	
	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}
	
	
}