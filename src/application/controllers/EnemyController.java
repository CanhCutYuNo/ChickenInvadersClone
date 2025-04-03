package application.controllers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        // System.out.println("Creating enemy of type: " + enemyType + " at(" + posX +
        // "," + posY + ")");
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

    public void render(Graphics g) {
        if(isActive) {
            for(Enemy enemy : enemies) {
                if(!enemy.isDead()) {
                    enemy.render(g);
                }
            }
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
    }
}