package data;
import syntaxtree.Node;
import syntaxtree.Statement;
import syntaxtree.Identifier;

public class Ex implements Value {
	Identifier var;
	Env    env;
	Statement stmt1;
	Contour   fp;
	Statement stmt2;
	
	public Ex(Identifier v, Env e, Statement s1, Contour c, Statement s2){
		var  = v;
		env  = e;
		stmt1 = s1;
		fp   = c;
		stmt2 = s2;
	}

	public Identifier getVar(){
		return var;
	}
	
	public Env getEnv(){
		return env;
	}
	
	public Node getStmt1(){
		return stmt1;
	}
	
	public Node getStmt2(){
		return stmt2;
	}
	
	public Contour getContour(){
		return fp;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (var == null) ? 0 : var.hashCode());
		result = PRIME * result + ( (env == null) ? 0 : env.hashCode());
		result = PRIME * result + ( (stmt1 == null) ? 0 : stmt1.hashCode());
		result = PRIME * result + ( (stmt2 == null) ? 0 : stmt2.hashCode());
		result = PRIME * result + ( (fp == null) ? 0 : fp.hashCode());
		
		return result;
	}
	
	public int compareTo(Value v){	
		int classCompare = getClass().getName().compareTo(v.getClass().getName());
		if (classCompare != 0){
			return classCompare;
		}
		
		Ex ex = (Ex) v;
		return var.compareTo(ex.getVar());
	}
	
	public boolean equals(Object o){
		if (this == o ) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		
		Ex ret = (Ex) o;
		if(!var.equals(ret.getVar())) return false;
		if(!env.equals(ret.getEnv())) return false;
		if(!stmt1.equals(ret.getStmt1())) return false;
		if(!stmt2.equals(ret.getStmt2())) return false;
		return fp.equals(getContour());
	}
}