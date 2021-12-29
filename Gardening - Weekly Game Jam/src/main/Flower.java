package main;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class Flower {
	private Random random = new Random();
	public Image flower;
	public float X = random.nextInt(672) + 15;
	public float Y = -48;
	public Rectangle hitbox = new Rectangle(X, Y, 48, 48);
	
	public void newX() {
		X = random.nextInt(672) + 15;
		Y = -48;
		hitbox.setLocation(X, Y);
	}
	
	public void updateY(int delta) {
		Y += delta * 0.15;
		hitbox.setLocation(X, Y);
	}
	
}
