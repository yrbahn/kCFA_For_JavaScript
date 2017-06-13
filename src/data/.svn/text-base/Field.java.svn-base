package data;

public class Field implements Addr{
	private Location loc;
	private String  str;
	
	public Field(Location l, String s){
		loc = l;
		str = s;
	}
	
	public Location getLoc(){
		return loc;
	}
	
	public String getString(){
		return str;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (loc == null) ? 0 : loc.hashCode());
		result = PRIME * result + ( (str == null) ? 0 : str.hashCode());
		
		return result;
	}
	
	public boolean equals(Object o){
		if (this == o ) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		Field f = (Field) o;
		if (!loc.equals(f.getLoc())) return false;
		return str.equals(f.getString());
	}
	
	public String toString(){
		return "(" + loc.toString() + "," + str + ")";
	}
	@Override
	public int compareTo(Addr o) {
		int classCompare = getClass().getName().compareTo(o.getClass().getName());
		if (classCompare != 0) return classCompare;
		Field f = (Field) o;
		int ret = loc.compareTo(f.getLoc());
		if (ret != 0) return ret;
		return str.compareTo(f.getString());
	}
	
}