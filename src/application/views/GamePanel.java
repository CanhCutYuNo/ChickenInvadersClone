package application.views;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import application.controllers.Manager;
import application.controllers.MouseController;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Manager gameManager;
    private final MouseController mouseController;
    private boolean paused;

    private BufferedImage levelImage;
    private float alpha = 0f;
    private boolean fadeIn = true;
    private boolean showTransition = true;
    private int currentLevel;
    private float fadeTime = 0f;
    private static final float FADE_DURATION = 0.5f;
    private static final float WAIT_DURATION = 0.5f;
    private boolean enemiesPrepared = false;
    private boolean isTransitionTriggered = false;
    private boolean transitionComplete = false;

    private Image hudBar;
    private Font font;

    public GamePanel(Manager gameManager) {
        this.gameManager = gameManager;
        this.paused = false;

        hideCursor();
        setLayout(null);
        setFocusable(true);
        setDoubleBuffered(true);
        setOpaque(false);
        requestFocusInWindow();

        this.currentLevel = gameManager.getGameStates().getLevel();

        try {
            File imageFile = new File("src/asset/resources/gfx/wave" + currentLevel + ".png");
            if(imageFile.exists()) {
                levelImage = ImageIO.read(imageFile);
            } else {
                levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
            }
        } catch (IOException e) {
            e.printStackTrace();
            levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
        }

        mouseController = new MouseController(this);
        addMouseListener(mouseController);
        addMouseMotionListener(mouseController);

        hudBar = new ImageIcon(getClass().getResource("/asset/resources/gfx/infohud.png")).getImage();
        font = new Font("Arial", Font.BOLD, 24);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        if(!paused) {
            requestFocusInWindow();
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void update(double deltaTime) {
        if(paused) {
            return; 
        }

        if(isTransitionTriggered && showTransition) {
            fadeTime += (float) deltaTime;
            if(fadeIn) {
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

        if(transitionComplete) {
            gameManager.update(deltaTime);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        drawHUD(g);
        if(showTransition && isTransitionTriggered) {

            if (levelImage != null) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                int x = (getWidth() - levelImage.getWidth()) / 2;
                int y = (getHeight() - levelImage.getHeight()) / 2;
                g2d.drawImage(levelImage, x, y, levelImage.getWidth(), levelImage.getHeight(), this);
            }
        }

        if(transitionComplete && gameManager != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            gameManager.render(g);
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        gameManager.renderPlayer(g);
    }

    public void triggerTransition() {
        if(paused) {
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
        if(gameManager != null && !paused) {
            int newLevel = gameManager.getGameStates().getLevel();
            if(newLevel != currentLevel) {
                this.currentLevel = newLevel;
                try {
                    File imageFile = new File("src/asset/resources/gfx/wave" + currentLevel + ".png");
                    if (imageFile.exists()) {
                        levelImage = ImageIO.read(imageFile);
                    } else {
                        levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
                }
            }
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

    private void drawHUD(Graphics g){
        int hudX = 0 - 50;
        int hudY = getHeight() - hudBar.getHeight(null) + 20;

        g.drawImage(hudBar, hudX, hudY, null);

        g.setFont(font);
        g.setColor(Color.WHITE);

        var player = gameManager.getPlayer();
        if(player != null){
            g.drawString(" " + player.getHP(), hudX + 140, hudY + 65);
            g.drawString(" " + gameManager.getGameStates().getFoodCounts(), hudX + 450, hudY + 65);
        }

    }


}