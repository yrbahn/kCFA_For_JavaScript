package syntaxtree;

import visitor.GJVisitor;

import visitor.Visitor;
import kcfa.*;

@SuppressWarnings("all")
public class ThrowStatement extends Statement {
	private Expression expression;
	
	public ThrowStatement(Expression e, int l){
		expression = e;
		setLabel(l);
	}

	public Expression getExpr(){
		return expression;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (expression == null) ? 0 : expression.hashCode());

		return result;
	}
	
	public String toString(){
		return "<THROW : " + expression.toString() + " >";
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		ThrowStatement ts = (ThrowStatement) o;
		return expression.equals(ts.getExpr());
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
			
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) throws GrammarException {
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}
	
	
}