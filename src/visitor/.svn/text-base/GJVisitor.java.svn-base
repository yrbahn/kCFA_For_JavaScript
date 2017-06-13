package visitor;
import syntaxtree.*;
import kcfa.*;

import java.util.*;

/**
 * All GJ visitors must implement this interface.
 */

@SuppressWarnings("all")
public interface GJVisitor<R,A> {

	public  R visit(Program n, A argu);
	public  R visit(Body n, A argu);
	public  R visit(DeclList n, A argu);
	public  R visit(EmptyStatement n, A argu) throws GrammarException;
	public  R visit(AssignStatement n, A argu) throws GrammarException;
	public  R visit(GotoStatement n, A argu) throws GrammarException;
    public  R visit(ReturnStatement n, A argu) throws GrammarException;
    public  R visit(IfStatement n, A argu) throws GrammarException;
    public  R visit(TryStatement n, A argu) throws GrammarException;
    public  R visit(ThrowStatement n, A argu) throws GrammarException;
    public  R visit(Function n, A argu) ;
    public  R visit(ConditionalExpression n, A argu) ;
    public  R visit(BinaryOperator n, A argu) ;
    public  R visit(NumberLiteral n, A argu) ;
    public  R visit(NullLiteral n, A argu);
    public  R visit(ThisLiteral n, A argu);
    public  R visit(BooleanLiteral n, A argu);
    public  R visit(Selection n, A argu);
    public  R visit(StringLiteral n, A argu) ;
    public  R visit(Identifier n, A argu) ;
    public  R visit(NewExpression n, A argu);
    public  R visit(CallExpression n, A argu) ;
    public  R visit(FieldExpression n, A argu);
    public  R visit(StmtList n, A argu);
    public  R visit(UnaryOperator n, A argu);
    
}
