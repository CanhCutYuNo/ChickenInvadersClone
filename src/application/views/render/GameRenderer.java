package application.views.render;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import application.controllers.bullet.BulletController;
import application.controllers.core.GameStateController;
import application.controllers.enemy.death.DeathEffectController;
import application.controllers.enemy.items.ItemsController;
import application.controllers.enemy.skills.EnemySkillsController;
import application.controllers.level.ILevelManager;
import application.controllers.util.ScreenUtil;
import application.models.bullet.BulletDame;
import application.views.bullet.BulletView;
import application.views.player.PlayerView;

public class GameRenderer {
    private final BulletController bulletController;
    private final EnemySkillsController skillsManager;
    private ILevelManager levelManager;
    private final DeathEffectController deathEffectController;
    private final ItemsController itemsController;
    private final GameStateController gameStates;
    private final PlayerView playerView;
    private final ScreenUtil screenUtil;
    private final Image hudBar;
    private final Font font;

    private BufferedImage levelImage;
    private BufferedImage gameOverImage;
    private int currentLevel;

    public GameRenderer(BulletController bulletController, EnemySkillsController skillsManager, ILevelManager levelManager,
                        DeathEffectController deathEffectController, ItemsController itemsController, GameStateController gameStates,
                        PlayerView playerView, ScreenUtil screenUtil) {
        this.bulletController = bulletController;
        this.skillsManager = skillsManager;
        this.levelManager = levelManager;
        this.deathEffectController = deathEffectController;
        this.itemsController = itemsController;
        this.gameStates = gameStates;
        this.playerView = playerView;
        this.screenUtil = ScreenUtil.getInstance();

        this.hudBar = new ImageIcon(getClass().getResource("/asset/resources/gfx/infohud.png")).getImage();
        this.font = new Font("Arial", Font.BOLD, 24);

        this.currentLevel = gameStates.getLevel();
        loadLevelImage();
        loadGameOverImage();
    }

    public void render(Graphics g, boolean transitionComplete, boolean showTransition, float alpha, 
                      boolean isGameOver, boolean isPlayerDead, int panelWidth, int panelHeight) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.scale(screenUtil.getWidth() / 1920f / screenUtil.getScaleX(),
                  screenUtil.getHeight() / 1080f / screenUtil.getScaleY());

        if(showTransition) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            if(isGameOver && gameOverImage != null) {
                System.out.println("Drawing gameOverImage: alpha=" + alpha + ", width=" + gameOverImage.getWidth() + ", height=" + gameOverImage.getHeight());
                int x = (1920 - gameOverImage.getWidth()) / 2;
                int y = (1080 - gameOverImage.getHeight()) / 2;
                g2d.drawImage(gameOverImage, x, y, gameOverImage.getWidth(), gameOverImage.getHeight(), null);
            } else if(levelImage != null) {
                int x = (1920 - levelImage.getWidth()) / 2;
                int y = (1080 - levelImage.getHeight()) / 2;
                g2d.drawImage(levelImage, x, y, levelImage.getWidth(), levelImage.getHeight(), null);
            }
        }

        if(transitionComplete && !isGameOver) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            renderBullet(g);
            skillsManager.drawSkills(g);
            if(levelManager != null) {
                levelManager.render(g);
            }
            deathEffectController.render(g);
            itemsController.drawItems(g);
            for(BulletDame text : gameStates.getFloatingTexts()) {
                text.render(g);
            }
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        if(!isGameOver && !isPlayerDead) {
            renderPlayer(g);
            drawHUD(g, panelWidth, panelHeight);
        }
        renderPlayerExploding(g);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.dispose();
    }

    private void renderPlayer(Graphics g) {
        playerView.render(g);
    }
    
    private void renderPlayerExploding(Graphics g) {
        if(playerView.isExploding()) {
            playerView.explosionRender(g);
        }
    }

    private void renderBullet(Graphics g) {
        for (BulletView bulletView : bulletController.getBulletViews()) {
            bulletView.render(g);
        }
    }

    private void drawHUD(Graphics g, int panelWidth, int panelHeight) {
        int hudX = 0 - 50;
        int hudY = 1080 - hudBar.getHeight(null) + 20;
        g.drawImage(hudBar, hudX, hudY, null);

        g.setFont(font);
        g.setColor(Color.WHITE);

        var player = playerView.getPlayer();
        if(player != null) {
            g.drawString(" " + player.getHP(), hudX + 140, hudY + 65);
            g.drawString(" " + gameStates.getFoodCounts(), hudX + 450, hudY + 65);
        }
    }

    public void updateLevel(int newLevel) {
        if(newLevel != currentLevel) {
            this.currentLevel = newLevel;
            loadLevelImage();
        }
    }

    private void loadLevelImage() {
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
    }

    private void loadGameOverImage() {
        try {
            gameOverImage = ImageIO.read(getClass().getResource("/asset/resources/gfx/gameover.png"));
            if(gameOverImage == null) {
                System.out.println("Failed to load gameOverImage");
                gameOverImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
            }
        } catch (IOException e) {
            e.printStackTrace();
            gameOverImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public void setLevelManager(ILevelManager levelManager) {
        this.levelManager = levelManager;
    }
}