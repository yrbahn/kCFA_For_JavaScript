package syntaxtree;
import data.*;
import java.util.SortedSet;

@SuppressWarnings("all")
public abstract class Expression extends Node{

	public int getType(){
		return Node.EXPRESSION;
	}
	
	abstract public SortedSet<Value> eval(Env e, Store s);	
}

