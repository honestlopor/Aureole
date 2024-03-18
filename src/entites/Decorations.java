package entites;

import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public enum Decorations {
	TREE_1(LoadSave.TREE_1, 103, 184,128,153,22,35),
	TREE_2(LoadSave.TREE_2, 102, 185,128,153,22,35),
	SMALL_TREE_1(LoadSave.S_TREE_1, 33, 64,37,47,8,14),
	SMALL_TREE_2(LoadSave.S_TREE_2, 32, 50,0,0,0,0),
	BUSH(LoadSave.BUSH_1, 52, 31,0,0,0,0),
	ROCK_1(LoadSave.ROCK_1, 49, 27,0,6,49,0),
	ROCK_2(LoadSave.ROCK_2, 43, 22,0,4,43,0),
	SMALL_ROCK_1(LoadSave.S_ROCK_1, 20, 14,0,1,20,0),
	ELEPHANT(LoadSave.ELEPHANT, 60, 52,40,42,60,0);
	
	BufferedImage decoImg;
	public int width, height, xOffset;
	public int hitboxRoof, hitboxHeight, hitboxFloor, hitboxWidth;

	Decorations(String filename, int width, int height, int hitboxRoof, int hitboxFloor, int hitboxWidth, int xOffset) {
		this.decoImg = LoadSave.GetSpriteAtlas(filename);
		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.hitboxRoof = hitboxRoof;
		this.hitboxFloor = hitboxFloor;
		this.hitboxWidth = hitboxWidth;
		this.hitboxHeight = hitboxFloor - hitboxRoof;
	}
	
	public int getHitboxRoof() {
		return hitboxRoof;
	}
	
	public int getHitboxHeight() {
		return hitboxHeight;
	}
	
	public int getHitboxWidth() {
		return hitboxWidth;
	}
	
	public BufferedImage getDecoImg() {
		return decoImg;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getXOffset() {
		return xOffset;
	}
}
