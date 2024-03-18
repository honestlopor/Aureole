package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import entites.EnemyManager;
import entites.Entity;
import entites.Building;
import entites.BuildingManager;
import entites.Decoration;
import entites.Player;
import main.Game;
import objectz.ObjectManager;
import tiles.Doorway;
import tiles.Floor;
import tiles.TileManager;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

public class Playing extends State implements Statemethods {
	private Player player;
	private TileManager tileManager;
	private EnemyManager enemyManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	
	private BuildingManager house;
	private ObjectManager objectManager;
	private boolean doorwayJustPassed;
	
	public boolean paused = false;
	
	private boolean gameOver;
	private boolean playerDying;
	 
    private Entity[] listOfDrawables;
    private boolean listOfEntitiesMade;
	
	public Playing(Game game) {
		super(game);
		initClasses();
	}
	
	private void initClasses() {
		tileManager = new TileManager(this);
		enemyManager = new EnemyManager(this);
		player = new Player((Game.SCREEN_WIDTH/2) - (Game.PLAYER_SIZE * Game.SCALE/2), (Game.SCREEN_HEIGHT/2) - (Game.PLAYER_SIZE * Game.SCALE/2), (int)((Game.PLAYER_WIDTH-5)*Game.SCALE), (int)((Game.PLAYER_HEIGHT+7)*Game.SCALE), this);
		objectManager = new ObjectManager(this);
		player.loadLvlData(tileManager.getCurrentTile().getTileData());
		enemyManager.loadLvlData(tileManager.getCurrentTile().getTileData());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		
	}
	
	public TileManager getTileManager() {
		return tileManager;
	}
	
	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
		} else if (gameOver) {
			gameOverOverlay.update();
		} else if (playerDying) {
			player.update();
		} else {
	        buildEntityList();
			tileManager.update();
			player.update();
			enemyManager.update(player);
			objectManager.update();
			tileManager.setCameravalues(player.getCameraX(), player.getCameraY());
			checkForDoorway(); // added
			objectManager.setCameraValues(player.getCameraX(), player.getCameraY());
			enemyManager.setCameraValues(player.getCameraX(), player.getCameraY());
		}
		sortArray();
	}
	
	private void buildEntityList() {
        //TODO: will add check for this next episode
		if (tileManager.getCurrentTile().getBuildingArrayList() != null)
	        listOfDrawables = tileManager.getCurrentTile().getDrawableList();
	        listOfDrawables[listOfDrawables.length - 1] = player;
	        listOfEntitiesMade = true;
    }

    private void sortArray() {
        player.setLastCameraYValue(player.getCameraY());
        Arrays.sort(listOfDrawables);
    }
	
	private void checkForDoorway() { // added
        Doorway doorwayPlayerIsOn = tileManager.isPlayerOnDoorway(player.getHitbox());
//        System.out.println(player.getHitbox().x);
        if (doorwayPlayerIsOn != null && player.hasKey1 >= 1) {
        	if (!doorwayJustPassed) {
            	tileManager.changeMap(doorwayPlayerIsOn.doorwayConnectedTo);
            	player.hasKey1-=1;
            	System.out.println(player.hasKey1);
//            	player.hasKey1;
//            	System.out.println(getTileManager().getCurrentTile());
        		getGame().getAudioPlayer().setTileSong(getTileManager().getCurrentTile());
        	}
        } else {
        	doorwayJustPassed = false;
        }
    }
	
	public void setDoorwayJustPassed(boolean doorwayJustPassed) { // added
		this.doorwayJustPassed = doorwayJustPassed;
	}

	@Override
	public void draw(Graphics g) {
		if (getTileManager().getCurrentTile().getFloorType() == Floor.WORLD) {
			BufferedImage backgroundSprite = LoadSave.GetSpriteAtlas(LoadSave.GAME_BACKGROUND);
			g.drawImage(backgroundSprite, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
		}
			
//		
		tileManager.drawTiles(g);
		enemyManager.draw(g, paused, gameOver);
		objectManager.draw(g);
		if (tileManager.getCurrentTile().getBuildingArrayList() != null)
			for (Building b : tileManager.getCurrentTile().getBuildingArrayList())
				tileManager.drawBuilding(g, b);
		if (listOfEntitiesMade) {
            drawSortedEntities(g);
//            System.out.println("drawwwwwww");
		}
//		player.render(g);
		
		if (paused)
			pauseOverlay.draw(g);
		else if (gameOver) 
			gameOverOverlay.draw(g);
	}
	
	private void drawSortedEntities(Graphics g) {
        for (Entity e : listOfDrawables) {
            
            if (e instanceof Decoration decoration) {
                tileManager.drawDecor(g, decoration);
//                System.out.println(decoration.getDecoType());
//                System.out.println("decoo");
//            } else if (e instanceof Building building) {
//                tileManager.drawBuilding(g,building);
            } else if (e instanceof Player) {
        		player.render(g);
//        		System.out.println("PLAYER");
            }
        }
    }
	
	public void resetAll() {
		//TODO: reset playing, enemy, tile etc.
		gameOver = false;
		paused = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObject();
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
		
	}
	
	public void checkObjectHit(Float attackBox) {
		objectManager.checkObjectHit(attackBox);
		
	}
	
	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver) {
			gameOverOverlay.keyPressed(e);
		}
		else
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				player.setUp(true);
				break;
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_S:
				player.setDown(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_UP:
				player.setUp(true);
				break;
			case KeyEvent.VK_LEFT:
				player.setLeft(true);
				break;
			case KeyEvent.VK_DOWN:
				player.setDown(true);
				break;
			case KeyEvent.VK_RIGHT:
				player.setRight(true);
				break;
			case KeyEvent.VK_J:
				player.setAttack(true);
				break;
			case KeyEvent.VK_K:
				player.powerAttack();
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				player.setMoving(false);
				player.setUp(false);
				break;
			case KeyEvent.VK_A:
				player.setMoving(false);
				player.setLeft(false);
				break;
			case KeyEvent.VK_S:
				player.setMoving(false);
				player.setDown(false);
				break;
			case KeyEvent.VK_D:
				player.setMoving(false);
				player.setRight(false);
				break;
			case KeyEvent.VK_UP:
				player.setMoving(false);
				player.setUp(false);			
				break;
			case KeyEvent.VK_LEFT:
				player.setMoving(false);
				player.setLeft(false);
				break;
			case KeyEvent.VK_DOWN:
				player.setMoving(false);
				player.setDown(false);
				break;
			case KeyEvent.VK_RIGHT:
				player.setMoving(false);
				player.setRight(false);
				break;
//			case KeyEvent.VK_K:
//				player.setAttack(false);
//				break;
			}

	}
	
	public void mouseDragged(MouseEvent e) {
		if (!gameOver)
			if (paused)
				pauseOverlay.mouseDragged(e);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused) 
				pauseOverlay.mousePressed(e);
		} else 
			gameOverOverlay.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);	
		} else 
			gameOverOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e); 
		} else 
			gameOverOverlay.mouseMoved(e);
	}
	
	public void unpauseGame() {
		paused = false;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
		
	}

}
