package application.views;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;

import application.models.EnemyProjectiles;

import java.util.ArrayList;
import java.util.List;

public class EnemyProjectilesView {
    private Image ProjectilesImage;
    private Image eggSheet;
    
    private List<int[]> EggSprites = new ArrayList<>();
    
    private static final int[][] EggBrokenSprite = {
            {1, 1, 31, 20}, {35, 1, 32, 21}, {69, 1, 34, 22},
            {105, 1, 35, 22}, {143, 1, 37, 23}, 	{183, 1, 39, 23},
            {225, 1, 40, 24}, {267, 1, 42, 24}, {311, 1, 43, 25},
            {357, 1, 44, 25}, {403, 1, 46, 27}, {451, 1, 47, 27}, 
            {1, 31, 49, 28}, {53, 31, 49, 28}, {105, 31, 51, 30}, {159, 31, 52, 30},
            {213, 31, 53, 31}, {269, 31, 54, 31}, {325, 31, 56, 33},
            {383, 31, 56, 33}, {441, 31, 58, 33}, {1, 67, 58, 34},
            {61, 67, 59, 34}, {123, 67, 60, 36}, {185, 67, 61, 36},
            {249, 67, 61, 36},  {313, 67, 63, 37}, {379, 67, 63, 37}, {445, 67, 64, 37},
            {1, 107, 65, 37}, {69, 107, 66, 39},  {137, 107, 66, 39},
            {205, 107, 67, 39}, {275, 107, 68, 39}, {345, 107, 68, 40},
            {415, 107, 68, 40}, {1, 149, 69, 40}, {73, 149, 70, 40},
            {145, 149, 70, 40}, {217, 149, 70, 41}, {289, 149, 71, 42}, 
            {363, 149, 71, 42}, {437, 149, 71, 42}, {1, 193, 71, 42}, 
            {75, 193, 72, 42}, {149, 193, 72, 42}, {223, 193, 72, 42},
            {297, 193, 72, 42}, {371, 193, 72, 42}
        };

    public EnemyProjectilesView(String path) {
    	ProjectilesImage = new ImageIcon(getClass().getResource(path)).getImage();
    	
    	try {
            eggSheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/eggbreak~1.png")).getImage();
        } catch(Exception e) {
            System.out.println("Error: Could not load player sprite sheet or exhaust image.");
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, EnemyProjectiles projectile) {
        g.drawImage(ProjectilesImage, (int) projectile.getPosX(), (int) projectile.getPosY(), 40, 80, null);    
    }
    
    public void drawEggBroken(Graphics g, EnemyProjectiles projectile, int eFrame) {
    	for(int[] frame : EggBrokenSprite) {
    		EggSprites.add(frame);
    	}
    	
    	if(eggSheet != null) {
    		int[] eggFrame = EggSprites.get(eFrame);
    		int ex = eggFrame[0], ey = eggFrame[1], ew = eggFrame[2], eh = eggFrame[3];;
    		
    		int[][] offsets = {
    			    {-15, -3}, {-16, -3}, {-17, -4}, {-18, -4}, {-19, -5}, {-21, -5}, {-22, -6}, {-23, -6},
    			    {-24, -6}, {-25, -6}, {-26, -8}, {-27, -8}, {-28, -9}, {-28, -9}, {-30, -10}, {-31, -10},
    			    {-31, -11}, {-32, -11}, {-34, -12}, {-34, -12}, {-35, -12}, {-35, -13}, {-36, -13}, {-37, -15},
    			    {-37, -15}, {-37, -15}, {-39, -15}, {-39, -15}, {-40, -15}, {-40, -15}, {-41, -17}, {-41, -17}, {-42, -17}
    			};

    		
    		int offsetX = offsets[eFrame][0];
            int offsetY = offsets[eFrame][1];

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            double scale = 2.0;

            g2d.drawImage(eggSheet, 
                        (int)(projectile.getPosX() + offsetX + 15), (int)(projectile.getPosY() + offsetY), 
                        (int)(projectile.getPosX() + offsetX + 15 + (ew * scale) / 2), (int)(projectile.getPosY() + offsetY + (eh * scale) / 2), 
                        ex, ey, ex + ew, ey + eh, 
                        null);
            //System.out.println("x = " +  projectile.getPosX() + ", y = " + projectile.getPosY() + ", f = " + eFrame);
    	}
    }
}
