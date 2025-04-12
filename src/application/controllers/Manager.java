package application.controllers;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import application.controllers.levels.Level1Manager;
import application.controllers.levels.Level2Manager;
import application.controllers.levels.Level3Manager;
import application.controllers.levels.Level4Manager;
import application.controllers.levels.Level5Manager;
import application.models.*;
import application.models.types.ChickenBoss;
import application.models.types.ChickenEnemy;
import application.models.types.EggShellEnemy;
import application.util.ScreenUtil;
import application.views.BackgroundPanel;
import application.views.GamePanel;
import application.views.MenuPanel;
import application.views.PlayerView;

public class Manager {
    private ScreenUtil screenUtil;
    private PlayerView playerView;
    private PlayerController playerController;
    private ArrayList<Bullet> bullets;
    private List<Enemy> enemies;
    private EnemySkillsController skillsManager;
    private DeathEffectController deathEffectController;
    private GameSettings gameSettings;
//    private EnemyProjectilesController eggs;
    private ItemsController items;
    private static BackgroundPanel backgroundPanel;
    private static MenuPanel menuPanel;
    private CardLayout cardLayout;
    private SoundController soundController;
    private JPanel mainPanel;
    private GameLoop gameLoop;
    private int frameDelay = 0;
    private int level = 1;
    private boolean playerExploded = false;
    // Thêm các biến để lưu trữ LevelXManager
    private Level1Manager level1Manager;
    private Level2Manager level2Manager;
    private Level3Manager level3Manager;
    private Level4Manager level4Manager;
    private Level5Manager level5Manager;

    private GamePanel gamePanel;

    private boolean levelTransitionTriggered = false;

    private boolean isDelaying = false;
    private long delayStartTime = 0;
    private static final long DELAY_DURATION = 2000;

    private int foodCount = 0;

    private List<FloatingText> floatingTexts = new ArrayList<>();

    public Manager(CardLayout _cardLayout, JPanel _mainPanel, BackgroundPanel _backgroundPanel, MenuPanel _menuPanel, GameLoop _gameLoop, SoundController _soundController, GamePanel _gamePanel) {
    	this.soundController = _soundController;
    	screenUtil = ScreenUtil.getInstance();
        gameSettings = GameSettings.getInstance();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        skillsManager = new EnemySkillsController(soundController);
        deathEffectController = new DeathEffectController();
        playerController = new PlayerController(null);
        playerView = new PlayerView(playerController);
        playerController.setPlayerView(playerView);

        items = new ItemsController("/asset/resources/gfx/flareSmall~1.png");
//        eggs = new EnemyProjectilesController("/asset/resources/gfx/introEgg.png");
        this.cardLayout = _cardLayout;
        this.mainPanel = _mainPanel;
        Manager.backgroundPanel = _backgroundPanel;
        Manager.menuPanel = _menuPanel;
        this.gameLoop = _gameLoop;
        this.gamePanel = _gamePanel;
    }

    public PlayerController getPlayer(){return playerController;}

    public int getFoodCount(){return foodCount;}

    public void setBackgroundPanel(BackgroundPanel _backgroundPanel) {
        Manager.backgroundPanel = _backgroundPanel;
    }
    
