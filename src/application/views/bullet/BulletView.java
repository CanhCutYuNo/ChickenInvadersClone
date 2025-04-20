package application.views.bullet;

import javax.swing.*;

import application.controllers.util.ImageCache;
import application.models.bullet.Bullet;

import java.awt.*;

public class BulletView {
    private Image image;
    private Bullet Bullet;
    private ImageCache imageCache = ImageCache.getInstance();

    public BulletView(Bullet Bullet) {
        this.Bullet = Bullet;
        loadImage();
    }

    private void loadImage() {
        try {
            image = imageCache.getResourceImage("/asset/resources/gfx/bullet.png");
        } catch (Exception e) {
            System.out.println("Error: Could not load bullet image.");
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, Bullet.getX(), Bullet.getY(), null);
        }
    }
}