package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.BasicType;
import data.Env;
import data.Store;
import data.Value;
import data.MyNumber;
import java.util.TreeSet;
import java.util.SortedSet;

@SuppressWarnings("all")
public class NumberLiteral extends Expression {
	private String value;
	
	public NumberLiteral(){
		value = null;
	}
	
	public NumberLiteral(String v){
		value = v;
	}
	
	public NumberLiteral(int i){
		value = Integer.toString(i);		
	}
	
	public void setValue(int i){
		value = Integer.toString(i);
	}
	
	public String getValue(){
		return value;
	}
	
	public String toString(){
		if (value != null)
			return value;
		else 
			return "";
	}
	
	public int toInt(){
		return Integer.parseInt(value);
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		SortedSet<Value> result = new TreeSet<Value>();
		System.out.println("DDD11:"+value);
		result.add(new MyNumber(value));
		return result;
	}
	public final int hashCode(){
		return  value.hashCode();
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		return true;
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