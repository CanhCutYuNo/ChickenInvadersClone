package application.views.bullet;

import java.awt.Graphics;
import java.awt.Image;

import application.controllers.util.ImageCache;
import application.models.bullet.Bullet;

public class BulletView {
    private Image imageBullet;         
    private Image imageDoubleBullet;   
    private Bullet bullet;             
    private ImageCache imageCache = ImageCache.getInstance();

    public BulletView(Bullet bullet) {
        this.bullet = bullet;         
        loadImage();                 
    }

    private void loadImage() {
        try {
            imageBullet = imageCache.getResourceImage("/asset/resources/gfx/bullet.png");
            imageDoubleBullet = imageCache.getResourceImage("/asset/resources/gfx/bulletion.png");
        } catch (Exception e) {
            System.out.println("Error: Could not load bullet image.");
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        if(bullet.getType() == Bullet.BulletType.NORMAL) {
            g.drawImage(imageBullet, bullet.getX() - 17, bullet.getY(), null);
        } else if(bullet.getType() == Bullet.BulletType.DOUBLE) {
            g.drawImage(imageDoubleBullet, bullet.getX() - 17, bullet.getY(), null);
        }
    }
}
