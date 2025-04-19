package application.views.panels;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import application.controllers.core.Manager;
import application.controllers.util.MouseController;
import application.controllers.util.ScreenUtil;
import application.views.render.GameRenderer;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Manager gameManager;
    private final MouseController mouseController;
    private final GameRenderer gameRenderer;
    private boolean paused;

    private float alpha = 0f;
    private boolean fadeIn = true;
    private boolean showTransition = true;
    private float fadeTime = 0f;
    private static final float FADE_DURATION = 0.5f;
    private static final float WAIT_DURATION = 0.5f;
    private boolean enemiesPrepared = false;
    private boolean isTransitionTriggered = false;
    private boolean transitionComplete = false;

    public GamePanel(Manager gameManager) {
        this.gameManager = gameManager;
        this.paused = false;

        this.gameRenderer = new GameRenderer(
            gameManager.getBulletController(),
            gameManager.getSkillsManager(),
            null,
            gameManager.getDeathEffectController(),
            gameManager.getItemsController(),
            gameManager.getGameStateController(),
            gameManager.getPlayerView(),
            ScreenUtil.getInstance()
        );

        hideCursor();
        setLayout(null);
        setFocusable(true);
        setDoubleBuffered(true);
        setOpaque(false);
        requestFocusInWindow();

        mouseController = new MouseController(this);
        addMouseListener(mouseController);
        addMouseMotionListener(mouseController);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        if (!paused) {
            requestFocusInWindow();
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void update(double deltaTime) {
        if (paused) {
            return;
        }

        if (isTransitionTriggered && showTransition) {
            fadeTime += (float) deltaTime;
            if (fadeIn) {
                alpha = Math.min(1.0f, fadeTime / FADE_DURATION);
                if (alpha >= 1.0f) {
                    fadeIn = false;
                    fadeTime = 0f;

                    if (!enemiesPrepared && gameManager != null) {
                        gameManager.spawnEnemiesAfterFade();
                        enemiesPrepared = true;
                    }
                }
            } else {
                if (fadeTime < WAIT_DURATION) {
                    alpha = 1.0f;
                } else {
                    alpha = Math.max(0.0f, 1.0f - ((fadeTime - WAIT_DURATION) / FADE_DURATION));
                    if (alpha <= 0.0f) {
                        showTransition = false;
                        transitionComplete = true;
                        isTransitionTriggered = false;
                        if (gameManager != null) {
                            gameManager.onTransitionComplete();
                        }
                    }
                }
            }
        }

        if (transitionComplete) {
            gameManager.update(deltaTime);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        gameRenderer.setLevelManager(gameManager.getLevelManager());
        gameRenderer.render(g, transitionComplete, showTransition, alpha, getWidth(), getHeight());
    }

    public void triggerTransition() {
        if (paused) {
            return;
        }
        this.isTransitionTriggered = true;
        this.showTransition = true;
        this.fadeIn = true;
        this.fadeTime = 0f;
        this.alpha = 0f;
        this.enemiesPrepared = false;
        this.transitionComplete = false;

        updateLevel();
    }

    public boolean isTransitionActive() {
        return showTransition;
    }

    public void updateLevel() {
        if (gameManager != null && !paused) {
            int newLevel = gameManager.getGameStateController().getLevel();
            gameRenderer.updateLevel(newLevel);
        }
    }

    private void hideCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "InvisibleCursor");
        setCursor(invisibleCursor);
    }

    public Manager getGameManager() {
        return gameManager;
    }

	public void setManager(Manager gameManager2) {
		// TODO Auto-generated method stub
		
	}
}