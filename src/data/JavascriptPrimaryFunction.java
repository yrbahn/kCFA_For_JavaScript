package data;

public class JavascriptPrimaryFunction implements Value{
	public final static int CREATE_ARRAY = 0;
	private int kind;
	
	public JavascriptPrimaryFunction(int k){
		kind = k;
	}
	
	@Override
	public int compareTo(Value o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getType(){
		return kind;
	}
}