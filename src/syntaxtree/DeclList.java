package syntaxtree;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Vector;

import data.Env;
import data.Store;
import data.Value;

import visitor.GJVisitor;
import visitor.Visitor;

@SuppressWarnings("all")
public class DeclList extends Node  implements Iterable<Identifier>{
	public final static int PRAMETER_LIST = 0;
	public final static int VAR_LIST  = 1;
	
	private int type;
	private Vector<Identifier> list;

	public DeclList(int t){
		type = t;
		list = new Vector<Identifier>();
	}
	
	
	public void union(DeclList dl){
		for (Identifier i:dl){
			addElement(i);
		}
	}
	
	public boolean addElement(Identifier e){
		return list.add(e);
	}
	
	public Identifier elementAt(int i){
		return list.elementAt(i);
	}
	
	private Vector<Identifier> getList(){
		return list;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (list == null) ? 0 : list.hashCode());
		return result;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		DeclList dl = (DeclList) o;
		return list.equals(dl.getList());
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		if (type == VAR_LIST){
			for(Identifier i:list){
				sb.append("var " + i.toString()+";\n");
			}
		} else {
			for(int i=0; i < list.size(); i++){
				sb.append(list.get(i).toString());
				if ( i+1 != list.size()){
					sb.append(", ");
				}
			}		
		}
		
		return sb.toString();
	}
	
	
	public int size(){
		return list.size();
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

	@Override
	public Iterator<Identifier> iterator() {
		// TODO Auto-generated method stub
		return list.iterator();
	}


}