package shreya.XStream_simple;

import com.thoughtworks.xstream.XStream;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;

public class XStreamTester {

	public static void main(String[] args) {
	
		XStream xstream = new XStream();
		
		Person joe = new Person("Joe", "Walnes");
		try {
			joe.setPhone(123,"123456");
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		System.out.println("Printing the XML version of the serialized objects");
		String xml = xstream.toXML(joe);
		System.out.println(xml);
		System.out.println("\n");
		System.out.println("\n");
		

		
		

	}

}
