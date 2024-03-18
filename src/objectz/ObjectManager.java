package objectz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import main.Game;
import tiles.Floor;
import utilz.LoadSave;

import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.PlayerConstants.IDLE_FRONT;


public class ObjectManager {

	private Playing playing;
	private BufferedImage[][] potionImgs, containerImgs, key_Imgs;
	private BufferedImage tree_Imgs;
	private ArrayList<Potion> potions;
	private ArrayList<Containerz> container;
	private ArrayList<Key> keys;
//	private ArrayList<Tree> trees;
	
	private float cameraX, cameraY;
	
	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();
		
		potions = new ArrayList<>();
		container = new ArrayList<>();
		keys = new ArrayList<>();
		

//		potions.add(new Potion(30*Game.TILES_SIZE + POTION_WIDTH/2 + (int) (3 * Game.SCALE), 17*Game.TILES_SIZE + POTION_HEIGHT/2, RED_POTION)); // RED
//		potions.add(new Potion(31*Game.TILES_SIZE + POTION_WIDTH/2 + (int) (3 * Game.SCALE), 17*Game.TILES_SIZE + POTION_HEIGHT/2 - (int) (2 * Game.SCALE), BLUE_POTION)); // BLUE
		
		
//		keys.add(new Key(20*Game.TILES_SIZE + KEY_WIDTH/2, 25*Game.TILES_SIZE + KEY_HEIGHT/2, KEY_1));
	}

	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for (Potion p : potions) {
			if (p.isActive()) {
				if (hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					applyPotionToPlayer(p);
				}
			}
		}
		
		for (Key k : keys) {
			if (k.isActive()) {
				if (hitbox.intersects(k.getHitbox())) {
					k.setActive(false);
					applyKeyToPlayer(k);
				}
			}
		}

	}
	
	public void checkObjectHit(Rectangle2D.Float attackbox) {
		if (playing.getTileManager().getCurrentTile().getContainerArr() != null) {
			for (Containerz c: playing.getTileManager().getCurrentTile().getContainerArr())
				if (c.isActive()) {
					if (attackbox.intersects(c.getHitbox())) {
						c.setAnimation(true);
						if (c.objType == BOX)
							potions.add(new Potion((int) (c.worldX + CONTAINER_WIDTH/3), (int) (c.worldY + CONTAINER_HEIGHT/3), RED_POTION));
						else if (c.objType == BARREL1)
							keys.add(new Key(c.worldX, c.worldY, KEY_1));
						else if (c.objType == BARREL2)
							keys.add(new Key(c.worldX, c.worldY, KEY_2));
						return;
					}
				}
		}
	}
	
	public void applyPotionToPlayer(Potion p) {
		if (p.getObjType() == RED_POTION)
			playing.getPlayer().changHealth(RED_POTION_VALUE);
		
	}
	
	public void applyKeyToPlayer(Key k) {
		if (k.getObjType() == KEY_1)
			playing.getPlayer().hasKey1 += 2;
	}
	
	private void loadImgs() {
		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
		potionImgs = new BufferedImage[2][7];
		for (int j=0;j<potionImgs.length;j++) {
			for (int i=0;i<potionImgs[j].length;i++) {
				potionImgs[j][i] = potionSprite.getSubimage(32*i, 32*j, 32, 32);
			}
		}
		
		BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImgs = new BufferedImage[2][8];
		for (int j=0;j<containerImgs.length;j++) {
			for (int i=0;i<containerImgs[j].length;i++) {
				containerImgs[j][i] = containerSprite.getSubimage(32*i, 32*j, 32, 32);
			}
		}
		
		BufferedImage key_Sprite = LoadSave.GetSpriteAtlas(LoadSave.KEYHOUSE_ATLAS);
		key_Imgs = new BufferedImage[2][8];
		for (int j=0;j<key_Imgs.length;j++) {
			for (int i=0;i<key_Imgs[j].length;i++) {
				key_Imgs[j][i] = key_Sprite.getSubimage(50*i, 50*j, 50, 50);
			}
		}
//		tree_Imgs = LoadSave.GetSpriteAtlas(LoadSave.TREE_1_ATLAS);
			
		
	}
	
	public void update() {
		for (Potion p: potions)
			if (p.isActive())
				p.update();
		
		if (playing.getTileManager().getCurrentTile().getContainerArr() != null) 
			for (Containerz c: playing.getTileManager().getCurrentTile().getContainerArr())
				if (c.isActive())
					c.update();
		
		for (Key k: keys)
			if (k.isActive())
				k.update();
	
	}
	
	public void setCameraValues(float x, float y) {
		this.cameraX = x;
		this.cameraY = y;
	}
	
	public void draw(Graphics g) {
		drawPotions(g);
		drawKeys(g);
		drawContainers(g);
//		drawTree(g);

	}

