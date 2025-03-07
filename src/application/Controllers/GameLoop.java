package application.Controllers;

import javax.swing.*;
import application.Views.GamePanel;

public class GameLoop {
    private final Timer gameTimer;
    private int frameCount = 0;
    private int fps = 0;

    public GameLoop(GamePanel gamePanel) {
        gameTimer = new Timer(16, e -> {
            gamePanel.getGameManager().update();
            gamePanel.repaint();
            frameCount++;
        });

        new Timer(1000, e -> {
            fps = frameCount;
            frameCount = 0;
        }).start();
    }

    public void start() {
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
    }

    public int getFPS() {
        return fps;
    }
}
