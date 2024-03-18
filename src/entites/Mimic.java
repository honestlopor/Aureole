package entites;

import static utilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class Mimic extends Enemy {

	public float velocityX = 0.75f;
	public float velocityY = 0.75f;
	public final float originX;
	public final float originY;
	public int walkDir = -1;
	public int actionCounter = 0;

	public Mimic(float x, float y) {
		super(x, y, MIMIC_WIDTH, MIMIC_HEIGHT, MIMIC);
		this.originX = x;
		this.originY = y;
		initHitbox(originX, originY, (int) (MIMIC_WIDTH_DEFAULT * Game.SCALE), (int) (MIMIC_HEIGHT_DEFAULT * Game.SCALE));
		initAttackBox();
		
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y,(int) (60 * Game.SCALE), (int)(60 * Game.SCALE));
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
		case M_IDLE:
			if (isPlayerCloseForAttack(player))
				newState(M_ATTACK);
			
			break;			
		case M_ATTACK:
			if (aniIndex == 0)
				attackChecked = false;
			
			if (aniIndex == 3 && !attackChecked)
 				checkPlayerHit(attackBox, player);
			
			break;
		case M_HIT:
			break;
		}
	}

	public void drawAttackBox(Graphics g) {
		g.setColor(Color.red);
		g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}
	
	
	
	
}
	
