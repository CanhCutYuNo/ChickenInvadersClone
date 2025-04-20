package application.controllers.level.levels;

import java.util.Random;

import application.controllers.enemy.EnemyController;
import application.controllers.enemy.death.EnemyDeathListener;
import application.controllers.level.LevelManager;
import application.controllers.util.SoundController;
import application.models.enemy.Enemy;
import application.views.enemy.EnemyView;

public class Level4Manager extends LevelManager implements EnemyDeathListener {
    private static final int SCREEN_WIDTH = 1920;
    private static final int NUM_EGG_SHELLS = 10;
    private static final float EGG_SHELL_GRAVITY = 1.0f; 
    private static final float CHICK_GRAVITY = 2.0f;

    private Random random;

    public Level4Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController);
        this.random = new Random();
        enemyController.addDeathListener(this); // Đăng ký lắng nghe sự kiện
    }

    @Override
    protected void initEnemies() {
        if (random == null) {
            random = new Random();
        }
        for (int i = 0; i < NUM_EGG_SHELLS; i++) {
            int posY = random.nextInt(100) - 300;
            int posX = random.nextInt(SCREEN_WIDTH - 200);
            Enemy model = new Enemy(75, 97, posX, posY, 0, Enemy.EnemyType.EGG_SHELL_ENEMY);
            model.setInitialIndex(i);
            model.setState(new Enemy.EnemyState(i * 1.5f));
            EnemyView view = new EnemyView(model);
            enemyController.addEnemy(model, view);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateEggShells(deltaTime);
        updateChicks(deltaTime);
        // Không cần checkEnemyDeath() nữa, xử lý qua sự kiện
    }

    private void updateEggShells(float deltaTime) {
        for (Enemy enemy : enemyController.getEnemyModels()) {
            if (enemy.getType() == Enemy.EnemyType.EGG_SHELL_ENEMY && enemy.state != null) {
                enemy.state.timeElapsed += deltaTime;

                if (!enemy.state.isActive && enemy.state.timeElapsed >= enemy.state.timeDelay) {
                    enemy.state.isActive = true;
                }

                if (enemy.state.isActive) {
                    float posY = enemy.getPosY() + EGG_SHELL_GRAVITY;
                    enemy.setPosY((int) posY);

                    if (enemy.getPosY() > 1000) {
                        enemy.setPosX(random.nextInt(1000) + 20);
                        enemy.setPosY(random.nextInt(50) + 3);
                        enemy.state.timeElapsed = 0;
                        enemy.state.timeDelay = 1.0f + random.nextFloat(); //Chỉ chờ 1-2s
                        enemy.state.isActive = false;
                    }
                }
            }
        }
    }

    private void updateChicks(float deltaTime) {
        for (Enemy enemy : enemyController.getEnemyModels()) {
            if (enemy.getType() == Enemy.EnemyType.CHICK_ENEMY) {
                float posY = enemy.getPosY() + CHICK_GRAVITY;
                enemy.setPosY((int) posY);

                if (enemy.getPosY() > 1000) {
                    enemy.setPosX(random.nextInt(1000) + 20);
                    enemy.setPosY(random.nextInt(100)); 
                }
            }
        }
    }

    @Override
    public void onEnemyDeath(Enemy enemy, int index) {
        if (enemy.getType() == Enemy.EnemyType.EGG_SHELL_ENEMY) {
            System.out.println("EggShellEnemy " + index + " died, spawning ChickEnemy");
            int chickPosX = enemy.getPosX() + (enemy.getModelWidth() - 46) / 2;
            int chickPosY = enemy.getPosY() + (enemy.getModelHeight() - 54) / 2;
            Enemy chickModel = new Enemy(46, 54, chickPosX, chickPosY, 0, Enemy.EnemyType.CHICK_ENEMY);
            EnemyView chickView = new EnemyView(chickModel);
            enemyController.addEnemy(chickModel, chickView);
        }
    }
}