package application.Models;

import java.awt.*;

public class PlayerModel {
    private int hp;
    private int damage;
    private double shootSpeed;
    private int PosX;
    private int PosY;
 
    // Constructor
    public PlayerModel(int hp, int damage, double shootSpeed, int PosX, int PosY) {
        this.hp = hp;
        this.damage = damage;
        this.shootSpeed = shootSpeed;
        this.PosX = PosX;
        this.PosY = PosY;
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
    
    public Point getPosition() { return new Point(PosX, PosY); }

}
