package application.Controllers;

import application.Models.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Manager {
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private CardLayout cardLayout;
	private JPanel mainPanel;
    private int level = 1;

    public Manager(CardLayout _cardLayout, JPanel _mainPanel) {
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        player = new Player(100, 10, 1.0, 950, 540);
        this.cardLayout = _cardLayout;
		this.mainPanel = _mainPanel;
    }

    public void update() {
        // Cập nhật đạn
    	updateBullets();
        bullets.removeIf(bullet ->  bullet.isOffScreen(1080));
        player.update(); 

        // Cập nhật va chạm
        checkCollisions();
        checkBulletEnemyCollisions();
        checkPlayerCollisions();

        //Cap nhat ga bay
        for (Enemy enemy : enemies) {
            enemy.update(level); // Cập nhật vị trí kẻ địch
        }

        //Cap nhat con Ga khong?
        if(enemies.isEmpty()){
            level++;
            System.out.println("New level !!");
            spawnEnemies();

        }


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
    
    private void checkPlayerCollisions() {
    	Iterator<Enemy> enemyIterator = enemies.iterator();
    	 while(enemyIterator.hasNext()) {
             Enemy enemy = enemyIterator.next();

             if(isColliding2(player, enemy)) {
                	 JOptionPane.showMessageDialog(null, "Game Over! You lost.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                	 enemies.clear();
                	 player.setPosX(800);
                	 player.setPosY(950);
                	 bullets.clear();
                	 spawnEnemies();
                	 cardLayout.show(mainPanel, "Menu");
                     break;
                 }
             }
    }
    
    public void spawnEnemies() {

        if(level == 1){
            enemies = new ArrayList<>();
            int nums = 10;
            int spacing = 120;
            int startX = 100;
            int posY = 50;
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < nums; j++){
                    int posX = startX + j * spacing;
                    enemies.add(new Enemy(100,posX, posY, 1));
                }
                posY+=100;
            }
        }
        else if(level == 2){
            enemies = new ArrayList<>();
            int nums = 10;
            int centerX = 1560 / 2;
            int centerY = 1080 / 4;
            for(int i = 0; i < nums; i++){
                double angle = 2 * Math.PI * i / nums;
                int posX = centerX + (int) (100 * Math.cos(angle));
                int posY = centerY + (int) (100 * Math.sin(angle));
                enemies.add(new Enemy(100,posX,posY,2));
            }
        }


    }

    public void render(Graphics g) {
        for(Bullet bullet : bullets) bullet.render(g);
        player.render(g);
        for(Enemy enemy : enemies) enemy.render(g);
    }

    public void movePlayer(int x, int y) {
    	player.setPosX(x - 32);
        player.setPosY(y - 32);
        player.updateDirection(x);
    }

    public void shoot() {
        bullets.add(new Bullet(player.getPosX() + 27, player.getPosY(), 50, 5.0, 1.0));
    }

    private void checkCollisions() {
        bullets.removeIf(bullet -> {
            for (Enemy enemy : enemies) {
                if (isColliding(bullet, enemy)) {
                    enemy.takeDamage(bullet.getDamage());
                    if (enemy.getHp() <= 0) enemies.remove(enemy);
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
    
    private boolean isColliding2(Player player, Enemy enemy) {
        Rectangle playerBounds = new Rectangle(player.getPosX(), player.getPosY(), 54, 50);
        Rectangle enemyBounds = new Rectangle(enemy.getPosX(), enemy.getPosY(), 54, 50);
        return playerBounds.intersects(enemyBounds);
    }
}
