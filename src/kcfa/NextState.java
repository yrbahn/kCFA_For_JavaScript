package kcfa;
import syntaxtree.*;
import visitor.GJVisitor;

import java.util.TreeSet;
import java.util.SortedSet;
import java.util.HashSet;
import java.util.Set;

import data.Env;
import data.MyString;
import data.Store;
import data.Contour;
import data.Bind;
import data.Value;
import data.Closure;
import data.Ret;
import data.Field;
import data.Ex;
import data.Addr;
import data.Location;
import data.JavascriptPrimaryFunction;

public class NextState implements GJVisitor< SortedSet<State>, State> {
	
	public SortedSet<State> checkBlockEnd(State state){
		SortedSet<State> res = new TreeSet<State>();
		
		Contour fp  = state.getFPtr();
		Env     env = state.getEnv();
		Store store = state.getStore();
		
		SortedSet<Value> setFp = store.get(fp);
		if (setFp != null ){
			for(Value v:setFp){
				if ( v instanceof Ret){
					Ret ret = (Ret) v;
					Addr addr = env.lookup(new Identifier("this"));
					Env retEnv = ret.getEnv();
					if (addr != null){
						SortedSet<Value> d = store.get(addr);
						Store newStore = new Store(store);
						Addr addr2 = retEnv.lookup(ret.getVar());
						newStore.insert(addr2,d);
						State newState = new State();
						newState.setStatement(ret.getStmt());
						newState.setEnv(retEnv);
						newState.setFPtr(ret.getContour());
						newState.setStore(newStore);
						res.add(newState);
					}
				} else if ( v instanceof Ex){
					Ex ex = (Ex) v;
					State newState = new State();
					newState.setStatement(ex.getStmt1());
					newState.setEnv(ex.getEnv());
					newState.setStore(new Store(store));
					newState.setFPtr(ex.getContour());
					res.add(newState);
				}
			}
			return res;
		} 
		
		// Reach to the end of Program
		res.add(state);
		return res;
	}
	
	public SortedSet<State> visit(Program n, State argu) {
		
		State nextState = argu;
		
		nextState.setStatement(n.getBody());
		nextState.setEnv(new Env());
		
		SortedSet<State> result = new TreeSet<State>();
		result.add(nextState);
		
		return result;
	}

	public  SortedSet<State> visit(Body n, State argu) {
		State nextState = new State();		
		Env e = argu.getEnv(); //
		Store store = argu.getStore();
		DeclList dl = n.getDeclList();  
		for(int i=0; i < dl.size(); i++){       
			Identifier v = dl.elementAt(i);
			Bind bind = new Bind(v,argu.getFPtr());
			e.put(v, bind);
			store.insert(bind,null);
		}
				
		nextState.setEnv(e);
		nextState.setStore(argu.getStore());
		nextState.setFPtr(argu.getFPtr());
		Statement nextStmt = n.getStmtList().elementAt(0);
		if (nextStmt == null ){
			return this.checkBlockEnd(nextState);
		}
		nextState.setStatement(nextStmt); // move to the first stmt in body.
		
		SortedSet<State> result = new TreeSet<State>();
		result.add(nextState);
		
		return result;
	}

    // /////////////////////////////////////////////////////////////////////////
    // START OF STATEMENT
    ////////////////////////////////////////////////////////////////////////////
	
