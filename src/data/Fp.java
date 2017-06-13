package data;

public class Fp implements  Value {
	private Contour c;
	
	public Fp(){
		c = new Contour();
	}
	
	public Fp(Contour con){
		c = con;
	}
	
	public Fp(Fp f){
		c = new Contour(f.getContour());
	}
	
	public Contour getContour(){
		return c;
	}
	
	
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		return result;
	}
	
	
	public boolean equals(Object o){
		if (this == o)
			return true;
		
		if (o == null)
			return false;
		
		if (getClass() != o.getClass())
			return false;
		
		Fp fp = (Fp) o;
		return c.equals(fp.getContour());
	}
	
	public int compareTo(Value v) {
		int classCompare = getClass().getName().compareTo(v.getClass().getName());
		if (classCompare != 0){
			return classCompare;
		}
		Fp fp = (Fp) v;
		return c.compareTo(fp.getContour());
	}

	public String toString(){
		return c.toString();
	}
	
}