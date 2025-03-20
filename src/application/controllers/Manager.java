package application.controllers;

import application.controllers.levels.*;
import application.models.*;
import application.views.*;
import application.models.types.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Manager {
    private PlayerView playerView;
    private PlayerController playerController;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private ArrayList<DeathEffect> deathEffects;
    private EnemyProjectilesController eggs;
    private static BackgroundPanel backgroundPanel;
    private static MenuPanel menuPanel; 
    private CardLayout cardLayout;
    private SoundController soundController;
    private JPanel mainPanel;
    private GameLoop gameLoop;
    private int frameDelay = 0;
    private int level = 1; // Khai báo level
    private boolean playerExploded = false;

    public Manager(CardLayout _cardLayout, JPanel _mainPanel, BackgroundPanel _backgroundPanel, MenuPanel _menuPanel, GameLoop _gameLoop, SoundController _soundController) {
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        deathEffects = new ArrayList<>();
        playerController = new PlayerController(0.5, null);
        playerView = new PlayerView(playerController);
        playerController.setPlayerView(playerView);

        eggs = new EnemyProjectilesController("/asset/resources/gfx/introEgg.png");
        this.cardLayout = _cardLayout;
        this.mainPanel = _mainPanel;
        Manager.backgroundPanel = _backgroundPanel;
        Manager.menuPanel = _menuPanel;
        this.gameLoop = _gameLoop;
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

    // Thêm phương thức getLevel()
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int lv) {
		level = lv;
	}
    
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void update(double deltaTime) {
     //   // System.out.println("Đang update, số enemies: " + enemies.size());
        if (playerView.isExploding()) {
            playerView.updateExplosion();
            if (52 < playerView.getExFrame()) {
                restartGame();
            }
            return;
        }

        updateBullets();
        bullets.removeIf(bullet -> bullet.isOffScreen(1080));
        if (frameDelay == 1) {
            frameDelay = 0;
        }
        frameDelay++;

        updateEggs();

        playerController.update();
        
        updateEnemies();
        
        updateDeathEffects();

        checkCollisions();
        checkBulletEnemyCollisions();
        checkPlayerCollisionsWithEnemies();
        checkPlayerCollisionsWithEgg();
//
//        if (enemies.isEmpty()) {
//            level++;
//            // System.out.println("New level !! " + level);
//        }
    }

    private void restartGame() {
        enemies.clear();
        playerController.setPosX(800);
        playerController.setPosY(950);
        bullets.clear();
        eggs.clear();
        level = 1;
        // Không cần gọi spawnEnemies, để GamePanel xử lý khi khởi tạo lại

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
    
    private void updateEggs() {
        Random rand = new Random();
     
        for(Enemy enemy : enemies) {
            if(rand.nextInt(1000) < 1) {
                eggs.addProjectile(enemy.getPosX() + 15, enemy.getPosY() + 30);
            }
        }
        eggs.updateProjectiles();
    }
    
    private void updateEnemies(){
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();        
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                enemy.nextFrame();
                enemy.update();
                if(enemy.isDead()) {
                    DeathEffect tempDeathEffect = enemy.getDeathEffect();
                    if(tempDeathEffect != null){
                        deathEffects.add(tempDeathEffect); 
                    }
                    enemiesToRemove.add(enemy);
                }                
            }
        } 
        enemies.removeAll(enemiesToRemove);        
    }
    
    private void updateDeathEffects(){
        ArrayList<DeathEffect> deathEffectsToRemove = new ArrayList<>();
        for(DeathEffect deathEffect: deathEffects){
            if(deathEffect != null){
                deathEffect.update();
                if(deathEffect.isEnd()){
                    deathEffectsToRemove.add(deathEffect);
                }
            }
        }
        deathEffects.removeAll(deathEffectsToRemove);
    }
    
    private void checkBulletEnemyCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while(bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();

            Iterator<Enemy> enemyIterator = enemies.iterator();
            while(enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();

                if(isColliding(bullet, enemy)) {
                    enemy.takeDamage(bullet.getDamage());
                    bulletIterator.remove(); 

                    break;
                }
            }
        }
    }
    
    private void checkPlayerCollisionsWithEnemies() {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while(enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();

            if(isColliding2(playerController, enemy)) {
                if(!playerExploded) {
                    playerController.getPlayerView().startExplosion();
                    soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/explosionPlayer.wav").getPath());
                    playerExploded = true;
                }
            }
        }
    }
    
    private void checkPlayerCollisionsWithEgg() {
        Iterator<EnemyProjectiles> eggIterator = eggs.getProjectiles().iterator();
        while(eggIterator.hasNext()) {
            EnemyProjectiles egg = eggIterator.next();

            if(isColliding3(playerController, egg)) {
                playerController.isDamaged(egg.getDamage());
                if(playerController.getHP() <= 0) {
                    playerController.getPlayerView().startExplosion();
                    soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/explosionPlayer.wav").getPath());
                    playerExploded = true;
                }
                eggIterator.remove();
            }
        }
    }

    // Loại bỏ tham chiếu gamePanel, chỉ giữ spawnEnemiesAfterFade
    public void spawnEnemiesAfterFade() {
        enemies = new ArrayList<>();
      //  // System.out.println("Spawn enemies for level: " + level);
        if (level == 1) {
            enemies = new Level1Manager(soundController).getEnemies();
        //    // System.out.println("Số lượng enemies level 1: " + enemies.size());
        } else if (level == 2) {
            enemies = new Level2Manager(soundController).getEnemies();
         //   // System.out.println("Số lượng enemies level 2: " + enemies.size());
        } else if (level == 3) {
            enemies = new Level3Manager(soundController).getEnemies();
         //   // System.out.println("Số lượng enemies level 3: " + enemies.size());
        } else {
         //   System.err.println("Level " + level + " không được hỗ trợ!");
        }
        // Test
//        enemies = new TestLevelManager(soundController).getEnemies();
      //  // System.out.println("Tổng số enemies sau spawn: " + enemies.size());
    }

    public void render(Graphics g) {
        long startTime = System.nanoTime();
        for (Bullet bullet : bullets) bullet.render(g);
        eggs.drawProjectiles(g);
       // // System.out.println("Số lượng enemies để render: " + enemies.size()); // Debug
        for (Enemy enemy : enemies) {
            if (enemy != null) enemy.render(g); // Kiểm tra null để tránh lỗi
        }
        
        for (DeathEffect deathEffect : deathEffects) {
            if (deathEffect != null) deathEffect.render(g);
        }
       
        int fps = gameLoop.getFPS();
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("FPS: " + fps, 50, 50);
      //  // System.out.println("Render time: " + (System.nanoTime() - startTime) / 1_000_000.0 + " ms");
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

    public void shoot() {
        bullets.add(new Bullet(playerController.getPosX() + 39, playerController.getPosY(), 50, 1.0, 0.4));
        soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/bulletHenSolo.wav").getPath());
         System.out.println("Bắn");
    }

    private void checkCollisions() {
        bullets.removeIf(bullet -> {
            for(Enemy enemy : enemies) {
                if(isColliding(bullet, enemy)) {
                    enemy.takeDamage(bullet.getDamage());
                    return true;
                }
            }
            return false;
        });
    }

    private boolean isColliding(Bullet bullet, Enemy enemy) {
        Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 9, 52);
        Rectangle enemyBounds = new Rectangle(enemy.getPosX(), enemy.getPosY(), enemy.getMODEL_WIDTH(), enemy.getMODEL_HEIGHT());
        return bulletBounds.intersects(enemyBounds);
    }
    
    private boolean isColliding2(PlayerController player, Enemy enemy) {
        Rectangle playerBounds = new Rectangle(player.getPosX(), player.getPosY(), 54, 50);
        Rectangle enemyBounds = new Rectangle(enemy.getPosX(), enemy.getPosY(), enemy.getMODEL_WIDTH(), enemy.getMODEL_HEIGHT());
        return playerBounds.intersects(enemyBounds);
    }
    
    private boolean isColliding3(PlayerController player, EnemyProjectiles egg) {
        Rectangle playerBounds = new Rectangle(player.getPosX(), player.getPosY(), 54, 50);
        Rectangle eggBounds = new Rectangle((int)egg.getPosX(), (int)egg.getPosY(), 5, 5);
        return playerBounds.intersects(eggBounds);
    }

}