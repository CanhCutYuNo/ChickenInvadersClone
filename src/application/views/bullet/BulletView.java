package application.views.bullet;

import javax.swing.*;

import application.models.bullet.Bullet;

import java.awt.*;

public class BulletView {
    private Image image;
    private Bullet Bullet;

    public BulletView(Bullet Bullet) {
        this.Bullet = Bullet;
        loadImage();
    }

    private void loadImage() {
        try {
            image = new ImageIcon(getClass().getResource("/asset/resources/gfx/bullet.png")).getImage();
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