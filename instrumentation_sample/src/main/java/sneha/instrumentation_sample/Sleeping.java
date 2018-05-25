package sneha.instrumentation_sample;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Sleeping {
	public void randomSleep(String name) throws InterruptedException {
		// randomly sleeps between 500ms and 1200s
		long randomSleepDuration = (long) (500 + Math.random() * 700);
		System.out.printf(name+" Sleeping for %d ms ..\n", randomSleepDuration);
		Thread.sleep(randomSleepDuration);
		}
}