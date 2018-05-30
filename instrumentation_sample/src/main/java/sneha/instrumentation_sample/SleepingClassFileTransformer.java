package sneha.instrumentation_sample;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import java.io.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

 
public class SleepingClassFileTransformer implements ClassFileTransformer {
 
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    	//System.out.println("inside SleepingClassFileTransformer's transform!");
        byte[] byteCode = classfileBuffer;
        String path = "C/Users/Sneha/eclipse-workspace/instrumentation_sample";
        //System.out.println("ClassName ="+className.toString());
        if (className.equals("sneha/instrumentation_sample/Sleeping")) {
        	System.out.println("Found class!");
            try {
            	System.out.println("Beginning to instrument!!!");
                ClassPool cp = ClassPool.getDefault(); 
                CtClass cc = cp.get("sneha.instrumentation_sample.Sleeping");
                CtMethod m = cc.getDeclaredMethod("randomSleep");
                String methodName = m.getName();
                
                CtClass[] pTypes = m.getParameterTypes();
                //StringBuilder sbs = new StringBuilder();
                
                //Set path here
                String in = "java.lang.String path = \"/mnt/c/Users/Sneha/eclipse-workspace/instrumentation_sample/serialize_"+cc.getName()+".txt\";";
                
                //Create xstream object here
                in+= "java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(new java.io.File(path), true));";
                in+="com.thoughtworks.xstream.XStream xs = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.xml.StaxDriver());";
            	in+="java.lang.String xml;";
            	in+="java.lang.String completeXML=\"\";";
                //sbs.append( "StringBuilder sbArgs = new StringBuilder();" );
                
            	//Serialize each argument
                for( int i=0; i < pTypes.length; ++i ) {
                    CtClass pType = pTypes[i];
                    in+="xml = xs.toXML($args[" + i + "]);";
                	in+="completeXML+=xml;\n";
                    if(i == pTypes.length-1) {
                    	in+="System.out.println(completeXML);";
                    	in+="System.out.println(\"--------------------------------------------------\");";
                    }
                }
                /*
                sbs.append( "StringBuilder sb = new StringBuilder();" );
                sbs.append("sb.append("+methodName+");");
                sbs.append( "sb.append( \"(\" );" );  
                sbs.append( "sb.append( sbArgs.toString() );" );
                sbs.append( "sbArgs.toString();" );
                sbs.append( "sb.append( \")\" );" );
                System.out.println(in);
                sbs.append("System.out.println(sb.toString());");
                String temp=sbs.toString();*/
    			
                //Write to file
                in+="out.writeObject(completeXML);";
                
                //insert at the beginning of the method execution
                m.insertAt(1,"{" + in + "}");
                
                byteCode = cc.toBytecode();
                cc.detach();
                System.out.println("Finished instrumenting!!!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
 
        return byteCode;
    }
}