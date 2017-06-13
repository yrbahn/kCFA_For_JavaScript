package data;

public class MyString implements BasicType {
	private String value;
	
	public MyString(String s){
		value = s;
	}
	
	public int hashCode(){
		return value.hashCode();
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		return true;
	}
	
	public int compareTo(Value o){
		int classCompare = getClass().getName().compareTo(o.getClass().getName());
		if (classCompare != 0) return classCompare;
		return 0;
	}
	
	public String getValue(){
		return "String";
	}
	
	public String toString(){
	return "String";
	}
}