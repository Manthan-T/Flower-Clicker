package main;

import java.awt.Font;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;

public class FlowerClicker extends BasicGame {

	public static final String gamename = "Flower Clicker";
	
	Flower[] flowers = new Flower[10];
	LargeCan largeCan = new LargeCan();	
	Image shield;
	
	Image map;
	Image title;
	Image bottomMenu;
	Image instructions;
	Image cursor;

	Image[] shops = new Image[6];
	Image heartShop;
	Image shieldShop;
	Image largerCanShop;

	PlayButton play = new PlayButton();
	HowToPlayButton how_to_play = new HowToPlayButton();
	QuitButton quit = new QuitButton();
	Image[] pausume = new Image[2];
	Image pause;
	Image home;
	Image[] volumeSetting = new Image[2];
	Image volume;
	
	Image[] cursorTypes = new Image[4];
	Image[] heartTypes = new Image[2];
	Image[] hearts = new Image[3];
	
	Font font;
	TrueTypeFont ttf;
	
	Sound purchase;
	Sound flower_click;
	Sound heart_loss;
	Sound click;
	Sound shield_block;
	
	static enum gameState {On, Off, Paused};
	gameState state = gameState.Off;
	
	static int score = 0;
	static int trueScore = 0;

	Anticheat anticheat = new Anticheat();
	static int takeHeart = 0;
	
	ShieldTimer shieldTimer = new ShieldTimer();
	static boolean isShieldActive = false;
	
	LargerCanTimer largerCanTimer = new LargerCanTimer();
	static boolean isLargerCanActive = false;
	
	float howX = 750;
	float howY = 750;
	float shieldY = 1000;
	float titleY = 25;
	
	public FlowerClicker(String gamename) {	            
   		super(gamename);
	}

