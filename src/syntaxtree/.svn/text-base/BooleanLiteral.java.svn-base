package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.Store;
import data.Env;
import data.BasicType;
import data.Value;
import data.MyBoolean;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("all")
public class BooleanLiteral extends Expression {
	private Boolean bool;
	
	public BooleanLiteral(boolean b){
		bool = b;
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		SortedSet<Value> result = new TreeSet<Value>();
		result.add(new MyBoolean(bool));
		return result;
	}
	
	public Boolean getBoolean(){
		return bool;
	}
	
	public String toString(){
		return bool.toString();
	}
	
	public int hashCode(){	
		return bool.hashCode();
	}
	
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		BooleanLiteral bl = (BooleanLiteral) o;
		return (bool == bl.getBoolean());
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