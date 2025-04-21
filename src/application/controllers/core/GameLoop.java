package application.controllers.core;

import javax.swing.JFrame;
import javax.swing.Timer;

import application.views.panels.BackgroundPanel;
import application.views.panels.GamePanel;

public class GameLoop {

    private final Timer updateTimer;
    private final Timer renderTimer;
    private boolean paused;
    private int frameCount = 0;
    private int tickCount = 0;
    private int fps = 0;
    private int tps = 0;
    private long lastUpdateTime = System.nanoTime();
    private final Manager gameManager;
    private final GamePanel gamePanel;
    
    public GameLoop(GamePanel gamePanel, BackgroundPanel backgroundPanel, JFrame frame) {
        this.gamePanel = gamePanel;
        this.gameManager = gamePanel.getGameManager();
        this.paused = false;

        updateTimer = new Timer(1000 / 60, e -> { 
            if(!paused) {
                long currentTime = System.nanoTime();
                double deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;
                lastUpdateTime = currentTime;

                gamePanel.update(deltaTime);
                gameManager.update(deltaTime);
                backgroundPanel.update(deltaTime);

                tickCount++;
            }
        });

        renderTimer = new Timer(1000 / 144, e -> { 
            frame.repaint();
            frameCount++;
        });

        new Timer(1000, e -> {
            fps = frameCount;
            tps = tickCount;
            frameCount = 0;
            tickCount = 0;
        }).start();
    }

    public void start() {
        lastUpdateTime = System.nanoTime();
        paused = false;
        gamePanel.setPaused(false);
        updateTimer.start();
        renderTimer.start();
    }

    public void stop() {
        updateTimer.stop();
        renderTimer.stop();
        paused = false;
        gamePanel.setPaused(false);
    }

    public void pause() {
        if(!paused) {
            paused = true;
            updateTimer.stop();
            renderTimer.stop();
            gamePanel.setPaused(true);
        }
    }

    public void resume() {
        if(paused) {
            paused = false;
            lastUpdateTime = System.nanoTime();
            updateTimer.start();
            renderTimer.start();
            gamePanel.setPaused(false);
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public int getFPS() {
        return fps;
    }

    public int getTPS() {
        return tps;
    }
    
    public Manager getManager() {
    	return gameManager;
    }
}