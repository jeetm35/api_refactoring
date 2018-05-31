package sneha.instrumentation_sample;

import com.thoughtworks.xstream.XStream;

import com.thoughtworks.xstream.io.xml.StaxDriver;

public class AgentTest {

	public static void main(String[] args) throws InterruptedException{
    	System.out.println("Executing main...");
    	Sleeping sleeping = new Sleeping();
        sleeping.randomSleep("Sneha", "Shankar");
        sleeping.randomSleep("ABC", "XYZ");
        System.out.println("Exit main...");
    }
}