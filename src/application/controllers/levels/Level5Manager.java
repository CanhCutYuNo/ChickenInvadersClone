package application.controllers.levels;

import application.controllers.EnemyController;
import application.controllers.LevelManager;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;
import application.views.EnemyView;

public class Level5Manager extends LevelManager {
    private static final int SCREEN_WIDTH = 1920;

    public Level5Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController);
    }

    @Override
    protected void initEnemies() {
        // Tạo một ChickenBoss tại vị trí trung tâm
        Enemy model = new Enemy(0, 250, 250, SCREEN_WIDTH / 2 - 250, 100, 0, Enemy.EnemyType.CHICKEN_BOSS);
        model.getSkills().put(SkillType.FIREBALL, "/asset/resources/gfx/fireball.png");
        EnemyView view = new EnemyView(model);
        enemyController.addEnemy(model, view);
    }
}