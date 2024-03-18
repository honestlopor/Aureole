package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.BackgroundManager;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {
	
	private BackgroundManager backgroundManager;
	
	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage backgroundImg;
	private int menuX, menuY, menuWidth, menuHeight;

	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackground();
		initClasses();


	}
	
private void initClasses() {
	backgroundManager = new BackgroundManager(this);
		
	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.GAME_NAME);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE*1.5);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE*1.5);
		menuX = Game.SCREEN_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (50 * Game.SCALE);
		
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(Game.SCREEN_WIDTH/2, (int) (175 * Game.SCALE), 0, Gamestate.SELECT); //150
		buttons[1] = new MenuButton(Game.SCREEN_WIDTH/2, (int) (245 * Game.SCALE), 1, Gamestate.OPTIONS); //220
		buttons[2] = new MenuButton(Game.SCREEN_WIDTH/2, (int) (315 * Game.SCALE), 2, Gamestate.QUIT); //290
	}

	@Override
	public void update() {
		for (MenuButton mb : buttons) {
			backgroundManager.update();
			mb.update();
		}
	}

	@Override
	public void draw(Graphics g) {
		backgroundManager.draw(g);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
		
		for (MenuButton mb : buttons)
			mb.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMousePressed(true);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed())
					mb.applyGamestate();
			}
		}
		
		resetButtons();
	}
	
	private void resetButtons() {
		for (MenuButton mb : buttons) 
			mb.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons)
			mb.setMouseOver(false);

		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			Gamestate.state = Gamestate.SELECT;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
