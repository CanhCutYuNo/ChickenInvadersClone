package application.controllers.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.controllers.EnemyController;
import application.controllers.LevelManager;
import application.controllers.SoundController;
import application.models.Enemy;
import application.views.EnemyView;

public class Level4Manager extends LevelManager {
    private static final int SCREEN_WIDTH = 1920;
    private static final int NUM_EGG_SHELLS = 10;
    private static final float EGG_SHELL_GRAVITY = 1.0f; // Tốc độ rơi cho EggShellEnemy
    private static final float CHICK_GRAVITY = 2.0f; // Tốc độ rơi cho ChickEnemy

    private Random random;
    private List<EnemyState> eggShellStates;

    private class EnemyState {
        float timeElapsed;
        boolean isActive;
        float timeDelay;

        EnemyState(float timeDelay) {
            this.timeElapsed = 0;
            this.isActive = false;
            this.timeDelay = timeDelay;
        }
    }

    public Level4Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController);
        this.random = new Random();
        this.eggShellStates = new ArrayList<>();
        for (int i = 0; i < NUM_EGG_SHELLS; i++) {
            eggShellStates.add(new EnemyState(i * 2.0f));
        }
    }

    @Override
    protected void initEnemies() {
        for (int i = 0; i < NUM_EGG_SHELLS; i++) {
            int posY = random.nextInt(100) - 50; // Từ -50 đến 50
            int posX = random.nextInt(SCREEN_WIDTH - 200); // Từ 0 đến 1720
            Enemy model = new Enemy(0, 75, 97, posX, posY, 0, Enemy.EnemyType.EGG_SHELL_ENEMY);
            model.setInitialIndex(i);
            EnemyView view = new EnemyView(model);
            enemyController.addEnemy(model, view);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateEggShells(deltaTime);
        updateChicks(deltaTime);
        checkEnemyDeath();
    }

    private void updateEggShells(float deltaTime) {
        for (int i = 0; i < eggShellStates.size(); i++) {
            EnemyState state = eggShellStates.get(i);
            Enemy enemy = enemyController.getEnemyModels().get(i);
            state.timeElapsed += deltaTime;

            if (!state.isActive && state.timeElapsed >= state.timeDelay) {
                state.isActive = true;
            }

            if (state.isActive && enemy.getType() == Enemy.EnemyType.EGG_SHELL_ENEMY) {
                float posY = enemy.getPosY() + EGG_SHELL_GRAVITY; // Rơi xuống
                enemy.setPosY((int) posY);

                if (enemy.getPosY() > 1000) {
                    enemy.setPosX(random.nextInt(1000) + 20); // Từ 20 đến 1020
                    enemy.setPosY(random.nextInt(50) + 3); // Từ 3 đến 53
                }
            }
        }
    }

    private void updateChicks(float deltaTime) {
        for (int i = 0; i < enemyController.getEnemyModels().size(); i++) {
            Enemy enemy = enemyController.getEnemyModels().get(i);
            if (enemy.getType() == Enemy.EnemyType.CHICK_ENEMY) {
                float posY = enemy.getPosY() + CHICK_GRAVITY; // Rơi xuống
                enemy.setPosY((int) posY);

                if (enemy.getPosY() > 1000) {
                    enemy.setPosX(random.nextInt(1000) + 20); // Từ 20 đến 1020
                    enemy.setPosY(random.nextInt(100)); // Từ 0 đến 100
                }
            }
        }
    }

    @Override
    public void checkEnemyDeath() {
        for (int i = enemyController.getEnemyModels().size() - 1; i >= 0; i--) {
            Enemy enemy = enemyController.getEnemyModels().get(i);
            if (enemy.isDead() && enemy.getType() == Enemy.EnemyType.EGG_SHELL_ENEMY) {
                // Tạo ChickEnemy tại vị trí trung tâm của EggShellEnemy
                int chickPosX = enemy.getPosX() + (enemy.getModelWidth() - 46) / 2; // 46 là chiều rộng ChickEnemy
                int chickPosY = enemy.getPosY() + (enemy.getModelHeight() - 54) / 2; // 54 là chiều cao ChickEnemy
                Enemy chickModel = new Enemy(0, 46, 54, chickPosX, chickPosY, 0, Enemy.EnemyType.CHICK_ENEMY);
                EnemyView chickView = new EnemyView(chickModel);
                enemyController.addEnemy(chickModel, chickView);
                enemyController.removeEnemy(i);
                if (i < eggShellStates.size()) {
                    eggShellStates.remove(i); // Xóa trạng thái tương ứng
                }
            }
        }
    }
}