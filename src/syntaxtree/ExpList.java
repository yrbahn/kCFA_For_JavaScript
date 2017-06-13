package syntaxtree;
import java.util.Vector;
import java.util.SortedSet;

import visitor.GJVisitor;
import visitor.Visitor;
import data.Store;
import data.Env;
import data.Value;

@SuppressWarnings("all")
public class ExpList  extends Node{
	private Vector<Expression> list;

	public ExpList(){
		list = new Vector<Expression>();
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		return null;
	}
	
	public boolean addElement(Expression e){
		return list.add(e);
	}
	
	public void insertElementAt(Expression e, int index){
		 list.insertElementAt(e, index);
	}
	
	public Expression elementAt(int i){
		return list.elementAt(i);
	}
	
	
	public int size(){
		return list.size();
	}
	
	public Vector<Expression> getList(){
		return list;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (list == null) ? 0 : list.hashCode());
		return result;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		int s = list.size();
		int i = 0;
		for(Expression e: list){
			sb.append(e.toString());
			i = i + 1;
			if ( i < s)
				sb.append(", ");
		}
		return sb.toString();
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		ExpList el = (ExpList) o;
		return list.equals(el.getList());
	}
	
	public void setLabel(int l){}
	
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);	
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		return null;
	}

}