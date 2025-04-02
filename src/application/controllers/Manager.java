package application.controllers;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

import application.controllers.levels.*;
import application.models.Bullet;
import application.models.DeathEffect;
import application.models.Enemy;
import application.models.EnemySkills;
import application.models.Items;
import application.models.types.ChickEnemy;
import application.models.types.EggShellEnemy;
import application.views.BackgroundPanel;
import application.views.MenuPanel;
import application.views.PlayerView;

public class Manager {
    private PlayerView playerView;
    private PlayerController playerController;
    private ArrayList<Bullet> bullets;
    private List<Enemy> enemies;
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
    private int level = 1;
    private boolean playerExploded = false;
    // Thêm các biến để lưu trữ LevelXManager
    private Level1Manager level1Manager;
    private Level2Manager level2Manager;
    private Level3Manager level3Manager;
    private Level4Manager level4Manager;
    private Level5Manager level5Manager;

    public Manager(CardLayout _cardLayout, JPanel _mainPanel, BackgroundPanel _backgroundPanel, MenuPanel _menuPanel, GameLoop _gameLoop, SoundController _soundController) {
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        deathEffectController = new DeathEffectController();
        playerController = new PlayerController(0.5, null);
        playerView = new PlayerView(playerController);
        playerController.setPlayerView(playerView);

        items = new ItemsController("/asset/resources/gfx/flareSmall~1.png");
//        eggs = new EnemyProjectilesController("/asset/resources/gfx/introEgg.png");
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

    public void update(double deltaTime) {

        //   //   System.out.println("Manager update with deltaTime: " + deltaTime);
        if(playerView.isExploding()) {
            playerView.updateExplosion();
            if(52 < playerView.getExFrame()) {
                restartGame();
            }
            return;
        }

        updateBullets();
        bullets.removeIf(bullet -> bullet.isOffScreen(1080));
        if(frameDelay == 1) {
            frameDelay = 0;
        }
        frameDelay++;

//        if(level != 4){
//            updateEggs();
//        }


        playerController.update();

        items.updateItems();
        
        // Cập nhật LevelXManager thay vì updateEnemies() trực tiếp
        if(level == 1 && level1Manager != null) {
            level1Manager.update((float) deltaTime);
        }
        else if(level == 2 && level2Manager != null) {
            level2Manager.update((float) deltaTime);
        }
        else if(level == 3 && level3Manager != null) {
            level3Manager.update((float) deltaTime);
        } else if (level == 5 && level5Manager != null) {
            level5Manager.update((float) deltaTime);
        }
        else if(level == 4 && level4Manager != null){
            level4Manager.update((float) deltaTime);
        }
        deathEffectController.update();

        //checkCollisions();
        checkBulletEnemyCollisions();
        checkPlayerCollisionsWithEnemies();
        checkPlayerCollisionsWithEgg();
        checkPlayerCollisionsWithItems();


        if (getEnemies().isEmpty() && level1Manager != null) {
            level++;
            spawnEnemiesAfterFade(); // Gọi lại để tạo kẻ địch mới cho level tiếp theo
            System.out.println("Level Up! Chuyển sang Level " + level);
        }
        if (getEnemies().isEmpty() && level2Manager != null) {
            level++;
            spawnEnemiesAfterFade(); // Gọi lại để tạo kẻ địch mới cho level tiếp theo
            System.out.println("Level Up! Chuyển sang Level " + level);
        }
        if (getEnemies().isEmpty() && level3Manager != null) {
            level++;
            spawnEnemiesAfterFade(); // Gọi lại để tạo kẻ địch mới cho level tiếp theo
            System.out.println("Level Up! Chuyển sang Level " + level);
        }
        if(getEnemies().isEmpty() && level4Manager != null){
            level++;
            System.out.println("Level up! Chuyển sang level " + level);
        }

    }



    private void restartGame() {
        enemies.clear();
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
        while (iterator.hasNext()) {
            Items item = iterator.next();

            // Kiểm tra va chạm với người chơi
            if (isColliding4(playerController,item)) {
                System.out.println("Player picked up an item!");

                // Gọi hàm xử lý khi nhặt item (tăng máu, đạn, điểm...)
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
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            //   System.out.println("Checking bullet at (" + bullet.getX() + "," + bullet.getY() + ") with damage: " + bullet.getDamage());

            Iterator<Enemy> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                //   System.out.println("Checking enemy at (" + enemy.getPosX() + "," + enemy.getPosY() + ") with HP: " + enemy.getHp());

                if (isColliding(bullet, enemy)) {
                    //   System.out.println("Collision detected! Enemy HP before: " + enemy.getHp());
                    enemy.takeDamage(bullet.getDamage());
                    //   System.out.println("Enemy HP after: " + enemy.getHp() + ", Is Dead: " + enemy.isDead());

                    bulletsToRemove.add(bullet);
                    if (enemy.isDead()) {
                        if(enemy instanceof EggShellEnemy){
                            ChickEnemy chickEnemy = new ChickEnemy(enemy.getPosX(), enemy.getPosY(), soundController);
                            enemiesToAdd.add(chickEnemy);
                            enemiesToRemove.add(enemy);
                        }
                        else{
                            DeathEffect tempDeathEffect = enemy.getDeathEffect();
                            if(tempDeathEffect != null){
                                deathEffectController.add(tempDeathEffect);
                            }
                            enemiesToRemove.add(enemy);
                            //   System.out.println("Enemy marked for removal at (" + enemy.getPosX() + "," + enemy.getPosY() + ")");
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

        // Xóa và thông báo cho Level1Manager
        int bulletsRemoved = bulletsToRemove.size();
        int enemiesRemoved = enemiesToRemove.size();
        bullets.removeAll(bulletsToRemove);
        for (Enemy enemy : enemiesToRemove) {
            if (level == 1 && level1Manager != null) {
                level1Manager.removeEnemy(enemy);
            }
            else if (level== 2 && level2Manager != null){
                level2Manager.removeEnemy(enemy);
            }
//            else if (level == 5 && level5Manager != null) {
//                level5Manager.removeEnemy(enemy);
//            }
            else if (level == 3 && level3Manager != null) {
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
        for (Enemy enemy : enemies) {
            Iterator<EnemySkills> eggIterator = enemy.getSkillsController().getSkills().iterator();
            while (eggIterator.hasNext()) {
                EnemySkills egg = eggIterator.next();
                if (isColliding3(playerController, egg)) {
                    playerController.isDamaged(egg.getDamage());
                    if (playerController.getHP() <= 0) {
                        playerController.getPlayerView().startExplosion();
                        soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/explosionPlayer.wav").getPath());
                        playerExploded = true;
                    }
                    eggIterator.remove();
                }
            }
        }
    }



    public void spawnEnemiesAfterFade() {

        System.out.println("Spawning enemies for level: " + level + ". Current enemies size before: " + enemies.size());
        enemies.clear();
        if (level == 1) {
            level1Manager = new Level1Manager(soundController);
            enemies.addAll(level1Manager.getEnemies());
        }
        else if(level == 2){
            level2Manager = new Level2Manager(soundController);
            enemies.addAll(level2Manager.getEnemies());
        } else if (level == 5) {
            level5Manager = new Level5Manager(soundController);
            enemies.addAll(level5Manager.getEnemies());
        } else if (level == 3) {
            level3Manager = new Level3Manager(soundController);
            enemies.addAll(level3Manager.getEnemies());
        }
        else if(level == 4){
            level4Manager = new Level4Manager(soundController);
            enemies.addAll(level4Manager.getEnemies());
        }
//        else {
//            System.err.println("Level " + level + " không được hỗ trợ!");
//        }
        System.out.println("Spawned enemies. New enemies size: " + enemies.size());
    }

    public void render(Graphics g) {
        long startTime = System.nanoTime();
        for (Bullet bullet : bullets) bullet.render(g);

        // Render thông qua LevelXManager thay vì render trực tiếp
        if(level == 1 && level1Manager != null) {
            level1Manager.render(g);
        }
        else if(level == 2 && level2Manager != null){
            level2Manager.render(g);
        }
//        else if(level == 5 && level5Manager != null) {
//            level5Manager.render(g);
//        }
        else if(level == 3 && level3Manager != null) {
            level3Manager.render(g);
        }
        else if(level == 4 && level4Manager != null){
            level4Manager.render(g);
        }

        deathEffectController.render(g);

        items.drawItems(g);

        int fps = gameLoop.getFPS();
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("FPS: " + fps, 50, 50);
        //   //   System.out.println("Render time: " +(System.nanoTime() - startTime) / 1_000_000.0 + " ms");
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
        Rectangle enemyBounds = new Rectangle(enemy.getPosX(), enemy.getPosY(), enemy.getMODEL_WIDTH(), enemy.getMODEL_HEIGHT());
       // //   System.out.println("Checking collision: Bullet (" + bullet.getX() + "," + bullet.getY() + ") vs Enemy (" + enemy.getPosX() + "," + enemy.getPosY() + ")");
        return bulletBounds.intersects(enemyBounds);
    }
    
    private boolean isColliding2(PlayerController player, Enemy enemy) {
        Rectangle playerBounds = new Rectangle(player.getPosX(), player.getPosY(), 54, 50);
        Rectangle enemyBounds = new Rectangle(enemy.getPosX(), enemy.getPosY(), enemy.getMODEL_WIDTH(), enemy.getMODEL_HEIGHT());
        return playerBounds.intersects(enemyBounds);
    }
    
    private boolean isColliding3(PlayerController player, EnemySkills egg) {
        Rectangle playerBounds = new Rectangle(player.getPosX(), player.getPosY(), 54, 50);
        Rectangle eggBounds = new Rectangle((int) egg.getPosX(), (int) egg.getPosY(), 5, 5);
        return playerBounds.intersects(eggBounds);
    }

    private boolean isColliding4(PlayerController player, Items item){
        Rectangle playerBounds = new Rectangle(player.getPosX(), player.getPosY(), 54, 50);
        Rectangle itemBounds = new Rectangle(item.getPosX(), item.getPosY(), 4,4);
        return playerBounds.intersects(itemBounds);
    }
}