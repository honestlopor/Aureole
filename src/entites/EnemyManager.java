package entites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import gamestates.Playing;
import main.Game;
import utilz.HelpMethods;
import utilz.LoadSave;

import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.Directions.UP;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;


public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] slimeArr;
	private ArrayList<Slime> slimies = new ArrayList<>();
	private BufferedImage[][] mimicArr;
	private ArrayList<Mimic> mimics = new ArrayList<>();
	private BufferedImage[][] bossArr;
	private ArrayList<Boss> bosses = new ArrayList<>();
	private int[][] lvlData;

	private float cameraX, cameraY;
	
	private BufferedImage statusBarImg;


	private int statusBarWidth = (int) (300 * Game.SCALE);
	private int statusBarHeight = (int) (95 * Game.SCALE);
	private int statusBarX = (int) (265 * Game.SCALE);
	private int statusBarY = (int) (350 * Game.SCALE);
	
	private int healthBarWidth = (int) (214 * Game.SCALE);
	private int healthBarHeight = (int) (5.8 * Game.SCALE);
	private int healthBarXStart = (int) (43 * Game.SCALE);
	private int healthBarYStart = (int) (58.5 * Game.SCALE);
	private int healthWidth = healthBarWidth;
	
	protected int maxHealth;
	protected int currentHealth;

	public EnemyManager(Playing playing) {
		this.playing = playing;
		maxHealth = GetMaxHealth(BOSS);
		currentHealth = maxHealth;
		loadEnemyImgs();
		addEnemies();
	}
	
	private void addEnemies() {
		
//		slimies.add(new Slime(20 * Game.TILES_SIZE, 20*Game.TILES_SIZE));
//		System.out.println("size of crab: " + crabbies.size());
		
	}
	
	public void hurt(int amount) {
		currentHealth -= amount;
	}
	
	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
		
	}

	public void update(Player player) {
		if (playing.getTileManager().getCurrentTile().getslimeArr() != null)
			for (Slime s : playing.getTileManager().getCurrentTile().getslimeArr()) 
				if (s.isActive())
					s.update(player);
		if (playing.getTileManager().getCurrentTile().getMimicArr() != null)
			for (Mimic m : playing.getTileManager().getCurrentTile().getMimicArr())
				if (m.isActive())
					m.update(player);
		if (playing.getTileManager().getCurrentTile().getBossArrayList() != null)
			for (Boss b : playing.getTileManager().getCurrentTile().getBossArrayList())
				if (b.isActive()) {
					b.update(player);
					updateHealthBar();
				} else {
					player.hasKey1 = 1000;
				}
	}
	
	public void draw(Graphics g, Boolean paused, Boolean gameOver) {
		drawSlimes(g, paused, gameOver);
		drawMimics(g);
		drawBoss(g);
		
	}
	
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
	}
	
	public void setCameraValues(float x, float y) {
		this.cameraX = x;
		this.cameraY = y;
	}
	
	private void changeWalkDir(Slime s) {
		int walkDis = Game.TILES_SIZE;
		s.actionCounter ++;
		if (s.actionCounter > 0*walkDis && s.actionCounter <= 2*walkDis)  {
			s.walkDir = UP;
		}
		if (s.actionCounter > 2*walkDis && s.actionCounter <= 6*walkDis) {
			s.walkDir = LEFT;
		}
		if (s.actionCounter > 6*walkDis && s.actionCounter <= 8*walkDis) {
			s.walkDir = DOWN;
		}
		if (s.actionCounter > 8*walkDis && s.actionCounter <= 12*walkDis) {
			s.walkDir = RIGHT;
		}
		if (s.actionCounter > 12*walkDis) {
			s.actionCounter = 0;
		}
		
	}
	
	private void updateSlimes(Slime s) {
		changeWalkDir(s);
		
		float xDelta = 0, yDelta = 0;
		if (s.walkDir == UP)  
			yDelta += s.velocityY;
		if (s.walkDir == DOWN)
			yDelta -= s.velocityY;
		if (s.walkDir == LEFT) 
			xDelta += s.velocityX;
		if (s.walkDir == RIGHT) 
			xDelta -= s.velocityX;
		
		if (HelpMethods.CanWalkHere(s.hitbox ,s.x - xDelta, s.y - yDelta, playing.getTileManager().getCurrentTile(), xDelta, yDelta)) {
			s.x += -xDelta;
			s.y += -yDelta;
		}
	}
	
	private void drawSlimes(Graphics g, Boolean paused, Boolean gameOver) {
		if (playing.getTileManager().getCurrentTile().getslimeArr() != null)
			for (Slime s : playing.getTileManager().getCurrentTile().getslimeArr()) 
				if (s.isActive()) {
					int screenX = (int) (s.x - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(SLIME_WIDTH/2)));
					int screenY = (int) (s.y - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(SLIME_HEIGHT/2)));
					
					g.drawImage(slimeArr[s.getEnemyState()][s.getAniIndex()], screenX, screenY, (int) ((50*Game.SCALE)), (int) (50*Game.SCALE), null);
	
					screenX = (int) (s.x - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(50*Game.SCALE/3.5)));
					screenY = (int) (s.y - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(50*Game.SCALE/8)));
					s.hitbox.x = screenX + SLIME_WIDTH/2 + s.hitbox.width/2;
					s.hitbox.y = screenY + SLIME_HEIGHT/2 + s.hitbox.height/2;
					g.setColor(Color.pink);
	//				g.drawRect((int) (s.hitbox.x), (int) (s.hitbox.y), (int) (s.hitbox.width), (int) (s.hitbox.height));
	//				s.drawAttackBox(g);
					if (!paused && !gameOver)
						updateSlimes(s);

			
			}

	}
	private void drawMimics(Graphics g) {
		if (playing.getTileManager().getCurrentTile().getMimicArr() != null) 
			for (Mimic m : playing.getTileManager().getCurrentTile().getMimicArr()) 
				if (m.isActive()) {
					int screenX = (int) (m.x - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(SLIME_WIDTH/2)));
					int screenY = (int) (m.y - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(SLIME_HEIGHT/2)));
					
					g.drawImage(mimicArr[m.getEnemyState()][m.getAniIndex()], screenX, screenY, (int) ((50*Game.SCALE)), (int) (50*Game.SCALE), null);
	
					screenX = (int) (m.x - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(50*Game.SCALE/3.5)));
					screenY = (int) (m.y - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(50*Game.SCALE/8)));
					m.hitbox.x = screenX + MIMIC_WIDTH/2 + m.hitbox.width/2 - 10;
					m.hitbox.y = screenY + MIMIC_HEIGHT/2 + m.hitbox.height/2 - 10;
