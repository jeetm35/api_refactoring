package myPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.xml.stream.XMLStreamException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.StaxWriter;

public class myXstreamClass {
    
    private static StringWriter strWriter;
    
    public static XStream getXstream() {
        return new XStream(new StaxDriver());     
    }
    
    public static StringWriter getStrWriter() {
        return strWriter ;     
    }
     
    public static  StaxWriter getStaxWriter() {
        
        StaxDriver drv = new StaxDriver();
        strWriter = new StringWriter();
        StaxWriter sw = null;
        try {
            sw= new StaxWriter(drv.getQnameMap(), drv.getOutputFactory().createXMLStreamWriter(strWriter), false, true);
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return sw;  
    }

    
    public static void printFile(String xml, Method m ) {
        String root = "/Users/shreya/eclipse/Workspace/InstrumentationHelper/XmlDump";
        String className = m.getDeclaringClass().getCanonicalName();
        String methodName = m.getName();
        String fileName=  root+ "/"+className+"."+methodName+"_";
        for(Object i: m.getParameters()) {
            String name = i.toString();
            name=name.replaceAll(" ", ".");
            fileName += name+"_";              
        }
        
        fileName+=m.getReturnType().toString();
//        System.out.println("Filename = "+fileName);
        
        File file = new File(fileName);
        FileWriter fw = null ;
        try {
            fw = new FileWriter(file.getAbsoluteFile(),true); 
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(xml);
            bw.close();
            
        } catch (IOException e) {
   
            e.printStackTrace();
        }
    

    }
    
}

