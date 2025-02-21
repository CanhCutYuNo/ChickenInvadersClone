package application.Views;

import application.Controllers.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GamePanel extends BasePanel {
    private final Manager gameManager;
    //private final Controller controller;

    public GamePanel(Manager gameManager) {
        super("/asset/resources/background.png");
        this.gameManager = gameManager;
        
        hideCursor();
        gameManager.spawnEnemies();
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                gameManager.movePlayer(e.getX(), e.getY());
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                gameManager.shoot();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameManager.render(g);
    }
    
    private void hideCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "InvisibleCursor");
        setCursor(invisibleCursor);
    }

}
