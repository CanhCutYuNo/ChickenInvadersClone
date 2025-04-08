package application.controllers.levels;

import application.controllers.EnemyController;
import application.controllers.GameSettings;
import application.controllers.LevelManager;
import application.models.Enemy;
import application.models.types.ChickenEnemy;
import application.controllers.SoundController;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Level1Manager extends LevelManager {
    private boolean hardWaveTriggered = false;
    public Level1Manager(SoundController soundController, List<Enemy> enemies) {
        super(soundController, enemies);

        addEnemyController(new EnemyControllerLevel1(10, EnemyController.CHICKEN, 100, 0.0f, soundController));
        addEnemyController(new EnemyControllerLevel1(10, EnemyController.CHICKEN, 300, 0.0f, soundController));
        addEnemyController(new EnemyControllerLevel1(10, EnemyController.CHICKEN, 500, 0.0f, soundController));
        // System.out.println("Initialized " + enemies.size() + " enemies in Level1Manager");
    }

    public void addWithDifficulty(){
        if(!hardWaveTriggered){
            addEnemyController(new EnemyControllerLevel1(10, EnemyController.CHICKEN, 100, 0.0f, soundController));
            addEnemyController(new EnemyControllerLevel1(10, EnemyController.CHICKEN, 300, 0.0f, soundController));
            addEnemyController(new EnemyControllerLevel1(10, EnemyController.CHICKEN, 500, 0.0f, soundController));
            hardWaveTriggered = true;
        }
    }

    public boolean isAllEnemiesDefeated() {
        return enemies.isEmpty(); // hoặc enemies.size() == 0
    }

    private class EnemyControllerLevel1 extends EnemyController {

        public EnemyControllerLevel1(int numEnemies, int enemyType, int startY, float timeDelay, SoundController soundController) {
            super(numEnemies, enemyType, startY, timeDelay, soundController);
            for (int i = 0; i < numEnemies; i++) {
                Enemy enemy = createEnemy(-50 - i * SPACING, startY);
                enemy.setInitialIndex(i);
                enemies.add(enemy);
            }
        }

        @Override
        public void update(float deltaTime) {
            timeElapsed += deltaTime;

            if (!isActive && timeElapsed >= timeDelay) {
                isActive = true;
                //      System.out.println("Row at Y=" + startY + " is now active!");
            }

            if (isActive) {
                t += deltaTime * 50 * direction;
                // rotate =(float)(20 * Math.sin(0.05 * t));

                for (Enemy enemy : enemies) {
                    if (enemy instanceof ChickenEnemy) {
                        // Logic di chuyển theo hàng cho ChickenEnemy
                        int index = enemy.getInitialIndex();
                        float posX = -1800 + t + index * SPACING;
                        float posY = startY + 20 * (float) Math.sin(0.02 * posX);
                        enemy.setPosX((int) posX);
                        enemy.setPosY((int) posY);
                        ((ChickenEnemy) enemy).setRotate(rotate);
                    }

                    enemy.nextFrame();
                }

                if (!enemies.isEmpty()) {
                    // Chỉ áp dụng logic di chuyển hàng cho ChickenEnemy
                    Enemy firstEnemy = enemies.get(0);
                    Enemy lastEnemy = enemies.get(enemies.size() - 1);

                    if (lastEnemy.getPosX() > SCREEN_RIGHT && direction == 1) {
                        direction = -1;
                        t -= 2 * (lastEnemy.getPosX() - SCREEN_RIGHT);
                        System.out.println("Row at Y=" + startY + " turning left at right edge");
                    } else if (firstEnemy.getPosX() < SCREEN_LEFT && direction == -1) {
                        direction = 1;
                        t += 2 * (SCREEN_LEFT - firstEnemy.getPosX());
                        System.out.println("Row at Y=" + startY + " turning right at left edge");
                    }
                }
            }
        }
    }

}
