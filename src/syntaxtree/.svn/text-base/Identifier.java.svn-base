package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.Env;
import data.Store;
import data.Value;
import data.Addr;
import data.MyString;
import data.JavascriptPrimaryFunction;

import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("all")
public class Identifier extends Expression{
	private String ident;
	public Identifier(String s){
		ident = s;
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		Addr addr = e.lookup(this);
		if (addr != null)
			return s.get(e.lookup(this));
		else {
			SortedSet<Value> setValue = new TreeSet<Value>();
			System.out.println("eee:"+ident);
			if (ident.equals("Array")){
				System.out.println("eee:"+ident);
					
				setValue.add(new JavascriptPrimaryFunction(JavascriptPrimaryFunction.CREATE_ARRAY));
			} else {
				return null;
			}
			
			return setValue;
			
		}
			
		// if Indent is not in Env, Consider this as String.
	}
	
	public String getIdent(){
		return ident;
	}
	
	public String toString(){
		return ident.toString();
		//return "ID(" + ident.toString() +")";
	}
	
	public final int hashCode(){
		return ident.hashCode();
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		Identifier i = (Identifier) o;
		return ident.equals(i.getIdent());
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