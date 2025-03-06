package application.Models;

import javax.swing.*;
import java.awt.*;

public class Egg {
    private Image imgEgg;
    private boolean isActive;
    private int PosX;
    private int PosY;
    private int speed;
    private static final int EGG_WIDTH = 5;
    private static final int EGG_HEIGHT = 5;

    public Egg(int PosX, int PosY){
        this.PosX = PosX;
        this.PosY = PosY;
        this.isActive = true;
        this.speed = 5;
        this.imgEgg = new ImageIcon(getClass().getResource("/asset/resources/gfx/introEgg.png")).getImage();
    }

    public Rectangle getBounds(){
        return new Rectangle(PosX,PosY,EGG_WIDTH,EGG_HEIGHT);
    }

    public void drawEgg(Graphics g){
        if(isActive){
            int newWidth = imgEgg.getWidth(null) / 8;  // Giảm kích thước xuống 50%
            int newHeight = imgEgg.getHeight(null) / 8;
            g.drawImage(imgEgg,PosX,PosY,newWidth,newHeight,null);
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




}