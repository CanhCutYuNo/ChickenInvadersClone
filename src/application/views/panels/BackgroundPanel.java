package application.views.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import application.controllers.util.ImageCache;
import application.controllers.util.ScreenUtil;

public class BackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final ScreenUtil screenUtil;
    private final int width = 1920;
    private final int height = 1080;
    private final int scrollSpeed = 80; 

    private Image backgroundImage;
    private double y;
    private ImageCache imageCache = ImageCache.getInstance();

    public BackgroundPanel() {
        screenUtil = ScreenUtil.getInstance();
        backgroundImage = imageCache.getResourceImage("/asset/resources/backgrounds/starfield5-ci5.png");
        y = 0;
    }

    public void update(double deltaTime) {
        y += scrollSpeed * deltaTime;
        if(y >= height) {
            y -= height;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.scale(screenUtil.getWidth() / 1920f / screenUtil.getScaleX(),
                screenUtil.getHeight() / 1080f / screenUtil.getScaleY());

        g.drawImage(backgroundImage, 0, (int) y, width, height, null);
        g.drawImage(backgroundImage, 0, (int) y - height, width, height, null);

        g2D.dispose();
    }
}
