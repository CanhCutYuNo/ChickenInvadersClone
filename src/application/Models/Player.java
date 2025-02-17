package application.Models;

import javax.swing.*;
import java.awt.*;

public class Player {
    private Image model;
    private int hp;
    private int damage;
    private double shootSpeed;
    private int PosX;
    private int PosY;
    private static final int MODEL_WIDTH = 64;
    private static final int MODEL_HEIGHT = 64;

    // Constructor
    public Player(int hp, int damage, double shootSpeed, int PosX, int PosY) {
        this.hp = hp;
        this.damage = damage;
        this.shootSpeed = shootSpeed;
        this.PosX = PosX;
        this.PosY = PosY;

        try {
            Image originalModel = new ImageIcon(getClass().getResource("/asset/resources/player.png")).getImage();
            this.model = originalModel.getScaledInstance(MODEL_WIDTH, MODEL_HEIGHT, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.out.println("Error: Could not load or scale player model image.");
            e.printStackTrace();
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp() {
        this.hp = 1;
    }
    
    public boolean death() {
    	return hp <= 0;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getShootSpeed() {
        return shootSpeed;
    }

    public void setShootSpeed(double shootSpeed) {
        this.shootSpeed = shootSpeed;
    }

    public int getPosX() {
        return PosX;
    }

    public void setPosX(int PosX) {
        this.PosX = PosX;
    }

    public int getPosY() {
        return PosY;
    }

    public void setPosY(int PosY) {
        this.PosY = PosY;
    }

    // Vẽ player trên màn hình
    public void render(Graphics g) {
        if (model != null) {
            g.drawImage(model, PosX, PosY, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT); // Hình thay thế với kích thước 64x64
        }
    }

    // Hàm cập nhật trạng thái của player (di chuyển, tấn công,...)
    public void update() {
        // Logic để cập nhật trạng thái player (nếu cần)
    }
}
