package application.controllers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import application.views.GamePanel;

public class MouseController implements MouseListener, MouseMotionListener {

    private final GamePanel gamePanel;
    private Timer shootTimer;
    private static final int SHOOT_DELAY = 200; // Đổi sang int vì Timer sử dụng ms
    private SoundController soundController;

    public MouseController(GamePanel gamePanel, SoundController soundController) {
        this.gamePanel = gamePanel;
        this.soundController = soundController;
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
            soundController.playSoundEffect("/asset/resources/sfx/clickXP.wav"); // Thêm âm thanh click
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
