package application.controllers;

import java.awt.event.*;
import javax.swing.*;

import application.views.GamePanel;

public class MouseController implements MouseListener, MouseMotionListener {

    private final GamePanel gamePanel;
    private Timer shootTimer;
    private static final int SHOOT_DELAY = 200; // Đổi sang int vì Timer sử dụng ms

    public MouseController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        // Tạo Timer, nhưng không chạy ngay
        shootTimer = new Timer(SHOOT_DELAY, e -> gamePanel.getGameManager().shoot());
        shootTimer.setRepeats(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gamePanel.getGameManager().movePlayer(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        gamePanel.getGameManager().movePlayer(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (!shootTimer.isRunning()) {
                gamePanel.getGameManager().shoot();
                shootTimer.start();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            shootTimer.stop();
        }
    }

    // Không sử dụng các phương thức này nhưng cần override
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
