package main;

public class ShieldTimer implements Runnable {

	private long startTime;
	
	public void run() {
		FlowerClicker.isShieldActive = true;
		startTime = System.currentTimeMillis();
		
		while (System.currentTimeMillis() - startTime < 5000) {
		}		
		
		FlowerClicker.isShieldActive = false;
		
    }

}
