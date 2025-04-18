package application.controllers;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JPanel;

import application.controllers.levels.Level1Manager;
import application.controllers.levels.Level2Manager;
import application.controllers.levels.Level3Manager;
import application.controllers.levels.Level4Manager;
import application.controllers.levels.Level5Manager;
import application.models.Bullet;
import application.models.EnemySkills;
import application.models.FloatingText;
import application.models.GameStates;
import application.models.Items;
import application.util.ScreenUtil;
import application.views.BackgroundPanel;
import application.views.BulletView;
import application.views.GamePanel;
import application.views.MenuPanel;
import application.views.PlayerView;

public class Manager {
    private ScreenUtil screenUtil;
    private PlayerView playerView;
    private PlayerController playerController;
    private BulletController bullets;
    private EnemyController enemyController;
    private EnemySkillsController skillsManager;
    private DeathEffectController deathEffectController;
    private GameSettings gameSettings;
    private ItemsController items;
    private static BackgroundPanel backgroundPanel;
    private static MenuPanel menuPanel;
    private CardLayout cardLayout;
    private SoundController soundController;
    private JPanel mainPanel;
    private GameLoop gameLoop;
    private GameStates gameStates;
    private LevelManager levelManager;
    private GamePanel gamePanel;

    private static final long DELAY_DURATION = 2000;

    public Manager(CardLayout _cardLayout, JPanel _mainPanel, BackgroundPanel _backgroundPanel, MenuPanel _menuPanel, GameLoop _gameLoop, SoundController _soundController, GamePanel _gamePanel) {
        this.soundController = _soundController;
        gameStates = new GameStates();
        screenUtil = ScreenUtil.getInstance();
        gameSettings = GameSettings.getInstance();
        bullets = new BulletController();
        enemyController = new EnemyController(soundController);
        skillsManager = new EnemySkillsController(soundController);
        enemyController.setSkillsManager(skillsManager);
        deathEffectController = new DeathEffectController();
        playerController = new PlayerController(null);
        playerView = new PlayerView(playerController);
        playerController.setPlayerView(playerView);
        items = new ItemsController("/asset/resources/gfx/flareSmall~1.png");
        this.cardLayout = _cardLayout;
        this.mainPanel = _mainPanel;
        Manager.backgroundPanel = _backgroundPanel;
        Manager.menuPanel = _menuPanel;
        this.gameLoop = _gameLoop;
        this.gamePanel = _gamePanel;
    }

    public PlayerController getPlayer() {
        return playerController;
    }

    public GameStates getGameStates() {
        return gameStates;
    }

    public void setBackgroundPanel(BackgroundPanel _backgroundPanel) {
        Manager.backgroundPanel = _backgroundPanel;
    }

    public void setMenuPanel(MenuPanel _menuPanel) {
        Manager.menuPanel = _menuPanel;
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    public void onTransitionComplete() {
        gameStates.setLevelTransitionTriggered(false);
    }

    public void update(double deltaTime) {
        if (playerView.isExploding()) {
            playerView.updateExplosion();
            if (52 < playerView.getExFrame()) {
                restartGame();
            }
            return;
        }

        if (gamePanel.isTransitionActive()) {
            return;
        }

        if (gameStates.isDelaying()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - gameStates.getDelayStartTime() >= DELAY_DURATION) {
                gameStates.setDelaying(false);
                gameStates.setLevelTransitionTriggered(true);
                gamePanel.triggerTransition();
            }
            return;
        }

        updateBullets();
        playerController.update();
        items.updateItems();
        skillsManager.updateSkills();
        enemyController.update();
        deathEffectController.update();

        if (levelManager != null) {
            levelManager.update((float) deltaTime);
            levelManager.checkEnemyDeath();
        }

        checkBulletEnemyCollisions();
        checkPlayerCollisionsWithEnemies();
        checkPlayerCollisionsWithSkills();
        checkPlayerCollisionsWithItems();
        updateFloatingTexts();

        if (enemyController.getEnemyModels().isEmpty() && !gameStates.isLevelTransitionTriggered() && !gameStates.isDelaying()) {
            gameStates.setLevel(gameStates.getLevel() + 1);
            gameStates.setDelaying(true);
            gameStates.setDelayStartTime(System.currentTimeMillis());
            gameSettings.setContinueLevel(gameStates.getLevel());
            gameSettings.saveSettings();
        }
    }

