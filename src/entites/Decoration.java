package entites;

import java.awt.Point;

import main.Game;

public class Decoration extends Entity{
	private Decorations decoType;
	private Point pos;

	public Decoration (Point pos, Decorations decoType) {
		super(pos.x, pos.y + (decoType.hitboxRoof * Game.SCALE) , decoType.getHitboxWidth() * (int)Game.SCALE, decoType.hitboxHeight * (int)Game.SCALE);
        this.pos = pos;
		this.decoType = decoType;
	}
	 
	public Decorations getDecoType() {
		return decoType;
	}
	
	public Point getPos() {
        return pos;
    }

}
