package sneha.instrumentation_sample;


public class AgentTest {
	
	
	public static void main(String[] args) throws InterruptedException{
    	System.out.println("Executing main...");
    	Sleeping sleeping = new Sleeping();
        sleeping.randomSleep("Sneha");
        System.out.println("Exit main...");
    }
}