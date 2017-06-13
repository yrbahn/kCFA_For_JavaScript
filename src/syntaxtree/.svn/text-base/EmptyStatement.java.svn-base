package syntaxtree;

import kcfa.GrammarException;
import visitor.GJVisitor;
import visitor.Visitor;

@SuppressWarnings("all")
public class EmptyStatement extends Statement{

	public String toString(){
		return ";"+getLabel();
	}
	
	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) throws GrammarException {
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}

	
}