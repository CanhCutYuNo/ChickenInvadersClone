package application.Models;

import javax.swing.*;
import java.awt.*;

public class EnemyProjectiles {
    private Image imgFeather;
    private Image imgObject;
    private boolean isActive;
    private int PosX;
    private int PosY;
    private int speed;
    private static final int OB_WIDTH = 5;
    private static final int OB_HEIGHT = 5;

//    private int[][] spriteData = {
//        {1, 1, 31, 20}, {35, 1, 32, 21}, {1, 903, 104, 114},
//        {1, 787, 105, 114}, {1, 672, 106, 113}, {1, 556, 107, 113},
//        {1, 440, 109, 113}, {1, 325, 110, 112}, {1, 212, 111, 111},
//        {1, 98, 112, 111}, {115, 1139, 114, 110}, {115, 1028, 115, 109},
//        {115, 917, 116, 108}, {115, 808, 117, 107}, {115, 699, 117, 106},
//        {115, 591, 117, 106}, {115, 375, 117, 106}, {115, 267, 117, 106},
//        {115, 158, 117, 107}, {115, 47, 116, 108}, {235, 1140, 114, 109},
//        {235, 1027, 114, 110}, {235, 915, 112, 110}, {235, 802, 111, 111},
//        {235, 687, 110, 112}, {235, 573, 109, 112}, {235, 458, 107, 113},
//        {235, 342, 106, 113}, {235, 225, 105, 114}, {235, 109, 104, 114},
//        {351, 1135, 104, 114}, {351, 1019, 104, 114}
//    };

    public EnemyProjectiles(int PosX, int PosY) {
        this.PosX = PosX;
        this.PosY = PosY;
        this.isActive = true;
        this.speed = 2;
        this.imgFeather = new ImageIcon(getClass().getResource("/asset/resources/gfx/feather-large.png")).getImage();
//        this.imgObject = new ImageIcon(getClass().getResource("/asset/resources/gfx/introEgg.png")).getImage();
    }

    public Rectangle getBounds() {
        return new Rectangle(PosX,PosY, OB_WIDTH, OB_HEIGHT);
    }

    public void drawF(Graphics g) {
        if (isActive) {
            int newWidth = imgFeather.getWidth(null) / 10;  // Giảm kích thước xuống
            int newHeight = imgFeather.getHeight(null) / 10;
            g.drawImage(imgFeather,PosX,PosY, newWidth, newHeight, null);
        }
    }

    public void update() {
        PosY += speed;

        if (PosY >= 1080) {
            isActive = false;
        }

        if (imgFeather == null) {
            System.out.println("Lỗi: Ảnh trứng chưa được load!");
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isOffScreen() {
        return PosY > 1080;
    }

    public double getPosX() {
        return PosX;
    }

    public double getPosY() {
        return PosY;
    }
}
