package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.*;
import java.util.SortedSet;

@SuppressWarnings("all")
public class NewExpression extends Expression {
	private Identifier variable;
	private ExpList expList;
	
	public NewExpression(){
		variable = null;
		expList  = null;
	}
	
	public NewExpression(Identifier var, ExpList el){
		variable = var;
		expList = el;
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		return null;
	}
	
	public void setVariable(Identifier var){
		variable = var;
	}
	
	public void setExpList(ExpList el){
		expList = el;
	}
	
	public Identifier getVariable(){
		return variable;
	}
	public ExpList getExpList(){
		return expList;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (variable == null) ? 0 : variable.hashCode());
		result = PRIME * result + ( (expList == null) ? 0 : expList.hashCode());
		return result;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		NewExpression ne = (NewExpression) o;
		if (!variable.equals(ne.getVariable())) return false;
		return (expList.equals(ne.getExpList()));
	}
	
	public String toString(){
		return "<NEW: " + variable.toString() + " (" + expList.toString() + " ) >";
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