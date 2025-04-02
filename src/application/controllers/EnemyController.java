package application.controllers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.models.Enemy;
import application.models.types.ChickEnemy;
import application.models.types.ChickenBoss;
import application.models.types.ChickenEnemy;

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
    protected String enemyType;
    protected int level;

    public EnemyController(int numEnemies, String enemyType, int startY, float timeDelay, SoundController soundController, int level ) {
        this.enemies = new ArrayList<>();
        this.startY = startY;
        this.timeDelay = timeDelay;
        this.enemyType = enemyType;
        this.soundController = soundController;
        this.level = level;

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