    private void restartGame() {
        gameStates.reset();
        enemyController.clear();
        skillsManager.clear();
        playerController.setPosX(800);
        playerController.setPosY(950);
        bullets.clear();
        levelManager = null;
        cardLayout.show(mainPanel, "Menu");
        menuPanel.setBackgroundPanel(backgroundPanel);
        soundController.playBackgroundMusic(getClass().getResource("/asset/resources/sfx/CI4Theme.wav").getPath());
        playerController.setHP();
    }

    private void updateBullets() {
        bullets.update();
    }

    private void checkPlayerCollisionsWithItems() {
        Iterator<Items> iterator = items.iterator();
        while (iterator.hasNext()) {
            Items item = iterator.next();
            if (isColliding(playerController, item)) {
                playerController.isDamaged(item.getDamage());
                spawnFloatingText(playerController.getPosX(), playerController.getPosY() - 10, "+ " + String.valueOf(Math.abs(item.getDamage())), Color.GREEN);
                soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/(eating1).wav").getPath());
                gameStates.setFoodCounts(gameStates.getFoodCounts() + 1);
                iterator.remove();
            }
        }
    }

    private void checkBulletEnemyCollisions() {
        for (int i = bullets.getBullets().size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.getBullets().get(i);
            for (int j = 0; j < enemyController.getEnemyModels().size(); j++) {
                if (isColliding(bullet, j)) {
                    spawnFloatingText(enemyController.getEnemyModels().get(j).getPosX() - 2, enemyController.getEnemyModels().get(j).getPosY(), "- " + String.valueOf(bullet.getDamage()), Color.RED);
                    enemyController.takeDamage(j, bullet.getDamage(), null, null);
                    bullets.removeBullet(i);
                    if (enemyController.getEnemyModels().get(j).isDead()) {
                        Random random = new Random();
                        float chance = 0;
                        int damageItem = 0;
                        switch (GameSettings.getInstance().getDifficulty()) {
                            case EASY:
                                chance = 0.5F;
                                damageItem = -15;
                                break;
                            case NORMAL:
                                chance = 0.3F;
                                damageItem = -10;
                                break;
                            case HARD:
                                chance = 0.2F;
                                damageItem = -5;
                                break;
                            case EXTREME:
                                chance = 0.15F;
                                damageItem = -5;
                                break;
                        }
                        if (random.nextDouble() < chance) {
                            items.addItem(enemyController.getEnemyModels().get(j).getPosX(), enemyController.getEnemyModels().get(j).getPosY() - 15, damageItem);
                        }
                    }
                    break;
                }
            }
        }
    }

    private void updateFloatingTexts() {
        for (FloatingText floatingText : gameStates.getFloatingTexts()) {
            floatingText.update();
        }
        gameStates.removeExpiredFloatingTexts();
    }

    private void checkPlayerCollisionsWithEnemies() {
        for (int i = 0; i < enemyController.getEnemyModels().size(); i++) {
            if (isColliding(playerController, i)) {
                if (!gameStates.isPlayerExploded()) {
                    playerController.getPlayerView().startExplosion();
                    soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/explosionPlayer.wav").getPath());
                    gameStates.setPlayerExploded(true);
                }
                break;
            }
        }
    }

