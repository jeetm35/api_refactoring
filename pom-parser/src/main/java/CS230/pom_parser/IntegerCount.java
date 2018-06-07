package CS230.pom_parser;

import java.io.Serializable;

public class IntegerCount implements Serializable,Comparable<IntegerCount>{

	int count;
	public IntegerCount(int a ){
		this.count = a;
	}
	
	public void increment(){
		this.count++;
	}
	
	public static void main(String[] args) {
		
		try {
			Class.forName("java.lang.String");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int compareTo(IntegerCount o) {
		// TODO Auto-generated method stub
		return new Integer(count).compareTo(new Integer(o.count));
		
	}
	public String toString(){
		return count+"";
	}
}
