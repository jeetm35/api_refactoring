package shreya.XStream_simple2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.ListIterator;

import javax.xml.stream.XMLStreamException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.StaxWriter;

public class XStreamClass {

	public static void printInfo(Parameter[] L) throws IOException, XMLStreamException {
		
		try {
		String path = "output.txt" ;
		
		File file = new java.io.File(path);
		FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		XStream xs = new XStream(new StaxDriver());

		String xml;
		String completeXML="<functionCall>";
		StaxDriver drv = new StaxDriver();
    	XStream xstream = new XStream();
    	StringWriter strWriter = new StringWriter();
    	StaxWriter sw = new StaxWriter(drv.getQnameMap(), drv.getOutputFactory().createXMLStreamWriter(strWriter), false, true);
    	
    	
    	System.out.println("Printing the parameters");
		for (Parameter parameter :L) {
		    // Do something with parameter
		
			System.out.println(parameter.toString());
			xstream.marshal(parameter, sw);
			xml = strWriter.toString();
			completeXML += xml;
			
		  }
		
		completeXML += "</functionCall>";
        System.out.println("************ Printing complete xml ************");

		System.out.println(completeXML);
		bw.write(completeXML);
		bw.close();
		

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
