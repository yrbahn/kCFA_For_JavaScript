package syntaxtree;
import java.util.Iterator;
import java.util.Vector;

import visitor.GJVisitor;
import visitor.Visitor;

@SuppressWarnings("all")
public class StmtList  extends Node implements Iterable<Statement>{
	private Vector<Statement> stmtList;
	private Statement lastStmtPtr;
	private boolean empty;
	
	public StmtList(){
		stmtList = new Vector<Statement>();
		lastStmtPtr=null;
		empty = true;
	}
	
	public boolean addElement(Statement s){
		if (empty){ // first addition
			lastStmtPtr = s;
			boolean ret = stmtList.add(s);
			if ( ret ) {
				empty=false;
				return true;
			} else
				return false;
		} else {
			lastStmtPtr.setNextStmt(s);
			lastStmtPtr = s;
			return stmtList.add(s);
		}		
	}
	
	public void union(StmtList sList){
		for(Statement s: sList){
			addElement(s);
		}
	}
	
	public void union(NodeList nList){
		for(Node n: nList){
			if (n instanceof Statement){
				addElement((Statement) n);
			}
		}
	}

	
	public void addElementFirst(Statement s){
		s.setNextStmt(stmtList.get(0));
		stmtList.insertElementAt(s,0);
	}
	
	public Statement elementAt(int i){
		return stmtList.elementAt(i);
	}
	
	public Statement getLastStmt(){
		return stmtList.elementAt(stmtList.size()-1);
	}
	
	
	public void removeLastElement(){
		stmtList.removeElementAt(stmtList.size()-1);
		lastStmtPtr = getLastStmt();
		lastStmtPtr.setNextStmt(null);
	}
	
	public void removeElementFirst(){
		stmtList.removeElementAt(0);
	}
	
	private Vector<Statement> getList(){
		return stmtList;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		for(Statement s: stmtList){
			sb.append(s.toString() + "\n");
		}
		
		return sb.toString();
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (stmtList == null) ? 0 : stmtList.hashCode());

		return result;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		StmtList sl = (StmtList) o;
		return stmtList.equals(sl.getList());
	}
	
	public int size(){
		return stmtList.size();
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Statement> iterator() {
		// TODO Auto-generated method stub
		return stmtList.iterator();
	}
	
	

}