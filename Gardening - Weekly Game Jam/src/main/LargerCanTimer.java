package main;

public class LargerCanTimer implements Runnable {

	private long startTime;
	
	public void run() {
		FlowerClicker.isLargerCanActive = true;
		startTime = System.currentTimeMillis();
		
		while (System.currentTimeMillis() - startTime < 5000) {
		}		
		
		FlowerClicker.isLargerCanActive = false;		
    }

}
