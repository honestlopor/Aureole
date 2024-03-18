package tiles;

import static utilz.Constants.EnemyConstants.MIMIC_HEIGHT;
import static utilz.Constants.EnemyConstants.MIMIC_WIDTH;
import static utilz.Constants.ObjectConstants.BARREL1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entites.Boss;
import entites.Building;
import entites.Buildings;
import entites.Decoration;
import entites.Decorations;
import entites.Mimic;
import entites.Slime;
import gamestates.Playing;
import main.Game;
import objectz.Containerz;
import utilz.Constants;
import utilz.HelpMethods;
import utilz.LoadSave;
import objectz.*;

public class TileManager {

	private Tile currentTile, worldMap, houseMap, house2Map, caveMap, bossMap;
	private float cameraX, cameraY;
	private Playing playing;

	
	public TileManager(Playing playing) {
		this.playing = playing;
		initTestMap();
	}
	
	public void setCameravalues(float x, float y) {
		this.cameraX = x;
		this.cameraY = y;
	}
	
	public void drawDecor(Graphics g, Decoration d) {
		if (currentTile.getDecoArr() != null) {
//			for (Decoration d : currentTile.getDecoArr()) {
			int screenX = (int) (d.getPos().x - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_WIDTH*Game.SCALE/2)));
			int screenY = (int) (d.getPos().y - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(Game.PLAYER_HEIGHT*Game.SCALE/2)));
//			d.hitbox.y = d.getDecoType().getHitboxHeight();
			d.hitbox.x = screenX + d.getDecoType().getXOffset() * Game.SCALE;
			d.hitbox.y = screenY + d.getDecoType().hitboxRoof * Game.SCALE;
//			System.out.println(screenX + " ;1");
			g.drawImage(d.getDecoType().getDecoImg(), screenX, screenY, (int)(d.getDecoType().getWidth() * Game.SCALE) , (int)(d.getDecoType().getHeight() * Game.SCALE) ,null);
//			g.setColor(Color.pink);
//			g.drawRect((int) (d.hitbox.x), (int) (d.hitbox.y), (int) (d.hitbox.width), (int) (d.hitbox.height));
		}
			
	}

	public void drawBuilding(Graphics g, Building b) {
	
			int screenX = (int) (b.getPos().x - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_WIDTH*Game.SCALE/2)));
			int screenY = (int) (b.getPos().y - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(Game.PLAYER_HEIGHT*Game.SCALE/2)));
//			System.out.println(screenX + " ;2");
			b.hitbox.x = screenX;
			b.hitbox.y = screenY;
			
			g.drawImage(b.getBuildingType().getHouseImg(), screenX, screenY, (int)(b.getWidth() * Game.SCALE) , (int)(b.getHeight() * Game.SCALE) ,null);
//			g.setColor(Color.pink);
//			g.drawRect((int) (b.hitbox.x), (int) (b.hitbox.y), (int) (b.hitbox.width), (int) (b.hitbox.height));
			
    }

	public void drawTiles(Graphics g) {
		
		if (currentTile == worldMap) {
			for(int j = 0; j < currentTile.getTileData().length; j++) 
				for(int i = 0; i < currentTile.getTileData()[0].length; i++) {
					int index = currentTile.getSpriteIndex(i, j);
					
					int worldx = i * Game.TILES_SIZE;
					int worldy = j * Game.TILES_SIZE;
					int screenX = (int) (worldx - cameraX + (int) ((Game.SCREEN_WIDTH/2)));
					int screenY = (int) (worldy - cameraY + (int) ((Game.SCREEN_HEIGHT/2)));
					//System.out.println(cameraX + " : " + cameraY);
	
					if (worldx + Game.TILES_SIZE * 2 > cameraX - ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) && 
						worldx - Game.TILES_SIZE * 2 < cameraX + ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) &&
						worldy + Game.TILES_SIZE * 2 > cameraY - ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2)) && 
						worldy - Game.TILES_SIZE * 2 < cameraY + ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2))) {
						g.drawImage(currentTile.getFloorType().getSprite()[index], screenX, screenY, Game.TILES_SIZE, Game.TILES_SIZE, null);
	//					System.out.println(screenX + " : " + screenY);
				}
			}
		} else {
			for (int j = 0; j < currentTile.getTileData().length; j++)
				for (int i = 0; i < currentTile.getTileData()[0].length; i++) {
					int index = currentTile.getSpriteIndex(i, j);
					
					int worldx = i * Game.TILES_SIZE;
					int worldy = j * Game.TILES_SIZE;
					int screenX = (int) (worldx - cameraX + (int) ((Game.SCREEN_WIDTH/2)));
					int screenY = (int) (worldy - cameraY + (int) ((Game.SCREEN_HEIGHT/2)));
					g.drawImage(currentTile.getFloorType().getSprite()[index], screenX, screenY, Game.TILES_SIZE, Game.TILES_SIZE, null);
				}
					
		}
	}
	
	public void draw(Graphics g) {
//		drawTiles(g);
//		drawBuilding(g); //added
	}
	
	public Doorway isPlayerOnDoorway(Rectangle2D.Float playerHitbox) { //added
		for (Doorway doorway : currentTile.getDoorwayArrayList()) {
			if (doorway.isPlayerInsideDoorway(playerHitbox, cameraX, cameraY))	
				return doorway;
		}
		return null;	
	}
	
	public void changeMap(Doorway doorwayTarget) { //added
		this.currentTile = doorwayTarget.getTileLocatedIn();
		
		float cx = doorwayTarget.getPosOfDoorway().x - ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_WIDTH));
