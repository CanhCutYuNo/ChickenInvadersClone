package application.controllers.core;

import javax.swing.JFrame;
import javax.swing.Timer;

import application.views.panels.BackgroundPanel;

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
    private final TransitionManager transitionManager;
    
    public GameLoop(TransitionManager transitionManager, BackgroundPanel backgroundPanel, JFrame frame) {
        this.transitionManager = transitionManager;
        this.gameManager = transitionManager.getGameManager();
        this.paused = false;

        updateTimer = new Timer(1000 / 240, e -> { 
            if(!paused) {
                long currentTime = System.nanoTime();
                double deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;
                lastUpdateTime = currentTime;

                transitionManager.update(deltaTime);
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
        transitionManager.setPaused(false);
        updateTimer.start();
        renderTimer.start();
    }

    public void stop() {
        updateTimer.stop();
        renderTimer.stop();
        paused = true;
        transitionManager.setPaused(true);
    }

    public void pause() {
        if(!paused) {
            paused = true;
            updateTimer.stop();
            renderTimer.stop();
            transitionManager.setPaused(true);
        }
    }

    public void resume() {
        if(paused) {
            paused = false;
            lastUpdateTime = System.nanoTime();
            updateTimer.start();
            renderTimer.start();
            transitionManager.setPaused(false);
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