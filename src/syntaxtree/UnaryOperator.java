package syntaxtree;

import visitor.GJVisitor;
import visitor.Visitor;
import data.*;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("all")
public class UnaryOperator extends Expression {

	public final static int UNARY_NOT = 0;
	public final static int UNARY_INC = 1;
	public final static int UNARY_DEC = 2;
	public final static int UNARY_NEG = 3;
	public final static int UNARY_POS = 4;

	private Expression expr1;
	private int unaryOp;
	private boolean isPostfix;
	
	
	public UnaryOperator(){
		expr1 = null;
		unaryOp = -1;
		isPostfix = false;
	}
	
	
	public UnaryOperator(int op, Expression e1){
		expr1 = e1;
		unaryOp = op;
		isPostfix = false;
	}
	
	
	public UnaryOperator(int op, Expression e, boolean isPostfix){
		expr1 = e;
		unaryOp = op;
		this.isPostfix = isPostfix;
	}
	
	
	public Expression getExpr(){
		return expr1;
	}
	
	public void setExpr(Expression e){
		expr1 = e;
	}
	
	public void setOP(int op){
		unaryOp = op;
	}
	
	public int getOP(){
		return unaryOp;
	}
	
	public void setIsPostfix(boolean p){
		isPostfix = p;
	}
	
	public boolean getIsPostfix(){
		return isPostfix;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (expr1 == null) ? 0 : expr1.hashCode());
		result = PRIME * result + unaryOp;
		return result;
	}

	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		UnaryOperator uo = (UnaryOperator) o;
		if (!expr1.equals(uo.getExpr())) return false;
		return (unaryOp == uo.getOP());
		
	}
	
	public String toString(){
		String unary ="";
		switch(unaryOp){
			case UNARY_NOT:
				unary = "not";
				break;
			case UNARY_INC:
				unary = "++";
				break;
			case UNARY_DEC:
				unary = "--";
				break;
			
		}
		
		if (isPostfix){
			return "postfix_" + unary + "(" + expr1.toString() +")";
		} else{
			return unary + "(" + expr1.toString() +")";
		}
	}
	
	public SortedSet<Value> eval(Env e, Store s){
		SortedSet<Value> d = expr1.eval(e, s);
		if (d == null) return null;
		SortedSet<Value> result = new TreeSet<Value>();
		for (Value v: d){
			if ( v instanceof MyBoolean){
				MyBoolean bool = (MyBoolean) v;
				bool.not();
				result.add(bool);
			} else if ( v instanceof MyNumber){
				MyNumber num = (MyNumber) v;
				result.add(num);
			} 
		}
		
		return result;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public <R, A> R accept(GJVisitor<R, A> v, A argu) {
		// TODO Auto-generated method stub
		return v.visit(this, argu);
	}
	
	
}