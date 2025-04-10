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

    public void draw(Graphics g, Items item) {
        // Vẽ hình ảnh
        g.drawImage(I, item.getPosX(), item.getPosY(), 40, 60, null);

        // Vẽ hitbox (chuyển sang Graphics2D để vẽ dễ hơn)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED); // Màu hitbox

        Rectangle hitbox = item.getHitbox();
        g2d.draw(hitbox);
    }

}