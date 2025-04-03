package application.controllers;

import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;

import application.models.Enemy;

public abstract class LevelManager {

    protected final List<EnemyController> enemyControllers = new ArrayList<>();
    protected final List<Enemy> enemies;
    protected SoundController soundController;

    public LevelManager(SoundController soundController, List<Enemy> enemies) {
        this.soundController = soundController;
        this.enemies = enemies;
    }

    public final List<Enemy> getEnemies() {
        return enemies;
    }

    public final void update(float deltaTime) {
        for (EnemyController controller : enemyControllers) {
            controller.update(deltaTime);
        }
    }

    public final void render(Graphics g) {
        for (EnemyController controller : enemyControllers) {
            controller.render(g);
        }
    }    

    public final void removeEnemy(Enemy enemy) {
        for (EnemyController controller : enemyControllers) {
            if (controller.getEnemies().contains(enemy)) {
                controller.removeEnemy(enemy);
                break;
            }
        }
        enemies.remove(enemy);
    }
}