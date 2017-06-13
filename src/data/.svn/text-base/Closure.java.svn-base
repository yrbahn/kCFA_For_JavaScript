package data;

import syntaxtree.*;

public class Closure implements Value {
	private Function fun;
	private Env env;
	
	public Closure(Function f, Env e){
		fun = f;
		env = e;
	}
	
	public Env getEnv(){
		return env;
	}
	
	public Function getFunction(){
		return fun;
	}
	

	public int compareTo(Value v){
		int classCompare = getClass().getName().compareTo(v.getClass().getName());
		if (classCompare != 0){
			return classCompare;
		}
		
		Closure c = (Closure) v;
		return fun.compareTo(c.getFunction());
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (fun == null) ? 0 : fun.hashCode());
		result = PRIME * result + ( (env == null) ? 0 : env.hashCode());
		return result;
	}
	
	public boolean equals(Object o){

		if (this == o ) return true;
		if (o == null ) return false;
		if (getClass() != o.getClass()) return false;
		Closure c = (Closure) o;
		if(!fun.equals(c.getFunction())) return false;
		return env.equals(c.getEnv());
	}

}