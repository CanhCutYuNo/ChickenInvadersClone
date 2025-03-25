package application.controllers;

import javax.swing.JFrame;
import javax.swing.Timer;

import application.views.GamePanel;

public class GameLoop {

    private final Timer gameTimer;
    private int frameCount = 0;
    private int fps = 0;
    private long lastTime = System.nanoTime();
    private final Manager gameManager;

    public GameLoop(GamePanel gamePanel, JFrame frame) {
        this.gameManager = gamePanel.getGameManager();

        gameTimer = new Timer(16, e -> {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
            lastTime = currentTime;

            gamePanel.update(deltaTime);
            gameManager.update(deltaTime);

            if (gameManager.getEnemies().isEmpty() && !gamePanel.isTransitionActive()) {
            //    gameManager.setLevel(gameManager.getLevel());
                gamePanel.updateLevel();
            }

            frame.repaint();
         //   System.out.println("Đã gọi repaint, deltaTime: " + deltaTime); // Debug

            frameCount++;
        });

        new Timer(1000, e -> {
            fps = frameCount;
            frameCount = 0;
        }).start();
    }

    public void start() {
        lastTime = System.nanoTime();
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
    }

    public int getFPS() {
        return fps;
    }
}