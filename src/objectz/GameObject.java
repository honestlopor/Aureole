package objectz;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class GameObject {

	protected int worldX, worldY, objType;
	protected Rectangle2D.Float hitbox;
	protected boolean doAnimation, active = true;
	protected int aniTick, aniIndex;
	protected int xDrawOffset, yDrawOffset;
	
	public GameObject(int x, int y, int objType) {
		worldX = x;
		worldY = y;
		this.objType = objType;
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(objType)) {
				aniIndex = 0;
				if (objType == BARREL1 || objType == BARREL2 || objType == BOX) {
					doAnimation = false;
					active = false;
				}
			}
		}
	}
	
	public void reset() {
		aniIndex = 0;
		aniTick = 0;
		active = true;
		
		if (objType == BARREL1 || objType == BARREL2 || objType == BOX) 
			doAnimation = false;
		else
			doAnimation = true;
	}
	
	protected void initHitbox(float width, float height) {
		hitbox = new Rectangle2D.Float(worldX, worldY, width, height);
	}
	
	

	public int getObjType() {
		return objType;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public void setAnimation(boolean doAnimation) {
		this.doAnimation = doAnimation;
	}
	
	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}

	
}

