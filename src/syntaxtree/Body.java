package syntaxtree;
import visitor.*;

import java.util.List;

@SuppressWarnings("all") 
public class Body extends Node{
	private DeclList declList;
	private StmtList stmtList;
	
	public Body(){
		declList = null;
		stmtList = null;
	}
	
	public Body(DeclList dl, StmtList sl){
		declList = dl;
		stmtList = sl;
	}
	
	public DeclList getDeclList(){
		return declList;
	}
	
	public StmtList getStmtList(){
		return stmtList;
	}
	
	public void setDeclList(DeclList dl){
		declList = dl;
	}
	
	public void setStmtList(StmtList sl){
		stmtList = sl;
	}
	
	public String toString(){
		return declList.toString() + "\n" + stmtList.toString();
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (declList == null) ? 0 : declList.hashCode());
		result = PRIME * result + ( (stmtList == null) ? 0 : stmtList.hashCode());
		
		return result;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		Body b = (Body) o;
		if (!declList.equals(b.getDeclList())) return false;
		return stmtList.equals(b.getStmtList());
	}
	
	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}
}