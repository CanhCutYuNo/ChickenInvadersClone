package application.Models;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy {
	private Image model;
	private int hp;
	private int PosX;
	private int PosY;
	private static final int MODEL_WIDTH = 64;
    private static final int MODEL_HEIGHT = 64;
    private static final int MAP_WIDTH = 1900;
    private static final int MAP_HEIGH = 1080;
	
	public Enemy(int hp, int PosX, int PosY) {
		this.hp = hp;
		 Random random = new Random();
        this.PosX = random.nextInt(MAP_WIDTH - MODEL_WIDTH);
        this.PosY = random.nextInt(MAP_HEIGH / 2 - MODEL_HEIGHT); 
		
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
}
