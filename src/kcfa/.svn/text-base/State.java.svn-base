package kcfa;
import syntaxtree.Node;
import syntaxtree.Identifier;

import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.SortedSet;

import data.Contour;
import data.Env;
import data.Store;
import data.Value;
import data.Ret;
import data.Ex;
import data.Addr;
import data.Bind;
import data.Fp;

public class State implements Comparable<State>{
	private Node statement;
	private Env env;
	private Store store;
	private Contour fp;
	
	public State(){
		statement = null;
		env = null;
		store = null;
		fp = null;
	
	}
	
	public State(Node stmt, Env e, Store s, Contour f){
		statement = stmt;
		env  = e;
		store = s;
		fp  = f;
	}
	
	public void setEnv(Env e){
		env = e;
	}
	
	public Env getEnv(){
		return env;
	}
	
	public void setStore(Store s){
		store =s;
	}
	
	public Store getStore(){
		return store;
	}
	
	public void setFPtr(Contour f){
		fp = f; 
	}
	
	public Contour getFPtr(){
		return fp;
	}
	
	public Node getStatement(){
		return statement;
	}
	
	public void setStatement(Node stmt){
		statement = stmt;
	}
	
	public Set<State> next(NextState ns) throws GrammarException{
		if (statement != null){
			//System.out.println(ns);
			return statement.accept(ns, this);
		}
		return null;
	}
	
	public Set<State> checkBlockEnd(){
		Contour fp = getFPtr();
		Set<Value> setFp = store.get(fp);
		if (setFp != null ){
			SortedSet<State> res = new TreeSet<State>();
			for(Value v:setFp){
				if ( v instanceof Ret){
					Ret ret = (Ret) v;
					Addr addr = env.lookup(new Identifier("this"));
					Env retEnv = ret.getEnv();
					if (addr != null){
						SortedSet<Value> d = store.get(addr);
						Store newStore = new Store(store);
						Addr addr2 = retEnv.lookup(ret.getVar());
						newStore.insert(addr2,d);
						State newState = new State();
						newState.setStatement(ret.getStmt());
						newState.setEnv(retEnv);
						newState.setFPtr(ret.getContour());
						newState.setStore(newStore);
						res.add(newState);
					}
				} else if ( v instanceof Ex){
					Ex ex = (Ex) v;
					State newState = new State();
					newState.setStatement(ex.getStmt1());
					newState.setEnv(ex.getEnv());
					newState.setStore(new Store(store));
					newState.setFPtr(ex.getContour());
					res.add(newState);
				}
			}
			return res;
		}	
		return null;
	}

	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (statement == null) ? 0 : statement.hashCode());
		result = PRIME * result + ( (env == null) ? 0 : env.hashCode());
		result = PRIME * result + ( (store == null) ? 0 : store.hashCode());
		result = PRIME * result + ( (fp == null) ? 0 : fp.hashCode());
		System.out.println("hasCode:"+result);
		return result;
	}
	
	public boolean equals(Object o){
		System.out.println("EQUAL STATE");
		if (this == o ) return true;
		if (o == null ) return false;

		if (getClass() != o.getClass()) return false;
		
		State state = (State) o;

		if(!statement.equals(state.getStatement())) return false;
		if(!env.equals(state.getEnv())) return false;		
		if(!store.equals(state.getStore())) return false;
		
		return fp.equals(state.getFPtr());
	}
	
	public int compareTo(State s){
		int hashCode1 = hashCode();
		int hashCode2 = s.hashCode();
		if (hashCode1 == hashCode2){
			return 0;
		} else if ( hashCode1 > hashCode2){
			return 1;
		} else {
			return -1;
		}
	}
	
	public String toString(){
		if (statement == null ) {
			return "State:============\n [NextStmt]  StmtEnd "  
			+ "\n [Env]    " + env.toString() + "\n [Store]     " + store.toString() +"\n ===================";

		}
		return "State:============\n [NextStmt]   " +  statement.toString() 
		+ "\n [Env]    " + env.toString() + "\n [Store]     " + store.toString() +"\n ===================";
	}
	
	public static void main(String argv[]){
		Set<State> setState = new TreeSet<State>();
		
		Identifier id1 = new Identifier("1232");
		Identifier id2 = new Identifier("111");
		
		Env env1 = new Env();
		Env env2 = new Env();
		
		Contour con = new Contour();
		Contour con1 = con.tick(23, 1);
		Contour con2 = con.tick(23, 1);
		Fp f1 = new Fp(con1);
		Fp f2 = new Fp(con2);
		
		Bind bind1 = new Bind(id1,con1);
		Bind bind2 = new Bind(id2,con2);
		
		env1.put(id1,bind1);
		env2.put(id2,bind2);
			
		SortedSet<Value> setVal1 = new TreeSet<Value>();
		SortedSet<Value> setVal2 = new TreeSet<Value>();
		setVal1.add(f1);
		setVal1.add(f2);
		setVal2.add(f2);
		setVal2.add(f1);
		
		Store store1 = new Store();
		store1.insert(bind1, null);

		Store store2 = new Store();
		store2.insert(bind2, null);
		
		State state1 = new State(id1,env1,store1,con1);
		State state2 = new State(id2,env2,store2,con2);
		setState.add(state1);
		setState.add(state2);
		System.out.println(state1);
		System.out.println(state2);
		System.out.println("======="+setState.size());
		System.out.println(state1.equals(state2));
	}
}