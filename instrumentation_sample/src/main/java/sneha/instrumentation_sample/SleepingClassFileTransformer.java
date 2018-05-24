package sneha.instrumentation_sample;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
 
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
                
                System.out.println("Signature of method is:"+m.getSignature().toString());
                m.addLocalVariable("elapsedTime", CtClass.longType);
                m.insertBefore("elapsedTime = System.currentTimeMillis();");
                m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
                        + "System.out.println(\"Method Executed in ms: \" + elapsedTime);}");
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