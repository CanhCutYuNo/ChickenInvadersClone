package application.controllers.level.levels;

import java.util.ArrayList;
import java.util.List;

import application.controllers.enemy.EnemyController;
import application.controllers.level.LevelManager;
import application.controllers.util.SoundController;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;
import application.views.enemy.EnemyView;

public class Level1Manager extends LevelManager {
    // Hằng số:
    private static final int SCREEN_LEFT = 75;
    private static final int SCREEN_RIGHT = 1850;
    private static final int SPACING = 150;
    private static final int NUM_ENEMIES_PER_ROW = 10;
    private static final int[] START_Y = {100, 300, 500};
    private static final float[] TIME_DELAYS = {0.0f, 0.0f, 0.0f};

    private List<RowState> rowStates;

    public Level1Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController);
        rowStates = new ArrayList<>();
        for(int i = 0; i < START_Y.length; i++) {
            rowStates.add(new RowState(START_Y[i], TIME_DELAYS[i]));
        }
    }

    @Override
    protected void initEnemies() {
        for(int row = 0; row < START_Y.length; row++) {
            for(int i = 0; i < NUM_ENEMIES_PER_ROW; i++) {
                Enemy model = new Enemy(64, 64, -50 - i * SPACING, START_Y[row], 5, Enemy.EnemyType.CHICKEN_ENEMY);
                model.setInitialIndex(i);
                model.getSkills().put(SkillType.EGG, "/asset/resources/gfx/introEgg.png");
                EnemyView view = new EnemyView(model);
                enemyController.addEnemy(model, view);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateRows(deltaTime);
    }

    private void updateRows(float deltaTime) {
        for(int row = 0; row < rowStates.size(); row++) {
            RowState state = rowStates.get(row);
            state.timeElapsed += deltaTime;

            if(!state.isActive && state.timeElapsed >= state.timeDelay) {
                state.isActive = true;
            }

            if(state.isActive) {
                state.t += deltaTime * 100 * state.direction;

                List<Enemy> rowEnemies = new ArrayList<>();
                for(Enemy enemy : enemyController.getEnemyModels()) {
                    if(enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY && Math.abs(enemy.getPosY() - state.startY) < 40) {
                        rowEnemies.add(enemy);
                    }
                }

                for(Enemy enemy : rowEnemies) {
                    int index = enemy.getInitialIndex() % NUM_ENEMIES_PER_ROW;
                    float posX = -1800 + state.t + index * SPACING;
                    float posY = state.startY + 20 * (float) Math.sin(0.02 * posX);
                    enemy.setPosX((int) posX);
                    enemy.setPosY((int) posY);
                }

                if(!rowEnemies.isEmpty()) {
                    Enemy firstEnemy = rowEnemies.get(0);
                    Enemy lastEnemy = rowEnemies.get(rowEnemies.size() - 1);
                    if(lastEnemy.getPosX() > SCREEN_RIGHT && state.direction == 1) {
                        state.direction = -1;
                        state.t -= (lastEnemy.getPosX() - SCREEN_RIGHT);
                    } else if(firstEnemy.getPosX() < SCREEN_LEFT && state.direction == -1) {
                        state.direction = 1;
                        state.t += (SCREEN_LEFT - firstEnemy.getPosX());
                    }
                }
            }
        }
    }
}