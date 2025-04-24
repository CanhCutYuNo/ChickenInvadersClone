package application.controllers.level.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.controllers.enemy.EnemyController;
import application.controllers.level.LevelManager;
import application.controllers.util.SoundController;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;
import application.views.enemy.EnemyView;

public class Level3Manager extends LevelManager {
    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_LEFT = -50;
    private static final int SCREEN_RIGHT = 1920;
    private static final int SPACING = 150;
    private static final int NUM_CHICKEN_ROWS = 4;
    private static final int NUM_CHICKENS_PER_ROW = 5;
    private static final int NUM_CHICKS = 20;
    private static final float GRAVITY = 2.0f;
    private static final float OSCILLATION_SPEED = 0.5f;
    private static final float OSCILLATION_AMPLITUDE = 1.0f;
    private static final int CHICKEN_START_Y = 100;
    private static final float CHICKEN_TIME_DELAY = 2.0f;

    private Random rand = new Random();
    private List<RowState> chickenRowStates;

    public Level3Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController);
        this.rand = new Random();
        chickenRowStates = new ArrayList<>();
        for(int i = 0; i < NUM_CHICKEN_ROWS; i++) {
            chickenRowStates.add(new RowState(CHICKEN_START_Y + 100 * i, CHICKEN_TIME_DELAY * i));
        }
    }

    @Override
    protected void initEnemies() {
        if(rand == null) {
            rand = new Random();
        }
        for(int row = 0; row < NUM_CHICKEN_ROWS; row++) {
            for(int i = 0; i < NUM_CHICKENS_PER_ROW; i++) {
                int uniqueInitialIndex = row * NUM_CHICKENS_PER_ROW + i;

                Enemy model = new Enemy(64, 64, -150 - i * SPACING, CHICKEN_START_Y + 100 * row, 0, Enemy.EnemyType.CHICKEN_ENEMY);
                model.setInitialIndex(uniqueInitialIndex);
                model.getSkills().put(SkillType.EGG, "/asset/resources/gfx/introEgg.png");
                EnemyView view = new EnemyView(model);
                enemyController.addEnemy(model, view);
            }
        }

        for(int i = 0; i < NUM_CHICKS; i++) {
            int posY = rand.nextInt(100) - 200; 
            int posX = rand.nextInt(SCREEN_WIDTH - 200);
            Enemy model = new Enemy(46, 54, posX, posY, 0, Enemy.EnemyType.CHICK_ENEMY);
            model.setInitialIndex(i); 
            model.setState(new Enemy.EnemyState(i * 2.0f)); 
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
        for(int row = 0; row < NUM_CHICKEN_ROWS; row++) {
            RowState state = chickenRowStates.get(row);
            state.timeElapsed += deltaTime;

            if(!state.isActive && state.timeElapsed >= state.timeDelay) {
                state.isActive = true;
            }

            if(state.isActive) {
                state.t += deltaTime * 100 * state.direction;

                List<Enemy> rowEnemies = new ArrayList<>();
                for(Enemy enemy : enemyController.getEnemyModels()) {
                    if(enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY
                        && enemy.getInitialIndex() / NUM_CHICKENS_PER_ROW == row) { 
                        rowEnemies.add(enemy);
                    }
                }

                for(Enemy rowEnemy : rowEnemies) {
                    int index = rowEnemy.getInitialIndex() % NUM_CHICKENS_PER_ROW;
                    float posX = -1800 + state.t + index * SPACING; 
                    float posY = state.startY + 20 * (float) Math.sin(0.02 * posX);
                    rowEnemy.setPosX((int) posX);
                    rowEnemy.setPosY((int) posY);
                }

                if(!rowEnemies.isEmpty()) {
                     Enemy firstEnemy = null;
                     Enemy lastEnemy = null;
                     int minIndexInRow = Integer.MAX_VALUE;
                     int maxIndexInRow = Integer.MIN_VALUE;

                     for(Enemy e : rowEnemies) {
                         int indexInRow = e.getInitialIndex() % NUM_CHICKENS_PER_ROW;
                         if(indexInRow < minIndexInRow) {
                             minIndexInRow = indexInRow;
                             firstEnemy = e;
                         }
                         if(indexInRow > maxIndexInRow) {
                             maxIndexInRow = indexInRow;
                             lastEnemy = e;
                         }
                     }

                    if(firstEnemy != null && lastEnemy != null) {
                         if(lastEnemy.getPosX() > SCREEN_RIGHT && state.direction == 1) {
                            state.direction = -1;
                        } else if(firstEnemy.getPosX() < SCREEN_LEFT && state.direction == -1) {
                            state.direction = 1;
                        }
                    }
                }
            }
        }
    }

    private void updateChicks(float deltaTime) {
        for(Enemy enemy : enemyController.getEnemyModels()) {
            if(enemy.getType() == Enemy.EnemyType.CHICK_ENEMY && enemy.state != null) {
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

                    if(enemy.getPosY() > 1000) {
                        enemy.setPosY(rand.nextInt(200) - 300);
                        enemy.setPosX(rand.nextInt(1600) + 100);
                    }
                }
            }
        }
    }
}