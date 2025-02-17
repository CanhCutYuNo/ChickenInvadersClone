package application.Controllers;

import application.Models.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.*;

public class Manager {
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;

    public Manager() {
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        player = new Player(100, 10, 1.0, 400, 300);
    }

    public void update() {
        // Cập nhật đạn
    	 updateBullets();
        bullets.removeIf(bullet ->  bullet.isOffScreen(1080));

        // Cập nhật va chạm
        checkCollisions();
        checkBulletEnemyCollisions();
    }
    
    private void updateGame() {
       
        //updateenemies();
        
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
    
    public void spawnEnemies() {
        enemies = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            enemies.add(new Enemy(100, 1920, 1080));
        }
    }

    public void render(Graphics g) {
        for (Bullet bullet : bullets) bullet.render(g);
        player.render(g);
        for (Enemy enemy : enemies) enemy.render(g);
    }

    public void movePlayer(int x, int y) {
        player.setPosX(x - 32);
        player.setPosY(y - 32);
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
        Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 9, 57);
        Rectangle enemyBounds = new Rectangle(enemy.getPosX(), enemy.getPosY(), 64, 64);
        return bulletBounds.intersects(enemyBounds);
    }
}
