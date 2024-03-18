package objectz;

import main.Game;
import static utilz.Constants.ObjectConstants.*;

public class Potion extends GameObject {

	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true;
		// blue_potion_size
		initHitbox(POTION_WIDTH, POTION_HEIGHT); 

		xDrawOffset = (int) (3 * Game.SCALE); // 3 pix to the left
		yDrawOffset = (int) (2 * Game.SCALE); // 2 pix to the top
		
	}
	
	public void update() {
		updateAnimationTick();
	}
	
	

}