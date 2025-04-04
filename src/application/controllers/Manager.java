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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import application.controllers.levels.Level1Manager;
import application.controllers.levels.Level2Manager;
import application.controllers.levels.Level3Manager;
import application.controllers.levels.Level4Manager;
import application.controllers.levels.Level5Manager;
import application.models.Bullet;
import application.models.DeathEffect;
import application.models.Enemy;
import application.models.EnemySkills;
import application.models.Items;
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
//    private EnemyProjectilesController eggs;
    private ItemsController items;
    private static BackgroundPanel backgroundPanel;
    private static MenuPanel menuPanel;
    private CardLayout cardLayout;
    private SoundController soundController;
    private JPanel mainPanel;
    private GameLoop gameLoop;
    private int frameDelay = 0;
    private int level = 5;
    private boolean playerExploded = false;
    // Thêm các biến để lưu trữ LevelXManager
    private Level1Manager level1Manager;
    private Level2Manager level2Manager;
    private Level3Manager level3Manager;
    private Level4Manager level4Manager;
    private Level5Manager level5Manager;

    GamePanel gamePanel;

    private boolean levelTransitionTriggered = false;

    private boolean isDelaying = false;
    private long delayStartTime = 0;
    private static final long DELAY_DURATION = 2000;

    public Manager(CardLayout _cardLayout, JPanel _mainPanel, BackgroundPanel _backgroundPanel, MenuPanel _menuPanel, GameLoop _gameLoop, SoundController _soundController, GamePanel _gamePanel) {
        screenUtil = ScreenUtil.getInstance();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        skillsManager = new EnemySkillsController(new HashMap<>());
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
        this.soundController = _soundController;
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
                System.out.println("Delay finished, triggering transition to Level " + level);
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

        if(getEnemies().isEmpty() && !levelTransitionTriggered && !isDelaying) {
            if(level == 1 && level1Manager != null) {
                level++;
                isDelaying = true;
                delayStartTime = System.currentTimeMillis();
            } else if(level == 2 && level2Manager != null) {
                level++;
                isDelaying = true;
                delayStartTime = System.currentTimeMillis();
            } else if(level == 3 && level3Manager != null) {
                level++;
                isDelaying = true;
                delayStartTime = System.currentTimeMillis();
            } else if(level == 4 && level4Manager != null) {
                level++;
                isDelaying = true;
                delayStartTime = System.currentTimeMillis();
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
                System.out.println("Player picked up an item!");

                // Gọi hàm xử lý khi nhặt item(tăng máu, đạn, điểm...)
                playerController.isDamaged(item.getDamage());

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
                            if(random.nextFloat() <  0.3){
                                items.addItem((int)enemy.getPosX(),(int) enemy.getPosY()-15, 10);
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
                System.out.println("Player collides with skill: " + skill.getSkillType());
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

      //  System.out.println(skillsManager.getSkills().size());

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

    public void shoot() {
        bullets.add(new Bullet(playerController.getPosX() + 39, playerController.getPosY(), 50, 1.0, 0.4));
        soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/bulletHenSolo.wav").getPath());
         System.out.println("Bắn");
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
        Rectangle itemBounds = new Rectangle(item.getPosX(), item.getPosY(), 4, 4);
        return player.getHitbox().intersects(itemBounds);
    }

	public void setGamePanel(GamePanel _gamePanel) {
		this.gamePanel = _gamePanel;
	}
}