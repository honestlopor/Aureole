package entites;

import main.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class Entity implements Comparable<Entity>{
	
	protected float originX, originY;
	protected float x, y;
	protected int width, height;
	public Rectangle2D.Float hitbox;
	protected int aniTick, aniIndex; // 15
	protected int state;
	protected int maxHealth;
	protected int currentHealth;
	protected Rectangle2D.Float attackBox;
	protected Rectangle2D.Float powerAttackBox;
	protected float walkSpeed = 1.0f * Game.SCALE;
	protected float daiwalkSpeed = (float)(1.0f * Game.SCALE / Math.sqrt(2));
	private float lastCameraYValue = 0;

	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		initHitbox(x, y, width, height);
	}
	
//	public void drawAttackBox(Graphics g) {
//		g.setColor(Color.red);
//		g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
//	}
	
	protected void drawhitbox(Graphics g) {
		g.setColor(Color.pink);
		g.drawRect((int) (hitbox.x), (int) (hitbox.y), (int) (hitbox.width), (int) (hitbox.height-14));
	}
	
	protected void initHitbox(float x, float y, int width, int height) {
		this.hitbox = new Rectangle2D.Float(x, y, width, height);
	}
	
//	protected void updateHitbox() {
//		hitbox.x = (int) x;
//		hitbox.y = (int) y;
//	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public int getEnemyState() {
		return state;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}
	
	public void setLastCameraYValue(float lastCameraYValue) {
        this.lastCameraYValue  = lastCameraYValue;
    }
	
	public int compareTo(Entity other) {
        return Float.compare(hitbox.y, other.hitbox.y);
    }

}
