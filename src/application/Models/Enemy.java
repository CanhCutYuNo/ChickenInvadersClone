package application.Models;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy {
	private Image model;
	private int hp;
	private int PosX;
	private int PosY;
	private int speed;
//	private int level = 1;
	private boolean movingRight;
	private int centerX, centerY;
	private int radius;
	private double theta; // góc quay chuyển động tròn

	private static final int MODEL_WIDTH = 64;
    private static final int MODEL_HEIGHT = 64;
    private static final int MAP_WIDTH = 1560;
    private static final int MAP_HEIGH = 1080;
	
	public Enemy(int hp, int PosX, int PosY, int level) {
		this.hp = hp;
		Random random = new Random();
        this.PosX = Math.max(0, Math.min(MAP_WIDTH-MODEL_WIDTH, PosX));
        this.PosY = Math.max(0, Math.min(MAP_HEIGH-MODEL_HEIGHT, PosY));
		this.speed = 2;
		if(level == 1){
			this.movingRight = true;
		}
		else if(level == 2){
			this.theta = Math.random() * 2 * Math.PI;
			this.centerX = PosX;
			this.centerY = PosY;
			this.radius = 100;
		}

//		this.level = level;
		try {
            Image originalModel = new ImageIcon(getClass().getResource("/asset/resources/enemy.png")).getImage();
            this.model = originalModel.getScaledInstance(MODEL_WIDTH, MODEL_HEIGHT, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.out.println("Error: Could not load or scale enemy model image.");
            e.printStackTrace();
        }
	}
	
	public void render(Graphics g) {
		if(model != null) {
            g.drawImage(model, PosX, PosY, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT); 
        }
	}
	
	public void takeDamage(int damage) {
		hp -= damage;
	}
	
	public boolean isDead() {
		return hp <= 0;
	}
	
	public Rectangle getBounds() {
        return new Rectangle(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT);
    }
	
	public int getHp() {
		return hp;
	}
	
	public int getPosX() {
        return PosX;
    }
	
	public int getPosY() {
        return PosY;
    }

	public void update(int level) {
		if(level == 1){
			if (movingRight) {
				PosX += speed;
				if (PosX >= MAP_WIDTH - MODEL_WIDTH) {
					movingRight = false;
				}
			}
			else {
				PosX -= speed;
				if (PosX <= 0) {
					movingRight = true;
				}
			}

		}
		else if(level == 2){
			theta += 0.05;
			PosX = centerX + (int) (radius * Math.cos(theta));
			PosY = centerY + (int) (radius * Math.sin(theta));
		}

	}
}