	public  SortedSet<State> visit(AssignStatement n, State argu) throws GrammarException{	
	  	System.out.println("ASSIGN VISST");
	    
		SortedSet<State> result = new TreeSet<State>();
		Env   env      = argu.getEnv(); //Env
		//Env   newEnv   = new  Env(env); //NewEnv

		Contour fPtr   = argu.getFPtr();
		Store store    = argu.getStore();
		int label = n.getLabel();
		//int   label    = ((Statement) argu.getStatement()).getLabel();
		System.out.println(label);
		
		Expression left = n.getLeft();
		if (left instanceof Identifier) {
			Identifier var = (Identifier) left;
			Addr varAddr = env.lookup(var);
			if (varAddr == null){ // variable var was not declared!
				varAddr = new Bind(var,new Contour());
				env.putTopEnv(var, varAddr);
			} /*else {
				varBind = new Bind(var,fPtr);
			} */
	
			if ( n.getRight() instanceof NewExpression){
				Contour newFPtr    = fPtr.tick(label, KCFA.getK()); // new Fptr

				NewExpression newExp = (NewExpression) n.getRight();
				Addr addr = env.lookup(newExp.getVariable());
				if (addr == null ){
					throw new GrammarException("Cannot find the constructor!");
					
				}
			
				SortedSet<Value> constructList = store.get(addr);
				for (Value v: constructList){
					if ( !(v instanceof Closure)) {
						System.out.println(v);
						throw new GrammarException("variable should be bounded with a closure!");
					}
				
					Closure clo        = (Closure) v;  // Closure
					Env newCloEnv      = clo.getEnv().extendEnv(); // new Env in closure
					Store newStore     = new Store(store); // new Store
					Function f         = clo.getFunction(); // function in closure
					DeclList fDeclList = f.getDeclList(); // Declaration List in Closure
					DeclList fArgList  = f.getArgList(); // Argument List in Closure
					ExpList  expList   = newExp.getExpList(); // Expression List in New Statement
					Statement nextStmt;
					
					// Update NewCloEnv
					if ( fArgList.size() != expList.size() ){
						throw new GrammarException("The number of formal arguments is different with the number of actual arguments");
					}
						
					for(int i=0 ; i < fDeclList.size(); i++){
						Identifier decl = fDeclList.elementAt(i);
						Bind bind = new Bind(decl,newFPtr);
						newCloEnv.put(decl, bind);
						newStore.insert(bind, null);
					}
						
					for(int i=0 ; i < fArgList.size(); i++){
						Identifier arg = fArgList.elementAt(i);
						Bind bind = new Bind(arg,newFPtr);
						newCloEnv.put(arg, bind);
						SortedSet<Value> expValue = expList.elementAt(i).eval(env,store);
						newStore.insert(bind, expValue);
					}
						
					newCloEnv.put(new Identifier("this"), new Bind(new Identifier("this"), newFPtr));
					Ret ret = new Ret(var, env, n.nextStmt(), fPtr);
					SortedSet<Value> setFPtr = new TreeSet<Value>();
					setFPtr.add(ret);
					
					SortedSet<Value> setLoc = new TreeSet<Value>();
					setLoc.add(new Location(var.toString(), fPtr));
					
					newStore.insert(new Bind(new Identifier("this"), newFPtr), setLoc);
					newStore.insert(newFPtr, setFPtr);				
						
				// New State Setting
					State newState = new State();
					newState.setEnv(newCloEnv);
					newState.setFPtr(newFPtr);
					newState.setStore(newStore);
					nextStmt =  f.getBody().getStmtList().elementAt(0);
					if (nextStmt == null) return checkBlockEnd(newState);
					newState.setStatement(nextStmt);
					result.add(newState);
						
				} // for-end
				
			} else if ( n.getRight() instanceof CallExpression){ // Call Statement : Expression.CallExpr(ExpList))			
				System.out.println("Call Function");
				Contour newFPtr    = fPtr.tick(label, KCFA.getK()); // new Fptr
				CallExpression callE = (CallExpression) n.getRight();
				System.out.println("EE:"+callE.getTarget());
				SortedSet<Value> setVal = callE.getTarget().eval(env, store);
				if (setVal != null){
					for(Value v:setVal){
						if ( v instanceof Closure ) {
		
							Closure clo = (Closure) v;
							Function f  = clo.getFunction();
							Env cloEnv  = clo.getEnv();
						
							DeclList fArgList = f.getArgList();
							DeclList fDecList = f.getDeclList();
							ExpList callArgList = callE.getExpList();
							if (fArgList.size() != callArgList.size() - 1){	
								throw new GrammarException("the number of agruments is different!");
							}
					
							Env newCloEnv  = cloEnv.extendEnv();
							Store newStore = new Store(store);
									
							for (int i=0; i < fArgList.size(); i++){
								Identifier id = fArgList.elementAt(i);
								Bind bind = new Bind(id, newFPtr);
								newCloEnv.put(id, bind);
								SortedSet<Value> argVal = callArgList.elementAt(i+1).eval(env, store);
								newStore.insert(bind, argVal);
							}
									
							for (int i=0; i < fDecList.size(); i++){
								Identifier id = fDecList.elementAt(i);
								Bind bind = new Bind(id,newFPtr);
								newCloEnv.put(id, bind);
								newStore.insert(bind, null);
							}	
			
							Identifier thisId = new Identifier("this");
							Bind thisBind = new Bind(thisId, newFPtr);
							newCloEnv.put(thisId, thisBind);
									
							SortedSet<Value> objLoc = callArgList.elementAt(0).eval(env,store);
							newStore.insert(thisBind, objLoc);
									
							Ret ret = new Ret(var, env, n.nextStmt(), fPtr);
							SortedSet<Value> setFPtr = new TreeSet<Value>();
							setFPtr.add(ret);
									
							newStore.insert(newFPtr, setFPtr);
									
							State nextState = new State();
							nextState.setEnv(newCloEnv);
							nextState.setStore(newStore);
							nextState.setFPtr(newFPtr);
					
							Statement nextStmt = (Statement) f.getBody().getStmtList().elementAt(0);
							if (nextStmt == null) return checkBlockEnd(nextState);
							nextState.setStatement(nextStmt);
						
							result.add(nextState);	
						} else if (v instanceof JavascriptPrimaryFunction) { // Check Array();
							JavascriptPrimaryFunction jf = (JavascriptPrimaryFunction) v;
							if (jf.getType() == JavascriptPrimaryFunction.CREATE_ARRAY){
								
								System.out.println("!!!IMPORT");
								ExpList eList = callE.getExpList();
								System.out.println("Array");
								System.out.println(callE.getExpList());
								Location loc = new Location(var.toString(), fPtr);
								Bind bind = new Bind(var,fPtr);
								env.put(var, bind);
								SortedSet<Value> setLoc = new TreeSet<Value>();
								setLoc.add(loc);
								store.insert(bind, setLoc);
								State nextState = new State();
								nextState.setEnv(env);
								nextState.setStore(store);
								nextState.setFPtr(fPtr);
								Statement nextStmt = n.nextStmt();
								nextState.setStatement(nextStmt);
								result.add(nextState);
								
							} else {
								System.out.println("DDDDEDAFAFD");
								throw new GrammarException("!!!variable should be bounded with a Array or Closure");
							}
						} // for
					} 
				} else {
					// Cannot find the function definition.
					State nextState = new State();
					nextState.setEnv(env);
					nextState.setStore(store);
					nextState.setFPtr(fPtr);
			
					Statement nextStmt = n.nextStmt();
					nextState.setStatement(nextStmt);
					result.add(nextState);
				}
			} else {
				State nextState = new State();
				//Store newStore = new Store(store);
				
				
				System.out.println("DDD:"+n.getRight());
				SortedSet<Value> setExpValue = n.getRight().eval(env, store);
				//newStore.insert(varBind, setExpValue);
				
				store.insert(varAddr,setExpValue);
			
				nextState.setEnv(env);
				nextState.setStore(store);
				nextState.setFPtr(fPtr);
				Statement nextStmt = n.nextStmt();
				if ( nextStmt == null ){
					return checkBlockEnd(nextState);
				}
	
				nextState.setStatement(nextStmt);
				
				result.add(nextState);
			}
		} else { //Field Assignment
			if ( !( n.getLeft() instanceof Selection )) {
				throw new GrammarException("Exp should be a selection expr");
			}
			
			State nextState = new State();
			
			SortedSet<Value> setRight = n.getRight().eval(env,store);
			System.out.println("Right:"+setRight);
			if ( setRight == null){
				System.err.println("Error");
			}
			
			Selection sel = (Selection) n.getLeft();
			syntaxtree.Expression target = sel.getTarget();
			Set<Value> setTarget = target.eval(env, store);
			if (setTarget == null){/*
				if (target instanceof Identifier){
					Identifier ident = (Identifier) target;
					setTarget = new TreeSet<Value>();
					Bind bind = new Bind(ident, fPtr);
					env.putTopEnv(ident, bind);
					SortedSet<Value> setLoc = new TreeSet<Value>();
					setLoc.add(new Location(ident.toString(), fPtr));
					store.insert(bind, setLoc);
					setTarget = setLoc;
				} else {
					System.err.println("Should be a ident");
				}
				*/
				System.err.println("Should be a ident DEBUG");	
			}
			
			Expression elem = sel.getElement();
			Set<Value> setElem = elem.eval(env, store);
			if (setElem == null){
				System.err.println("Error i:"+elem);
			}
			
		
			Field f = null;
			for(Value v:setTarget){
				if (!(v instanceof Location)){
					throw new GrammarException("Val should be a Location");
				}
				
				Location loc = (Location) v;
				if (setElem == null){
					f = new Field(loc, elem.toString());
					store.insert(f,setRight);	
				} else {
					for(Value e:setElem){
						if (!(e instanceof MyString)){
							throw new GrammarException("Val should be a String");
						}
						
						//MyString str = (MyString) v1;
						f = new Field(loc, e.toString());
						store.insert(f,setRight);		
					}
				}

			}
			
			nextState.setEnv(env);
			nextState.setStore(store);
			nextState.setFPtr(fPtr);
			
			Statement nextStmt = (Statement) n.nextStmt();
			if ( nextStmt == null ){
				return checkBlockEnd(nextState);
			}
			nextState.setStatement(nextStmt);

			result.add(nextState);

		}
		
		return result;
	}

