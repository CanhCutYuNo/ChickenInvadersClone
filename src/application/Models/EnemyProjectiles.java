package application.models;

public class EnemyProjectiles {
    private double posX, posY;
    private boolean isExploding = false;
    private int explosionFrame = 0;
    
    public EnemyProjectiles(double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    public void update() {
        if (!isExploding) {
            posY += 5; // Di chuyển xuống
        } else {
            explosionFrame++;
        }
    }

    public boolean isOffScreen() {
        return posY > 1000; // Nếu rơi quá màn hình
    }

    public boolean removed() {
        return isExploding && explosionFrame > 32; // Sau 32 frame thì xoá
    }

    public void explode() {
        isExploding = true;
        explosionFrame = 0;
    }

    public boolean isExploding() {
        return isExploding;
    }

    public int getExplosionFrame() {
        return explosionFrame;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}