//					g.setColor(Color.pink);
//					g.drawRect((int) (m.hitbox.x), (int) (m.hitbox.y), (int) (m.hitbox.width), (int) (m.hitbox.height));
//					m.drawAttackBox(g);
			
			}

	}
	
	private void drawBoss(Graphics g) {
		if (playing.getTileManager().getCurrentTile().getBossArrayList() != null)
			for (Boss b : playing.getTileManager().getCurrentTile().getBossArrayList())
				if (b.isActive()) {
					int screenX = (int) (b.x - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(SLIME_WIDTH/2)));
					int screenY = (int) (b.y - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(SLIME_HEIGHT/2)));
					
					g.drawImage(bossArr[b.getEnemyState()][b.getAniIndex()], screenX, screenY, (int) ((288*Game.SCALE)), (int) (351*Game.SCALE), null);
	
					screenX = (int) (b.x - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(288*Game.SCALE/3.5)));
					screenY = (int) (b.y - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(351*Game.SCALE/8)));
					b.hitbox.x = screenX + BOSS_WIDTH/2 + b.hitbox.width/2 - 10;
					b.hitbox.y = screenY + BOSS_HEIGHT/2 + b.hitbox.height/2 - 10;
					
//					g.setColor(Color.pink);
//					g.drawRect((int) (b.hitbox.x), (int) (b.hitbox.y), (int) (b.hitbox.width), (int) (b.hitbox.height));
//					b.drawAttackBox(g);
					g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
					g.setColor(Color.red);
					g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
			}

	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		if (playing.getTileManager().getCurrentTile().getslimeArr() != null)
			for (Slime s : playing.getTileManager().getCurrentTile().getslimeArr()) 
				if (s.isActive())
					if (attackBox.intersects(s.getHitbox())) {
						if (playing.getPlayer().attacking)
							s.hurt(10);
						else if (playing.getPlayer().powerAttackActive)
							s.hurt(20);
						return;
					}
		if (playing.getTileManager().getCurrentTile().getMimicArr() != null)
			for (Mimic m : playing.getTileManager().getCurrentTile().getMimicArr()) 
				if (m.isActive())
					if (attackBox.intersects(m.getHitbox())) {
						if (playing.getPlayer().attacking)
							m.hurt(10);
						else if (playing.getPlayer().powerAttackActive)
							m.hurt(20);
						return;
					}
		if (playing.getTileManager().getCurrentTile().getBossArrayList() != null)
			for (Boss b : playing.getTileManager().getCurrentTile().getBossArrayList())
				if (b.isActive())
					if (attackBox.intersects(b.getHitbox())) {
						if (playing.getPlayer().attacking) {
							b.hurt(10);
							hurt(10); 
						}
						else if (playing.getPlayer().powerAttackActive) {
							b.hurt(20);
							hurt(20); 
						}
						return;
					}
	}

	private void loadEnemyImgs() {
		slimeArr = new BufferedImage[5][6];
		BufferedImage slimeImgs = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
		for (int j = 0; j < slimeArr.length; j++)
			for (int i = 0; i < slimeArr[j].length; i++)
				slimeArr[j][i] = slimeImgs.getSubimage(i * 50, j * 50, 50, 50);
		mimicArr = new BufferedImage[4][6];
		BufferedImage mimicImgs = LoadSave.GetSpriteAtlas(LoadSave.MIMIC_SPRITE);
		for (int j = 0; j < mimicArr.length; j++)
			for (int i = 0; i < mimicArr[j].length; i++)
				mimicArr[j][i] = mimicImgs.getSubimage(i * 100, j * 100, 100, 100);
		bossArr = new BufferedImage[4][50];
		BufferedImage bossImgs = LoadSave.GetSpriteAtlas(LoadSave.BOSS_SPRITE);
		for (int j = 0; j < bossArr.length; j++)
			for (int i = 0; i < bossArr[j].length; i++)
				bossArr[j][i] = bossImgs.getSubimage(i * 288, j * 351, 288, 351);
		
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.BOSS_STATUS_BAR);
	}
	
	public void resetAllEnemies() {
		if (playing.getTileManager().getCurrentTile().getslimeArr() != null)
			for (Slime s : playing.getTileManager().getCurrentTile().getslimeArr()) 
				s.resetEnemy();
		if (playing.getTileManager().getCurrentTile().getMimicArr() != null)
			for (Mimic m : playing.getTileManager().getCurrentTile().getMimicArr())
				m.resetEnemy();
		if (playing.getTileManager().getCurrentTile().getBossArrayList() != null)
			for (Boss b : playing.getTileManager().getCurrentTile().getBossArrayList()) 
				b.resetEnemy();
			currentHealth = maxHealth;
	}
	
}