	public  SortedSet<State> visit(GotoStatement n, State argu) throws GrammarException{
		Contour fp  = argu.getFPtr();
    	Env env     = argu.getEnv();
    	Store store = argu.getStore();
    	SortedSet<State> result = new TreeSet<State>();
    
    	State newState = new State();
    	Statement nextStmt = Statement.getStatement(n.getGotoLabel());
    	if  (nextStmt == null){
    		throw new GrammarException("there is no statemnet with the label");
    	}
    	
    	newState.setStatement(nextStmt);
    	newState.setEnv(env);
    	newState.setStore(store);
    	newState.setFPtr(fp);
    	result.add(newState);
    	
    	return result;
	}
	
    public SortedSet<State> visit(ReturnStatement n, State argu) throws GrammarException{
    	System.out.println("RETURN VISST");
    	
    	Contour fp  = argu.getFPtr();
    	Env env     = argu.getEnv();
    	Store store = argu.getStore();
    	SortedSet<State> result = new TreeSet<State>();
    	
    	SortedSet<Value> setVal = store.get(fp);
    	for(Value v: setVal){
    		if (v instanceof Ret){
    			Ret ret = (Ret) v;
    			SortedSet<Value> setValRet = n.getExpr().eval(env, store);
    			Store newStore = new Store(store);
    			Addr addr = ret.getEnv().lookup(ret.getVar());
    			newStore.insert(addr, setValRet);
    			
    			State newState = new State();
    			newState.setStatement(ret.getStmt());
    			newState.setEnv(ret.getEnv());
    			newState.setStore(newStore);
    			newState.setFPtr(ret.getContour());
    			
    			result.add(newState);
    			
    		} else if ( v instanceof Ex){
    			Ex ex = (Ex) v;
    			
    			State newState = new State();
    			newState.setStatement(n);
    			newState.setEnv(env);
    			newState.setStore(store);
    			newState.setFPtr(ex.getContour());
    			
    			result.add(newState);
    		} else {
    			throw new GrammarException("Val should be a Ret or Ex");
    		}
 
    	}
    	
    	return result;
	}
    
