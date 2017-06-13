package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import kcfa.*;

@SuppressWarnings("all")
public class GotoStatement extends Statement {
	private NumberLiteral gotoLabel;
	private int type;

	public final static int BREAK = 0;
	public final static int CONTINUE = 1;
	

	public GotoStatement(){
		gotoLabel = null;
	}
	
	public GotoStatement(int i){
		gotoLabel = new NumberLiteral(i);
	}
	

	
	public GotoStatement(NumberLiteral l1, int l2){
		gotoLabel = l1;
		setLabel(l2);
	}
	

	public GotoStatement(int l1, int l2){
		gotoLabel = new NumberLiteral(l1);
		setLabel(l2);
	}
	
	
	public void setGotoType(int t){
		type = t;
	}
	
	public void setGotoLabel(int i){
		gotoLabel = new NumberLiteral(i);
	}
	
	public void setGotoLabel(NumberLiteral i){
		gotoLabel = i;
	}
	
	public int getGotoType(){
		return type;
	}
	
	public int getGotoLabel(){
		if (gotoLabel != null)
			return gotoLabel.toInt();
		else
			return -1;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (gotoLabel == null) ? 0 : gotoLabel.hashCode());
		return result;
	}
	
	public String toString(){
		return "goto "+ gotoLabel.toString() + ";" + getLabel();
	}

	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		GotoStatement g = (GotoStatement) o;
		return (gotoLabel.equals(g.getGotoLabel()));
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