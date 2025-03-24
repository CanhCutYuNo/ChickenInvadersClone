package application.views;
import application.models.Items;

import javax.swing.*;
import java.awt.*;

public class ItemsView {
    private Image I;

    public ItemsView(String path){
        I = new ImageIcon(getClass().getResource(path)).getImage();

        try{
            I = new ImageIcon(getClass().getResource("/asset/resources/gfx/food_thighs.png")).getImage();

        } catch (Exception e) {
            System.out.println("Khong load duoc anh");
        }
    }

    public void draw(Graphics g, Items item){
        g.drawImage(I, (int) item.getPosX(), item.getPosY(), 40, 60, null);
    }
}
