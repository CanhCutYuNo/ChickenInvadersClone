package application.controllers.bullet;

import application.models.bullet.Bullet;
import application.views.bullet.BulletView;

import java.util.ArrayList;
import java.util.List;

public class BulletController {
    private List<Bullet> bullets;
    private List<BulletView> bulletViews;

    public BulletController() {
        this.bullets = new ArrayList<>();
        this.bulletViews = new ArrayList<>();
    }
    
    public void addBullet(int x, int y, int damage, double initialSpeedY, double acceleration, Bullet.BulletType type) {
        Bullet Bullet = new Bullet(x, y, damage, initialSpeedY, acceleration, type );
        BulletView bulletView = new BulletView(Bullet);
        bullets.add(Bullet);
        bulletViews.add(bulletView);
    }

    public void update() {
        for(int i = 0; i < bullets.size(); i++) {
            Bullet Bullet = bullets.get(i);
            double speedY = Bullet.getSpeedY();
            double acceleration = Bullet.getAcceleration();
            speedY += acceleration;
            Bullet.setSpeedY(speedY);
            Bullet.setY(Bullet.getY() - (int) speedY);

            if(isOffScreen(Bullet, 1080)) {
                bullets.remove(i);
                bulletViews.remove(i);
                i--;
            }
        }
    }

    private boolean isOffScreen(Bullet Bullet, int screenHeight) {
        return Bullet.getY() < -30;
    }

    public void removeBullet(int index) {
        if(index >= 0 && index < bullets.size()) {
            bullets.remove(index);
            bulletViews.remove(index);
        }
    }

    public List<Bullet> getBullets() {
        return new ArrayList<>(bullets);
    }

    public List<BulletView> getBulletViews() {
        return new ArrayList<>(bulletViews);
    }

    public void clear() {
        bullets.clear();
        bulletViews.clear();
    }
}