    public void setMenuPanel(MenuPanel _menuPanel) {
        Manager.menuPanel = _menuPanel;
    }
    
    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }
    
    public Point getPlayerPosition() {
        return playerController.getPosition();
    }

    public int getLevel() {
        return level;
    }
    
    public void setLevel(int lv) {
        level = lv;
    }
    
    public List<Enemy> getEnemies() {
        return enemies;
    }

    public SoundController getSound() {
    	return soundController;
    }

    public void onTransitionComplete() {
        levelTransitionTriggered = false;
    }

    public void update(double deltaTime) {
        if(playerView.isExploding()) {
            playerView.updateExplosion();
            if(52 < playerView.getExFrame()) {
                restartGame();
            }
            return;
        }

        if(gamePanel.isTransitionActive()) {
            return;
        }

        if(isDelaying) {
            long currentTime = System.currentTimeMillis();
            if(currentTime - delayStartTime >= DELAY_DURATION) {
                isDelaying = false;
                levelTransitionTriggered = true;
                gamePanel.triggerTransition();
            }
        }

        updateBullets();
        bullets.removeIf(bullet -> bullet.isOffScreen(1080));
        if(frameDelay == 1) {
            frameDelay = 0;
        }
        frameDelay++;

        playerController.update();
        items.updateItems();

        skillsManager.updateSkills();

        for(Enemy enemy : enemies) {
            enemy.update();
            if(enemy instanceof ChickenBoss) {
                ChickenBoss boss =(ChickenBoss) enemy;
                if(!boss.isMovingToCenter()) {
                    if(boss.shouldCreateHole()) {
                        boss.createHoleSkill(skillsManager);
                    }
                    if(boss.shouldCreateFireballBurst()) {
                        boss.createFireballBurst(skillsManager);
                    }
                }
            }
            else if(enemy instanceof ChickenEnemy) {
            	ChickenEnemy chicken =(ChickenEnemy) enemy;
                chicken.createEggs(skillsManager);
            }
        }

        if(level == 1 && level1Manager != null) {
            level1Manager.update((float) deltaTime);
        } else if(level == 2 && level2Manager != null) {
            level2Manager.update((float) deltaTime);
        } else if(level == 3 && level3Manager != null) {
            level3Manager.update((float) deltaTime);
        } else if(level == 5 && level5Manager != null) {
            level5Manager.update((float) deltaTime);
        } else if(level == 4 && level4Manager != null) {
            level4Manager.update((float) deltaTime);
        }
        deathEffectController.update();

        checkBulletEnemyCollisions();
        checkPlayerCollisionsWithEnemies();
        checkPlayerCollisionsWithSkills();
        checkPlayerCollisionsWithItems();

        updateFloatingTexts();

        if(getEnemies().isEmpty() && !levelTransitionTriggered && !isDelaying) {
            if(level == 1 && level1Manager != null) {
                level++;
                isDelaying = true;
                delayStartTime = System.currentTimeMillis();

                gameSettings.setContinueLevel(level);
                gameSettings.saveSettings();
            } else if(level == 2 && level2Manager != null) {
                level++;
                isDelaying = true;
                delayStartTime = System.currentTimeMillis();

                gameSettings.setContinueLevel(level);
                gameSettings.saveSettings();                
            } else if(level == 3 && level3Manager != null) {
                level++;
                isDelaying = true;
                delayStartTime = System.currentTimeMillis();

                gameSettings.setContinueLevel(level);
                gameSettings.saveSettings();                
            } else if(level == 4 && level4Manager != null) {
                level++;
                isDelaying = true;
                delayStartTime = System.currentTimeMillis();
                
                gameSettings.setContinueLevel(level);
                gameSettings.saveSettings();                
            }
        }

    }



    private void restartGame() {
        enemies.clear();
        skillsManager.clear();
        playerController.setPosX(800);
        playerController.setPosY(950);
        bullets.clear();
//        eggs.clear();
        level = 1;
        level1Manager = null; // Reset LevelXManager
        level2Manager = null;
        level3Manager = null;
        level5Manager = null;
        level4Manager = null;

        cardLayout.show(mainPanel, "Menu");        
        menuPanel.setBackgroundPanel(backgroundPanel);
        soundController.playBackgroundMusic(getClass().getResource("/asset/resources/sfx/CI4Theme.wav").getPath());
        playerExploded = false;

        foodCount = 0;
        playerController.setHP();

    }

    private void updateBullets() {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for(Bullet bullet : bullets) {
            bullet.update();
            if(bullet.isOffScreen(1080)) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);
    }

    private void checkPlayerCollisionsWithItems() {
        Iterator<Items> iterator = items.iterator();
        while(iterator.hasNext()) {
            Items item = iterator.next();

            // Kiểm tra va chạm với người chơi
            if(isColliding(playerController,item)) {
                // Gọi hàm xử lý khi nhặt item(tăng máu, đạn, điểm...)
                playerController.isDamaged(item.getDamage());
                spawnFloatingText(playerController.getPosX(), playerController.getPosY() - 10,"+ "+String.valueOf(Math.abs(item.getDamage())), Color.GREEN);
                soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/(eating1).wav").getPath());
                foodCount++;
                // Xóa item khỏi danh sách
                iterator.remove();
            }
        }
    }

    private void checkBulletEnemyCollisions() {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        ArrayList<Enemy> enemiesToAdd = new ArrayList<>();

        Iterator<Bullet> bulletIterator = bullets.iterator();
        while(bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Enemy> enemyIterator = enemies.iterator();
            while(enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                if(isColliding(bullet, enemy)) {
                    spawnFloatingText(enemy.getPosX()-2, enemy.getPosY(),"- "+String.valueOf(bullet.getDamage()),Color.red);
                    enemy.takeDamage(bullet.getDamage());
                    bulletsToRemove.add(bullet);
                    if(enemy.isDead()) {
                        if(enemy instanceof EggShellEnemy && level4Manager != null) {
                            level4Manager.addChickAfterEggShellDeath((int) enemy.getPosX(),(int) enemy.getPosY());
                            enemiesToRemove.add(enemy);
                        }
                        else{
                            DeathEffect tempDeathEffect = enemy.getDeathEffect();
                            if(tempDeathEffect != null){
                                deathEffectController.add(tempDeathEffect);
                            }
                            enemiesToRemove.add(enemy);

                            //Them item
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

                            if(random.nextDouble() < chance){
                                items.addItem((int) enemy.getPosX(), (int) enemy.getPosY()-15, damageItem);
                            }
                        }
                    }

                    break;
                }
            }
        }


        bullets.removeAll(bulletsToRemove);
        for(Enemy enemy : enemiesToRemove) {
            if(level == 1 && level1Manager != null) {
                level1Manager.removeEnemy(enemy);
            }
            else if(level== 2 && level2Manager != null){
                level2Manager.removeEnemy(enemy);
            }
            else if(level == 5 && level5Manager != null) {
                level5Manager.removeEnemy(enemy);
            }
            else if(level == 3 && level3Manager != null) {
                level3Manager.removeEnemy(enemy);
            }
            else if(level == 4 && level4Manager != null){
                level4Manager.removeEnemy(enemy);
            }
        }
        enemies.addAll(enemiesToAdd);
        enemies.removeAll(enemiesToRemove);
        //   System.out.println("Removed " + bulletsRemoved + " bullets and " + enemiesRemoved + " enemies. Current enemies size: " + enemies.size());
    }

    // Cập nhật và loại bỏ FloatingText đã hết thời gian tồn tại
    public void updateFloatingTexts() {
        for (int i = 0; i < floatingTexts.size(); i++) {
            FloatingText floatingText = floatingTexts.get(i);
            floatingText.update();
            if (floatingText.isExpired()) {
                floatingTexts.remove(i);
                i--;  // Điều chỉnh lại chỉ số sau khi xóa
            }
        }
    }


    private void checkPlayerCollisionsWithEnemies() {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while(enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();

            if(isColliding(playerController, enemy)) {
                if(!playerExploded) {
                    playerController.getPlayerView().startExplosion();
                    soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/explosionPlayer.wav").getPath());
                    playerExploded = true;
                }
            }
        }
    }
    
    private void checkPlayerCollisionsWithSkills() {
        Iterator<EnemySkills> skillIterator = skillsManager.getSkills().iterator();
        while(skillIterator.hasNext()) {
            EnemySkills skill = skillIterator.next();
            if(skill.isActive() && isColliding(playerController, skill)) {
                playerController.isDamaged(skill.getDamage());
                spawnFloatingText(playerController.getPosX(), playerController.getPosY() - 10,"- "+String.valueOf(skill.getDamage()),Color.red);
                soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/eggshellCrack.wav").getPath());
                if(playerController.getHP() <= 0) {
                    playerController.getPlayerView().startExplosion();
                    soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/explosionPlayer.wav").getPath());
                    playerExploded = true;
                }
                skillIterator.remove();
            }
        }
    }

    public void spawnEnemiesAfterFade() {
        enemies.clear();
        if(level == 1) {
            level1Manager = new Level1Manager(soundController, enemies);
            enemies.addAll(level1Manager.getEnemies());
        }
        else if(level == 2){
            level2Manager = new Level2Manager(soundController, enemies);
            enemies.addAll(level2Manager.getEnemies());
        } else if(level == 5) {
            level5Manager = new Level5Manager(soundController, enemies);
            enemies.addAll(level5Manager.getEnemies());
        } else if(level == 3) {
            level3Manager = new Level3Manager(soundController, enemies);
            enemies.addAll(level3Manager.getEnemies());
        }
        else if(level == 4){
            level4Manager = new Level4Manager(soundController, enemies);
            enemies.addAll(level4Manager.getEnemies());
        }
    }

    public void render(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.scale(screenUtil.getWidth() / 1920f / screenUtil.getScaleX(),
                screenUtil.getHeight() / 1080f / screenUtil.getScaleY());

        for(Bullet bullet : bullets) bullet.render(g);

        skillsManager.drawSkills(g);

        // Render thông qua LevelXManager thay vì render trực tiếp
        if(level == 1 && level1Manager != null) {
            level1Manager.render(g);
        }
        else if(level == 2 && level2Manager != null){
            level2Manager.render(g);
        }
        else if(level == 5 && level5Manager != null) {
            level5Manager.render(g);
        }
        else if(level == 3 && level3Manager != null) {
            level3Manager.render(g);
        }
        else if(level == 4 && level4Manager != null){
            level4Manager.render(g);
        }

        deathEffectController.render(g);

        items.drawItems(g);

        for(FloatingText text: floatingTexts){
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
        if(playerView.isExploding()) {
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
        floatingTexts.add(new FloatingText(x, y, text, color));
    }

    public void shoot() {
        switch (GameSettings.getInstance().getDifficulty()){
            case EASY:
                bullets.add(new Bullet(playerController.getPosX() + 39, playerController.getPosY(), 40, 1.0, 0.4));
                break;

            case NORMAL:
                bullets.add(new Bullet(playerController.getPosX() + 39, playerController.getPosY(), 30, 1.0, 0.4));
                break;

            case HARD,EXTREME:
                bullets.add(new Bullet(playerController.getPosX() + 39, playerController.getPosY(), 25, 1.0, 0.4));
                break;
        }
        soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/bulletHenSolo.wav").getPath());
    }

    private boolean isColliding(Bullet bullet, Enemy enemy) {
        Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 9, 52);
        return enemy.getHitbox().intersects(bulletBounds);
    }

    private boolean isColliding(PlayerController player, Enemy enemy) {
        return player.getHitbox().intersects(enemy.getHitbox());
    }

    private boolean isColliding(PlayerController player, EnemySkills skill) {
        Shape skillHitbox = skill.getHitbox();
        Rectangle playerHitbox = player.getHitbox();
        return skillHitbox.intersects(playerHitbox);
    }

    private boolean isColliding(PlayerController player, Items item) {
        return player.getHitbox().intersects(item.getHitbox());
    }

	public void setGamePanel(GamePanel _gamePanel) {
		this.gamePanel = _gamePanel;
	}

    public void load(){
        level = gameSettings.getContinueLevel();
    }
}