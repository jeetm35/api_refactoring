package testing.instrumentationTest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import testing.instrumentationTest.App;
public class sample {

	public int add(int a, int b){
		return a+b;
	}
	public String hello(){
		return "Hello World :) ";
	}
	
	/*public void  foo(){
		int c= add(10,2);
		String f=hello();
		App a=new App();
		a.just();
	}*/
	
	public void stuff() throws IOException{
		File file = new File("c:\\file.xml");
	      InputStream inputStream= new FileInputStream(file);
	      Reader reader = new InputStreamReader(inputStream,"UTF-8");
	      reader.read();
	      
	}
}
