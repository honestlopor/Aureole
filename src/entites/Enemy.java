package entites;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.*;

import java.awt.geom.Rectangle2D;

import main.Game;


public abstract class Enemy extends Entity {
	protected int enemyType;
	
	protected float walkSpeed = 1.0f * Game.SCALE;
	protected int walkDir = LEFT;
	protected float tileX, tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean attackChecked;
	
//	private int[][] lvlData;
	
	protected int actionCounter = 0;
	
	protected Rectangle2D.Float attackBox;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		tileX = x;
		tileY = y;
		this.enemyType = enemyType;
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
		walkSpeed = Game.SCALE * 0.35f;
		
	}
	
	protected boolean isPlayerCloseForAttack(Player player) {
		return player.hitbox.intersects(attackBox);
	}

	protected void newState(int enemyState) {
		this.state = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	public void hurt(int amount) {
		currentHealth -= amount;
		if (currentHealth <= 0) {
			if (enemyType == MIMIC) {
				newState(M_DEAD);
			}
			else if (enemyType == BOSS)
				newState(B_DEAD);
			else
				newState(DEAD);
		} else {
			if (enemyType == MIMIC)
				newState(M_HIT);
//			else if (enemyType == BOSS)
////				newState(IDLE);
			else
				if (enemyType == SLIME)
				newState(HIT);
		}

	}
	
	protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.hitbox))
			player.changeHealth(-GetEnemyDmg(enemyType));
		attackChecked = true; 
		
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		
			
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= getSpriteAmount(enemyType, state)) {
				aniIndex = 0;
				if (enemyType == MIMIC) {
					switch (state) {
					case M_ATTACK, M_HIT -> state = M_IDLE;
					case M_DEAD -> active = false;
					}
				}else if (enemyType == SLIME){
					switch (state) {
					case ATTACK, HIT -> state = IDLE;
					case DEAD -> active = false;
					}
				
				}else {
					switch (state) {
					case B_ATTACK, B_HIT -> state = B_IDLE;
					case B_DEAD -> active = false;
					}
				}
			}
		}
	}
	
	public void resetEnemy() {
		x = tileX;
		y = tileY;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
	}
	
	

	
	public boolean isActive() {
		return active;
	}
	
}
