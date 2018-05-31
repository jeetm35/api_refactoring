package sneha.instrumentation_sample;

import com.thoughtworks.xstream.XStream;

import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.junit.Test;

public class AgentTest {
	@Test
	public void testInstrumentation() throws InterruptedException{
    	System.out.println("Executing main...");
    	Sleeping sleeping = new Sleeping();
        sleeping.randomSleep("Sneha", "Shankar");
        sleeping.randomSleep("ABC", "XYZ");
        System.out.println("Exit main...");
    }
}
