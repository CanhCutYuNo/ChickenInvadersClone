package application.controllers;

import javax.swing.JFrame;
import javax.swing.Timer;
import application.views.GamePanel;

public class GameLoop {

    private final Timer updateTimer;
    private final Timer renderTimer;
    
    private int frameCount = 0;
    private int tickCount = 0;
    private int fps = 0;
    private int tps = 0;

    private long lastUpdateTime = System.nanoTime();
    private final Manager gameManager;

    public GameLoop(GamePanel gamePanel, JFrame frame) {
        this.gameManager = gamePanel.getGameManager();

        // TPS Timer
        updateTimer = new Timer(1000 / 60, e -> { 
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;
            lastUpdateTime = currentTime;

            gamePanel.update(deltaTime);
            gameManager.update(deltaTime);

            if (gameManager.getEnemies().isEmpty() && !gamePanel.isTransitionActive()) {
                gamePanel.updateLevel();
            }

            tickCount++;
        });

        // FPS 
        renderTimer = new Timer(1000 / 144, e -> { 
            frame.repaint();
            frameCount++;
        });

        // TPS
        new Timer(16, e -> {
            fps = frameCount;
            tps = tickCount;
            frameCount = 0;
            tickCount = 0;
        }).start();
    }

    public void start() {
        lastUpdateTime = System.nanoTime();
        updateTimer.start();
        renderTimer.start();
    }

    public void stop() {
        updateTimer.stop();
        renderTimer.stop();
    }

    public int getFPS() {
        return fps;
    }

    public int getTPS() {
        return tps;
    }
}
