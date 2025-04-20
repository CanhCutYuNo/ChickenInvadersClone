package application.models.bullet;

public class Bullet {
    private int x, y;
    private int damage;
    private double speedY;
    private double acceleration;
    public enum BulletType{
        NORMAL, DOUBLE
    }
    private int originDame;

    private BulletType type;

    public Bullet(int x, int y, int damage, double initialSpeedY, double acceleration, BulletType type) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.speedY = initialSpeedY;
        this.acceleration = acceleration;
        this.type = type;
        this.originDame = damage;
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

    public void setDamage(int damage){this.damage = damage;}

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

    public void transformToStrongerBullet() {
        this.type = BulletType.DOUBLE;
        this.damage = damage + 15;
    }

    public void resetToNormal(){
        this.type = BulletType.NORMAL;
    }

    public BulletType getType(){return type;}
}