package CS230.pom_parser;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import junit.framework.Assert;


public class sample {

	private int add(App c){
		int a=0,b=0;
		return a+b;
	}
	protected String hello(){
		return "Hello World :) ";
	}
	private static void haha(){
		
	}
	
	static public int ya(){
		return 0;
		
	}
	public void  foo(){
		//int c= add(10,2);
		//String f=hello();
		//App a=new App();
		//App.just();
		
		Assert.assertFalse(5<10);
	}
	
	
	public void stuff() throws IOException{
		File file = new File("c:\\file.xml");
	      InputStream inputStream= new FileInputStream(file);
	      Reader reader = new InputStreamReader(inputStream,"UTF-8");
	      reader.read();
	      System.out.println( 2 < 3);
	}
	
}