    public SortedSet<State> visit(IfStatement n, State argu) throws GrammarException {
      	System.out.println("IF VISST");
        
    	Env env     = argu.getEnv();
    	Store store = argu.getStore();
    	Contour fp  = argu.getFPtr();
    	int ifLabel   = n.getIfLabel();
    	
    	SortedSet<State> result = new TreeSet<State>();
    	//SortedSet<Value> setValIf = n.getCondition().eval(env,store);
    	//SortedSet<Value> setValIf = new TreeSet<Value>();
    	
    	State nextState = new State();
   		nextState.setEnv(env);
    	nextState.setStore(store);
    	nextState.setFPtr(fp);
    	Statement nextStmt;
    	nextStmt = Statement.getStatement(ifLabel);
    	System.out.println("A:"+ifLabel);
    	if  (nextStmt == null){
    		throw new GrammarException("There is no statement with the label");
       	}
    		
    	nextState.setStatement(nextStmt);
    	result.add(nextState);
    	
    	State nextState2 = new State();
   		nextState2.setEnv(env);
    	nextState2.setStore(store);
    	nextState2.setFPtr(fp);
    	nextStmt = (Statement) n.nextStmt();
		if ( nextStmt == null ){
    		return checkBlockEnd(nextState);
    	}
    		
   		nextState2.setStatement(nextStmt);
    	result.add(nextState2);
    	
    	return result;
	}
    
