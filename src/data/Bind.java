package data;
import syntaxtree.Identifier;

public class Bind implements Addr{
	private Identifier var;
	private Contour time;
	
	public Bind(Identifier v, Contour t){
		var  = v;
		time = t;
	}
	
	public Identifier getVar(){
		return var;
	}
	
	public Contour getContour(){
		return time;
	}
	
	public int hashCode(){
	
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (var == null) ? 0 : var.hashCode());
		result = PRIME * result + ( (time == null) ? 0 : time.hashCode());
		return result;	
	}
	
	public boolean equals(Object o){		
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()){
			return false;
		}
		
		Bind bind = (Bind) o;
		if ( var == null){
			if(bind.getVar() == null){
				return time.equals(bind.getContour());
			}
			return false;
		}
		
		if (!var.equals(bind.getVar())){
			return false;
		}
		
		if (time == null){
			if (bind.getClass() == null) return true;
			return false;
		}
		return time.equals(bind.getContour());
	}
	
	public int compareTo(Addr addr){
		int classCompare = getClass().getName().compareTo(addr.getClass().getName());
		if (classCompare != 0 ){
			return classCompare;
		}
		
		Bind b = (Bind) addr;
		int cvar = var.compareTo(b.getVar());
		System.out.println(cvar);

		if (cvar != 0){
			return cvar;
		}
		return time.compareTo(b.getContour());
	}
	
	public String toString(){
		return "(" + var + ", " + time.toString() + ")";
	}
	
	public static void main(String argv[]){
		Contour c = new Contour();
		c = c.tick(2, 1);
		Bind b1 = new Bind(new Identifier("x"),c);
		Bind b2 = new Bind(new Identifier("x"),c);
		Addr a1 = b1;
		Addr a2 = b2;
		System.out.println(a1.equals(a2));
	}
}