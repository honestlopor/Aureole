package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.GameBackground;
import ui.SelectButton;
import utilz.LoadSave;

public class GameSelect extends State implements Statemethods {
	
	private SelectButton[] buttons = new SelectButton[2];
	
	private BufferedImage[][] backgroundImgs;
	private GameBackground background;

	public GameSelect(Game game) {
		super(game);
		loadImgs();
		background = new GameBackground();

		loadButtons();
		
	}

	private void loadButtons() {
		buttons[0] = new SelectButton((int) (285 * Game.SCALE), Game.SCREEN_HEIGHT/2 + 45, 0, Gamestate.PLAYING, LoadSave.HIMMEL); //150
		buttons[1] = new SelectButton((int) (400 * Game.SCALE), Game.SCREEN_HEIGHT/2 + 45, 1, Gamestate.PLAYING, LoadSave.FRIEREN); //220
	}

	private void loadImgs() {
		BufferedImage backgroundSprite = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		backgroundImgs = new BufferedImage[1][8];
		
		for (int j=0;j<1;j++) {
			for (int i=0;i<7;i++) {
				backgroundImgs[j][i] = backgroundSprite.getSubimage(400*i, 225*j, 400, 225);
			}
		}
		
	}

	@Override
	public void update() {
		for (SelectButton sb : buttons) {
			background.updateAnimationTick(130);
			sb.update();
		}		
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImgs[0][background.getAniIndex()], 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
		BufferedImage backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.SELECT_BACKGROUND);
		g.drawImage(backgroundImage, (Game.SCREEN_WIDTH/2 - 385), (Game.MAP_HEIGHT + 130), Game.SCREEN_WIDTH/2 - 50, Game.SCREEN_WIDTH/2 - 50, null);
		for (SelectButton sb : buttons) {
			sb.draw(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (SelectButton sb : buttons) {
			if (isIn(e, sb)) {
				sb.setMousePressed(true);
			}
		}		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (SelectButton sb : buttons) {
			if (isIn(e, sb)) {
				if (sb.isMousePressed())
					sb.applyGamestate();
				if (sb.getState() == Gamestate.PLAYING) {
					game.getAudioPlayer().setTileSong(game.getPlaying().getTileManager().getCurrentTile());
					game.getPlaying().getPlayer().loadAnimations(sb.getCharacter());

				}
		
				break;
			}
		}
		
		resetButtons();		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (SelectButton sb : buttons)
			sb.setMouseOver(false);

		for (SelectButton sb : buttons)
			if (isIn(e, sb)) {
				sb.setMouseOver(true);
				break;
			}			
	}
	
	private void resetButtons() {
		for (SelectButton sb : buttons) 
			sb.resetBools();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Gamestate.state = Gamestate.MENU;		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
