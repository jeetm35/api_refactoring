package sneha.instrumentation_sample;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class AgentTest {
	
	
	public static void main(String[] args) throws InterruptedException{
    	System.out.println("Executing main...");
    	Sleeping sleeping = new Sleeping();
        sleeping.randomSleep("Sneha");
        System.out.println("Exit main...");
    }
}