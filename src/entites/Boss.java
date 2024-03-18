package entites;

import static utilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.css.Counter;

import main.Game;

public class Boss extends Enemy {
	public float velocityX = 0.75f;
	public float velocityY = 0.75f;
	public final float originX;
	public final float originY;
	public int walkDir = -1;
	public int actionCounter = 0;
	public boolean attackTick;
	public int counter = 0;

	public Boss(float x, float y) {
		super(x, y, BOSS_WIDTH, BOSS_HEIGHT, BOSS);
		this.originX = x;
		this.originY = y;
		initHitbox(originX, originY, (int) (BOSS_WIDTH_DEFAULT * Game.SCALE), (int) (BOSS_HEIGHT_DEFAULT * Game.SCALE));
		initAttackBox();
		
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y,(int) (256 * Game.SCALE), (int)(224 * Game.SCALE));
//		attackBoxOffsetX = (int) (Game.SCALE * 30);
//		attackBoxOffsetY = (int) (Game.SCALE * 30);
		
	}

	public void update(Player player) {
		updateBehavior(player); 
		updateAnimationTick();
		updateAttackBox();   

	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBox.width/2 + hitbox.width/2;
		attackBox.y = hitbox.y - attackBox.height/2 + hitbox.height/2;
		
	}

	private void updateBehavior(Player player) {
		switch (state) {
		case B_IDLE:
			if (isPlayerCloseForAttack(player))
				newState(B_ATTACK);
			counter = 0;
			break;			
		case B_ATTACK:
			counter ++;
//			System.out.println(aniIndex + " " + counter);
		
			if (aniIndex == 29 && counter == 725) {
 				checkPlayerHit(attackBox, player);
			}
			if (aniIndex == 30 &&  counter == 750) {
 				checkPlayerHit(attackBox, player);
			}
			if (aniIndex == 31 && counter == 775) {
 				checkPlayerHit(attackBox, player);
			}
			if (aniIndex == 36 && counter == 900) {
 				checkPlayerHit(attackBox, player);
			}
			if (aniIndex == 39 && counter == 975 ) {
 				checkPlayerHit(attackBox, player);
			}
			if (aniIndex == 44 && counter == 1100 ) {
 				checkPlayerHit(attackBox, player);
			}
			
			
			break;
		case B_HIT:
			break;
		}
	}

	public void drawAttackBox(Graphics g) {
		g.setColor(Color.red);
		g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}
	
	
	
}
