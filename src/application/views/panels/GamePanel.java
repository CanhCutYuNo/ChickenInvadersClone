package application.views.panels;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import application.controllers.core.Manager;
import application.controllers.core.TransitionManager;
import application.controllers.util.MouseController;
import application.controllers.util.ScreenUtil;
import application.controllers.util.SoundController;
import application.views.render.GameRenderer;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Manager gameManager;
    private final MouseController mouseController;
    private final GameRenderer gameRenderer;
    private final TransitionManager transitionManager;

    public GamePanel(Manager gameManager, SoundController soundController) {
        this.gameManager = gameManager;
        this.transitionManager = new TransitionManager(gameManager, soundController, this);
        this.gameRenderer = new GameRenderer(
            gameManager.getBulletController(),
            gameManager.getSkillsManager(),
            null,
            gameManager.getDeathEffectController(),
            gameManager.getItemsController(),
            gameManager.getGameStateController(),
            gameManager.getPlayerView(),
            ScreenUtil.getInstance(),
            transitionManager
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

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        gameRenderer.setLevelManager(gameManager.getLevelManager());
        gameRenderer.render(g, transitionManager.isTransitionComplete(), transitionManager.isTransitionActive(), 
                           transitionManager.getAlpha(), transitionManager.isGameOver(), transitionManager.isVictory(), 
                           getWidth(), getHeight());
    }

    public void updateLevel(int newLevel) {
        gameRenderer.updateLevel(newLevel);
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

    public TransitionManager getTransitionManager() {
        return transitionManager;
    }
}