package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.Env;
import data.Store;
import data.Value;
import data.Addr;

import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("all")
public class ThisLiteral extends Expression {

	
	public boolean equal(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		return true;
	}
	
	public final int hashCode(){
		return 10;	
	}
	
	public String toString(){
		return "<THIS>";
	}
	
	public boolean equals(Object o){
		if ( this == o) return true;
		if ( o == null ) return false;
		if (getClass() != o.getClass()) return false;
		return true;
	}
	
	
	public SortedSet<Value> eval(Env e, Store s){
		Addr addr = e.lookup(new Identifier("this"));
		if (addr != null) {
			return s.get(addr);
		}
		return null;
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