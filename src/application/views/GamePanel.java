package application.views;

import javax.swing.*;
import application.controllers.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Manager gameManager;
    private final MouseController mouseController;
    private final SoundController soundController;

    // Biến cho hiệu ứng fade
    private BufferedImage levelImage;
    private float alpha = 0f;
    private boolean fadeIn = true;
    private boolean showTransition = true;
    private Timer transitionTimer;
    private int currentLevel;

    public GamePanel(Manager gameManager) {
        this.gameManager = gameManager;
        this.soundController = new SoundController();

        hideCursor();
        setLayout(null);
        setFocusable(true);
        setDoubleBuffered(true);
        setOpaque(false);
        requestFocusInWindow();

        // Lấy level từ gameManager
        this.currentLevel = gameManager.getLevel();

        // Tải ảnh level và khởi chạy fade
        try {
            File imageFile = new File("src/asset/resources/gfx/wave" + currentLevel + ".png");
            if (imageFile.exists()) {
                System.out.println("Tìm thấy ảnh tại: " + imageFile.getAbsolutePath());
                levelImage = ImageIO.read(imageFile);
            } else {
                System.err.println("Không tìm thấy ảnh tại: " + imageFile.getAbsolutePath());
                levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
            }
        } catch (IOException e) {
            e.printStackTrace();
            levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
        }

        mouseController = new MouseController(this, soundController);
        addMouseListener(mouseController);
        addMouseMotionListener(mouseController);

        // Khởi tạo timer cho hiệu ứng fade
        transitionTimer = new Timer(8, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fadeIn) {
                    alpha += 0.1f;
                    if (alpha >= 1f) {
                        fadeIn = false;
                        transitionTimer.setDelay(500); // Đợi 1 giây
                    }
                } else {
                    alpha -= 0.2f;
                    if (alpha <= 0f) {
                        transitionTimer.stop();
                        showTransition = false;
                        if (gameManager != null) {
                            System.out.println("Fade xong, gọi spawnEnemiesAfterFade với level: " + currentLevel);
                            gameManager.spawnEnemiesAfterFade();
                            gameManager.update(0);
                            repaint(); // Thử repaint frame trực tiếp
                            System.out.println("Số lượng enemies sau spawn: " + gameManager.getEnemies().size());
                        }
                    }
                }
                repaint();
            }
        });
        transitionTimer.start(); // Bắt đầu fade ngay khi khởi tạo
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        gameManager.render(g);

        // Vẽ hiệu ứng fade nếu đang active
        if (showTransition && levelImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(levelImage, 760, 440, 400, 200, this);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    // Phương thức gọi spawnEnemiesAfterFade từ Manager
    private void spawnEnemiesAfterFade() {
        if (gameManager != null) {
            gameManager.spawnEnemiesAfterFade();
        }
    }

    // Phương thức để kiểm tra trạng thái transition
    public boolean isTransitionActive() {
        return showTransition;
    }

    public void updateLevel() {
        if (gameManager != null) {
            int newLevel = gameManager.getLevel();
            if (newLevel != currentLevel && (newLevel == 1 || newLevel == 2 || newLevel == 3)) {
                this.currentLevel = newLevel;
                try {
                    File imageFile = new File("src/asset/resources/gfx/lv" + currentLevel + ".png");
                    if (imageFile.exists()) {
                        System.out.println("Tìm thấy ảnh tại: " + imageFile.getAbsolutePath());
                        levelImage = ImageIO.read(imageFile);
                    } else {
                        System.err.println("Không tìm thấy ảnh tại: " + imageFile.getAbsolutePath());
                        levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
                }
                showTransition = true;
                alpha = 0f;
                fadeIn = true;
                transitionTimer.start();
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
}