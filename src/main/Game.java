package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.GameSelect;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import utilz.LoadSave;

public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;
	private GameSelect gameSelect;
	private GameOptions gameOptions;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;
	
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 2f;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int SCREEN_WIDTH = TILES_SIZE * TILES_IN_WIDTH; //1280
	public final static int SCREEN_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT; //720

	public final static int MAP_WIDTH = 40;
	public final static int MAP_HEIGHT = 40;
	
	public final static int PLAYER_SIZE = (int)(350 * 0.76);
	public final static int PLAYER_WIDTH = (int)(31 * 0.76);
	public final static int PLAYER_HEIGHT = (int)(44 * 0.76);	

	
	public Game() {
		initClasses();
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		
		startGameLoop();
	}

	private void initClasses() {
		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		menu = new Menu(this);
		gameSelect = new GameSelect(this);
		playing = new Playing(this);
		gameOptions = new GameOptions(this); 
		
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case SELECT:
			gameSelect.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
			gameOptions.update();
			break;
		case QUIT:
		default:
			System.exit(0);
			break;

		}
	}
	
	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case SELECT:
			gameSelect.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		case OPTIONS:
			gameOptions.draw(g);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
//				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public void windowFocusLost() {
		if(Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
		// add pause later
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Playing getPlaying() {
		return playing;
	}
	
	public GameOptions getGameOptions() {
		return gameOptions;
	}
	
	public AudioOptions getAudioOptions() {
		return audioOptions;
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
	
	public GameSelect getGameSelect() {
		return gameSelect;
	}

 }