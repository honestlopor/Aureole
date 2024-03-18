package utilz;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import entites.Building;
import entites.Decoration;
import main.Game;
import tiles.Doorway;
import tiles.Floor;
import tiles.Tile;

public class HelpMethods {
//	
//	public static void AddDoorwayToTile(Tile tileLocatedIn, Tile tileTarget, int buildingIndex) {
//		float houseX = tileLocatedIn.getBuildingArrayList().get(buildingIndex).getPos().x;
//        float houseY = tileLocatedIn.getBuildingArrayList().get(buildingIndex).getPos().y;
////      System.out.println(tileLocatedIn.getBuildingArrayList().get(buildingIndex).getPos().x);
//        float screenX = houseX + ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_SIZE*Game.SCALE/2));
//        float screenY = houseY + ((Game.SCREEN_HEIGHT/2)-(Game.PLAYER_SIZE*Game.SCALE/2));
//        
//        Rectangle2D.Float hitbox = tileLocatedIn.getBuildingArrayList().get(buildingIndex).getBuildingType().getHitboxDoorway();
//        Doorway doorway = new Doorway(new Rectangle2D.Float(screenX + hitbox.x * Game.SCALE
//        												  , screenY + hitbox.y * Game.SCALE
//        												  , hitbox.width, hitbox.height)
//        											, tileTarget); // create new doorway
////		System.out.println(houseX + ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_SIZE*Game.SCALE/2)));
//        tileLocatedIn.addDoorway(doorway);
//	}
	
	public static Rectangle2D.Float CreateHitboxForDoorWayFloat(Tile tileLocatedIn, int buildingIndex) { //added
		Building building = tileLocatedIn.getBuildingArrayList().get(buildingIndex); 
		
		float x = building.getPos().x + ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_WIDTH*Game.SCALE/2));
		float y = building.getPos().y + ((Game.SCREEN_HEIGHT/2)-(Game.PLAYER_HEIGHT*Game.SCALE/2));
        Rectangle2D.Float hitbox = tileLocatedIn.getBuildingArrayList().get(buildingIndex).getBuildingType().getHitboxDoorway();

		
		return new Rectangle2D.Float(x + hitbox.x * Game.SCALE, y + hitbox.y * Game.SCALE, hitbox.width, hitbox.height);
	}
	
	public static Rectangle2D.Float CreateHitboxForDoorway(int xTile, int yTile) { //added
        float x = xTile * Game.TILES_SIZE + ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_WIDTH*Game.SCALE/2));
