package application.Models;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D.Double;

public class EnemyProjectiles {
    private Image imgEgg;
    private boolean isActive;
    private double PosX;
    private double PosY;
    private double speed;
    private static final int EGG_WIDTH = 5;
    private static final int EGG_HEIGHT = 5;

    public EnemyProjectiles(double PosX, double PosY){
        this.PosX = PosX;
        this.PosY = PosY;
        this.isActive = true;
        this.speed = 0.2;
        this.imgEgg = new ImageIcon(getClass().getResource("/asset/resources/gfx/introEgg.png")).getImage();
    }

    public Rectangle getBounds(){
        return new Rectangle((int)PosX, (int)PosY,EGG_WIDTH,EGG_HEIGHT);
    }

    public void drawEgg(Graphics g){
        if(isActive){
            int newWidth = imgEgg.getWidth(null) / 8;  // Giảm kích thước xuống 50%
            int newHeight = imgEgg.getHeight(null) / 8;
            g.drawImage(imgEgg, (int)PosX, (int)PosY,newWidth,newHeight,null);
        }
    }

    public void update(){
        PosY += speed;

        if(PosY>=1080){
            isActive = false;
        }

        if (imgEgg == null) {
            System.out.println("Lỗi: Ảnh trứng chưa được load!");
        }
    }

    public boolean isActive(){
        return isActive;
    }

    public boolean isOffScreen(int screenHeight) {
        return PosY > 1080;
    }

    public double getPosX() { return PosX; }
    public double getPosY() { return PosY; }
}