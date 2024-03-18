package ui;

import static utilz.Constants.UI.SelectButton.SELECT_WIDTH;
import static utilz.Constants.UI.SelectButton.SELECT_DEFAULT_WIDTH;
import static utilz.Constants.UI.SelectButton.SELECT_HEIGHT;
import static utilz.Constants.UI.SelectButton.SELECT_DEFAULT_HEIGHT;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import utilz.LoadSave;

public class SelectButton {

	public int xPos, yPos;
	public int rowIndex;
	public int index;
	private int xOffsetCenter = SELECT_WIDTH / 2;
	private int yOffsetCenter = SELECT_HEIGHT / 2;
	private Gamestate state;
	private BufferedImage[] imgs;
	private Boolean mouseOver = false, mousePressed = false;
	private Rectangle bounds;
	private String character;
	
	private int buttonAni = 0;
	
	public SelectButton(int xPos, int yPos, int rowIndex, Gamestate state, String character) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		this.character = character;
		loadImgs();
		initBound();
	}

	private void initBound() {
		bounds = new Rectangle(xPos, yPos - yOffsetCenter, SELECT_WIDTH, SELECT_HEIGHT);
		
	}

	private void loadImgs() {
		imgs = new BufferedImage[7];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SELECT_BUTTONS);
		for (int i = 0; i < imgs.length; i++) 
			imgs[i] = temp.getSubimage(i * SELECT_DEFAULT_WIDTH, rowIndex * SELECT_DEFAULT_HEIGHT, SELECT_DEFAULT_WIDTH, SELECT_DEFAULT_HEIGHT);
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos, yPos - yOffsetCenter, SELECT_WIDTH, SELECT_HEIGHT, null);
	}
	
	public void update() {
		
		if (mousePressed)
			index = 6;
		else if (mouseOver) {
			if (index == 6)
				index = 1;
			buttonAni++;
			if (buttonAni == 25) {
				index++;
				buttonAni = 0;
			}
			
		}
		
		else
			index = 0;
		
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void applyGamestate() {
		Gamestate.state = state;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	
	public Gamestate getState() {
		return state;
	}
	
	public String getCharacter() {
		return character;
	}
	
}
