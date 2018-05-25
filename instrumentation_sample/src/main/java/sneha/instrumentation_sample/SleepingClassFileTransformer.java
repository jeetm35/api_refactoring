package sneha.instrumentation_sample;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
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

 
public class SleepingClassFileTransformer implements ClassFileTransformer {
 
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    	//System.out.println("inside SleepingClassFileTransformer's transform!");
        byte[] byteCode = classfileBuffer;
        //System.out.println("ClassName ="+className.toString());
        if (className.equals("sneha/instrumentation_sample/Sleeping")) {
        	System.out.println("Found class!");
            try {
            	System.out.println("Beginning to instrument!!!");
                ClassPool cp = ClassPool.getDefault(); 
                CtClass cc = cp.get("sneha.instrumentation_sample.Sleeping");
                CtMethod m = cc.getDeclaredMethod("randomSleep");
                //cp.importPackage("com.thoughtworks.xstream.XStream");
                //cp.importPackage("com.thoughtworks.xstream.io.xml.StaxDriver");
                //System.out.println("Signature of method is:"+m.getSignature().toString());
                m.addLocalVariable("elapsedTime", CtClass.longType);
                
                //m.insertBefore("elapsedTime = System.currentTimeMillis();");
                //m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"+ "System.out.println(\"Method Executed in ms: \" + elapsedTime);}");
                String methodName = m.getName();
               
                //m.insertAt(1, "System.out.println(\"Val=\"+name);");
                
                //m.insertAfter("{System.out.println(\"Val=\"+$1);}");
                
                CtClass[] pTypes = m.getParameterTypes();
                StringBuilder sbs = new StringBuilder();
                sbs.append( "StringBuilder sbArgs = new StringBuilder();" );
                //sbs.append( "sbArgs.append( System.identityHashCode( $0 ) );" );
                for( int i=0; i < pTypes.length; ++i ) {
                    CtClass pType = pTypes[i];
                    	//System.out.println("argVal=");
                    	if(i == pTypes.length-1)
                    		sbs.append( "sbArgs.append( $args[" + i + "]);" );
                    	else
                    		sbs.append( "sbArgs.append( $args[" + i + "] + \", \" );" );
                    
                }
                
                sbs.append( "StringBuilder sb = new StringBuilder();" );
                //sbs.append("sb.append("+methodName+");");
                sbs.append( "sb.append( \"(\" );" );  
                sbs.append( "sb.append( sbArgs.toString() );" );
                sbs.append( "sb.append( \")\" );" );
                //System.out.println(sbs.toString());
                sbs.append("System.out.println(sb.toString());");
                m.insertAt(1,"{" + sbs.toString() + "}");
                
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