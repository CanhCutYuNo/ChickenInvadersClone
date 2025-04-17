package application.models;

public class Bullet {
    private int x, y;
    private int damage;
    private double speedY;
    private double acceleration;

    public Bullet(int x, int y, int damage, double initialSpeedY, double acceleration) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.speedY = initialSpeedY;
        this.acceleration = acceleration;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDamage() {
        return damage;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }
}