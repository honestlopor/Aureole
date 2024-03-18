package environment;

import java.awt.Graphics2D;

import main.Game;

public class EnvironmentManager {
	
	Game game;
	Lighting lighting;
	
	public EnvironmentManager(Game game) {
		this.game = game;
	}
	
	public void setup() {
		
		lighting = new Lighting(game, 350);
	}
	
	public void draw(Graphics2D g2) {
		
		lighting.draw(g2);
	}
}
