package main;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class LargeCan {

	public Image largeCan;
	public float X = 1000;
	public float Y = 1000;
	public Rectangle hitbox = new Rectangle(X, Y, 96, 96);
	
	public void update() {
		hitbox.setLocation(Mouse.getX(), 950 - Mouse.getY());
	}
	
	public void reset() {
		X = 1000;
		Y = 1000;
		hitbox.setLocation(X, Y);
	}
	
}
