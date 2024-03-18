package tiles;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Doorway {

	private Rectangle2D.Float hitbox;
	private boolean active = true;
	private final Tile tileLocatedIn;
	public Doorway doorwayConnectedTo;
	
	public Doorway(Rectangle2D.Float doorwayHitbox, Tile tileLocatedIn) {
		this.hitbox = doorwayHitbox;
		this.tileLocatedIn = tileLocatedIn;
		tileLocatedIn.addDoorway(this);
	}
	
	public void connectDoorway(Doorway destinationDoorway) {
		this.doorwayConnectedTo = destinationDoorway;
	}
	
	public Doorway getDoorwayConnectedTo() {
		if (doorwayConnectedTo != null)
			return doorwayConnectedTo;
		return null;
	}
	
	public boolean isPlayerInsideDoorway(Rectangle2D.Float playerHitbox, float cameraX, float cameraY) {
//		hitbox.x -= cameraX;
//		hitbox.y -= cameraY;
//		System.out.println(hitbox.x - cameraX + " : " + (hitbox.y - cameraY));
//		System.out.println(playerHitbox.x + " : " + playerHitbox.y);
		return playerHitbox.intersects(hitbox.x - cameraX , hitbox.y - cameraY, hitbox.width, hitbox.width);
	}
	
	public boolean isDoorwayActive() {
		return active;
	}
	
	public void setDoorwayActive(boolean active) {
		this.active = active;
	}
	
	public Point2D.Float getPosOfDoorway() {
		return new Point2D.Float(hitbox.x, hitbox.y);
	}
	
	public Tile getTileLocatedIn() {
		return tileLocatedIn;
	} 
	
}

