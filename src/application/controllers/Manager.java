package application.controllers;

import application.controllers.levels.*;
import application.models.*;
import application.views.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Manager {
//	private PlayerModel playerModel;
	private PlayerView playerView;
	private PlayerController playerController;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private EnemyProjectilesController eggs;
    private static BackgroundPanel backgroundPanel;
    private static MenuPanel menuPanel; 
    private static GamePanel gamePanel; 
    private CardLayout cardLayout;
    private SoundController soundController;
    private JPanel mainPanel;
    private GameLoop gameLoop;
    private int frameDelay = 0;
    private int level = 1;
    private boolean playerExploded = false;



    public Manager(CardLayout _cardLayout, JPanel _mainPanel, BackgroundPanel _backgroundPanel, MenuPanel _menuPanel, GamePanel _gamePanel, GameLoop _gameLoop, SoundController _soundController) {
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
//      playerModel = new PlayerModel(100, 10, 1.0, 950, 540);
        playerController = new PlayerController(1.0, 950, 540, null);
        playerView = new PlayerView(playerController);
        playerController.setPlayerView(playerView);

        eggs = new EnemyProjectilesController("/asset/resources/gfx/introEgg.png");
        this.cardLayout = _cardLayout;

        this.mainPanel = _mainPanel;
        Manager.backgroundPanel = _backgroundPanel;
        Manager.menuPanel = _menuPanel;
        Manager.gamePanel = _gamePanel;
        this.gameLoop = _gameLoop;
        this.soundController = _soundController;
    }

    public void setBackgroundPanel(BackgroundPanel _backgroundPanel) {
        Manager.backgroundPanel = _backgroundPanel;
    }
    
    public void setMenuPanel(MenuPanel _menuPanel) {
        Manager.menuPanel = _menuPanel;
    }
    
    public void setGamePanel(GamePanel _gamePanel) {
        Manager.gamePanel = _gamePanel;
    }
    
    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }
    
    public Point getPlayerPosition() {
        return playerController.getPosition();
    }

    
    public void update(double deltaTime) {
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
        
        updateEggs();
        
        playerController.update();
        
        for(Enemy enemy : enemies) {
            enemy.nextFrame();
            enemy.update();
        }
      
        checkCollisions();
        checkBulletEnemyCollisions();
        checkPlayerCollisionsWithEnemies();
        checkPlayerCollisionsWithEgg();

        if(enemies.isEmpty()) {
            level++;
            System.out.println("New level !!");
            spawnEnemies();
        }
    }

    private void restartGame() {
    	
        enemies.clear();
        playerController.setPosX(800);
        playerController.setPosY(950);
        bullets.clear();
        eggs.clear();
        spawnEnemies();

        cardLayout.show(mainPanel, "Menu");        

        menuPanel.setBackgroundPanel(backgroundPanel);
        soundController.switchTrack(getClass().getResource("/asset/resources/sfx/CI4Theme.wav").getPath());

       
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

                    if(enemy.getHp() <= 0) {
                        enemyIterator.remove();
                    }
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
                if(playerController.getHP()<=0) {

                    playerController.getPlayerView().startExplosion();
                    playerExploded = true;
                }
                // Xóa viên đạn sau khi va chạm
                eggIterator.remove();
            }
        }
    }

    
    public void spawnEnemies() {
        enemies = new ArrayList<>();
        if(level == 1){
//          Random random = new Random()
        	//TransitionManager.applyFadeTransition(gamePanel, "/asset/resources/gfx/lv1.png", 2000);
            enemies = new Level1Manager().getEnemies();
        }
        else if(level == 2){
            enemies = new Level2Manager().getEnemies();
        }
        else if(level ==3){
            enemies = new Level3Manager().getEnemies();
//
        }
    }

    public void render(Graphics g) {
        for(Bullet bullet : bullets) bullet.render(g);
        eggs.drawProjectiles(g);
        for(Enemy enemy : enemies) enemy.render(g);
        if(playerView.isExploding()) {
            playerView.explosionRender(g); // Vẽ hiệu ứng nổ
        } else {
            playerView.render(g); // Vẽ player nếu không đang nổ
        }
        
        int fps = gameLoop.getFPS();

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("FPS: " + fps, 50, 50);
    }

    public void movePlayer(int x, int y) {
    	playerController.setPosX(x - 35);
        playerController.setPosY(y - 32);
        playerController.updateDirection(x);
        playerController.setLastMoveTime((System.currentTimeMillis()));
    }

    public void shoot() {
        bullets.add(new Bullet(playerController.getPosX() + 39, playerController.getPosY(), 50, 1.0, 0.4));
    }

    private void checkCollisions() {
        bullets.removeIf(bullet -> {
            for(Enemy enemy : enemies) {
                if(isColliding(bullet, enemy)) {
                    enemy.takeDamage(bullet.getDamage());
                    if(enemy.getHp() <= 0) enemies.remove(enemy);
                    return true;
                }
            }
            return false;
        });
    }

    private boolean isColliding(Bullet bullet, Enemy enemy) {
        Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 9, 52);
        Rectangle enemyBounds = new Rectangle(enemy.getPosX(), enemy.getPosY(), 54, 50);
        return bulletBounds.intersects(enemyBounds);
    }
    
    private boolean isColliding2(PlayerController player, Enemy enemy) {
        Rectangle playerBounds = new Rectangle(player.getPosX(), player.getPosY(), 54, 50);
        Rectangle enemyBounds = new Rectangle(enemy.getPosX(), enemy.getPosY(), 54, 50);
        return playerBounds.intersects(enemyBounds);
    }
    
    private boolean isColliding3(PlayerController player, EnemyProjectiles egg) {
        Rectangle playerBounds = new Rectangle(player.getPosX(), player.getPosY(), 54, 50);
        Rectangle eggBounds = new Rectangle((int)egg.getPosX(),(int)egg.getPosY(), 5, 5);
        return playerBounds.intersects(eggBounds);
    }
}
