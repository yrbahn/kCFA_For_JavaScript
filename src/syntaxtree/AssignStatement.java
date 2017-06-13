package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import kcfa.*;

@SuppressWarnings("all")
public class AssignStatement extends Statement{
	private Expression left;
	private Expression right;
	
	public AssignStatement(){
		left = null;
		right = null;
	}
	public AssignStatement(Expression l, Expression r){
		left = l;
		right = r;
	}
	
	public AssignStatement(Expression l, Expression r, int n){
		left = l;
		right = r;
		setLabel(n);
	}
	
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (left == null) ? 0 : left.hashCode());
		result = PRIME * result + ( (right == null) ? 0 : right.hashCode());
		return result;
	}
	
	public String toString(){
		return left.toString() + " = " + right.toString() + "; " + getLabel();
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		AssignStatement assign = (AssignStatement) o;
		if (!left.equals(assign.getLeft())) return false;
		if (!right.equals(assign.getRight())) return false;
		return true;
	}
	
	
	public Expression getLeft(){
		return left;
	}
	
	public Expression getRight(){
		return right;
	}

	public void setLeft(Expression i){
		left = i;
	}
	
	public void setRight(Expression r){
		right = r;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) throws GrammarException{
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}

}