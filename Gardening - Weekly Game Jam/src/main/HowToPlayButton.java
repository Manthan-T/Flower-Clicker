package main;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class HowToPlayButton {

	public Image how;
	public float X = 100;
	public float Y = 150;
	public float X2 = 250; //cuz scaling while drawing the score string carried over
	public float Y2 = 175;
	public Rectangle hitbox = new Rectangle(X2, Y2, 250, 77);
	
}
