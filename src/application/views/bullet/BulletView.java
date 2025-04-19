package application.views.bullet;

import javax.swing.*;
import application.models.bullet.Bullet;
import java.awt.*;

public class BulletView {
    private Image imageBullet;          // Hình ảnh của bullet bình thường
    private Image imageDoubleBullet;    // Hình ảnh của bullet đôi
    private Bullet bullet;              // Đối tượng Bullet

    public BulletView(Bullet bullet) {
        this.bullet = bullet;          // Gán đối tượng bullet
        loadImage();                   // Tải các hình ảnh khi tạo BulletView
    }

    // Tải các hình ảnh của bullet
    private void loadImage() {
        try {
            imageBullet = new ImageIcon(getClass().getResource("/asset/resources/gfx/bullet.png")).getImage();
            imageDoubleBullet = new ImageIcon(getClass().getResource("/asset/resources/gfx/bulletion.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error: Could not load bullet image.");
            e.printStackTrace();
        }
    }

    // Render bullet lên màn hình
    public void render(Graphics g) {
        // Kiểm tra loại bullet và vẽ hình ảnh tương ứng
        if (bullet.getType() == Bullet.BulletType.NORMAL) {
            g.drawImage(imageBullet, bullet.getX(), bullet.getY(), null);
        } else if (bullet.getType() == Bullet.BulletType.DOUBLE) {
            g.drawImage(imageDoubleBullet, bullet.getX(), bullet.getY(), null);
        }
    }
}
