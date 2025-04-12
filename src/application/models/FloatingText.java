package application.models;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class FloatingText {
    private float x, y; // Dùng float để di chuyển mượt
    private String text;
    private Color color;
    private long startTime;
    private static final long LIFESPAN = 1000;  // Thời gian tồn tại (ms)

    public FloatingText(int x, int y, String text, Color color){
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.startTime = System.currentTimeMillis();  // Lưu thời gian bắt đầu
    }

    public void update(){
        y -= 0.05f;  // Di chuyển lên từ từ
    }

    public void render(Graphics g){
        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString(text, (int)x, (int)y); //
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > LIFESPAN;
    }
}
