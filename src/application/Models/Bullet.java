package application.Models;

import javax.swing.*;
import java.awt.*;

public class Bullet {
    private int x, y;
    private int dame;
    private boolean isActive = true;
    private double speedY;
    private double acceleration;
    private Image image;

    public Bullet(int x, int y, int dame, double initialSpeedY, double acceleration) {
        this.x = x;
        this.y = y;
        this.dame = dame;
        this.speedY = initialSpeedY;
        this.acceleration = acceleration;

        loadImage();
    }

    private void loadImage() {
        try {
            image = new ImageIcon(getClass().getResource("/asset/resources/gfx/bullet.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error: Could not load bullet image.");
            e.printStackTrace();
        }
    }

    public void update() {
        speedY += acceleration;
        y -= (int)speedY;
    }

    public void render(Graphics g) {
        if(image != null) {
            g.drawImage(image, x, y, null);
        }
    }

    public boolean isOffScreen(int screenHeight) {
        return y < -30;
    }
    
    public void deactivate() {isActive = false;} 

    public int getX() {
        return x;
    }
    
    public int getDamage() {
    	return dame;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
