package application.models.enemy;

import java.awt.Rectangle;

public class Items {
    private int posY, posX;
    private int damage;

    public Items(int posX, int posY, int damage) {
        this.posY = posY;
        this.posX = posX;
        this.damage = damage;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void update() {
        posY += 5;
    }

    public boolean isOffScreen() {
        return posY > 1000;
    }

    // ✅ Hàm hitbox mới
    public Rectangle getHitbox() {
        return new Rectangle(posX, posY, 40, 60); // hoặc thay đổi kích thước tùy game
    }
}
