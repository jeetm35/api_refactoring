package sneha.instrumentation_sample;

import java.lang.instrument.Instrumentation;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Agent {
 
    public static void premain(String agentArgs, Instrumentation inst) {
         System.out.println("Executing premain....");
        // registers the transformer
        inst.addTransformer(new SleepingClassFileTransformer());
    }
}