    private void checkPlayerCollisionsWithSkills() {
        Iterator<EnemySkills> skillIterator = skillsManager.getSkills().iterator();
        while (skillIterator.hasNext()) {
            EnemySkills skill = skillIterator.next();
            if (skill.isActive() && isColliding(playerController, skill)) {
                playerController.isDamaged(skill.getDamage());
                spawnFloatingText(playerController.getPosX(), playerController.getPosY() - 10, "- " + String.valueOf(skill.getDamage()), Color.RED);
                soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/eggshellCrack.wav").getPath());
                if (playerController.getHP() <= 0) {
                    playerController.getPlayerView().startExplosion();
                    soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/explosionPlayer.wav").getPath());
                    gameStates.setPlayerExploded(true);
                }
                skillIterator.remove();
            }
        }
    }

    public void spawnEnemiesAfterFade() {
        enemyController.clear();
        if(gameStates.getLevel() == 1) {
            levelManager = new Level1Manager(soundController, enemyController);
        } else if (gameStates.getLevel() == 2) {
            levelManager = new Level2Manager(soundController, enemyController);
        } else if (gameStates.getLevel() == 3) {
            levelManager = new Level3Manager(soundController, enemyController);
        } else if (gameStates.getLevel() == 4) {
            levelManager = new Level4Manager(soundController, enemyController);
        } else if (gameStates.getLevel() == 5) {
            levelManager = new Level5Manager(soundController, enemyController);
        }
    }

    public void render(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.scale(screenUtil.getWidth() / 1920f / screenUtil.getScaleX(),
                screenUtil.getHeight() / 1080f / screenUtil.getScaleY());

        for (BulletView bulletView : bullets.getBulletViews()) {
            bulletView.render(g);
        }

        skillsManager.drawSkills(g);

        if (levelManager != null) {
            levelManager.render(g);
        }

        deathEffectController.render(g);
        items.drawItems(g);

        for (FloatingText text : gameStates.getFloatingTexts()) {
            text.render(g);
        }

        int fps = gameLoop.getFPS();
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("FPS: " + fps, 50, 50);

        renderPlayer(g);

        g2D.dispose();
    }

    public void renderPlayer(Graphics g) {
        if (playerView.isExploding()) {
            playerView.explosionRender(g);
        } else {
            playerView.render(g);
        }
    }

    public void movePlayer(int x, int y) {
        playerController.setPosX(x - 35);
        playerController.setPosY(y - 32);
        playerController.updateDirection(x);
        playerController.setLastMoveTime(System.currentTimeMillis());
    }

    public void spawnFloatingText(int x, int y, String text, Color color) {
        gameStates.addFloatingText(new FloatingText(x, y, text, color));
    }

    public void shoot() {
        switch (GameSettings.getInstance().getDifficulty()) {
            case EASY:
                bullets.addBullet(playerController.getPosX() + 39, playerController.getPosY(), 40, 1.0, 0.4);
                break;
            case NORMAL:
                bullets.addBullet(playerController.getPosX() + 39, playerController.getPosY(), 30, 1.0, 0.4);
                break;
            case HARD:
            case EXTREME:
                bullets.addBullet(playerController.getPosX() + 39, playerController.getPosY(), 25, 1.0, 0.4);
                break;
        }
        soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/bulletHenSolo.wav").getPath());
    }

    private boolean isColliding(Bullet bullet, int enemyIndex) {
        Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 9, 52);
        return enemyController.getHitbox(enemyIndex).intersects(bulletBounds);
    }

    private boolean isColliding(PlayerController player, int enemyIndex) {
        return player.getHitbox().intersects(enemyController.getHitbox(enemyIndex));
    }

    private boolean isColliding(PlayerController player, EnemySkills skill) {
        return skill.getHitbox().intersects(player.getHitbox());
    }

    private boolean isColliding(PlayerController player, Items item) {
        return player.getHitbox().intersects(item.getHitbox());
    }

    public void setGamePanel(GamePanel _gamePanel) {
        this.gamePanel = _gamePanel;
    }

    public void load() {
        gameStates.setLevel(gameSettings.getContinueLevel());
    }
}