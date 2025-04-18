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

public class Level3Manager extends LevelManager {
    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_LEFT = -50;
    private static final int SCREEN_RIGHT = 1920;
    private static final int SPACING = 100;
    private static final int NUM_CHICKEN_ROWS = 4;
    private static final int NUM_CHICKENS_PER_ROW = 5;
    private static final int NUM_CHICKS = 20; // Giả định 20 con từ mã gốc (10 controller x 2)
    private static final float GRAVITY = 2.0f; // Tốc độ rơi cho ChickEnemy
    private static final float OSCILLATION_SPEED = 0.5f; // Tốc độ lắc
    private static final float OSCILLATION_AMPLITUDE = 1.0f; // Biên độ lắc

    private Random random;
    private List<ChickenRowState> chickenRowStates;
    private List<ChickState> chickStates;

    private class ChickenRowState {
        float t;
        int direction;
        float timeElapsed;
        boolean isActive;
        int startY;
        float timeDelay;

        ChickenRowState(int startY, float timeDelay) {
            this.t = 0;
            this.direction = 1;
            this.timeElapsed = 0;
            this.isActive = false;
            this.startY = startY;
            this.timeDelay = timeDelay;
        }
    }

    private class ChickState {
        float timeElapsed;
        boolean isActive;
        float timeDelay;
        float t; // Biến thời gian cho xoay sprite

        ChickState(float timeDelay) {
            this.timeElapsed = 0;
            this.isActive = false;
            this.timeDelay = timeDelay;
            this.t = 0;
        }
    }

    public Level3Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController);
        this.random = new Random();
        this.chickenRowStates = new ArrayList<>();
        this.chickStates = new ArrayList<>();
        for (int i = 0; i < NUM_CHICKEN_ROWS; i++) {
            chickenRowStates.add(new ChickenRowState(100 * i + 100, i * 20.0f));
        }
        for (int i = 0; i < NUM_CHICKS; i++) {
            chickStates.add(new ChickState(i * 2.0f));
        }
    }

    @Override
    protected void initEnemies() {
        // Tạo ChickenEnemy (4 hàng, mỗi hàng 5 con)
        for (int row = 0; row < NUM_CHICKEN_ROWS; row++) {
            for (int i = 0; i < NUM_CHICKENS_PER_ROW; i++) {
                Enemy model = new Enemy(0, 64, 64, -50 - i * SPACING, 100 * row + 100, 0, Enemy.EnemyType.CHICKEN_ENEMY);
                model.setInitialIndex(i);
                model.getSkills().put(SkillType.EGG, "/asset/resources/gfx/introEgg.png");
                EnemyView view = new EnemyView(model);
                enemyController.addEnemy(model, view);
            }
        }
        // Tạo ChickEnemy (20 con)
        for (int i = 0; i < NUM_CHICKS; i++) {
            int posY = random.nextInt(100) - 50; // Từ -50 đến 50
            int posX = random.nextInt(SCREEN_WIDTH - 200); // Từ 0 đến 1720
            Enemy model = new Enemy(0, 46, 54, posX, posY, 0, Enemy.EnemyType.CHICK_ENEMY);
            model.setInitialIndex(i);
            EnemyView view = new EnemyView(model);
            enemyController.addEnemy(model, view);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateChickenRows(deltaTime);
        updateChicks(deltaTime);
    }

    private void updateChickenRows(float deltaTime) {
        for (int row = 0; row < chickenRowStates.size(); row++) {
            ChickenRowState state = chickenRowStates.get(row);
            state.timeElapsed += deltaTime;

            if (!state.isActive && state.timeElapsed >= state.timeDelay) {
                state.isActive = true;
            }

            if (state.isActive) {
                state.t += deltaTime * 100 * state.direction;
                float rotate = (float) (20 * Math.sin(0.05 * state.t));

                // Tìm các ChickenEnemy thuộc hàng này
                List<Enemy> rowEnemies = new ArrayList<>();
                for (Enemy enemy : enemyController.getEnemyModels()) {
                    if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY && enemy.getPosY() == state.startY) {
                        rowEnemies.add(enemy);
                    }
                }

                for (Enemy enemy : rowEnemies) {
                    int index = enemy.getInitialIndex();
                    float posX = -1800 + state.t + index * SPACING;
                    float posY = state.startY + 20 * (float) Math.sin(0.02 * posX);
                    enemy.setPosX((int) posX);
                    enemy.setPosY((int) posY);
                    enemy.setRotate(rotate);
                }

                if (!rowEnemies.isEmpty()) {
                    Enemy firstEnemy = rowEnemies.get(0);
                    Enemy lastEnemy = rowEnemies.get(rowEnemies.size() - 1);

                    if (lastEnemy.getPosX() > SCREEN_RIGHT && state.direction == 1) {
                        state.direction = -1;
                        state.t -= 2 * (lastEnemy.getPosX() - SCREEN_RIGHT);
                    } else if (firstEnemy.getPosX() < SCREEN_LEFT && state.direction == -1) {
                        state.direction = 1;
                        state.t += 2 * (SCREEN_LEFT - firstEnemy.getPosX());
                    }
                }
            }
        }
    }

    private void updateChicks(float deltaTime) {
        for (int i = 0; i < chickStates.size(); i++) {
            ChickState state = chickStates.get(i);
            Enemy enemy = enemyController.getEnemyModels().get(i + NUM_CHICKEN_ROWS * NUM_CHICKENS_PER_ROW); // Bỏ qua ChickenEnemy
            state.timeElapsed += deltaTime;

            if (!state.isActive && state.timeElapsed >= state.timeDelay) {
                state.isActive = true;
            }

            if (state.isActive && enemy.getType() == Enemy.EnemyType.CHICK_ENEMY) {
                state.t += deltaTime * 100;
                float posY = enemy.getPosY() + GRAVITY;
                float offsetX = (float) Math.sin(posY * 0.005) * OSCILLATION_AMPLITUDE;
                float posX = enemy.getPosX() + offsetX * deltaTime * OSCILLATION_SPEED;

                enemy.setPosX((int) posX);
                enemy.setPosY((int) posY);
                enemy.setRotate((float) (20 * Math.sin(0.02 * state.t)));

                if (enemy.getPosY() > 1000) {
                    enemy.setPosY(random.nextInt(200) - 300); // Từ -300 đến -100
                    enemy.setPosX(random.nextInt(1600) + 100); // Từ 100 đến 1700
                }
            }
        }
    }
}