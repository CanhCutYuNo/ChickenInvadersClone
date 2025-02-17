package application.Views;

import application.Controllers.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends BasePanel {
    private final Manager gameManager;

    public GamePanel(Manager gameManager) {
        super("/asset/resources/background.png");
        this.gameManager = gameManager;

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
}
