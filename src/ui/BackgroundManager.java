package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.GameSelect;
import gamestates.Menu;
import main.Game;
import utilz.LoadSave;

public class BackgroundManager {
	
	
	private Menu menu;
	private BufferedImage[][] backgroundImgs;
	private GameBackground background;
	
	public BackgroundManager(Menu menu) {
		this.menu = menu;
		loadImgs();
		
		background = new GameBackground();
	}
	
	public BackgroundManager(GameSelect gameSelect) {
		// TODO Auto-generated constructor stub
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
	
	public void update() {
		background.updateAnimationTick(200);
	}
	
	public void draw(Graphics g) {
		g.drawImage(backgroundImgs[0][background.getAniIndex()], 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
	}
}
