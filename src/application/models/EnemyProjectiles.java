package application.models;

public class EnemyProjectiles {
    private double posX, posY;
    private boolean isExploding = false;
    private int animationFrame = 0;
    private int damage;
    
    public EnemyProjectiles(double x, double y, int damage) {
        this.posX = x;
        this.posY = y;
        this.damage = damage;
    }

    public void update() {
        if (!isExploding) {
            posY += 5; // Di chuyển xuống
        } else {
            animationFrame++;
        }

    }

    public boolean isOffScreen() {
        return posY > 1000; // Nếu rơi quá màn hình
    }

    public boolean removed() {
        return isExploding && animationFrame > 32; // Sau 32 frame thì xoá
    }

    public void explode() {
        isExploding = true;
        animationFrame = 0;
    }

    public boolean isExploding() {
        return isExploding;
    }

    public int getAnimationFrame() {
        return animationFrame;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getDamage() {
        return damage;
    }
}