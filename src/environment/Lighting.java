package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Shape;

import entites.Player;
import main.Game;

public class Lighting {
	
	BufferedImage darknessFilter;
	
	public Lighting(Game game, int circleSize) {		
		
		darknessFilter = new BufferedImage(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();
		
		Area screenArea = new Area(new Rectangle2D.Double(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT));
		
		int centerX = (int) (game.getPlaying().getPlayer().cameraX + (Game.TILES_SIZE/2));
		int centerY = (int) (game.getPlaying().getPlayer().cameraY + (Game.TILES_SIZE/2));

		double x = centerX - (circleSize/2);
		double y = centerY - (circleSize/2);
		
		Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);
		
		Area lightArea = new Area(circleShape);
		
		screenArea.subtract(lightArea);
		
		g2.setColor(new Color(0, 0, 0, 0.95f));
		
		g2.fill(screenArea);
		
		g2.dispose();
	}
	
	public void draw(Graphics2D g2) {
		
		g2.drawImage(darknessFilter, 0, 0, null);
	}

}
