package application.controllers.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.controllers.EnemyController;
import application.controllers.LevelManager;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;
import application.views.EnemyView;

public class Level2Manager extends LevelManager {
    private static final int SCREEN_WIDTH = 1920;
    private static final int NUM_ENEMIES = 20;
    private static final float GRAVITY = 2.0f; // Tốc độ rơi (giảm từ 30.0f xuống 2.0f để khớp với mã gốc)
    private static final float OSCILLATION_SPEED = 0.5f; // Tốc độ lắc
    private static final float OSCILLATION_AMPLITUDE = 1.0f; // Biên độ lắc

    private Random random;
    private List<EnemyState> enemyStates;

    private class EnemyState {
        float timeElapsed;
        boolean isActive;
        float timeDelay;
        float t; // Biến thời gian cho xoay sprite

        EnemyState(float timeDelay) {
            this.timeElapsed = 0;
            this.isActive = false;
            this.timeDelay = timeDelay;
            this.t = 0;
        }
    }

    public Level2Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController);
        this.random = new Random();
        this.enemyStates = new ArrayList<>();
        for (int i = 0; i < NUM_ENEMIES; i++) {
            enemyStates.add(new EnemyState(i * 1.0f));
        }
    }

    @Override
    protected void initEnemies() {
        for (int i = 0; i < NUM_ENEMIES; i++) {
            int posY = random.nextInt(100) - 100; // Từ -100 đến 0
            int posX = random.nextInt(SCREEN_WIDTH - 200); // Từ 0 đến 1720
            Enemy model = new Enemy(0, 64, 64, posX, posY, 0, Enemy.EnemyType.CHICKEN_ENEMY);
            model.setInitialIndex(i);
            model.getSkills().put(SkillType.EGG, "/asset/resources/gfx/introEgg.png");
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
        for (int i = 0; i < enemyStates.size(); i++) {
            EnemyState state = enemyStates.get(i);
            Enemy enemy = enemyController.getEnemyModels().get(i);
            state.timeElapsed += deltaTime;

            if (!state.isActive && state.timeElapsed >= state.timeDelay) {
                state.isActive = true;
            }

            if (state.isActive) {
                state.t += deltaTime * 100; // Tương ứng với t += deltaTime * 100 trong mã gốc
                float posY = enemy.getPosY() + GRAVITY; // Rơi xuống
                float offsetX = (float) Math.sin(posY * 0.005) * OSCILLATION_AMPLITUDE; // Lắc nhẹ
                float posX = enemy.getPosX() + offsetX * deltaTime * OSCILLATION_SPEED; // Cập nhật X

                enemy.setPosX((int) posX);
                enemy.setPosY((int) posY);
                enemy.setRotate((float) (20 * Math.sin(0.02 * state.t))); // Xoay sprite

                // Tái sinh khi chạm đáy
                if (enemy.getPosY() > 1000) {
                    enemy.setPosY(random.nextInt(200) - 300); // Từ -300 đến -100
                    enemy.setPosX(random.nextInt(1600) + 100); // Từ 100 đến 1700
                }
            }
        }
    }
}