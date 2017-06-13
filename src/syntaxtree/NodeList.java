package syntaxtree;
import java.util.Iterator;
import java.util.Vector;

import visitor.GJVisitor;
import visitor.Visitor;

@SuppressWarnings("all")
public class NodeList  extends Node implements Iterable<Node>{
	private Vector<Node> list;
	
	
	public NodeList(){
		list = new Vector<Node>();
	}
	
	
	public boolean addElement(Node n){
		if (n instanceof NodeList){
			union((NodeList)n);
			return true;
		} else {
			return list.add(n);
		}
	}
	
	public void union(NodeList nList){
		for(Node n: nList){
			addElement(n);
		}
	}
	
	public void union2(NodeList nList){
		for(Node n: nList){
			addElementFirst(n);
		}
	}
	
	public void addElementFirst(Node n){
		if (n instanceof NodeList)
			union2((NodeList) n);
		else 
			list.insertElementAt(n,0);
	}
	
	public Node elementAt(int i){
		return list.elementAt(i);
	}
	
	public Node getLastNode(){
		return list.elementAt(list.size()-1);
	}
	
	
	public void removeLastElement(){
		list.removeElementAt(list.size()-1);
	}
	
	public void removeElementFirst(){
		list.removeElementAt(0);
	}
	
	private Vector<Node> getList(){
		return list;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		for(Node n: list){
			sb.append(n.toString() + "\n");
		}
		
		return sb.toString();
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (list == null) ? 0 : list.hashCode());

		return result;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		NodeList sl = (NodeList) o;
		return list.equals(sl.getList());
	}
	
	public int size(){
		return list.size();
	}

	@Override
	public void accept(Visitor v) {
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Node> iterator() {
		// TODO Auto-generated method stub
		return list.iterator();
	}
	
	

}