//		System.out.println(doorwayTarget.get);
		float cy = doorwayTarget.getPosOfDoorway().y - ((Game.SCREEN_HEIGHT/2)-(Game.PLAYER_HEIGHT/2));
//		System.out.println(cx + " : " + cy);
		if (currentTile == worldMap)
			cy += 100;
		playing.getPlayer().setCameraValues(cx, cy);
		cameraX = cx;
		cameraY = cy;
		System.out.println(currentTile.getFloorType());
		
		playing.setDoorwayJustPassed(true);
	}

	
	public void update() {
		
	}
	
	public Tile getCurrentTile() {
		return currentTile;
	}
	
	public void resetMap() {
		currentTile = worldMap;
	}
	
	private void initTestMap() { //added

		//add building
		ArrayList<Building> buildingArrayList = new ArrayList<>();
		buildingArrayList.add(new Building(new Point(1980,1370), Buildings.HOUSE, 168, 224));
		buildingArrayList.add(new Building(new Point(1230,2000), Buildings.HOUSE, 168, 224));
		buildingArrayList.add(new Building(new Point(1600,0), Buildings.BOSSROOM, 321, 256));
		
		//add deco
		ArrayList<Decoration> decoArrayList = new ArrayList<Decoration>();
//		decoArrayList.add(new Decoration(new Point(28, 25), Decorations.BUSH));
		decoArrayList.add(new Decoration(new Point(23 * Game.TILES_SIZE, 22 * Game.TILES_SIZE), Decorations.BUSH));
		decoArrayList.add(new Decoration(new Point(22* Game.TILES_SIZE, 22* Game.TILES_SIZE), Decorations.SMALL_ROCK_1));
		decoArrayList.add(new Decoration(new Point(23* Game.TILES_SIZE, 17* Game.TILES_SIZE), Decorations.TREE_1));
		decoArrayList.add(new Decoration(new Point(21* Game.TILES_SIZE, 16* Game.TILES_SIZE), Decorations.TREE_2));
		
		decoArrayList.add(new Decoration(new Point(22* Game.TILES_SIZE, 29* Game.TILES_SIZE), Decorations.ROCK_1));
		
		decoArrayList.add(new Decoration(new Point(37* Game.TILES_SIZE, 27* Game.TILES_SIZE), Decorations.ROCK_2));

		decoArrayList.add(new Decoration(new Point(16* Game.TILES_SIZE, 33* Game.TILES_SIZE), Decorations.TREE_2));
		decoArrayList.add(new Decoration(new Point(15* Game.TILES_SIZE, 31* Game.TILES_SIZE), Decorations.TREE_1));

		
		decoArrayList.add(new Decoration(new Point(28* Game.TILES_SIZE, 26* Game.TILES_SIZE), Decorations.SMALL_ROCK_1));
		
		decoArrayList.add(new Decoration(new Point(25* Game.TILES_SIZE, 29* Game.TILES_SIZE), Decorations.SMALL_TREE_1));
		decoArrayList.add(new Decoration(new Point(26* Game.TILES_SIZE, 31* Game.TILES_SIZE), Decorations.SMALL_ROCK_1));
		
		decoArrayList.add(new Decoration(new Point(34* Game.TILES_SIZE, 37* Game.TILES_SIZE), Decorations.ELEPHANT));


		
		//add map
		worldMap = new Tile(LoadSave.GetTileData(LoadSave.TILE_DATA, 40 , 40), Floor.WORLD, buildingArrayList, decoArrayList);
        houseMap = new Tile(LoadSave.GetTileData(LoadSave.HOUSE_TILE_DATA, 10, 10), Floor.HOUSE , null, null);
        house2Map = new Tile(LoadSave.GetTileData(LoadSave.HOUSE_2_TILE_DATA, 10, 10), Floor.HOUSE_2, null, null);
        caveMap = new Tile(LoadSave.GetTileData(LoadSave.CAVE_TILE_DATA, 40, 30), Floor.CAVE, null, null);
        bossMap = new Tile(LoadSave.GetTileData(LoadSave.BOSS_ROOM_TILE_DATA, 21, 14), Floor.BOSSROOM, null, null);

        //add enemies
        caveMap.addMimic(new Mimic(16*Game.TILES_SIZE, 20*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(18*Game.TILES_SIZE, 5*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(11*Game.TILES_SIZE, 5*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(5*Game.TILES_SIZE, 16*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(18*Game.TILES_SIZE, 14*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(16*Game.TILES_SIZE, 26*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(28*Game.TILES_SIZE, 24*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(39*Game.TILES_SIZE, 13*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(39*Game.TILES_SIZE, 4*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(25*Game.TILES_SIZE, 14*Game.TILES_SIZE));
        caveMap.addMimic(new Mimic(37*Game.TILES_SIZE, 3*Game.TILES_SIZE));
        bossMap.addBoss(new Boss(7*Game.TILES_SIZE, 3 *Game.TILES_SIZE));
        worldMap.addSlime(new Slime(7*Game.TILES_SIZE, 23 *Game.TILES_SIZE));
        caveMap.addSlime(new Slime(12 * Game.TILES_SIZE, 14 * Game.TILES_SIZE));
        worldMap.addSlime(new Slime(9*Game.TILES_SIZE, 20 *Game.TILES_SIZE));
        worldMap.addSlime(new Slime(7*Game.TILES_SIZE, 17 *Game.TILES_SIZE));

        
        //add containers
        caveMap.addContainerz(new Containerz(25*Game.TILES_SIZE,  13 * Game.TILES_SIZE, Constants.ObjectConstants.BOX));
        caveMap.addContainerz(new Containerz(38*Game.TILES_SIZE, 3*Game.TILES_SIZE, BARREL1));
        worldMap.addContainerz(new Containerz(3*Game.TILES_SIZE, 20*Game.TILES_SIZE, BARREL1));
        bossMap.addContainerz(new Containerz(20*Game.TILES_SIZE,  8 * Game.TILES_SIZE, Constants.ObjectConstants.BOX));
        houseMap.addContainerz(new Containerz(3*Game.TILES_SIZE, 4*Game.TILES_SIZE, BARREL1));
        house2Map.addContainerz(new Containerz(7*Game.TILES_SIZE, 3*Game.TILES_SIZE, BARREL1));
        
        //add doorway
        HelpMethods.ConnectTwoDoorways(
        		worldMap, 
        		HelpMethods.CreateHitboxForDoorWayFloat(worldMap, 0), 
        		houseMap, 
        		HelpMethods.CreateHitboxForDoorway(4, 9));
        
        HelpMethods.ConnectTwoDoorways(
        		worldMap, 
        		HelpMethods.CreateHitboxForDoorWayFloat(worldMap, 1), 
        		house2Map, 
        		HelpMethods.CreateHitboxForDoorway(4, 9));
        
        HelpMethods.ConnectTwoDoorways(
        		worldMap, 
        		HelpMethods.CreateHitboxForDoorway(7, 15), 
        		caveMap, 
        		HelpMethods.CreateHitboxForDoorway(7, 29));
        
        HelpMethods.ConnectTwoDoorways(
        		worldMap, 
        		HelpMethods.CreateHitboxForDoorWayFloat(worldMap, 2), 
        		bossMap, 
        		HelpMethods.CreateHitboxForDoorway(0, 12));
        	
        
        currentTile = worldMap;
    }


}
