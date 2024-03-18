package objectz;

import main.Game;
import static utilz.Constants.ObjectConstants.*;

public class Key extends GameObject {

	public Key(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true;
		initHitbox(18*Game.SCALE, 28*Game.SCALE);
		xDrawOffset = 18;
		yDrawOffset = 10;
		
	}
	
	public void update() {
		updateAnimationTick();
	}
	
}
