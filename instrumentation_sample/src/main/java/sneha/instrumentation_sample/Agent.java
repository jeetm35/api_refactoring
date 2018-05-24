package sneha.instrumentation_sample;

import java.lang.instrument.Instrumentation;

public class Agent {
 
    public static void premain(String agentArgs, Instrumentation inst) {
         System.out.println("Executing premain....");
        // registers the transformer
        inst.addTransformer(new SleepingClassFileTransformer());
    }
}