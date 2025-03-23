package application.controllers.levels;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import application.controllers.EnemyController;
import application.controllers.SoundController;
import application.models.Enemy;

public class Level5Manager {
    private EnemyController bossController;
    private List<Enemy> boss;
    private SoundController soundController;

    public Level5Manager(SoundController soundController) {
        this.soundController = soundController;
        this.bossController = new EnemyController(1, "Boss", 100, 0.0f, soundController);
        this.boss = new ArrayList<>();

        boss.addAll(bossController.getEnemies());
     //   System.out.println("Initialized " + boss.size() + " enemies in Level5Manager");
    }

    public void update(float deltaTime) {
        bossController.update(deltaTime);
    }

    public void render(Graphics g) {
        bossController.render(g);
    }

    public List<Enemy> getEnemies() {
        return boss;
    }

    public void removeEnemy(Enemy enemy) {
        if (bossController.getEnemies().contains(enemy)) {
            bossController.removeEnemy(enemy);
        }
        boss.remove(enemy);
    }
}