package application.controllers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import application.models.Enemy;
import application.models.types.ChickEnemy;
import application.models.types.ChickenBoss;
import application.models.types.ChickenEnemy;
import application.models.types.EggShellEnemy;

public class EnemyController {
    protected List<Enemy> enemies;
    protected float timeDelay;
    protected float timeElapsed = 0f;
    protected boolean isActive = false;
    protected float t = 0;
    protected int startY;
    protected SoundController soundController;
    protected float rotate = 0f;
    protected static final int SPACING = 150;
    protected int direction = 1;
    protected static final int SCREEN_WIDTH = 1920;
    protected static final int SCREEN_LEFT = 0;
    protected static final int SCREEN_RIGHT = SCREEN_WIDTH;
    protected int enemyType;

    public static final int CHICKEN = 1;
    public static final int CHICK = 2;
    public static final int EGG_SHELL = 3;
    public static final int BOSS = 4;

    public EnemyController(int numEnemies, int enemyType, int startY, float timeDelay, SoundController soundController) {
        this.enemies = new ArrayList<>();
        this.startY = startY;
        this.timeDelay = timeDelay;
        this.enemyType = enemyType;
        this.soundController = soundController;
    }

    public Enemy createEnemy(int posX, int posY) {
        switch (enemyType) {
            case CHICKEN:
                return new ChickenEnemy(posX, posY, soundController);
            case CHICK:
                return new ChickEnemy(posX, posY, soundController);
            case EGG_SHELL:
                return new EggShellEnemy(posX, posY, soundController);
            case BOSS:
                // Đặt tọa độ cố định cho ChickenBoss(giữa màn hình)
                return new ChickenBoss(posX, posY, soundController);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + enemyType);
        }
    }

    public void update(float deltaTime) {

    }

    public void update3(float deltaTime){
        timeElapsed += deltaTime;
        if (!isActive && timeElapsed >= timeDelay) {
            isActive = true;
            System.out.println("Row at Y=" + startY + " is now active!");
        }

        if (isActive) {
            t += deltaTime * 100 * direction;
            // rotate = (float) (20 * Math.sin(0.05 * t));
//            Random random = new Random();
//            float gravity = 30.0f; // Tốc độ rơi
//            float oscillationSpeed = 0.5f; // Tốc độ lắc
//            float oscillationAmplitude = 1.0f; // Biên độ lắc
            for (Enemy enemy : enemies) {
                if (enemy instanceof EggShellEnemy) {
                    float posY = enemy.getPosY() + 2; // Gà rơi xuống
//                    float offsetX = (float) Math.sin(posY * 0.005) * oscillationAmplitude; // Lắc nhẹ
//                    float posX = enemy.getPosX() + offsetX * deltaTime * oscillationSpeed; // Lắc mượt hơn

                    enemy.setPosY((int) posY);

//                    enemy.setPosX((int) posX);
//                    ((EggShellEnemy) enemy).setRotate((float) (20 * Math.sin(0.05 * t))); // Xoay nhẹ
                }
            }
        }
    }

    public void render(Graphics g) {
        if(isActive) {
            for(Enemy enemy : enemies) {
                if(!enemy.isDead()) {
                    enemy.render(g);
                }
            }
         //   System.out.println("Rendering row at Y=" + startY + ", Active enemies: " + enemies.size());
        } else {
          //  System.out.println("Row at Y=" + startY + " not active yet");
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public boolean isActive() {
        return isActive;
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
     //   System.out.println("Removed enemy from EnemyController at (" + enemy.getPosX() + "," + enemy.getPosY() + "). New size: " + enemies.size());
    }
}