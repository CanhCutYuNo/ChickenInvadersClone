package application.Views;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected Image backgroundImage;

    public BasePanel(String imagePath) {
        setLayout(new BorderLayout());
        loadBackgroundImage(imagePath);
    }

    private void loadBackgroundImage(String path) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(path)).getImage();
        } catch (Exception e) {
            System.out.println("Error loading background image: " + path);
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
