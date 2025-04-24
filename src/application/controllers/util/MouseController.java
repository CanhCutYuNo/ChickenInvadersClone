package application.controllers.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import application.views.panels.GamePanel;

public class MouseController implements MouseListener, MouseMotionListener {

    private final GamePanel gamePanel;
    private Timer shootTimer;
    private static final int SHOOT_DELAY = 200;

    public MouseController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        shootTimer = new Timer(SHOOT_DELAY, e -> {
            if(!gamePanel.getTransitionManager().isPaused()) {
                gamePanel.getGameManager().getPlayerActionHandler().shoot();
            }
        });
        shootTimer.setRepeats(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    	if(!gamePanel.getTransitionManager().isPaused()) {
            gamePanel.getGameManager().getPlayerActionHandler().movePlayer(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	if(!gamePanel.getTransitionManager().isPaused()) {
            gamePanel.getGameManager().getPlayerActionHandler().movePlayer(e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e) && !gamePanel.getTransitionManager().isPaused()) { 
            if(!shootTimer.isRunning() && !gamePanel.getTransitionManager().isGameOver()) {
                gamePanel.getGameManager().getPlayerActionHandler().shoot();
                shootTimer.start();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
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