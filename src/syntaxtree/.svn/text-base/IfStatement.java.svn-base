package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import kcfa.*;

@SuppressWarnings("all")
public class IfStatement extends Statement {
	private Expression condition;
	private NumberLiteral thenLabel;
	
	public IfStatement(){
		condition = null;
		thenLabel = null;
	}
	
	public IfStatement(Expression e, int l1){
		condition = e;
		thenLabel = new NumberLiteral(l1);

	}
	
	public IfStatement(Expression e, NumberLiteral l1, int l2){
		condition = e;
		thenLabel =l1;
		setLabel(l2);
	}
	
	public IfStatement(Expression e, int l1, int l2){
		condition = e;
		thenLabel = new NumberLiteral(l1);
		setLabel(l2);
	}
	
	public Expression getCondition(){
		return condition;
	}
	
	public int getIfLabel(){
		return thenLabel.toInt();
	}
	
	public void setCondition(Expression e){
		condition = e;
	}
	
	public void setIfLabel(int n){
		thenLabel = new NumberLiteral(n);
	}
	
	public String toString(){
		return "if (" + condition.toString() +") goto " + thenLabel.toString() +"; " + getLabel();
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (condition == null) ? 0 : condition.hashCode());
		result = PRIME * result + ( (thenLabel == null) ? 0 : thenLabel.hashCode());
		return result;
	}
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		IfStatement i = (IfStatement) o;
		if(!condition.equals(i.getCondition())) return false;
		return (thenLabel.equals(i.getIfLabel()));
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