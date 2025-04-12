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
    private static final int SHOOT_DELAY = 200;

    public MouseController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        shootTimer = new Timer(SHOOT_DELAY, e -> {
            if (!gamePanel.isPaused()) {
                gamePanel.getGameManager().shoot();
            }
        });
        shootTimer.setRepeats(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gamePanel.isPaused()) {
            gamePanel.getGameManager().movePlayer(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!gamePanel.isPaused()) {
            gamePanel.getGameManager().movePlayer(e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && !gamePanel.isPaused()) { 
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