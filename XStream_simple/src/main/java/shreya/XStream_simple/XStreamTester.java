package shreya.XStream_simple;

import com.thoughtworks.xstream.XStream;

public class XStreamTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XStream xstream = new XStream();
		
		Person joe = new Person("Joe", "Walnes");
	//	joe.setPhone(new PhoneNumber(123, "1234-456"));
	//	joe.setFax(new PhoneNumber(123, "9999-999"))
		
		String xml = xstream.toXML(joe);
		
		System.out.println(xml);
		

	}

}
