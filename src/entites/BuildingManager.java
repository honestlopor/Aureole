package entites;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import utilz.LoadSave;

public class BuildingManager {

	private ArrayList<Building> buildingArrayList;
    private float cameraX, cameraY;
    
//    public BuildingManager(Point pos) {
//        this.pos = pos;
//        loadHouseImg();
//    }


    public void setCameraValues(float cameraX, float cameraY) {
        this.cameraX = cameraX;
        this.cameraY = cameraY;
    }

    public void render(Graphics g) {
    	for (Building b : buildingArrayList)
    		g.drawImage(b.getBuildingType().getHouseImg(), (int)(b.getPos().x-cameraX), (int)(b.getPos().y-cameraY), (int)(b.getWidth() * Game.SCALE) , (int)(b.getHeight() * Game.SCALE) ,null);
    }
}