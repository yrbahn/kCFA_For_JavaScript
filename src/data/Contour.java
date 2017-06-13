package data;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class Contour implements  Addr {
	private List<Integer> list;
	
	public Contour(){
		list = new LinkedList<Integer>();
	}
	
	public Contour(Contour c){
		list = new LinkedList<Integer>(c.list);
	}
	
	private List<Integer> getList(){
		return list;
	}
	
	public void insertFront(Integer i){
		list.add(0,i);
	}
	
	public void insertEnd(Integer i){
		list.add(i);
	}
	
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}
	
	
	public boolean equals(Object o){
		if (this == o)
			return true;
		
		if (o == null)
			return false;
		
		if (getClass() != o.getClass())
			return false;
		
		Contour c = (Contour) o;
		if ( list == null ){
			if (c.getList() != null)
				return false;
		} else if (!list.equals(c.getList())){
			return false;
		}
	
		return true;
	}
	
	public int compareTo(Addr addr) {
		int classCompare = getClass().getName().compareTo(addr.getClass().getName());
		if (classCompare != 0){
			return classCompare;
		}
		
		Contour contour = (Contour) addr;
		Iterator<Integer> l1 = list.iterator();
		Iterator<Integer> l2 = contour.getList().iterator();
		while( l1.hasNext() || l2.hasNext() ){
			if(!l1.hasNext()) return -1;
			if(!l2.hasNext()) return 1;
			Integer ele1 = l1.next();
			Integer ele2 = l2.next();
			int res = ele1.compareTo(ele2);
			if (res != 0){
				return res;
			}
		}
		return 0;
	}

	public Contour take(int k){
		Contour newList = new Contour();
		for(Integer i:list ){
			k--;
			newList.insertEnd(i);
			if (k <= 0 ) break;
		}
		
		return newList;
	}
	
	public Contour tick(Integer ele, int k){
		Contour newList = new Contour();
		newList.list.addAll(list);
		newList.insertFront(ele);
		return newList.take(k);
	}
	
	public Boolean equals(Contour o){
		return list.equals(o.list);
	}
	
	public String toString(){
		return list.toString();
	}
	
	public static void main(String argv[]){
		Contour con = new Contour();
		Contour c1;
		
		c1 = con.tick(45,1);
		c1 = c1.tick(45,1);
		c1 = c1.tick(45,1);
		
		Contour t = new Contour(c1);
		t.insertFront(1);
		System.out.println(c1);	
		System.out.println(t);
	}
	
	
}