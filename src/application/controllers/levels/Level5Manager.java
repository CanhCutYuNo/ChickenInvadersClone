package application.controllers.levels;

import application.controllers.EnemyController;
import application.controllers.LevelManager;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.types.ChickenBoss;
import application.models.types.ChickenEnemy;

import java.util.List;

public class Level5Manager extends LevelManager {
    public Level5Manager(SoundController soundController, List<Enemy> enemies) {
        super(soundController, enemies);

        addEnemyController(new EnemyControllerLevel5(1, EnemyController.BOSS, 100, 0.0f, soundController));
     //   System.out.println("Initialized " + boss.size() + " enemies in Level5Manager");
    }

    private class EnemyControllerLevel5 extends EnemyController{
        
        public EnemyControllerLevel5(int numEnemies, int enemyType, int startY, float timeDelay, SoundController soundController) {
            super(numEnemies, enemyType, startY, timeDelay, soundController);
            Enemy enemy = createEnemy(SCREEN_WIDTH / 2 - 150, 100);
            enemy.setInitialIndex(0);
            enemies.add(enemy);
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
                    } else if (enemy instanceof ChickenBoss) {
                        ((ChickenBoss) enemy).setRotate(rotate);
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