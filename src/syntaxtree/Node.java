package syntaxtree;
import kcfa.*;

public abstract class Node implements Comparable<Node> {
	private static int TAG_COUNT = 1;
	public final static int NODE       = 0;
	public final static int STATEMENT  = 1;
	public final static int EXPRESSION = 2;
	public final static int NODE_LIST  = 3;
	
	private int tag; // Identification
	
	public Node(){
		tag = TAG_COUNT;
		TAG_COUNT++;
	}
	
	public int getTag(){
		return tag;
	}
	
	public int compareTo(Node n){
		if (tag > n.getTag()) return -1;
		else if ( tag < n.getTag()) return 1;
		return 0;
	}
	
	abstract public void accept(visitor.Visitor v);
	abstract public <R,A> R accept(visitor.GJVisitor<R,A> v, A argu) throws GrammarException;
}

