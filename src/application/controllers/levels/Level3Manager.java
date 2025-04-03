package application.controllers.levels;

import application.controllers.EnemyController;
import application.controllers.LevelManager;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.types.ChickEnemy;
import application.models.types.ChickenEnemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level3Manager extends LevelManager {
    Random random;

    public Level3Manager(SoundController sound, List<Enemy> enemies) {
        super(sound, enemies);
        this.random = new Random();


        for(int i = 0; i < 4; i++){
            addEnemyController(new ChickenEnemyControllerLevel3(5,EnemyController.CHICKEN, 100 * (i) + 100, 0.0f + i*20.f,sound));
        }


        for(int i = 0; i < 10; i++){
            int posY = random.nextInt(100);
            addEnemyController(new ChickEnemyControllerLevel3(1, EnemyController.CHICK, posY - 50, 0.0f + i * 2.0f,sound));
        }

    }

    private class ChickenEnemyControllerLevel3 extends EnemyController {

        public ChickenEnemyControllerLevel3(int numEnemies, int enemyType, int startY, float timeDelay,
                SoundController soundController) {
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
            }

            if (isActive) {
                t += deltaTime * 100 * direction;
                // rotate = (float) (20 * Math.sin(0.05 * t));
                Random random = new Random();
                float gravity = 30.0f; // Tốc độ rơi
                float oscillationSpeed = 0.5f; // Tốc độ lắc
                float oscillationAmplitude = 1.0f; // Biên độ lắc
                for (Enemy enemy : enemies) {
                    if (enemy instanceof ChickEnemy) {
                        float posY = enemy.getPosY() + 2; // Gà rơi xuống
                        float offsetX = (float) Math.sin(posY * 0.005) * oscillationAmplitude; // Lắc nhẹ
                        float posX = enemy.getPosX() + offsetX * deltaTime * oscillationSpeed; // Lắc mượt hơn

                        enemy.setPosY((int) posY);

                        enemy.setPosX((int) posX);
                        ((ChickEnemy) enemy).setRotate((float) (20 * Math.sin(0.02 * t))); // Xoay nhẹ
                    } else if (enemy instanceof ChickenEnemy) {
                        int index = enemy.getInitialIndex();
                        float posX = -1800 + t + index * SPACING;
                        float posY = startY + 20 * (float) Math.sin(0.02 * posX);
                        enemy.setPosX((int) posX);
                        enemy.setPosY((int) posY);
                        ((ChickenEnemy) enemy).setRotate(rotate);
                    }
                    enemy.nextFrame();
                }
                for (Enemy enemy : enemies) {
                    if (enemy instanceof ChickEnemy) {
                        if (enemy.getPosY() > 1000) {
                            enemy.setPosY(random.nextInt(200) - 300); // Xuất hiện lại từ trên
                            enemy.setPosX(random.nextInt(1600) + 100); // X ngẫu nhiên
                        }

                    } else if (enemy instanceof ChickenEnemy) {
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
    }

    private class ChickEnemyControllerLevel3 extends EnemyController {

        public ChickEnemyControllerLevel3(int numEnemies, int enemyType, int startY, float timeDelay, SoundController soundController) {
            super(numEnemies, enemyType, startY, timeDelay, soundController);
            for (int i = 0; i < numEnemies; i++) {
                int x = random.nextInt(SCREEN_WIDTH - 200);
                for (int j = 0; j < 2; j++) {
                    Enemy enemy = createEnemy(x + j * 100, startY);
                    enemy.setInitialIndex(j);
                    enemies.add(enemy);
                }
            }
        }

        @Override
        public void update(float deltaTime) {
            timeElapsed += deltaTime;
            if (!isActive && timeElapsed >= timeDelay) {
                isActive = true;
            }

            if (isActive) {
                t += deltaTime * 100 * direction;
                // rotate = (float) (20 * Math.sin(0.05 * t));
                Random random = new Random();
                float gravity = 30.0f; // Tốc độ rơi
                float oscillationSpeed = 0.5f; // Tốc độ lắc
                float oscillationAmplitude = 1.0f; // Biên độ lắc
                for (Enemy enemy : enemies) {
                    if (enemy instanceof ChickEnemy) {
                        float posY = enemy.getPosY() + 2; // Gà rơi xuống
                        float offsetX = (float) Math.sin(posY * 0.005) * oscillationAmplitude; // Lắc nhẹ
                        float posX = enemy.getPosX() + offsetX * deltaTime * oscillationSpeed; // Lắc mượt hơn

                        enemy.setPosY((int) posY);

                        enemy.setPosX((int) posX);
                        ((ChickEnemy) enemy).setRotate((float) (20 * Math.sin(0.02 * t))); // Xoay nhẹ
                    } else if (enemy instanceof ChickenEnemy) {
                        int index = enemy.getInitialIndex();
                        float posX = -1800 + t + index * SPACING;
                        float posY = startY + 20 * (float) Math.sin(0.02 * posX);
                        enemy.setPosX((int) posX);
                        enemy.setPosY((int) posY);
                        ((ChickenEnemy) enemy).setRotate(rotate);
                    }
                    enemy.nextFrame();
                }
                for (Enemy enemy : enemies) {
                    if (enemy instanceof ChickEnemy) {
                        if (enemy.getPosY() > 1000) {
                            enemy.setPosY(random.nextInt(200) - 300); // Xuất hiện lại từ trên
                            enemy.setPosX(random.nextInt(1600) + 100); // X ngẫu nhiên
                        }

                    } else if (enemy instanceof ChickenEnemy) {
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
    }
}
