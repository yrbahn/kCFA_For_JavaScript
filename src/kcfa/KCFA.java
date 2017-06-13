package kcfa;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import data.Contour;
import data.Env;
import data.Store;

import syntaxtree.*;

public class KCFA{
	static int k = 1;
	static NextState nextStateVisitor;
	
	public static void setK(int i){
		nextStateVisitor = new NextState();
		k = i;
	}
	
	public static int getK(){
		return k;
	}
	
	public static State makeInitalState(Program p){
		Env env = new Env();
		State state = new State(p,env, new Store(), new Contour());
		p.accept(nextStateVisitor, state); // Set intitalState
		return state;
	}
	
	public static Set<State> explore(State state){
		Set<State> seen = new HashSet<State>();
		Deque<State> todo = new ArrayDeque<State>();
		
		todo.push(state);
		while(!todo.isEmpty()){
			try {
				State nextState = todo.pop();
				System.out.println("====Next====");
				System.out.println(nextState);

				/*System.out.println("====Seen====BEGIN");
				System.out.println(seen);
				System.out.println("====Seen====End");
*/
				if (seen.contains(nextState)){
					System.out.println("SAME!!");
					continue;
				}
			
				seen.add(nextState);
			
				Set<State> newStates = nextState.next(nextStateVisitor);
				if ( newStates != null ) { // Not End of Program
					todo.addAll(newStates);
				}
				
			} catch (GrammarException e){
				System.err.print(e);
			}
		
		}
		
		return seen;
	}
	
	public static Store summarize(Set<State> states){
		Iterator<State> iter = states.iterator();
		if ( !iter.hasNext() )  return null;
		Store firstStore =  iter.next().getStore();
		while(iter.hasNext()){
			firstStore.join(iter.next().getStore());
		}
		
		return firstStore;
	}
}