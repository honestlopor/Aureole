package entites;

import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.RenderingHints.Key;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import tiles.Doorway;
import utilz.LoadSave;
import utilz.HelpMethods;

public class Player extends Entity {
	private BufferedImage[][] animations;
	public float cameraX = 2195;
	public float cameraY = 2236;
	private int playerDir = -1; // default at -1 (IDLE)
	private boolean moving = false;
	public boolean attacking = false;
	public boolean powerAttackActive = false;
	private boolean left, up, right, down;
	
	private int[][] lvlData;
	
	//StatusBarUI
	private BufferedImage statusBarImg;
	
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);
	
	private int healthBarWidth = (int) (146 * Game.SCALE);
	private int healthBarHeight = (int) (4.5 * Game.SCALE);
	private int healthBarXStart = (int) (37 * Game.SCALE);
	private int healthBarYStart = (int) (22 * Game.SCALE);
	private int healthWidth = healthBarWidth;
	
	private int powerBarWidth = (int) (104 * Game.SCALE);
	private int powerBarHeight = (int) (2 * Game.SCALE);
	private int powerBarXStart = (int) (50 * Game.SCALE);
	private int powerBarYStart = (int) (37 * Game.SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;
	
	private float originX;
	private float originY;
	
	//AttackBox
	private int attackBoxWidth = 24;
	private int attackBoxHeight = 42;
	private boolean attackChecked;
	private Playing playing;
	
	// PowerAttack
	private int powerAttackBoxWidth = 24;
	private int powerAttackBoxHeight = 68;
	private int powerAttackTick;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;
	
	// Object
	public int hasKey1 = 0;
	
	private String hp_bar;
		
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		originX = x + 248;
		originY = y + 238;
		this.playing = playing;
		this.state = IDLE_FRONT;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		this.daiwalkSpeed = (float) (Game.SCALE * 1.0f / Math.sqrt(2));
//		calcStartCameraValues();
		initHitbox(originX, originY, width, height);
//		System.out.println(hitbox.x);
		initAttackBox();
		initPowerAttackBox();

	}
	
	public void calcStartCameraValues() {
		cameraX = playing.getTileManager().getCurrentTile().getArrayWidth() / 2 * Game.TILES_SIZE;
		cameraY = playing.getTileManager().getCurrentTile().getArrayHeight() / 2 * Game.TILES_SIZE;
//		System.out.println("check");
	}

	public void setCameraValues(float cameraX, float cameraY) {
		this.cameraX = cameraX;
		this.cameraY = cameraY;
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(hitbox.x + (hitbox.width/2) - 19, hitbox.y + hitbox.height + (int) (Game.SCALE * 10) - 32, (int) (attackBoxWidth * Game.SCALE), (int) (attackBoxHeight * Game.SCALE));
		
	}
	private void initPowerAttackBox() {
		powerAttackBox = new Rectangle2D.Float(hitbox.x + (hitbox.width/2) - 19, hitbox.y + hitbox.height + (int) (Game.SCALE * 10) - 32, (int) (powerAttackBoxWidth * Game.SCALE), (int) (powerAttackBoxHeight * Game.SCALE));
		
	}

	public void update() {
		updateHealthBar();
		updatePowerBar();

		if (currentHealth <= 0) {
			if (state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
			} else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else
				updateAnimationsTick();
			
			return;
		}
		
		updateAttackBox();
		updatePowerAttackBox();
		
		updatePos();
		if (attacking || powerAttackActive) 
			checkAttack();
		
		if (moving)
			checkPotionTouched();
		
		updateAnimationsTick();
		setAnimation();
	}
	

	private void checkAttack() {

		if (attackChecked || aniIndex != 1) {
			return;
		}
		attackChecked = true;
		if (attacking) {
			playing.checkEnemyHit(attackBox);
			playing.checkObjectHit(attackBox);
		}
		else if (powerAttackActive) {
			playing.checkEnemyHit(powerAttackBox);
			playing.checkObjectHit(powerAttackBox);
		}
		if (!moving)
			playing.getGame().getAudioPlayer().playAttackSound();
	}

	private void updateAttackBox() {
		
		if (right) {
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 11);
			attackBox.y = hitbox.y + (hitbox.height/2) - 35;
		} else if (left) {
			attackBox.x = hitbox.x - (int) (Game.SCALE * 12) - 84;
			attackBox.y = hitbox.y + (hitbox.height/2) - 35;
		} else if (up) {
			attackBox.x = hitbox.x + (hitbox.width/2) - 24;
			attackBox.y = hitbox.y - (int) (Game.SCALE * 10) - 64;
		} else if (down) {
			attackBox.x = hitbox.x + (hitbox.width/2) - 24;
			attackBox.y = hitbox.y + hitbox.height + (int) (Game.SCALE * 10) - 48;
		}
		if ((up || down) && (attackBox.width > attackBox.height)) {
			attackBox.width = attackBoxWidth * Game.SCALE;
			attackBox.height = attackBoxHeight * Game.SCALE;
		}
		else if ((left || right) && (attackBox.width < attackBox.height)) {
			attackBox.width = attackBoxHeight * Game.SCALE;
			attackBox.height = attackBoxWidth * Game.SCALE;
		}
		
	}
	
	private void updatePowerAttackBox() {
		
		if (right) {
			powerAttackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 11);
			powerAttackBox.y = hitbox.y + (hitbox.height/2) - 35;
		} else if (left) {
			powerAttackBox.x = hitbox.x - (int) (Game.SCALE * 12) - 136;
			powerAttackBox.y = hitbox.y + (hitbox.height/2) - 35;
		} else if (up) {
			powerAttackBox.x = hitbox.x + (hitbox.width/2) - 24;
			powerAttackBox.y = hitbox.y - (int) (Game.SCALE * 10) - 116;
		} else if (down) {
			powerAttackBox.x = hitbox.x + (hitbox.width/2) - 24;
			powerAttackBox.y = hitbox.y + hitbox.height + (int) (Game.SCALE * 10) - 48;
		}
		if ((up || down) && (powerAttackBox.width > powerAttackBox.height)) {
			powerAttackBox.width = powerAttackBoxWidth * Game.SCALE;
			powerAttackBox.height = powerAttackBoxHeight * Game.SCALE;
		}
		else if ((left || right) && (powerAttackBox.width < powerAttackBox.height)) {
			powerAttackBox.width = powerAttackBoxHeight * Game.SCALE;
			powerAttackBox.height = powerAttackBoxWidth * Game.SCALE;
		}
		
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
		
	}
	
	private void updatePowerBar() {
		powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);
		powerGrowTick++;
		if (powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(1);
		}
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
	}

	public void render(Graphics g) {
		g.drawImage(animations[state][aniIndex], (int) (x), (int) (y), (int)(Game.PLAYER_SIZE*Game.SCALE), (int)(Game.PLAYER_SIZE*Game.SCALE), null); //size 64 == 16
		
		
//		drawhitbox(g);
//		drawPowerAttackBox(g);
//		drawAttackBox(g);
		
		
		drawUI(g);
			}
	
	private void drawAttackBox(Graphics g) {
		g.setColor(Color.red);
		g.drawRect((int) (attackBox.x), (int) (attackBox.y), (int) attackBox.width, (int) attackBox.height);
	}
	
	private void drawPowerAttackBox(Graphics g) {
		g.setColor(Color.blue);
		g.drawRect((int) (powerAttackBox.x), (int) (powerAttackBox.y), (int) powerAttackBox.width, (int) powerAttackBox.height);
	}

	private void drawUI(Graphics g) {
		// Background UI
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		
		// Health UI
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		
		// Power UI
		g.setColor(Color.blue);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);

	}


	
	private void updateAnimationsTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) { // 8 animation per second, change pos overy 1/8 = 0.125 second.
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				powerAttackActive = false;
				attackChecked = false;
			}	
		}
	}

	private void setAnimation() {
		int startAni = state;
		

		if (moving && playerDir == LEFT) {
			attacking = false;
			powerAttackActive = false;
			state = RUN_LEFT;
		} else if (moving && playerDir == DOWN) {
			attacking = false;
			powerAttackActive = false;
			state = RUN_FRONT;
		} else if (moving && playerDir == RIGHT) {
			attacking = false;
			powerAttackActive = false;
			state = RUN_RIGHT;
		} else if (moving && playerDir == UP) {
			attacking = false;
			powerAttackActive = false;
			state = RUN_BACK;
		} else if (!moving && playerDir == UP){
			state = IDLE_BACK;
		} else if (!moving && playerDir == LEFT) {
			state = IDLE_LEFT;
		} else if (!moving && playerDir == RIGHT){
			state = IDLE_RIGHT;
		} else if (!moving && playerDir == DOWN) {
			state = IDLE_FRONT;
		}
	
		if (attacking && powerAttackActive)
			return;
		
		if (attacking) {
			if (attacking && playerDir == RIGHT) {
				state = ATK_RIGHT;
				if (startAni != ATK_RIGHT) {
					aniIndex = 1;
					aniTick = 0;
					return;
				}
			} else if (attacking && playerDir == DOWN) {
				state = ATK_FRONT;
				if (startAni != ATK_FRONT) {
					aniIndex = 1;
					aniTick = 0;
					return;
				}
			} else if (attacking && playerDir == UP) {
				state = ATK_BACK;
				if (startAni != ATK_BACK) {
					aniIndex = 1;
					aniTick = 0;
					return;
				}
			} else if (attacking && playerDir == LEFT) {
				state = ATK_LEFT;
				if (startAni != ATK_LEFT) {
					aniIndex = 1;
					aniTick = 0;
					return;
				}
			}
	
		}
		
			
		if (powerAttackActive && playerDir == RIGHT) {
			state = POWERATK_RIGHT;
		} else if (powerAttackActive && playerDir == DOWN) {
			state = POWERATK_FRONT;
		} else if (powerAttackActive && playerDir == UP) {
			state = POWERATK_BACK;
		} else if (powerAttackActive && playerDir == LEFT) {
			state = POWERATK_LEFT;
		}
		
		if (startAni != state)
			resetAnitick();
	}
	
	private void resetAnitick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		
		if (!left & !right & !down & !up & !down)
			return;
		
		if (left && right || up && down)
			return;
			
		float xDelta = 0, yDelta = 0;
		
		if (left && !right) {
			playerDir =0;
			if (up || down)
				xDelta -= daiwalkSpeed *-1;
			else
				xDelta -= walkSpeed *-1;
		} else if (right && !left) {
			playerDir = 2;
			if (up || down)
				xDelta += daiwalkSpeed *-1;
			else
				xDelta += walkSpeed *-1;
		}
		
		if (up && !down) {
			if (left || right) {
				if (left) 
					playerDir = 0;
				else if (right)
					playerDir = 2;
				yDelta -= daiwalkSpeed *-1;
			} else {
				playerDir = 3;
				yDelta -= walkSpeed *-1;
			}	
		} else if (down && !up) {
			if (left || right) {
				if (left) 
					playerDir = 0;
				else if (right)
					playerDir = 2;
				yDelta += daiwalkSpeed *-1;
			} else {	
				playerDir = 1;
				yDelta += walkSpeed *-1;
			}	
		}
		
		if (HelpMethods.CanWalkHere(hitbox ,cameraX - xDelta, cameraY - yDelta, playing.getTileManager().getCurrentTile(), xDelta, yDelta)) { //added
			cameraX += -xDelta;
			cameraY += -yDelta;
//			System.out.println(hitbox.x);
//			System.out.println(cameraX + " : " + cameraY);
			moving = true;
		}
	}
	
	public void changeHealth(int value) {
		currentHealth += value;
		
		if (currentHealth <= 0) {
			currentHealth = 0;
			//gameOver();
		} else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}
	
	public void changePower(int value) {
		powerValue += value;
		if (powerValue >= powerMaxValue)
			powerValue = powerMaxValue;
		else if (powerValue <= 0)
			powerValue = 0;
	}
	
	public void loadAnimations(String character) {
		
		BufferedImage img = LoadSave.GetSpriteAtlas(character);
		
		animations = new BufferedImage[21][8];
		for (int j = 0; j < animations.length; j++) 
			for (int i = 0; i < animations[j].length; i++) 
					animations[j][i] = img.getSubimage(i*350, j*350, 350, 350);// (imgx, imgy, posx, posy)
		
		if (character.equals(LoadSave.HIMMEL))
			hp_bar = LoadSave.HIMMEL_STATUS_BAR;
		if (character.equals(LoadSave.FRIEREN))
			hp_bar = LoadSave.FRIEREN_STATUS_BAR;
		
		statusBarImg = LoadSave.GetSpriteAtlas(hp_bar);

	}
	
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
	}
	
	public void setDirection(int direction) {
		this.playerDir = direction;
	}
	
	public void setAttack(boolean attack) {
		this.attacking = attack;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public void setRectPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isLeft() {
		return left;
	}

	
	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public float getCameraX() {
		return cameraX;
	}
	
	public float getCameraY() {
		return cameraY;
	}

	public void changHealth(int redPotionValue) {
		System.out.println("Heal!");
		changeHealth(redPotionValue);
		
	}

	public void resetAll() {
		resetDirBooleans();
		attacking = false;
		moving = false;
		hasKey1 = 0;
		state = IDLE_FRONT;
		currentHealth = maxHealth;
		
        playing.getTileManager().resetMap();
		playing.getGame().getAudioPlayer().setTileSong(playing.getTileManager().getCurrentTile());

		
		cameraX = 2195;
		cameraY = 2236;
		
	}

	public void powerAttack() {
		if (powerAttackActive)
			return;
		if (powerValue >= 50 && !moving) {
			powerAttackActive = true;
			changePower(-50);
		}
		
	}

}
