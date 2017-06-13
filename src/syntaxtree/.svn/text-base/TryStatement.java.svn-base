package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import java.util.List;
import kcfa.*;


@SuppressWarnings("all")
public class TryStatement extends Statement {
	private StmtList tryBlock, catchBlock;
	private Identifier catchVar;
	
	public TryStatement(){
		tryBlock   = null;
		catchBlock = null;
		catchVar   = null;
	}
	
	public TryStatement(StmtList l1,Identifier v, StmtList l2, int l){
		tryBlock = l1;
		catchBlock = l2;
		catchVar       = v;
		setLabel(l);
	}


	public StmtList getTryBlock(){
		return tryBlock;
	}

	public StmtList getCatchBlock(){
		return catchBlock;
	}
	
	public void setCatchBlock(StmtList c){
		catchBlock =c;
	}
	
	public void setTryBlock(StmtList t){
		tryBlock =t;
	}
	
	public void setCatchVar(Identifier i){
		catchVar = i;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (tryBlock == null) ? 0 : tryBlock.hashCode());
		result = PRIME * result + ( (catchBlock == null) ? 0 : catchBlock.hashCode());
		result = PRIME * result + ( (catchVar == null) ? 0 : catchVar.hashCode());

		return result;
	}
	public Identifier getCatchVar(){
		return catchVar;
	}
	
	public String toString(){
		return "try {\n" + tryBlock.toString() + "\n}\n CATCH (" + catchVar.toString() +
			") {\n" + catchBlock.toString() + "\n}";
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		TryStatement ts = (TryStatement) o;
		if (!tryBlock.equals(ts.getTryBlock())) return false;
		if (!catchBlock.equals(ts.getCatchBlock())) return false;
		return catchVar.equals(ts.getCatchVar());	
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