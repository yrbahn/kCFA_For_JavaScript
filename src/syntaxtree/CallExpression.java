package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.*;
import java.util.SortedSet;

@SuppressWarnings("all")
public class CallExpression extends Expression {
	private Expression target;
	private ExpList expList;
	
	public CallExpression(){
		target = null;
		expList = null;
	}
	
	public CallExpression(Expression e, ExpList el){
		setTarget(e);
		setExpList(el);
	}

	public void setTarget(Expression e){
		target = e;
	}
	
	public void setExpList(ExpList el){
		expList = el;
	}
	
	public Expression getTarget(){
		return target;
	}
	
	public ExpList getExpList(){
		return expList;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (expList == null) ? 0 : expList.hashCode());
	
		return result;
	}
	
	public String toString(){
		return target.toString() + ".call(" + expList.toString() + ")";
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		CallExpression ce = (CallExpression) o;
		return expList.equals(ce.getExpList());
	
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		return null;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
		
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}