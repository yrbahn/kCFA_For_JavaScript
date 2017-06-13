package syntaxtree;

import java.util.SortedSet;
import java.util.TreeSet;

import visitor.GJVisitor;
import visitor.Visitor;
import data.BasicType;
import data.Env;
import data.Store;
import data.Value;
import data.BasicType;
import data.MyBoolean;
import data.MyNumber;

import java.util.Set;
import java.util.HashSet;

@SuppressWarnings("all")
public class BinaryOperator extends Expression {

	public final static int BIN_PLUS = 0;
	public final static int BIN_MINUS = 1;
	public final static int BIN_TIMES = 2;
	public final static int BIN_DIVIDE = 3;
	public final static int BIN_AND = 4;
	public final static int BIN_OR = 5;
	public final static int BIN_LT = 6;
	public final static int BIN_GT = 7;
	public final static int BIN_LE = 8;
	public final static int BIN_GE = 9;
	public final static int BIN_EQ = 10;
	public final static int BIN_NEQ = 11;
	public final static int BIN_BITOR = 12;
	public final static int BIN_BITXOR = 13;
	public final static int BIN_BITAND = 14;
	public final static int BIN_LSH = 15;
	public final static int BIN_RSH = 16;
	public final static int BIN_URSH = 17;
	public final static int BIN_MOD = 18;
	
	

	private Expression left, right;
	private int binOp;
	
	public BinaryOperator(){
		left = null;
		right = null;
		binOp = -1;
	}
	
	public BinaryOperator(int op, Expression e1, Expression e2){
		left = e1;
		right = e2;
		binOp = op;
	}

	public Expression getLeft(){
		return left;
	}
	
	public Expression getRight(){
		return right;
	}
	
	public int getOP(){
		return binOp;
	}
	
	public void setLeft(Expression e){
		left = e;
	}
	
	public void setRight(Expression e){
		right = e;
	}
	
	public void setOp(int op){
		binOp = op;
	}
	
	public int hashCode(){
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( (left == null) ? 0 : left.hashCode());
		result = PRIME * result + ( (right == null) ? 0 : right.hashCode());
		result = PRIME * result + binOp;
		
		return result;
	}
	
	
	
	public String toString(){
		String op ="";
		switch(binOp){
			case BIN_PLUS:
				op = "+";
				break;
			case BIN_MINUS:
				op = "-";
				break;
			case BIN_TIMES:
				op = "*";
				break;
			case BIN_DIVIDE:
				op = "/";
				break;
			case BIN_MOD:
				op = "%";
				break;
			case BIN_AND:
				op = "&";
				break;
			case BIN_OR:
				op = "|";
				break;
			case BIN_LT:
				op = "<";
				break;
			case BIN_GT:
				op = ">";
				break;
			case BIN_LE:
				op = "<=";
				break;
			case BIN_GE:
				op = ">=";
				break;
			case BIN_EQ:
				op = "==";
				break;
			case BIN_NEQ:
				op = "!=";
				break;
			case BIN_BITOR :
				op = "|";
				break;
			case BIN_BITXOR:
				op = "^";
				break;
			case BIN_BITAND:
				op = "&";
				break;
			case BIN_LSH:
				op = "<<";
				break;
			case BIN_RSH:
				op = ">>";
				break;
			case BIN_URSH:
				op = ">>>";
				break;
		}
		return left.toString() + " "+ op + " "+ right.toString();
	}
	
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;
		BinaryOperator bo = (BinaryOperator) o;
		if (!left.equals(bo.getLeft())) return false;
		if (!right.equals(bo.getRight())) return false;
		if (binOp != bo.getOP()) return false;
		return true;
	}
	public SortedSet<Value> eval(Env e, Store s){
		SortedSet<Value> d1 = left.eval(e,s);
		if (d1 == null) return null;
		SortedSet<Value> d2 = right.eval(e,s);
		if (d2 == null) return null;

		SortedSet<Value> temp1, temp2;
		SortedSet<Value> result = new TreeSet<Value>();
		Value resultOpBin;
		
		for (Value v1 : d1 ){
			for(Value v2: d2){
				if ( v1 instanceof MyNumber & v2 instanceof MyNumber){
					
					switch(binOp){
						case BIN_LT:
						case BIN_GT:
						case BIN_LE:
						case BIN_GE:
						case BIN_EQ:
						case BIN_NEQ:
							resultOpBin = new MyBoolean(false);
							break;
						default:
							resultOpBin = new MyNumber();
							break;
					}
					result.add(resultOpBin);

				} else if (v1 instanceof MyBoolean & v2 instanceof MyBoolean){
					switch(binOp){
						case BIN_AND:
						case BIN_OR:
							resultOpBin = new MyBoolean();
							break;
						default:
							return null;
					}
						result.add(resultOpBin);
				} else {
					return null;
				}
			}
		}
		return result;
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
	
	public static void main(String argv[]){
		Env e = new Env();
		Store s = new Store();
		NumberLiteral i1 = new NumberLiteral("3");
		NumberLiteral i2 = new NumberLiteral("3");
		BinaryOperator bo1 = new BinaryOperator(BIN_PLUS,i1,i2);
		System.out.println(bo1.eval(e,s));
		BinaryOperator bo2 = new BinaryOperator(BIN_TIMES,i1,i2);
		System.out.println(bo2.eval(e,s));
		BinaryOperator bo3 = new BinaryOperator(BIN_GT,i1,i2);
		System.out.println(bo3.eval(e,s));
		BooleanLiteral b1 = new BooleanLiteral(false);
		BooleanLiteral b2 = new BooleanLiteral(true);
		BinaryOperator bo4 = new BinaryOperator(BIN_AND,b1,b2);
		System.out.println(bo4.eval(e,s));
		BinaryOperator bo5 = new BinaryOperator(BIN_OR,b1,b2);
		System.out.println(bo5.eval(e,s));

	}
	
}