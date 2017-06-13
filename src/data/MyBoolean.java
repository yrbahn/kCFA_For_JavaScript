package data;

public class MyBoolean implements BasicType {
	private Boolean value;

	public MyBoolean(){
		value = false;
	}
	
	public MyBoolean(Boolean b){
		value = b;
	}
	
	public void not(){
		value = !value;
	}
	
	public String getValue(){
		return "BOOL";
	}
	
	public int hashCode(){
		return value.hashCode();
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		MyBoolean b = (MyBoolean) o;
		return value.equals(b.getValue());
	}
	
	public int compareTo(Value o) {
		// TODO Auto-generated method stub
		int classCompare = getClass().getName().compareTo(o.getClass().getName());
		if (classCompare != 0) return classCompare;
		//MyBoolean b = (MyBoolean) o;
		return 0;
	}

	public String toString(){
		return value.toString();
	}
}