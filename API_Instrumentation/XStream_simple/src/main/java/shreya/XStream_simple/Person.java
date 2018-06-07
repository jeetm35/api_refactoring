package shreya.XStream_simple;

import shreya.XStream_simple2.*;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.xml.stream.XMLStreamException;

public class Person {
	  private static final Method NULL = null;
	private String firstname;
	  private String lastname;
	  private PhoneNumber phone;
	  private PhoneNumber fax;
	  
	  public Person(String first,String last) {
		  this.firstname = first;
		  this.lastname = last;
		  
	  }
	  
	  @SuppressWarnings("restriction")
	public void setPhone(int n, String s ) throws XMLStreamException {
		try {
		  Method m = new Object() {}.getClass().getEnclosingMethod();
	      XStreamClass.printInfo(m.getParameters());
	      if(m==NULL) {
	    	  
	    	  throw new XMLStreamException();
	      }
	    
	      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			
		}
		
		
	  }
	 
	  
	  
}
