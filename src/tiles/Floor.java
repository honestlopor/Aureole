package tiles;

import java.awt.image.BufferedImage;

import utilz.LoadSave;

public enum Floor {
	
	WORLD(LoadSave.TILE_ATLAS, 14, 22), // pic
	HOUSE(LoadSave.HOUSE_TILE_ATLAS, 7, 5), // pic
	HOUSE_2(LoadSave.HOUSE_2_TILE_ATLAS, 10, 4),
	CAVE(LoadSave.CAVE_TILE_ATLAS, 6, 5),
	BOSSROOM(LoadSave.BOSS_ROOM_TILE_ATLAS, 21, 14);
	
	private BufferedImage[] tileSprite;

	
	Floor(String spriteName, int tilesInWidth, int tilesInHeight) {
		BufferedImage img = LoadSave.GetSpriteAtlas(spriteName);
		tileSprite = new BufferedImage[tilesInWidth * tilesInHeight];
		for(int j = 0; j < tilesInHeight; j++) 
			for(int i = 0; i < tilesInWidth; i++) {
				int index = j*tilesInWidth + i;
				tileSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
			}
	}
	
	public BufferedImage[] getSprite() {
		return tileSprite;
	}

}