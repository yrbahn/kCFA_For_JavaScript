package data;
import java.util.*;
import java.util.Map.Entry;

import syntaxtree.Identifier;

public class Store {
	public Map<Addr, SortedSet<Value>> entries;

	public Store(){
		entries = new HashMap<Addr, SortedSet<Value>>();
	}

	public Store(Store s){
		entries = new HashMap<Addr,SortedSet<Value>>();
		for(Map.Entry<Addr, SortedSet<Value>>  entry: s.entries.entrySet()){
			entries.put(entry.getKey(), entry.getValue());
		}
	}
	
	
	public Map<Addr,SortedSet<Value>> getEntries(){
		return entries;
	}

	public void insert(Addr addr, SortedSet<Value> d) {

		Set<Value> v = entries.get(addr);
		if ( v == null ){
			if ( d != null ){
				TreeSet<Value> newVal = new TreeSet<Value>(d);
				entries.put(addr, newVal);
			} else {
				entries.put(addr, null);
			}
		} else { 
			v.addAll(d);
		}
	
	}

	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (entries == null) ? 0 : entries.hashCode());
		
		return result;
	}
	
	public String toString(){

		StringBuilder sb = new StringBuilder();
		
		for (Entry<Addr, SortedSet<Value>> entry : this.getEntries().entrySet())
		{
		      sb.append(entry).append('\n');
		   
		}
		return sb.toString();
	}
	
	public SortedSet<Value> get(Addr a) {
		return entries.get(a);
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null ) return false;
		if (getClass() != o.getClass()) return false;
		Store store = (Store) o;
		if (entries == null){
			if (store.getEntries() !=null)
				return false;
		} else if (!entries.equals(store.getEntries()))
			return false;
		return true;
	}
	
	public void update(Addr addr, SortedSet<Value> values){
		SortedSet<Value> vs = get(addr);
		SortedSet<Value> newSet;
		
		if ( vs == null ){
			newSet = new TreeSet<Value>();
		} else {
			newSet = new TreeSet<Value>(vs);
		}
		
		if ( values != null ){
			newSet.addAll(values);
		}
		
		insert(addr, newSet);
		
	}
	
	public Store join(Store s){
	
		for ( Entry<Addr, SortedSet<Value>> entry : s.getEntries().entrySet()){
			update(entry.getKey(), entry.getValue());
		}
		
		return this;
	}
	
	public static void main(String argv[]){
		Map<Addr,Integer> s = new HashMap<Addr,Integer>();
		Contour con = new Contour();
		con = con.tick(23, 1);
		Addr bind = new Bind(new Identifier("x"), con );
		Addr bind2 = new Bind(new Identifier("x"), con );
		System.out.println(bind.equals(bind2));
		s.put(bind, 2);
		System.out.println(s);
		System.out.println(bind2);
		s.put(bind2, 1);
		System.out.println(s.containsKey(bind2));
		Map<String,Integer> s2 = new HashMap<String,Integer>();
		String ss = new String("x");
		String ss2 = new String("x");
		s2.put(ss,1);
		s2.put(ss2,1);
		System.out.println(s2);
		
	}
}