    public SortedSet<State> visit(TryStatement n, State argu){
       	Contour fp  = argu.getFPtr();
    	Env env     = argu.getEnv();
    	Store store = argu.getStore();
    	SortedSet<State> result = new TreeSet<State>();
  
    	Contour newFp = fp.tick(n.getLabel(), KCFA.getK());
    	Store newStore = new Store(store);
    	
    	Statement nextStmt1 = n.getCatchBlock().elementAt(0);
    	Statement nextStmt2 = (Statement) n.nextStmt();
    	
    	Ex ce = new Ex(n.getCatchVar(), env, nextStmt1, fp, nextStmt2);
    	SortedSet<Value> setVal = new TreeSet<Value>();
    	setVal.add(ce);
    	
    	newStore.insert(newFp, setVal);    	
    	State nextState = new State();

    	nextState.setEnv(env);
    	nextState.setStore(newStore);
    	nextState.setFPtr(newFp);
    	Statement nextStmt = n.getTryBlock().elementAt(0);
    	if (nextStmt == null ) {
    		return checkBlockEnd(nextState);
    	}
    	nextState.setStatement(nextStmt);
    	
    	result.add(nextState);
    	return result;
	}
    
    public SortedSet<State> visit(ThrowStatement n, State argu) throws GrammarException{
      	Contour fp  = argu.getFPtr();
    	Env env     = argu.getEnv();
    	Store store = argu.getStore();
    	SortedSet<State> result = new TreeSet<State>();
  
    	SortedSet<Value> setValFp = store.get(fp);
    	for (Value v: setValFp){
    		if ( v instanceof Ret){
    			Ret ret = (Ret) v;
    			State newState = new State();
    			newState.setStatement(n);
    			newState.setEnv(env);
    			newState.setStore(store);
    			newState.setFPtr(ret.getContour());
    			
    			result.add(newState);
    			
    		} else if (v instanceof Ex){
       			Ex ce = (Ex) v;
    			State newState = new State();
    			Store newStore = new Store(store);
    			SortedSet<Value> setValExpr = n.getExpr().eval(env, store);
    			Addr addr = ce.getEnv().lookup(ce.getVar());
    			newStore.insert(addr, setValExpr);
    			newState.setStatement(n);
    			newState.setEnv(env);
    			newState.setStore(newStore);
    			newState.setFPtr(ce.getContour());
    			
    			result.add(newState);
    		} else {
    			throw new GrammarException("value should be a Ret or Ex");
    		}
    	}
    	
    	return result;	
    }
    
    public  SortedSet<State> visit(EmptyStatement n, State argu) throws GrammarException{
    	System.out.println("Empty Visit");
    	Contour fp  = argu.getFPtr();
    	Env env     = argu.getEnv();
    	Store store = argu.getStore();
    	SortedSet<State> result = new TreeSet<State>();
    
    	State newState = new State();
    	Statement nextStmt = n.nextStmt();
    	if  (nextStmt == null){
    		throw new GrammarException("there is no statemnet with the label");
    	}
    	
    	newState.setStatement(nextStmt);
    	newState.setEnv(env);
    	newState.setStore(store);
    	newState.setFPtr(fp);
    	result.add(newState);
    	
    	return result;
	}
	
    // /////////////////////////////////////////////////////////////////////////
    // END OF STATEMENT
    ////////////////////////////////////////////////////////////////////////////
    
	public  SortedSet<State> visit(DeclList n, State argu){
		return null;
	}
	
    public SortedSet<State> visit(Function n, State argu){
    	return null;
	}
    
    public SortedSet<State> visit(ConditionalExpression n, State argu){
		return null;
	}
    
    public  SortedSet<State> visit(BinaryOperator n, State argu){
		return null;
	}
    
    public SortedSet<State> visit(NumberLiteral n, State argu){
		return null;
	}
    
    public SortedSet<State> visit(NullLiteral n, State argu){
		return null;
	}
    
    public SortedSet<State> visit(ThisLiteral n, State argu){
		return null;
	}
    
    public SortedSet<State> visit(BooleanLiteral n, State argu){
		return null;
	}
    
    public SortedSet<State> visit(Selection n, State argu){
		return null;
	}
    
    public SortedSet<State> visit(StringLiteral n, State argu){
		return null;
	}
    
    public SortedSet<State> visit(Identifier n, State argu){
		return null;
	}
    
    public SortedSet<State> visit(NewExpression n, State argu){
		return null;
	}
    
    public SortedSet<State> visit(CallExpression n, State argu){
		return null;
	}
    

    public SortedSet<State> visit(FieldExpression n, State argu){
    	return null;
    }
    
    public SortedSet<State> visit(StmtList n, State argu){
    	return null;
    }
    
    public SortedSet<State> visit(UnaryOperator n, State argu){
    	return null;
    }
    

}