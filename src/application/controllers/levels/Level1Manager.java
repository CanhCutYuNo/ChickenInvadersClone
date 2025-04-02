package application.controllers.levels;

import application.controllers.EnemyController;
import application.models.Enemy;
import application.models.types.ChickenEnemy;
import application.controllers.SoundController;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Level1Manager {
    private List<EnemyController> enemyControllers;
    private List<Enemy> enemies;
    
    public Level1Manager(SoundController soundController) {
        this.enemyControllers = new ArrayList<>();
        this.enemies = new ArrayList<>();

        enemyControllers.add(new EnemyControllerLevel1(10, "Chicken", 100, 0.0f, soundController,1));
        enemyControllers.add(new EnemyControllerLevel1(10, "Chicken", 300, 0.0f, soundController,1));
        enemyControllers.add(new EnemyControllerLevel1(10, "Chicken", 500, 0.0f, soundController,1));

        for(EnemyController controller : enemyControllers) {
            enemies.addAll(controller.getEnemies());
        }
        // System.out.println("Initialized " + enemies.size() + " enemies in Level1Manager");
    }

    public void update(float deltaTime) {
        for(EnemyController controller : enemyControllers) {
            controller.update(deltaTime);
        }
    }

    public void render(Graphics g) {
        for(EnemyController controller : enemyControllers) {
            controller.render(g);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    // Thêm phương thức để xóa Enemy khỏi các EnemyController
    public void removeEnemy(Enemy enemy) {
        for(EnemyController controller : enemyControllers) {
            if(controller.getEnemies().contains(enemy)) {
                controller.removeEnemy((ChickenEnemy) enemy);
                break;
            }
        }
        enemies.remove(enemy);
    }

    private class EnemyControllerLevel1 extends EnemyController {

        public EnemyControllerLevel1(int numEnemies, String enemyType, int startY, float timeDelay, SoundController soundController, int level) {
            super(numEnemies, enemyType, startY, timeDelay, soundController, level);
            for (int i = 0; i < numEnemies; i++) {
                Enemy enemy = new ChickenEnemy(-50 - i * SPACING, startY, soundController);
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

                if (!enemies.isEmpty() && enemyType.equals("Chicken")) {
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
