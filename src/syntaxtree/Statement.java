package syntaxtree;
import java.util.Map;
import java.util.HashMap;


@SuppressWarnings("all")
public abstract class Statement extends Node{
	public static int labelInc = 1;
	private int label;
	private Statement next;
	public static Map<Integer, Statement> stmtMap = new HashMap<Integer,Statement>();
	
	public Statement(){
		stmtMap.put(new Integer(labelInc), this);
		setLabel(labelInc);
		labelInc++;
	}
	
	public int getType(){
		return Node.STATEMENT;
	}
	
	public int getLabel(){
		return label;
	}
	
	public void setLabel(int l){
		label = l;
	}
	
	public void setNextStmt(Statement stmt){
		next = stmt;
	}
	
	public Statement nextStmt(){
		return next;
	}
	
	public static Statement getStatement(int label){
		return stmtMap.get(new Integer(label));
	}
}