//	public void drawTree(Graphics g) {
//		if (trees.size() != 0 && playing.getTileManager().getCurrentTile().getFloorType() == Floor.WORLD) {
//			for (Tree t: trees)
//				if (t.isActive()) {
//					int type = 0;
//					
//					int screenX = (int) (t.worldX - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_WIDTH*Game.SCALE/2)));
//					int screenY = (int) (t.worldY - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(Game.PLAYER_HEIGHT*Game.SCALE/2)));
//		//				p.hitbox.x = screenX - 10;
//		//				p.hitbox.y = screenY;
////					if (t.worldX + Game.TILES_SIZE > cameraX - ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) && 
////						t.worldX - Game.TILES_SIZE < cameraX + ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) &&
////						t.worldY + Game.TILES_SIZE > cameraY - ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2)) && 
////						t.worldY - Game.TILES_SIZE < cameraY + ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2))) {
//					g.drawImage(tree_Imgs, screenX-10, screenY, TREE_1_WIDTH, TREE_1_HEIGHT, null);
//		
//		//					g.setColor(Color.pink);
//		//					g.drawRect((int) (t.hitbox.x), (int) (t.hitbox.y), (int) t.hitbox.width, (int) t.hitbox.height);
//					}
//				}
//		}
	
	
	private void drawContainers(Graphics g) {
		if (playing.getTileManager().getCurrentTile().getContainerArr() != null) 
			for (Containerz c: playing.getTileManager().getCurrentTile().getContainerArr())
				if (c.isActive()) {
					int type = 0;
					if (c.getObjType() == BARREL1 || c.getObjType() == BARREL2) {
						type = 1;
					}
					
					int screenX = (int) (c.worldX - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(CONTAINER_WIDTH/2)));
					int screenY = (int) (c.worldY - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(CONTAINER_HEIGHT/2)));
					c.hitbox.x = screenX + 8 + 32;
					c.hitbox.y = screenY + 12 + 32;
					if (c.worldX + Game.TILES_SIZE > cameraX - ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) && 
						c.worldX - Game.TILES_SIZE < cameraX + ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) &&
						c.worldY + Game.TILES_SIZE > cameraY - ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2)) && 
						c.worldY - Game.TILES_SIZE < cameraY + ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2))) {
						g.drawImage(containerImgs[type][c.getAniIndex()], screenX + 32, screenY+32, CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
						
					}
				}
		
	}

	private void drawPotions(Graphics g) {
		for (Potion p: potions)
			if (p.isActive()) {
				int type = 0;
				if (p.getObjType() == RED_POTION)
					type = 1;
				
				int screenX = (int) (p.worldX - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_WIDTH*Game.SCALE/2)));
				int screenY = (int) (p.worldY - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(Game.PLAYER_HEIGHT*Game.SCALE/2)));
				p.hitbox.x = screenX - 10+16+16;
				p.hitbox.y = screenY+16+16;
				if (p.worldX + Game.TILES_SIZE > cameraX - ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) && 
					p.worldX - Game.TILES_SIZE < cameraX + ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) &&
					p.worldY + Game.TILES_SIZE > cameraY - ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2)) && 
					p.worldY - Game.TILES_SIZE < cameraY + ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2))) {
					g.drawImage(potionImgs[type][p.getAniIndex()], screenX-10+16+16, screenY+16+16, POTION_WIDTH, POTION_HEIGHT, null);

					
				}
			}
	}
	
	private void drawKeys(Graphics g) {
		for (Key k: keys) 
			if (k.isActive()) {
				int type = 0;
				if (k.getObjType() == KEY_2)
					type = 1;
				
				int screenX = (int) (k.worldX - cameraX + (int) ((Game.SCREEN_WIDTH/2)-(Game.PLAYER_WIDTH*Game.SCALE/2)));
				int screenY = (int) (k.worldY - cameraY + (int) ((Game.SCREEN_HEIGHT/2)-(Game.PLAYER_HEIGHT*Game.SCALE/2)));
//				System.out.println(cameraX);
									
				k.hitbox.x = screenX + 6 + 32;
				k.hitbox.y = screenY + 6 + 32 ;
				if (k.worldX + Game.TILES_SIZE > cameraX - ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) && 
					k.worldX - Game.TILES_SIZE < cameraX + ((Game.SCREEN_WIDTH/2)-(Game.TILES_SIZE/2)) &&
					k.worldY + Game.TILES_SIZE > cameraY - ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2)) && 
					k.worldY - Game.TILES_SIZE < cameraY + ((Game.SCREEN_HEIGHT/2)-(Game.TILES_SIZE/2))) {
					g.drawImage(key_Imgs[type][k.getAniIndex()], screenX+32, screenY + 8 + 32, KEY_WIDTH, KEY_HEIGHT, null);

					
				}
			}
			else if (playing.getPlayer().hasKey1 != 0){
				int type = 0;
				if (k.getObjType() == KEY_2)
					type = 1;
				g.drawImage(key_Imgs[type][0], 24*Game.TILES_SIZE, (int) (35+(type*KEY_HEIGHT*Game.SCALE)), (int) (KEY_WIDTH*Game.SCALE), (int) (KEY_HEIGHT*Game.SCALE), null);
			}
	}
	
	
	public void resetAllObject() {
//		potions.clear();
//		keys.clear();		
		if (playing.getTileManager().getCurrentTile().getContainerArr() != null) {
			for (Containerz c: playing.getTileManager().getCurrentTile().getContainerArr())
				c.reset();

			potions.clear();
			keys.clear();
			
		}
	}

}