	public void init(GameContainer container) throws SlickException {		
		anticheat.ac();
		font = new Font("Orbitron", Font.BOLD, 30);
		ttf = new TrueTypeFont(font, false);
		
		map = new Image("res/map.png");
		title = new Image("res/title.png");
		bottomMenu = new Image("res/bottom_menu.png");
		instructions = new Image("res/howtoplay.png");
		play.play = new Image("res/buttons/play_button.png");
		how_to_play.how = new Image("res/buttons/how_to_play.png");
		quit.quit = new Image("res/buttons/quit.png");
		
		purchase = new Sound("res/sfx/shop_buy.wav");
		flower_click = new Sound("res/sfx/flower_click.wav");
		heart_loss = new Sound("res/sfx/heart_loss.wav");
		click = new Sound("res/sfx/click.wav");
		shield_block = new Sound("res/sfx/shield_block.wav");

		cursorTypes[0] = new Image("res/cursor/cursor.png");
		cursorTypes[1] = new Image("res/cursor/cursor_pressed.png");
		cursorTypes[2] = new Image("res/upgrades/large_can.png");
		cursorTypes[3] = new Image("res/upgrades/large_can_pressed.png");
		heartTypes[0] = new Image("res/heart/heart.png");
		heartTypes[1] = new Image("res/heart/dead_heart.png");

		shops[0] = new Image("res/shops/heart_shop.png");
		shops[1] = new Image("res/shops/heart_shop_poor.png");
		shops[2] = new Image("res/shops/shield_shop.png");
		shops[3] = new Image("res/shops/shield_shop_poor.png");
		shops[4] = new Image("res/shops/largerCan_shop.png");
		shops[5] = new Image("res/shops/largerCan_shop_poor.png");
		heartShop = shops[1];
		shieldShop = shops[3];
		largerCanShop = shops[5];

		shield = new Image("res/upgrades/shield.png");
		largeCan.largeCan = cursorTypes[2];
		
		pausume[0] = new Image("res/buttons/pause.png");
		pausume[1] = new Image("res/buttons/resume.png");
		pause = pausume[0];
		home = new Image("res/buttons/home.png");
		volumeSetting[0] = new Image("res/buttons/mute.png");
		volumeSetting[1] = new Image("res/buttons/unmute.png");
		volume = volumeSetting[0];

		cursor = new Image("res/cursor/blank_cursor.png");
		
		for (int x = 0; x < flowers.length; x++) {
			flowers[x] = new Flower();
			flowers[x].flower = new Image("res/flower.png");
		}
		
		for (int x = 0; x < hearts.length; x++) {
			hearts[x] = heartTypes[0];
		}
		
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		g.drawImage(map, 0, 0);
		
		for (int x = 0; x < flowers.length; x++) {
			g.drawImage(flowers[x].flower, flowers[x].X, flowers[x].Y);
		}
		
		for (int x = 0; x < hearts.length; x++) {
			g.drawImage(hearts[x], 64 * x + 20 + x * 10, 20);
		}
		
		g.drawImage(bottomMenu, 0, 750);
		g.drawImage(heartShop, 25, 775);
		g.drawImage(shieldShop, 190, 775);
		g.drawImage(largerCanShop, 355, 775);
		g.drawImage(pause, 575, 775);
		g.drawImage(home, 655, 775);
		g.drawImage(volume, 575, 855);
		g.drawImage(quit.quit, 655, 855);
		
		g.drawImage(shield, 15, shieldY);
		g.drawImage(largeCan.largeCan, largeCan.X, largeCan.Y);

		int scoreLength = String.valueOf(score).length();
		ttf.drawString(585 - (scoreLength - 1) * 20, 20, "Score: " + score, Color.decode("0x00ffdd"));

		g.drawImage(cursor, Mouse.getX(), 950 - Mouse.getY());
		
		g.setColor(Color.magenta);
		g.scale(2.5f, 2.5f);
		
		g.drawImage(play.play, play.X, play.Y);
		g.drawImage(how_to_play.how, how_to_play.X, how_to_play.Y);
		g.drawImage(instructions, howX, howY);
		
		g.scale(1.5f, 1.5f);
		g.drawImage(title, 25, titleY);
	}

	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		
		if (state == gameState.On) {
			container.setMouseCursor("res/cursor/blank_cursor.png", 0, 0);
			
			if (isLargerCanActive == false) {
				largeCan.reset();
				if (Mouse.isButtonDown(0)) {
					cursor = cursorTypes[1];
					for (int x = 0; x < flowers.length; x++) {
						if (flowers[x].hitbox.contains(Mouse.getX(), 950 - Mouse.getY())) {
							score++;
							trueScore++;
							flower_click.play();
							flowers[x].newX();
						
						}
						
					}
						
				} else {
					cursor = cursorTypes[0];
				}
				
			} else {
				largeCan.X = Mouse.getX();
				largeCan.Y = 950 - Mouse.getY();
				largeCan.update();
				
				if (Mouse.isButtonDown(0)) {
					cursor = new Image("res/cursor/blank_cursor.png");
					for (int x = 0; x < flowers.length; x++) {
						if (largeCan.hitbox.intersects(flowers[x].hitbox)) {
							score++;
							trueScore++;
							flower_click.play();
							flowers[x].newX();
						
						}
						
					}
						
				} else {
					cursor = new Image("res/cursor/blank_cursor.png");
				}
				
			}
				
			
			if (trueScore < 10) {
				flowers[0].updateY(delta);
			
			} else if (trueScore >= 10 && trueScore < 20) {
				for (int x = 0; x < 2; x++) {
					flowers[x].updateY(delta);
				}
				
			} else if (trueScore >= 20 && trueScore < 30) {
				for (int x = 0; x < 3; x++) {
					flowers[x].updateY(delta);
				}
				
			} else if (trueScore >= 30 && trueScore < 40) {
				for (int x = 0; x < 4; x++) {
					flowers[x].updateY(delta);
				}
				
			} else if (trueScore >= 40 && trueScore < 50) {
				for (int x = 0; x < 5; x++) {
					flowers[x].updateY(delta);
				}
				
			} else if (trueScore >= 50 && trueScore < 60) {
				for (int x = 0; x < 6; x++) {
					flowers[x].updateY(delta);
				}
				
			} else if (trueScore >= 60 && trueScore < 70) {
				for (int x = 0; x < 7; x++) {
					flowers[x].updateY(delta);
				}
				
			} else if (trueScore >= 80 && trueScore < 90) {
				for (int x = 0; x < 8; x++) {
					flowers[x].updateY(delta);
				}
				
			} else if (trueScore >= 90 && trueScore < 100) {
				for (int x = 0; x < 9; x++) {
					flowers[x].updateY(delta);
				}
				
			} else {
				for (int x = 0; x < flowers.length; x++) {
					flowers[x].updateY(delta);
				}
				
			}
			
			if (isShieldActive == true) {
				shieldY = 677;
				for (int x = 0; x < flowers.length; x++) {
					if (flowers[x].Y >= shieldY) {
						shield_block.play();
						flowers[x].newX();
					}
					
				}
			
			} else {
				shieldY = 1000;
			}
			
			for (int x = 0; x < flowers.length; x++) {
				if (flowers[x].Y >= 750) {
					if (hearts[2] == heartTypes[0]) {
						hearts[2] = heartTypes[1];
						heart_loss.play();
						flowers[x].newX();
					
					} else if (hearts[1] == heartTypes[0]) {
						hearts[1] = heartTypes[1];
						heart_loss.play();
						flowers[x].newX();
					
					} else {
						hearts[0] = heartTypes[1];
						heart_loss.play();
						state = gameState.Off;
						play.Y = 100;
						how_to_play.Y = 150;
						play.hitbox.setY(50);
						how_to_play.hitbox.setY(175);
						
					}
				
				}
				
				if (takeHeart > 0) {
					if (hearts[2] == heartTypes[0]) {
						hearts[2] = heartTypes[1];
						takeHeart--;
						heart_loss.play();
					
					} else if (hearts[1] == heartTypes[0]) {
						hearts[1] = heartTypes[1];
						takeHeart--;
						heart_loss.play();
					
					} else {
						hearts[0] = heartTypes[1];
						takeHeart--;
						heart_loss.play();
						state = gameState.Off;
						play.Y = 100;
						how_to_play.Y = 150;
						play.hitbox.setY(50);
						how_to_play.hitbox.setY(175);
					
					}
						
				}
				
			}
			
			pause = pausume[0];
			
			if (score >= 25) {
				heartShop = shops[0];
				shieldShop = shops[2];
				largerCanShop = shops[4];
				
			} else {
				heartShop = shops[1];
				shieldShop = shops[3];
				largerCanShop = shops[5];
			}
			
			if (input.isMousePressed(0)) {
				if (score >= 25) {
					if (Mouse.getX() >= 25 && Mouse.getX() <= 175 && 950 - Mouse.getY() >= 775 && 950 - Mouse.getY() <= 925) {
						if (hearts[1] == heartTypes[1]) {
							hearts[1] = heartTypes[0];
							score -= 25;
							purchase.play();
						
						} else if (hearts[2] == heartTypes[1]) {
							hearts[2] = heartTypes[0];
							score -= 25;
							purchase.play();
							
						}
				
					}

					if (Mouse.getX() >= 190 && Mouse.getX() <= 340 && 950 - Mouse.getY() >= 775 && 950 - Mouse.getY() <= 925) {
						Thread shieldtimerThread = new Thread(shieldTimer);
						shieldtimerThread.start();
						score -= 25;
						purchase.play();
					}
					
					if (Mouse.getX() >= 355 && Mouse.getX() <= 505 && 950 - Mouse.getY() >= 775 && 950 - Mouse.getY() <= 925) {
						Thread largerCantimerThread = new Thread(largerCanTimer);
						largerCantimerThread.start();
						score -= 25;
						purchase.play();
					}
					
				}
				
				if (Mouse.getX() >= 575 && Mouse.getX() <= 645 && 950 - Mouse.getY() >= 775 && 950 - Mouse.getY() <= 845) {
					state = gameState.Paused;
					click.play();
				}
				
				if (Mouse.getX() >= 655 && Mouse.getX() <= 725 && 950 - Mouse.getY() >= 775 && 950 - Mouse.getY() <= 845) {
					state = gameState.Off;
					click.play();
					play.Y = 100;
					how_to_play.Y = 150;
					titleY = 25;
					play.hitbox.setY(50);
					how_to_play.hitbox.setY(175);
				}
				
				if ((Mouse.getX() >= 575 && Mouse.getX() <= 645 && 950 - Mouse.getY() >= 855 && 950 - Mouse.getY() <= 925)) {
					if (container.isSoundOn()) {
						container.setSoundOn(false);
						volume = volumeSetting[1];
				
					} else {
						container.setSoundOn(true);
						volume = volumeSetting[0];
						click.play();
					}
					
				}
				
				if (quit.hitbox.contains(Mouse.getX(), 950 - Mouse.getY())) {
					click.play();
					container.exit();
				}
				
			}
			
		} else if (state == gameState.Off) {
			anticheat.MilliSecondsPressed = 0;			
			container.setDefaultMouseCursor();
			cursor = new Image("res/cursor/blank_cursor.png");
			pause = pausume[1];
			
			heartShop = shops[1];
			shieldShop = shops[3];
			largerCanShop = shops[5];
			
			for (int x = 0; x < flowers.length; x++) {
				flowers[x].Y = -48;
			}
			
			if (input.isMousePressed(0)) {
				if (play.hitbox.contains(Mouse.getX(), 750 - Mouse.getY())) {
					click.play();
					state = gameState.On;
					play.Y = 750;
					how_to_play.Y = 750;
					play.hitbox.setY(750);
					how_to_play.hitbox.setY(750);
					score = 0;
					trueScore = 0;
					howX = 750;
					howY = 750;
					titleY = 750;
					
					for (int x = 0; x < hearts.length; x++) {
						hearts[x] = heartTypes[0];
					}
					
					for (int x = 0; x < flowers.length; x++) {
						flowers[x].newX();
					}
				
				}
				
				if ((Mouse.getX() >= 575 && Mouse.getX() <= 645 && 950 - Mouse.getY() >= 855 && 950 - Mouse.getY() <= 925)) {
					if (container.isSoundOn()) {
						container.setSoundOn(false);
						volume = volumeSetting[1];
				
					} else {
						container.setSoundOn(true);
						volume = volumeSetting[0];
						click.play();
					}
					
				}
				
				if (how_to_play.hitbox.contains(Mouse.getX(), 750 - Mouse.getY())) {
					click.play();
					play.Y = 15;
					play.hitbox.setY(-160);
					howX = 50;
					howY = 50;
					titleY = 750;
				}
				
				if (quit.hitbox.contains(Mouse.getX(), 950 - Mouse.getY())) {
					click.play();
					container.exit();
				}
				
			}
			
		} else {
			anticheat.MilliSecondsPressed = 0;
			container.setDefaultMouseCursor();
			cursor = new Image("res/cursor/blank_cursor.png");
			pause = pausume[1];

			heartShop = shops[1];
			shieldShop = shops[3];
			largerCanShop = shops[5];
			
			if (input.isMousePressed(0)) {
				if (Mouse.getX() >= 575 && Mouse.getX() <= 645 && 950 - Mouse.getY() >= 775 && 950 - Mouse.getY() <= 845) {
					state = gameState.On;
					click.play();
				}
				
				if (Mouse.getX() >= 655 && Mouse.getX() <= 725 && 950 - Mouse.getY() >= 775 && 950 - Mouse.getY() <= 845) {
					state = gameState.Off;
					click.play();
					play.Y = 100;
					how_to_play.Y = 150;
					titleY = 25;
					play.hitbox.setY(50);
					how_to_play.hitbox.setY(175);
				}
				
				if (quit.hitbox.contains(Mouse.getX(), 950 - Mouse.getY())) {
					click.play();
					container.exit();
				}
				
				if ((Mouse.getX() >= 575 && Mouse.getX() <= 645 && 950 - Mouse.getY() >= 855 && 950 - Mouse.getY() <= 925)) {
					if (container.isSoundOn()) {
						container.setSoundOn(false);
						volume = volumeSetting[1];
				
					} else {
						container.setSoundOn(true);
						volume = volumeSetting[0];
						click.play();
					}
					
				}
				
			}
			
		}
		
	}
	
	public static void main(String args[]) {
		try {
			AppGameContainer appContainer = new AppGameContainer(new FlowerClicker(gamename));
			appContainer.setIcon("res/icon.png");
			appContainer.setDisplayMode(750, 950, false);
			appContainer.setShowFPS(false);
			appContainer.setVSync(true);
			appContainer.start();
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
		
}
