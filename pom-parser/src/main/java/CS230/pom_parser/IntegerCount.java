package CS230.pom_parser;

import java.io.Serializable;

public class IntegerCount implements Serializable{

	int count;
	public IntegerCount(int a ){
		this.count = a;
	}
	
	public void increment(){
		this.count++;
	}
}
