package data;

public class MyNumber implements BasicType{
	public String value;
	
	public MyNumber(){
		value = "-1";
	}
	
	public MyNumber(String i){
		value = i;
	}
	
	public String getValue(){
		return "NUMBER";
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
		if (classCompare != 0 ) return classCompare;
		return 0;
	}
	
	public String toString(){
		return "NUMBER";
	}
	
}