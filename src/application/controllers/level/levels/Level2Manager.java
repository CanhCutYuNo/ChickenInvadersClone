package application.controllers.level.levels;

import java.util.Random;

import application.controllers.enemy.EnemyController;
import application.controllers.level.LevelManager;
import application.controllers.util.SoundController;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;
import application.views.enemy.EnemyView;

public class Level2Manager extends LevelManager {
    private static final int SCREEN_WIDTH = 1920;
    private static final int NUM_ENEMIES = 20;
    private static final float GRAVITY = 2.0f;
    private static final float OSCILLATION_SPEED = 0.5f;
    private static final float OSCILLATION_AMPLITUDE = 1.0f;

    private Random rand;

    public Level2Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController);
        this.rand = new Random();
    }

    @Override
    protected void initEnemies() {
        if(rand == null) {
            rand = new Random();
        }
        for(int i = 0; i < NUM_ENEMIES; i++) {
            int posY = rand.nextInt(100) - 250;
            int posX = rand.nextInt(SCREEN_WIDTH - 200);
            Enemy model = new Enemy(64, 64, posX, posY, 0, Enemy.EnemyType.CHICKEN_ENEMY);
            model.setInitialIndex(i);
            model.getSkills().put(SkillType.EGG, "/asset/resources/gfx/introEgg.png");
            model.setState(new Enemy.EnemyState(i * 1.0f));
            EnemyView view = new EnemyView(model);
            enemyController.addEnemy(model, view);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateEnemies(deltaTime);
    }

    private void updateEnemies(float deltaTime) {
        for(Enemy enemy : enemyController.getEnemyModels()) {
            if(enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY && enemy.state != null) {
                enemy.state.timeElapsed += deltaTime;

                if(!enemy.state.isActive && enemy.state.timeElapsed >= enemy.state.timeDelay) {
                    enemy.state.isActive = true;
                }

                if(enemy.state.isActive) {
                    enemy.state.t += deltaTime * 100;
                    float posY = enemy.getPosY() + GRAVITY;
                    float offsetX = (float) Math.sin(posY * 0.005) * OSCILLATION_AMPLITUDE;
                    float posX = enemy.getPosX() + offsetX * deltaTime * OSCILLATION_SPEED;

                    enemy.setPosX((int) posX);
                    enemy.setPosY((int) posY);
                    enemy.setRotate((float) (20 * Math.sin(0.02 * enemy.state.t)));

                    // Tái sinh khi chạm đáy
                    if(enemy.getPosY() > 1000) {
                        enemy.setPosY(rand.nextInt(200) - 600); // Dùng rand
                        enemy.setPosX(rand.nextInt(1600) + 100);
                        enemy.state.timeElapsed = 0;
                        enemy.state.isActive = false;
                    }
                }
            }
        }
    }
}