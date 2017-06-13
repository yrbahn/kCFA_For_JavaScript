package syntaxtree;

import java.util.List;
import data.*;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

@SuppressWarnings("all")
public class Function extends Expression {
	private Body body;
	private DeclList argList;
	
	public Function(){
		argList = null;
		body = null;
	}
	
	public Function(DeclList l, Body b){
		argList = l;
		body = b;
	}
	
	public Function(Function f){
		argList = f.getArgList();
		body = f.getBody();
	}
	
	
	public void setArgList(DeclList al){
		argList = al;
	}
	
	public void setBody(Body b){
		body = b;
	}
	
	public Body getBody(){
		return body;
	}
	
	public DeclList getArgList(){
		return argList;
	}
	
	public DeclList getDeclList(){
		return body.getDeclList();
	}

	
	public TreeSet<Value> eval(Env e, Store s){
		TreeSet<Value> ret = new TreeSet<Value>();
		Closure clo = new Closure(this ,e);
		ret.add(clo);

		return ret;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (body == null) ? 0 : body.hashCode());
		result = PRIME * result + ( (argList == null) ? 0 : argList.hashCode());
		return result;
	}
	
	public String toString(){
		return "function(" + argList.toString() + ")\n{"+ body.toString() + "}";
	}


	public boolean equals(Object o){

		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		Function f = (Function) o;
		if (!argList.equals(f.getArgList())) return false;
		return argList.equals(f.getArgList());
	}

	@Override
	public void accept(visitor.Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(visitor.GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}

}