package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.BasicType;
import data.Store;
import data.Env;
import data.Value;
import data.MyString;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("all")
public class StringLiteral extends Expression {
	public String str;
	
	public StringLiteral(String s){
		str = s;
	}
	
	private String getStr(){
		return str;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (str == null) ? 0 : str.hashCode());

		return result;
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		SortedSet<Value> result = new TreeSet<Value>();
		result.add(new MyString(str));
		return result;
	}
	
	public String toString(){
		return "\"" + str + "\"";
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		StringLiteral sl = (StringLiteral) o;
		return str.equals(sl.getStr());
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
	
	
}