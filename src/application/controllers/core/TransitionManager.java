package application.controllers.core;

import application.controllers.util.GameSettings;
import application.controllers.util.SoundController;
import application.views.panels.GamePanel;

public class TransitionManager {
    private final Manager gameManager;
    private final SoundController soundController;
    private final GamePanel gamePanel;
    private boolean paused;
    private float alpha = 0f;
    private boolean fadeIn = true;
    private boolean showTransition = true;
    private float fadeTime = 0f;
    private float FADE_DURATION = 0.5f;
    private float WAIT_DURATION = 1.0f;
    private boolean enemiesPrepared = false;
    private boolean isTransitionTriggered = false;
    private boolean transitionComplete = false;
    private boolean isGameOver = false;
    private boolean isPlayerExploding = false;
    private boolean isPlayerDead = false;
    private boolean isVictory = false;

    public TransitionManager(Manager gameManager, SoundController soundController, GamePanel gamePanel) {
        this.gameManager = gameManager;
        this.soundController = soundController;
        this.gamePanel = gamePanel;
        this.paused = false;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        if(!paused) {
            gamePanel.requestFocusInWindow();
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void update(double deltaTime) {
        if(paused) {
            return;
        }

        if(isVictory || isGameOver) this.WAIT_DURATION = 9.0f;
        else this.WAIT_DURATION = 1.0f;

        if(isTransitionTriggered && showTransition) {
            fadeTime += (float) deltaTime;
            if(fadeIn) {
                alpha = Math.min(1.0f, fadeTime / FADE_DURATION);
                if(alpha >= 1.0f) {
                    fadeIn = false;
                    fadeTime = 0f;

                    if(!isGameOver && !isVictory && !enemiesPrepared && gameManager != null) {
                        gameManager.spawnEnemiesAfterFade();
                        enemiesPrepared = true;
                    }
                }
            } else {
                if(fadeTime < WAIT_DURATION) {
                    alpha = 1.0f;
                } else {
                    alpha = Math.max(0.0f, 1.0f - ((fadeTime - WAIT_DURATION) / FADE_DURATION));
                    if(alpha <= 0.0f) {
                        showTransition = false;
                        transitionComplete = true;
                        isTransitionTriggered = false;
                        if(isGameOver) {
                            isGameOver = false;
                            isPlayerDead = false;
                            if(gameManager != null) {
                                gameManager.getGameStateHandler().restartGame();
                            }
                        } else if(isVictory) {
                            isVictory = false;
                            isPlayerDead = false;
                            if(gameManager != null) {
                                gameManager.getGameStateHandler().restartGame();
                            }
                        } else {
                            if(gameManager != null) {
                                gameManager.onTransitionComplete();
                            }
                        }
                    }
                }
            }
        }

        if(!isGameOver && !isVictory && !isTransitionTriggered && gameManager != null && gameManager.getPlayerView() != null
                && gameManager.getPlayerView().getPlayer() != null) {
            if(gameManager.getPlayerView().getPlayer().getHP() <= 0) {
                if(gameManager.getPlayerView().isExploding()) {
                    isPlayerExploding = true;
                    isPlayerDead = true;
                } else if(isPlayerExploding) {
                    triggerGameOver();
                    isPlayerExploding = false;
                }
            } else if(gameManager.getGameStateController().getLevel() >= 6) {
                triggerVictory();
            }
        }

        if(transitionComplete && !isGameOver && !isVictory) {
            gameManager.update(deltaTime);
        }
    }

    public void triggerGameOver() {
        if(paused) {
            return;
        }

        GameSettings gameSettings = GameSettings.getInstance();
        gameSettings.setContinueLevel(1);
        gameSettings.saveSettings();

        soundController.playBackgroundMusic(getClass().getResource("/asset/resources/sfx/CI4Gameover.wav").getPath());
        this.isGameOver = true;
        this.isTransitionTriggered = true;
        this.showTransition = true;
        this.fadeIn = true;
        this.fadeTime = 0f;
        this.alpha = 0f;
        this.transitionComplete = false;
        this.WAIT_DURATION = 9.0f;
    }

    public void triggerVictory() {
        if(paused) {
            return;
        }

        GameSettings gameSettings = GameSettings.getInstance();
        gameSettings.setContinueLevel(1);
        gameSettings.setComplete(true);
        gameSettings.saveSettings();

        soundController.playBackgroundMusic(getClass().getResource("/asset/resources/sfx/CI4MinorWin.wav").getPath());
        this.isVictory = true;
        this.isPlayerDead = true;
        this.isTransitionTriggered = true;
        this.showTransition = true;
        this.fadeIn = true;
        this.fadeTime = 0f;
        this.alpha = 0f;
        this.transitionComplete = false;
        this.WAIT_DURATION = 7.0f;
    }

    public boolean isPlayerDead() {
        return isPlayerDead;
    }

    public boolean isVictory() {
        return isVictory;
    }

    public void triggerTransition() {
        if(paused || isGameOver || isVictory) {
            return;
        }
        this.isTransitionTriggered = true;
        this.showTransition = true;
        this.fadeIn = true;
        this.fadeTime = 0f;
        this.alpha = 0f;
        this.enemiesPrepared = false;
        this.transitionComplete = false;

        if(gameManager != null) {
            int newLevel = gameManager.getGameStateController().getLevel();
            gamePanel.updateLevel(newLevel);
        }
    }

    public boolean isTransitionActive() {
        return showTransition;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isTransitionComplete() {
        return transitionComplete;
    }

    public float getAlpha() {
        return alpha;
    }

	public Manager getGameManager() {
		return gameManager;
	}
}