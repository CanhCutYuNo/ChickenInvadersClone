package application.Controllers;

import application.Models.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Manager {
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private CardLayout cardLayout;
	private JPanel mainPanel;
	private int frameDelay = 0;
	//private int frameCount = 0;

    public Manager(CardLayout _cardLayout, JPanel _mainPanel) {
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        player = new Player(100, 10, 1.0, 950, 540);
        this.cardLayout = _cardLayout;
		this.mainPanel = _mainPanel;
    }

    public void update() {
    	updateBullets();
        bullets.removeIf(bullet ->  bullet.isOffScreen(1080));
        player.update(); 
        if(frameDelay == 1) {
        	for(Enemy enemy : enemies) {
            	enemy.nextFrame();;
            }
        	 //frameCount++; // Tăng số đếm frame khi enemy đổi frame
        	frameDelay = 0;
        }
        frameDelay++;
        // Cập nhật va chạm
        checkCollisions();
        checkBulletEnemyCollisions();
        checkPlayerCollisions();
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
        enemies = new ArrayList<>();
        Image bodyImage = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-body-stripes.png")).getImage();
        Image wingsImage = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-wings.png")).getImage();
        Image headImage = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-face.png")).getImage();
        Image blinkImage = new ImageIcon(getClass().getResource("/asset/resources/gfx/chickenBlink.png")).getImage();
        for(int i = 0; i < 5; i++) {
            enemies.add(new Enemy(100, bodyImage, wingsImage, headImage, blinkImage));
        }
    }

    public void render(Graphics g) {
        for(Bullet bullet : bullets) bullet.render(g);
        player.render(g);
        for(Enemy enemy : enemies) enemy.render(g);
        
        int currentPlayerFrame = player.getCurFrame(); // Lấy frame của enemy đầu tiên

        // Vẽ giá trị currentFrame màu đỏ trên màn hình
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Current Frame: " + currentPlayerFrame, 50, 50);
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
