package sneha.instrumentation_sample;

import com.thoughtworks.xstream.XStream;

import com.thoughtworks.xstream.io.xml.StaxDriver;

public class AgentTest {

	public static void main(String[] args) throws InterruptedException{
    	System.out.println("Executing main...");
    	Sleeping sleeping = new Sleeping();
    	XStream xstream = new XStream(new StaxDriver());
    	String test = "SnehaS";
    	String xml = xstream.toXML(test);
    	//System.out.println("Printing serialized:");
    	//System.out.println(xml);
        sleeping.randomSleep("Sneha", "Shankar");
        System.out.println("Exit main...");
    }
}