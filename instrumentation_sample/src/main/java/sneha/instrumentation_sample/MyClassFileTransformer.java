package com.jakewharton.picasso;

import javassist.CannotCompileException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
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

 
public class MyClassFileTransformer implements ClassFileTransformer {
 
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    	//System.out.println("inside SleepingClassFileTransformer's transform!");
        byte[] byteCode = classfileBuffer;
        //System.out.println("ClassName ="+className.toString());
        if (className.equals("org/junit/Assert")) {
        	System.out.println("Found class!");
            try {
            	System.out.println("Beginning to instrument!!!");
                ClassPool cp = ClassPool.getDefault(); 
                CtClass cc = cp.get("org.junit.Assert");
				CtMethod[] methods = cc.getDeclaredMethods();
				
				for(CtMethod m:methods) {
					/*final StringBuffer buffer = new StringBuffer();
					String name = m.getName();
					buffer.append("System.out.println(\"Method " + name + " executed\" );");
					m.insertBefore(buffer.toString());
					*/
					String name = m.getName();
					String signature = m.getSignature();
					CtClass[] pTypes = m.getParameterTypes();
					String in ="System.out.println(\"Method " + name + " " +signature+" executed\" );";
					in+="java.lang.String path = \"/mnt/c/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/github-repos/JakeWharton_picasso2-okhttp3-downloader/serialize_"+cc.getName()+".txt\";";
					
					in+= "java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(new java.io.File(path), true));";
					in+="com.thoughtworks.xstream.XStream xs = new com.thoughtworks.xstream.XStream(new com.thoughtworks.xstream.io.xml.StaxDriver());";
					in+="java.lang.String xml;";
					in+="java.lang.String completeXML=\"\";";
				
					//Serialize each argument
					for( int i=0; i < pTypes.length; ++i ) {
						CtClass pType = pTypes[i];
						in+="xml = xs.toXML($args[" + i + "]);";
						in+="completeXML+=xml;\n";
					}

					in+="System.out.println(completeXML);";
					in+="System.out.println(\"************ This is the instrumented line ************\");"; 
    			
                //Write to file
					in+="out.writeObject(completeXML);";

                m.insertAfter("{" + in + "}");
					
				}

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
