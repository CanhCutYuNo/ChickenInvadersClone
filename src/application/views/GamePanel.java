package application.views;

import javax.swing.*;
import application.controllers.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Manager gameManager;
    private final MouseController mouseController;
    private final SoundController soundController; // Thêm SoundController

    public GamePanel(Manager gameManager) {
        this.gameManager = gameManager;
        this.soundController = new SoundController(); // Khởi tạo SoundController

        hideCursor();
        gameManager.spawnEnemies();
        setLayout(null);
        setFocusable(true);
        setDoubleBuffered(true);
        setOpaque(false);
        requestFocusInWindow();

        // Khởi tạo MouseController với SoundController
        mouseController = new MouseController(this, soundController);
        addMouseListener(mouseController);
        addMouseMotionListener(mouseController);
    }

    @Override
    public void paint(Graphics g) {
        gameManager.render(g);
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
}
