import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

import data.*;
import syntaxtree.Identifier;

public class Test {
	
	public static void main(String argv[]){
		Map<Addr,Integer> m = new HashMap<Addr,Integer>();
		Contour c = new Contour();
		Bind b1 = new Bind(new Identifier("dddd"),c);
		Bind b2 = new Bind(new Identifier("dddd"),c);
		System.out.println(b1.equals(b2));
		m.put(b1,1);
		m.put(b2, 2);
		System.out.println(m);
	}
}