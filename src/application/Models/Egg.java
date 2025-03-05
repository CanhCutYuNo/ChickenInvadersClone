package application.Models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

public class Egg {
    private int x, y;
    private int speed;
    private boolean isActive;
    private BufferedImage eggImage;

    public Egg(int x, int y){
        this.x = x;
        this.y = y;
        this.speed = 3;
        this.isActive = true;

        try {
            eggImage = ImageIO.read(new File("D:/TaiLieu/Nam2/HocKy2/CongngheJava/BTLJava/src/asset/resources/gfx/introEgg.png")); // Đọc ảnh
        } catch (IOException e) {
            System.out.println("Error!!!");
            e.printStackTrace();
        }
    }

    //Roi
    public void update(){
        y+=speed;

        if(y>800){
            isActive = false;
        }
    }

    public void draw(Graphics g){
        if(eggImage != null){
            g.drawImage(eggImage,x,y,25,30,null);
        }
    }

    public boolean isActive(){
        return isActive;
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,25,30);
    }


}
