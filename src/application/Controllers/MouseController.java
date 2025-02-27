package application.Controllers;

import java.awt.event.*;

import javax.swing.SwingUtilities;

import application.Views.GamePanel;

public class MouseController implements MouseListener, MouseMotionListener {
    private final GamePanel gamePanel;
    private long lastShotTime = 0;
    private static final long SHOOT_DELAY = 200;
    private boolean isShooting = false;

    public MouseController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gamePanel.getGameManager().movePlayer(e.getX(), e.getY());

        if (isShooting) {
            shootIfReady();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        gamePanel.getGameManager().movePlayer(e.getX(), e.getY());

        if(isShooting) {
            shootIfReady();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            isShooting = true;
            shootIfReady();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            isShooting = false;
        }
    }

    private void shootIfReady() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastShotTime >= SHOOT_DELAY) {
            gamePanel.getGameManager().shoot();
            lastShotTime = currentTime;
        }
    }

    // Không sử dụng các phương thức này nhưng cần override
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
