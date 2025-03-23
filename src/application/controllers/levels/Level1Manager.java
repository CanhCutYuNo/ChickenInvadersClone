package application.controllers.levels;

import application.controllers.EnemyController;
import application.models.Enemy;
import application.models.types.ChickenEnemy;
import application.controllers.SoundController;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Level1Manager {
    private List<EnemyController> enemyControllers;
    private List<Enemy> enemies;
    private SoundController soundController;

    public Level1Manager(SoundController soundController) {
        this.soundController = soundController;
        this.enemyControllers = new ArrayList<>();
        this.enemies = new ArrayList<>();

        enemyControllers.add(new EnemyController(10, "Chicken", 100, 0.0f, soundController));
        enemyControllers.add(new EnemyController(10, "Chicken", 300, 0.0f, soundController));
        enemyControllers.add(new EnemyController(10, "Chicken", 500, 0.0f, soundController));

        for(EnemyController controller : enemyControllers) {
            enemies.addAll(controller.getEnemies());
        }
        // System.out.println("Initialized " + enemies.size() + " enemies in Level1Manager");
    }

    public void update(float deltaTime) {
        for(EnemyController controller : enemyControllers) {
            controller.update(deltaTime);
        }
    }

    public void render(Graphics g) {
        for(EnemyController controller : enemyControllers) {
            controller.render(g);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    // Thêm phương thức để xóa Enemy khỏi các EnemyController
    public void removeEnemy(Enemy enemy) {
        for(EnemyController controller : enemyControllers) {
            if(controller.getEnemies().contains(enemy)) {
                controller.removeEnemy((ChickenEnemy) enemy);
                break;
            }
        }
        enemies.remove(enemy);
    }
}