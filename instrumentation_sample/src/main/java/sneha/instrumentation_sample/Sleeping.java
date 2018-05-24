package sneha.instrumentation_sample;

public class Sleeping {
	public void randomSleep(String name) throws InterruptedException {
		// randomly sleeps between 500ms and 1200s
		long randomSleepDuration = (long) (500 + Math.random() * 700);
		System.out.printf(name+"Sleeping for %d ms ..\n", randomSleepDuration);
		Thread.sleep(randomSleepDuration);
		}
}