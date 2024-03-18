package entites;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public enum Buildings {
	
	HOUSE(LoadSave.HOUSE, 82, 175, 37, 49, 200),// building pic and doorway pos
	BOSSROOM(LoadSave.BOSS_ROOM , 132, 195, 46, 60, 226);	
	
	BufferedImage houseImg;
	Rectangle2D.Float hitboxDoorway;
	public int hitboxHeight, hitboxFloor;

	Buildings(String BuildingType, int doorwayX, int doorwayY, int doorwayWidth, int doorwayHeight, int hitboxFloor) {
		houseImg = LoadSave.GetSpriteAtlas(BuildingType);
		this.hitboxFloor = hitboxFloor;
		
		hitboxDoorway = new Rectangle2D.Float(doorwayX, doorwayY, doorwayWidth * Game.SCALE, doorwayHeight * Game.SCALE);
	}
	
	public int getHitboxFloor() {
		return hitboxFloor;
	}
	
	public Rectangle2D.Float getHitboxDoorway() {
        return hitboxDoorway;
    }
	
	public BufferedImage getHouseImg() {
		return houseImg;
	}
}