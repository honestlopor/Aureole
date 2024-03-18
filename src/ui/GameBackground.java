package ui;

public class GameBackground {
	
	public boolean doAnimation, active = true;
	public int aniTick, aniIndex;
	
	public void updateAnimationTick(int speed) {
		aniTick++;
		if (aniTick >= speed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= 7) {
				aniIndex = 0;
				
			}
		}
	}
	
	public void reset() {
		aniIndex = 0;
		aniTick = 0;
		doAnimation = true;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}
}
