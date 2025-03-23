package application.views;

import javax.swing.*;
import application.controllers.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Manager gameManager;
    private final MouseController mouseController;
    private final SoundController soundController;

    private BufferedImage levelImage;
    private float alpha = 0f;
    private boolean fadeIn = true;
    private boolean showTransition = true;
    private int currentLevel;
    private float fadeTime = 0f;
    private float postFadeTime = 0f;
    private static final float FADE_DURATION = 0.5f;
    private static final float WAIT_DURATION = 0.5f;
    private static final float POST_FADE_DURATION = 0.2f;
    private boolean enemiesPrepared = false;
    private boolean isTransitionTriggered = false;

    public GamePanel(Manager gameManager) {
        this.gameManager = gameManager;
        this.soundController = gameManager.getSound();

        hideCursor();
        setLayout(null);
        setFocusable(true);
        setDoubleBuffered(true);
        setOpaque(false);
        requestFocusInWindow();

        this.currentLevel = gameManager.getLevel();
       // System.out.println("Level ban đầu: " + currentLevel);

        try {
            File imageFile = new File("src/asset/resources/gfx/wave" + currentLevel + ".png");
            if(imageFile.exists()) {
           //     System.out.println("Tìm thấy ảnh tại: " + imageFile.getAbsolutePath());
                levelImage = ImageIO.read(imageFile);
            } else {
            //    System.err.println("Không tìm thấy ảnh tại: " + imageFile.getAbsolutePath());
                levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
            }
        } catch(IOException e) {
            e.printStackTrace();
            levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
        }

        mouseController = new MouseController(this, soundController);
        addMouseListener(mouseController);
        addMouseMotionListener(mouseController);
    }

    public void update(double deltaTime) {
        if(isTransitionTriggered && showTransition) {
            fadeTime +=(float) deltaTime;
          //  System.out.println("FadeTime: " + fadeTime + ", Alpha: " + alpha + ", FadeIn: " + fadeIn);
            if(fadeIn) {
                alpha = Math.min(1.0f, fadeTime / FADE_DURATION);
                if(alpha >= 1.0f) {
                    fadeIn = false;
                    fadeTime = 0f;
                    if(!enemiesPrepared && gameManager != null) {
                  //      System.out.println("Fade in xong, chuẩn bị enemies cho level: " + currentLevel);
                        gameManager.spawnEnemiesAfterFade();
                        gameManager.update(0);
                        enemiesPrepared = true;
                    }
                }
            } else if(fadeTime >= WAIT_DURATION) {
                alpha = Math.max(0.0f, 1.0f -((fadeTime - WAIT_DURATION) / FADE_DURATION));
                if(alpha <= 0.0f) {
                    showTransition = false;
                    postFadeTime = 0f;
              //      System.out.println("Fade out hoàn tất, chuyển sang trạng thái game");
                }
            }
        } else if(!showTransition && postFadeTime < POST_FADE_DURATION) {
            postFadeTime +=(float) deltaTime;
        //    System.out.println("PostFadeTime: " + postFadeTime + ", POST_FADE_DURATION: " + POST_FADE_DURATION);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    //    System.out.println("Đang vẽ, showTransition: " + showTransition + ", postFadeTime: " + postFadeTime);

        Graphics2D g2d =(Graphics2D) g;

        if(showTransition && isTransitionTriggered) {
            // Vẽ transition(levelImage) lên trên với độ trong suốt
            if(levelImage != null) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                int x =(getWidth() - levelImage.getWidth()) / 2;
                int y =(getHeight() - levelImage.getHeight()) / 2;
                g2d.drawImage(levelImage, x, y, levelImage.getWidth(), levelImage.getHeight(), this);
            }
        } else if(postFadeTime < POST_FADE_DURATION) {
            float transitionAlpha = postFadeTime / POST_FADE_DURATION;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transitionAlpha));
        //    System.out.println("Vẽ game với alpha: " + transitionAlpha);
            // gameManager đã được vẽ ở trên, không cần vẽ lại
        } else {
        //    System.out.println("Vẽ game bình thường");
            gameManager.render(g);
            // gameManager đã được vẽ ở trên, không cần vẽ lại
        }
        
        if(gameManager != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            gameManager.renderPlayer(g);
        }


        // Đặt lại độ trong suốt về mặc định
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public void triggerTransition() {
        this.isTransitionTriggered = true;
        this.showTransition = true;
        this.fadeIn = true;
        this.fadeTime = 0f;
        this.alpha = 0f;
        this.enemiesPrepared = false;
   //     System.out.println("Transition đã được kích hoạt!");
    }

    public boolean isTransitionActive() {
        return showTransition;
    }

    public void updateLevel() {
        if(gameManager != null) {
            int newLevel = gameManager.getLevel();
            if(newLevel != currentLevel &&(newLevel == 1 || newLevel == 2 || newLevel == 3)) {
                this.currentLevel = newLevel;
                try {
                    File imageFile = new File("src/asset/resources/gfx/wave" + currentLevel + ".png");
                    if(imageFile.exists()) {
                    //    System.out.println("Tìm thấy ảnh tại: " + imageFile.getAbsolutePath());
                        levelImage = ImageIO.read(imageFile);
                    } else {
                     //   System.err.println("Không tìm thấy ảnh tại: " + imageFile.getAbsolutePath());
                        levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                    levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
                }
                fadeTime = 0f;
                alpha = 0f;
                fadeIn = true;
                showTransition = true;
                enemiesPrepared = false;
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