package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.*;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("all")
public class Selection extends Expression {
	private Expression left, right;
	
	public Selection(){
		left = null;
		right = null;
	}
	
	public Selection(Expression target, Expression element){
		set(target,element);
	}
	
	public void set(Expression target, Expression element){
		left = target;
		right = element;
	}
	
	public Expression getTarget(){
		return left;
	}
	
	public Expression getElement(){
		return right;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (left == null) ? 0 : left.hashCode());
		result = PRIME * result + ( (right == null) ? 0 : right.hashCode());
		return result;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		Selection s = (Selection) o;
		if (!left.equals(s.getTarget())) return false;
		return right.equals(s.getElement());
	}
	
	public String toString(){
		return left.toString() + "[" + right.toString() + "]";
	}
	public SortedSet<Value> eval(Env e, Store s){
		SortedSet<Value> setVar = left.eval(e,s);
		if (setVar == null) return null;
		
		SortedSet<Value> setSel = right.eval(e,s);
		SortedSet<Value> result = new TreeSet<Value>();
		
		Field f = null;
		for(Value v: setVar){
			if (v instanceof Location){
				Location loc = (Location) v;
				if (setVar == null){
					f = new Field(loc,left.toString());
					result.addAll(s.get(f));
				} else {
					for(Value v2: setSel){
						f = new Field(loc, v2.toString());
						SortedSet<Value> res = s.get(f);
						result.addAll(res);
					}
				}
			}
		}
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