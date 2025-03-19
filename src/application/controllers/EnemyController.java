package application.controllers;

import application.models.types.ChickenEnemy;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class EnemyController {
    private List<ChickenEnemy> enemies;
    private float timeDelay;
    private float timeElapsed = 0f;
    private boolean isActive = false;
    private float t = 0;
    private int startY;
    private SoundController soundController;
    private float rotate = 0f;
    private static final int SPACING = 150;
    private int direction = 1;
    private static final int SCREEN_WIDTH = 1880;
    private static final int SCREEN_LEFT = 0;
    private static final int SCREEN_RIGHT = SCREEN_WIDTH;

    public EnemyController(int numEnemies, int startY, float timeDelay, SoundController soundController) {
        this.enemies = new ArrayList<>();
        this.startY = startY;
        this.timeDelay = timeDelay;
        this.soundController = soundController;

        // Khởi tạo các con gà và gán initialIndex
        for(int i = 0; i < numEnemies; i++) {
            ChickenEnemy enemy = new ChickenEnemy(-50 - i * SPACING, startY, soundController);
            enemy.setInitialIndex(i); // Lưu chỉ số ban đầu
            enemies.add(enemy);
        }
    }

    public void update(float deltaTime) {
        timeElapsed += deltaTime;

        if(!isActive && timeElapsed >= timeDelay) {
            isActive = true;
            // System.out.println("Row at Y=" + startY + " is now active!");
        }

        if(isActive) {
            t += deltaTime * 100 * direction;

           // rotate =(float)(20 * Math.sin(0.05 * t));

            // Cập nhật vị trí dựa trên initialIndex
            for(ChickenEnemy enemy : enemies) {
                int index = enemy.getInitialIndex(); // Dùng initialIndex thay vì i
                float posX = -1800 + t + index * SPACING;
                float posY = startY + 10 *(float) Math.sin(0.02 * posX);

                enemy.setPosX((int) posX);
                enemy.setPosY((int) posY);
                enemy.setRotate(rotate);

                // System.out.println("Enemy at(" + posX + ", " + posY + "), Initial Index: " + index + ", Rotate: " + rotate);
            }

            // Kiểm tra biên
            if(!enemies.isEmpty()) {
                ChickenEnemy firstEnemy = enemies.get(0);
                ChickenEnemy lastEnemy = enemies.get(enemies.size() - 1);

                if(lastEnemy.getPosX() > SCREEN_RIGHT && direction == 1) {
                    direction = -1;
                    t -= 2 *(lastEnemy.getPosX() - SCREEN_RIGHT);
                    // System.out.println("Row at Y=" + startY + " turning left at right edge");
                } else if(firstEnemy.getPosX() < SCREEN_LEFT && direction == -1) {
                    direction = 1;
                    t += 2 *(SCREEN_LEFT - firstEnemy.getPosX());
                    // System.out.println("Row at Y=" + startY + " turning right at left edge");
                }
            }
        }

        enemies.forEach(ChickenEnemy::nextFrame);
    }
    
    public void update1(float deltaTime) {
    	
    }

    public void render(Graphics g) {
        if(isActive) {
            for(ChickenEnemy enemy : enemies) {
                if(!enemy.isDead()) {
                    enemy.render(g);
                }
            }
            // System.out.println("Rendering row at Y=" + startY + ", Active enemies: " + enemies.size());
        } else {
            // System.out.println("Row at Y=" + startY + " not active yet");
        }
    }

    public List<ChickenEnemy> getEnemies() {
        return enemies;
    }

    public boolean isActive() {
        return isActive;
    }

    public void removeEnemy(ChickenEnemy enemy) {
        enemies.remove(enemy);
        // System.out.println("Removed enemy from EnemyController at(" + enemy.getPosX() + "," + enemy.getPosY() + "). New size: " + enemies.size());
    }
}