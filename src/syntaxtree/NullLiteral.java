package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.Store;
import data.Env;
import data.Value;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("all")
public class NullLiteral extends Expression {

	
	public boolean equal(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		return true;
	}
	
	public final int hashCode(){
		return 19;
	}
	
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
		
	}
	
	public boolean equals(Object o){
		if ( this == o) return true;
		if ( o == null ) return false;
		if (getClass() != o.getClass()) return false;
		return true;
	}
	
	public String toString(){
		return "<NULL>";	
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		return  new TreeSet<Value>();
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}
	
	
}