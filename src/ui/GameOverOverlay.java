package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.URM_SIZE;

public class GameOverOverlay {

	private Playing playing;
	private BufferedImage img;
	private int imgX, imgY, imgW, imgH;
	private UrmButton menu, restart;
	
	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		createImg();
		createButtons();
	}
	
	private void createButtons() {
		int menuX = (int) (355 * Game.SCALE);
		int restartX = (int) (420 * Game.SCALE);
		int y = (int) (232 * Game.SCALE);
		restart = new UrmButton(restartX, y, URM_SIZE, URM_SIZE, 1);
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
		
	}

	private void createImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgW = (int) ((img.getWidth()-80) * Game.SCALE);
		imgH = (int) ((img.getHeight()-80) * Game.SCALE);
		imgX = Game.SCREEN_WIDTH / 2 - imgW / 2;
		imgY = (int) (70 * Game.SCALE);
		
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
		g.drawImage(img, imgX, imgY, imgW, imgH, null); 
		
		menu.draw(g);
		restart.draw(g);
		
//		g.setColor(Color.white);
//		g.drawString("Game Over", Game.SCREEN_WIDTH / 2, 150);
//		g.drawString("Press esc to enter Main Menu!", Game.SCREEN_WIDTH / 2, 300);
		
	}
	
	public void update() {
		menu.update();
		restart.update();
	}
	
	public void keyPressed(KeyEvent e) { 
		
	}
	
	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		restart.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e))
			menu.setMouseOver(true);
		else if (isIn(restart, e))
			restart.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				playing.resetAll();
				playing.setGameState(Gamestate.MENU);
			}
		} else if (isIn(restart, e))
			if (restart.isMousePressed()) { 
				playing.resetAll();
				playing.getGame().getAudioPlayer().setTileSong(playing.getTileManager().getCurrentTile());
			}
		menu.resetBools();
		restart.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e))
			menu.setMousePressed(true);
		else if (isIn(restart, e))
			restart.setMousePressed(true);
	}
	
}
