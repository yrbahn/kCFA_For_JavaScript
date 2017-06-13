package visitor;
import syntaxtree.*;
import java.util.*;

/**
 * All void visitors must implement this interface.
 */

@SuppressWarnings("all")
public interface Visitor {
	public void visit(Program n);
	public void visit(Body n);
	public void visit(DeclList n);
	public void visit(ExpList n);
	public void visit(StmtList n);
	public void visit(EmptyStatement n);
	public void visit(AssignStatement n);
	public void visit(GotoStatement n);
    public void visit(ReturnStatement n);
    public void visit(IfStatement n);
    public void visit(TryStatement n);
    public void visit(ThrowStatement n);
    public void visit(Function n);
    public void visit(ConditionalExpression n);
    public void visit(BinaryOperator n);
    public void visit(NumberLiteral n);
    public void visit(NullLiteral n);
    public void visit(ThisLiteral n);
    public void visit(BooleanLiteral n);
    public void visit(Selection n);
    public void visit(StringLiteral n);
    public void visit(Identifier n);
    public void visit(NewExpression n);
    public void visit(CallExpression n);
    public void visit(FieldExpression n);
    public void visit(UnaryOperator n);
}

