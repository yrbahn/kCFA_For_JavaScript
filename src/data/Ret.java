package data;
import syntaxtree.*;

public class Ret implements Value {
	private Identifier var;
	private Env    env;
	private Node stmt;
	private Contour   fp;

	public Ret(Identifier v, Env e, Node s, Contour c){
		var  = v;
		env  = e;
		stmt = s;
		fp   = c;
	}
	
	public Identifier getVar(){
		return var;
	}
	
	public Env getEnv(){
		return env;
	}
	
	public Node getStmt(){
		return stmt;
	}
	
	public Contour getContour(){
		return fp;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (var == null) ? 0 : var.hashCode());
		result = PRIME * result + ( (env == null) ? 0 : env.hashCode());
		result = PRIME * result + ( (stmt == null) ? 0 : stmt.hashCode());
		result = PRIME * result + ( (fp == null) ? 0 : fp.hashCode());
		
		return result;
	}
	
	public int compareTo(Value v){	
		int classCompare = getClass().getName().compareTo(v.getClass().getName());
		if (classCompare != 0){
			return classCompare;
		}
		Ret ret = (Ret) v;
		return var.compareTo(ret.getVar());
	}
	
	public boolean equals(Object o){
		if (this == o ) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		
		Ret ret = (Ret) o;
		if(!var.equals(ret.getVar())) return false;
		if(!env.equals(ret.getEnv())) return false;
		if(!stmt.equals(ret.getStmt())) return false;
		return fp.equals(getContour());
	}
}