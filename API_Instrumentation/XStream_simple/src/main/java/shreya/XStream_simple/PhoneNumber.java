package shreya.XStream_simple;
import shreya.XStream_simple2.*;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.xml.stream.XMLStreamException;


public class PhoneNumber {
	  private int code;
	  private String number;
	  
	  
	  public PhoneNumber(int code,String number) throws IOException, XMLStreamException {
		  this.code = code;
		  this.number = number;
		  
		  Method m = new Object() {}.getClass().getEnclosingMethod();
		  XStreamClass.printInfo(m.getParameters());
	  }
}
