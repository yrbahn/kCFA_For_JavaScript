package data;

import java.util.HashMap;
import java.util.Map;

import syntaxtree.Identifier;

public class Env{
	private Env outerEnv;
	private Map<Identifier,Addr> entries;
	
	public Env() {
		outerEnv = null;
		entries = new HashMap<Identifier,Addr>();
	}
	
	private Map<Identifier,Addr> getEntries(){
		return entries;
	}
	
	public Env(Env e){
		entries = new HashMap<Identifier,Addr>(e.getEntries());
	}
	
	private void setOuterEnv(Env e){
		outerEnv = e;
	}
	
	private Env getOuterEnv(){
		return outerEnv;
	}
	
	public Env extendEnv(){
		Env newEnv = new Env();
		newEnv.setOuterEnv(this);
		return newEnv;
	}
	
	 
	public Addr lookup(Identifier key){
		Addr addr;
		addr = entries.get(key);
		if ( addr == null ) {
			Env oEnv = getOuterEnv();
			if (oEnv == null ) return null;
			return oEnv.lookup(key);
		}
		return addr;
	}
	
	public Env getTopEnv(){
		Env e = this;
		while(e.getOuterEnv() != null){
			e = e.getOuterEnv();
		}
		return e;
	}
	
	public void putTopEnv(Identifier key, Addr addr){
		getTopEnv().put(key, addr);
	}
	
	public void put(Identifier key, Addr addr){
		entries.put(key, addr);
	}
	
	public String toString(){
		return entries.toString();
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (entries == null) ? 0 : entries.hashCode());
		
		return result;
	}

	public boolean equals(Object o){
		if (this == o ) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		Env e = (Env) o;
		if (  outerEnv == null || e.getOuterEnv() == null ){
			if ( outerEnv != e.getOuterEnv() ) return false; 
		} else if ( ! outerEnv.equals(e.getOuterEnv()) ) return false;
		
		return entries.equals(e.getEntries());
	}
	
	public static void main(String[] args){
		Env e1 = new Env();
		Contour c1 = new Contour();
		
		Bind b1 = new Bind(new Identifier("bbb"),c1.tick(22, 1) );
		Bind b2 = new Bind(new Identifier("bbb"),c1.tick(22, 1) );
		
		e1.put(new Identifier("a"), b1);
		Env e2 = e1.extendEnv();	
		e2.put(new Identifier("b"),b2);
		
		System.out.println(e1.equals(e2));
		
		Bind b3 = new Bind(new Identifier("bbb"),c1.tick(22, 1) );
		Bind b4 = new Bind(new Identifier("bbb"),c1.tick(22, 1) );
		
		Env e3 = new Env();
		e3.put(new Identifier("a"), b1);
		Env e4 = e3.extendEnv();	
		e4.put(new Identifier("b"),b2);
		
		System.out.println(e2.lookup(new Identifier("a")));
		System.out.println(e4.getTopEnv());
	}
	
}