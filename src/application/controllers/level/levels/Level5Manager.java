package application.controllers.level.levels;

import application.controllers.enemy.EnemyController;
import application.controllers.level.LevelManager;
import application.controllers.util.SoundController;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;
import application.views.enemy.EnemyView;

public class Level5Manager extends LevelManager {
    private static final int SCREEN_WIDTH = 1920;

    public Level5Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController);
    }

    @Override
    protected void initEnemies() {
        Enemy model = new Enemy(450, 600, SCREEN_WIDTH / 2 - 250, 3000, 0, Enemy.EnemyType.CHICKEN_BOSS);
        model.getSkills().put(SkillType.FIREBALL, "/asset/resources/gfx/fireball.png");
        model.getSkills().put(SkillType.HOLE, "/asset/resources/gfx/hole.png");
        EnemyView view = new EnemyView(model);
        enemyController.addEnemy(model, view);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (int i = 0; i < enemyController.getEnemyModels().size(); i++) {
            Enemy enemy = enemyController.getEnemyModels().get(i);
            if (enemy.getType() == Enemy.EnemyType.CHICKEN_BOSS) {
                enemyController.createHoleSkill(i, enemyController.getSkillsManager());
                enemyController.createFireballBurst(i, enemyController.getSkillsManager());
            }
        }
    }
}