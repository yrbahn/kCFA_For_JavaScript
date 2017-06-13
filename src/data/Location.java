package data;

public class Location implements Value {
	private String var;
	private Contour contour;
	
	public Location(String s, Contour c){
		var = s;
		contour = c;
	}
	
	public String toString(){
		return "(" + var + ", "+contour.toString()+ ")";
	}
	
	public String getVar(){
		return var;
	}
	
	public Contour getContour(){
		return contour;
	}
	
	public boolean equals(Object o){
		if (this == o)
			return true;
		
		if (o == null)
			return false;
		
		if (getClass() != o.getClass())
			return false;
		
		Location l = (Location) o;
		if (contour.equals(l.getContour())){
			return var.equals(l.getVar());
		} 
		return false;
	}
	
	public int compareTo(Value v) {
		int classCompare = getClass().getName().compareTo(v.getClass().getName());
		if (classCompare != 0){
			return classCompare;
		}
		
		Location location = (Location) v;
		
		int ret = contour.compareTo(location.getContour());
		if (ret == 0){
			return var.compareTo(location.getVar());
		}
		return ret;
	}

}