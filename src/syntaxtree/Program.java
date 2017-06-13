package syntaxtree;

@SuppressWarnings("all")
public class Program extends Node {
	private Body body;
	
	public Program(){
		body = null;
	}
	
	public Program(Body b){
		body = b;
	}

	public void setBody(Body b){
		body = b;
	}
	
	public Body getBody(){
		return body;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (body == null) ? 0 : body.hashCode());
		return result;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		Program p = (Program) o;
		return body.equals(p.getBody());
	}
	
	public String toString(){
		return body.toString(); 
	}
	@Override
	public void accept(visitor.Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(visitor.GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}

}