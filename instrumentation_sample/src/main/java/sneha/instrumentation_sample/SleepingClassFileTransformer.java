package sneha.instrumentation_sample;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.io.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

 
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
               
                
                /*To instrument method calls of a system class*/
                m.instrument(new ExprEditor() {
                	public void edit(final MethodCall m1) throws CannotCompileException {
                		System.out.println("className of m1="+m1.getClassName());
                		System.out.println("Method name of m1="+m1.getMethodName());
                		if("sneha.instrumentation_sample.Sleeping".equals(m1.getClassName()) && "randomSleep".equals(m1.getMethodName())) {
                			System.out.println("Inside if ......");
                			 m1.replace("{long startMs = System.currentTimeMillis(); "
                			 		+ "$_ = $proceed($$); "
                			 		+ "long endMs = System.currentTimeMillis(); "
                			 		+ "System.out.println(\"Executed in ms: \" + (endMs-startMs));}");					 
                		}
                	}
                });
                
                
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
            	System.out.println("#Arguments="+pTypes.length);
                for( int i=0; i < pTypes.length; ++i ) {
                    CtClass pType = pTypes[i];
                    in+="xml = xs.toXML($args[" + i + "]);";
                	in+="completeXML+=xml;\n";
                    /*if(i == pTypes.length-1) {
                    	
                    }*/
                }
                in+="System.out.println(completeXML);";
                in+="System.out.println(\"********* This is the instrumentated line **********\");";
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
                //m.insertAt(1,"{" + in + "}");
                //m.insertAfter("{" + in + "}");
                m.insertBefore("{" + in + "}");
                
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