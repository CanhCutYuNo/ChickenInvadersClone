package application.Models;

import javax.swing.*;
import java.awt.*;

public class Player {
    private Image spriteSheet;
    private int hp;
    private int damage;
    private double shootSpeed;
    private int PosX;
    private int PosY;
    private static final int MODEL_WIDTH = 64;
    private static final int MODEL_HEIGHT = 64;

    private int[][] spriteData = {
        {1, 1135, 104, 114}, {1, 1019, 104, 114}, {1, 903, 104, 114},
        {1, 787, 105, 114}, {1, 672, 106, 113}, {1, 556, 107, 113},
        {1, 440, 109, 113}, {1, 325, 110, 112}, {1, 212, 111, 111},
        {1, 98, 112, 111}, {115, 1139, 114, 110}, {115, 1028, 115, 109},
        {115, 917, 116, 108}, {115, 808, 117, 107}, {115, 699, 117, 106},
        {115, 591, 117, 106}, {115, 375, 117, 106}, {115, 267, 117, 106},
        {115, 158, 117, 107}, {115, 47, 116, 108}, {235, 1140, 114, 109},
        {235, 1027, 114, 110}, {235, 915, 112, 110}, {235, 802, 111, 111},
        {235, 687, 110, 112}, {235, 573, 109, 112}, {235, 458, 107, 113},
        {235, 342, 106, 113}, {235, 225, 105, 114}, {235, 109, 104, 114},
        {351, 1135, 104, 114}, {351, 1019, 104, 114}
    };

    private int curFrame = 16; 
    private int initialPosX; 
    private boolean moving = false;

    // Constructor
    public Player(int hp, int damage, double shootSpeed, int PosX, int PosY) {
        this.hp = hp;
        this.damage = damage;
        this.shootSpeed = shootSpeed;
        this.PosX = PosX;
        this.PosY = PosY;
        this.initialPosX = PosX;

        try {
            spriteSheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/spaceship.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error: Could not load player sprite sheet.");
            e.printStackTrace();
        }
    }

    // Getter & Setter
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public boolean isDead() { return hp <= 0; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    public double getShootSpeed() { return shootSpeed; }
    public void setShootSpeed(double shootSpeed) { this.shootSpeed = shootSpeed; }

    public int getPosX() { return PosX; }
    public void setPosX(int PosX) { this.PosX = PosX; }

    public int getPosY() { return PosY; }
    public void setPosY(int PosY) { this.PosY = PosY; }

    
    public void updateDirection(int newX) {
        if(newX < initialPosX) {
            moving = true;
            curFrame = Math.max(0, curFrame - 1); // Nghiêng trái
        } else if(newX > initialPosX) {
            moving = true;
            curFrame = Math.min(31, curFrame + 1); // Nghiêng phải
        } else {
            moving = false;
        }
        initialPosX = newX;
    }

    public void update() {
        if(!moving && curFrame != 16) {
            if(curFrame < 16) {
                curFrame++; // Nếu đang nghiêng trái, tăng dần về 16
            } else {
                curFrame--; // Nếu đang nghiêng phải, giảm dần về 16
            }
        }
    }

    public void render(Graphics g) {
        if(spriteSheet != null) {
            int[] data = spriteData[curFrame];
            int sx = data[0], sy = data[1], sw = data[2], sh = data[3];

            g.drawImage(spriteSheet, PosX, PosY, PosX + 80, PosY + 80,
                        sx, sy, sx + sw, sy + sh, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT);
        }
    }
}
