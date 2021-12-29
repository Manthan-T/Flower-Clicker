package main;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Mouse;

public class Anticheat {

int MilliSecondsPressed = 0;
	
	public void ac() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
					
			@Override
			public void run() {
				if (Mouse.isButtonDown(0)) {
					MilliSecondsPressed++;
						
				} else {
					MilliSecondsPressed = 0;
				}
				
				if (MilliSecondsPressed == 500) {
					MilliSecondsPressed = 0;
					FlowerClicker.takeHeart++;
				}
						
			}
		
		};
		timer.scheduleAtFixedRate(task, 0, 1);
		
	}	
		
}
