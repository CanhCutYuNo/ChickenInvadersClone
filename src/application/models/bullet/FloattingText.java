package application.models.bullet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class FloattingText {
    private float x, y;
    private String text;
    private Color color;
    private long startTime;
    private static final long LIFESPAN = 1000; 

    public FloattingText(int x, int y, String text, Color color){
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.startTime = System.currentTimeMillis();
    }

    public void update(){
        y -= 0.3f; 
    }

    public void render(Graphics g){
        g.setColor(color);
        g.setFont(new Font("Impact", Font.ITALIC, 24));
        g.drawString(text, (int)x, (int)y); 
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > LIFESPAN;
    }
}