//        System.out.println(x);
        float y = yTile * Game.TILES_SIZE + ((Game.SCREEN_HEIGHT/2));
        return new Rectangle2D.Float(x+32 , y, Game.TILES_SIZE-10, Game.TILES_SIZE-10);
    }

	
	public static void ConnectTwoDoorways(Tile tileOne, Rectangle2D.Float hitboxOne, Tile tileTwo, Rectangle2D.Float hitboxTwo) { //added
		
		Doorway doorwayOne = new Doorway(hitboxOne, tileOne);
		Doorway doorwayTwo = new Doorway(hitboxTwo, tileTwo);
		
		doorwayOne.connectDoorway(doorwayTwo);
		doorwayTwo.connectDoorway(doorwayOne);
	}
	
	public static boolean CanWalkHere(Rectangle2D hitbox, Float x, Float y, Tile currentTile, float dx, float dy) { //added
		if (x - hitbox.getWidth()/2 < 0 || y - hitbox.getHeight()/2 < 0)
			return false;
		
		if (x + hitbox.getWidth()/2  >= currentTile.getArrayWidth() * Game.TILES_SIZE || y + hitbox.getHeight()/2 >= currentTile.getArrayHeight() * Game.TILES_SIZE)
			return false;
		
		Rectangle2D.Float temphitbox = new Rectangle2D.Float((int)hitbox.getX() - dx, (int)(hitbox.getY()) - dy, (int)(hitbox.getWidth()), (int)(hitbox.getHeight()));
		if(currentTile.getDecoArr() != null) {
			for(Decoration d: currentTile.getDecoArr()) {
				if (d.getHitbox().intersects(temphitbox))
					return false;
				
		if(currentTile.getBuildingArrayList() != null) {
			for(Building b: currentTile.getBuildingArrayList()) {
				if (b.getHitbox().intersects(temphitbox))
					return false;
			}
		}

			}
		}
		
		Point[] tileCords = GetTileCords(hitbox, x, y);
		int[] tileIds = GetTileIds(tileCords, currentTile.getTileData());
		return isTileWalkable(tileIds, currentTile);
	}
	
	 private static Point[] GetTileCords(Rectangle2D hitbox, float x, float y) {
	        Point[] tileCords = new Point[4];
	        //System.out.println(x + " : " + y);
	        
	        int left = (int) ((x - hitbox.getWidth()/2) / Game.TILES_SIZE);
	        int right = (int) ((x + hitbox.getWidth()/2) / Game.TILES_SIZE);
	        int top = (int) ((y) / Game.TILES_SIZE);
	        int bottom = (int) ((y + hitbox.getHeight()/2) / Game.TILES_SIZE);

	        tileCords[0] = new Point(left, top);
	        tileCords[1] = new Point(right, top);
	        tileCords[2] = new Point(left, bottom);
	        tileCords[3] = new Point(right, bottom);

	        return tileCords;

	    }
	 
	 private static int[] GetTileIds(Point[] tileCords, int[][] lvlData) {
	        int[] tileIds = new int[4];

	        for (int i = 0; i < tileCords.length; i++) {
	            tileIds[i] = lvlData[tileCords[i].y][tileCords[i].x];
	        	//System.out.println(tileIds[i]);
	        }
	        return tileIds;
	    }

	public static boolean isTileWalkable(int[] tileIds, Tile currentTile) {
			for (int i : tileIds) 
				if (!(isTileWalkable(i, currentTile)))
						return false;
			return true;
		}	
	 
	public static boolean isTileWalkable(int tileId, Tile currentTile) { //added
		if (currentTile.getFloorType() == Floor.WORLD) 
			return (tileId != 94 && tileId != 95 && tileId != 108 && tileId != 109 && tileId != 84 && tileId != 85 && tileId != 86 && tileId != 72 &&
					tileId != 59 && tileId != 60 && tileId != 61 && tileId != 48 && tileId != 63 && tileId != 50 && tileId != 51 && tileId != 38 &&
					tileId != 52 && tileId != 66 && tileId != 80 && tileId != 99 && tileId != 70 && tileId != 98 && tileId != 112 && tileId != 126 && tileId != 283 &&
					(tileId < 145 || tileId > 150) && (tileId < 173 || tileId > 178) && (tileId < 187 || tileId > 192) && //bridge horizontal
					tileId != 210 && tileId != 212 && tileId != 224 && tileId != 226 && tileId != 238 && tileId != 240 && tileId != 252 && tileId != 254 && tileId != 266 && tileId != 268); //bridge vertical
					
		else if (currentTile.getFloorType() == Floor.HOUSE)
			return (tileId != 21 && tileId != 23 && (tileId < 0 || tileId > 2) && tileId != 9 && tileId != 16 && tileId != 15 && tileId != 14 && tileId != 17
					&& tileId != 24 && tileId != 25 && tileId != 26 && tileId != 6 && tileId != 20 && tileId != 28 && tileId != 11 && tileId != 7);
		
		else if (currentTile.getFloorType() == Floor.HOUSE_2)
			return ((tileId < 10 || tileId > 16) && tileId != 28 && tileId != 17 && tileId != 8 && tileId != 19 && tileId != 37 && tileId != 39);
		
		else if (currentTile.getFloorType() == Floor.CAVE)
			return (tileId != 11 && (tileId < 12 || tileId > 15) && (tileId < 18 || tileId > 20) && (tileId < 24 || tileId > 26));
		
		
		return true;
	}
	
	
}
