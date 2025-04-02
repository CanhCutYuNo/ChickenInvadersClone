package application.controllers.levels;

import application.controllers.EnemyController;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.types.ChickEnemy;
import application.models.types.ChickenEnemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level3Manager {
    SoundController sound;
    List<EnemyController> enemyControllers;
    List<Enemy> enemies;
    Random random;

    public Level3Manager(SoundController sound) {
        this.sound = sound;
        this.enemyControllers = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.random = new Random();

        EnemyController controller1 = new ChickenEnemyControllerLevel3(8,"Chicken", 100, 0.0f,sound,3);
        enemyControllers.add(controller1);
        enemies.addAll(controller1.getEnemies());

        for(int i = 0; i < 10; i++){
            int posY = random.nextInt(100);
            EnemyController controller = new ChickEnemyControllerLevel3(1,"Chick", posY - 50, 0.0f + i * 2.0f,sound,3);
            enemyControllers.add(controller);
            enemies.addAll(controller.getEnemies());
        }

    }

    public void update(float deltaTime){

        for(EnemyController controller: enemyControllers){

            controller.update(deltaTime);
        }
    }

    public List<Enemy> getEnemies(){return enemies;}
//    public List<Enemy> getEnemies() {
//        List<Enemy> allEnemies = new ArrayList<>();
//        for (EnemyController controller : enemyControllers) {
//            allEnemies.addAll(controller.getEnemies()); // ✅ Luôn cập nhật danh sách động
//        }
//        System.out.println("📋 Tổng số kẻ địch: " + allEnemies.size());
//        return allEnemies;
//    }
    public void render(Graphics g){
        for(EnemyController controller:enemyControllers){
            controller.render(g);
        }
    }

    public void removeEnemy(Enemy enemy){
        for(EnemyController controller : enemyControllers){
            if(controller.getEnemies().contains(enemy)){
                controller.removeEnemy(enemy);
                break;
            }
        }
        enemies.remove(enemy);
    }

    private class ChickenEnemyControllerLevel3 extends EnemyController {

        public ChickenEnemyControllerLevel3(int numEnemies, String enemyType, int startY, float timeDelay,
                SoundController soundController, int level) {
            super(numEnemies, enemyType, startY, timeDelay, soundController, level);
            for (int i = 0; i < numEnemies; i++) {
                Enemy enemy = new ChickEnemy(-50 - i * SPACING, startY, soundController);
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
    }

    private class ChickEnemyControllerLevel3 extends EnemyController {

        public ChickEnemyControllerLevel3(int numEnemies, String enemyType, int startY, float timeDelay, SoundController soundController, int level) {
            super(numEnemies, enemyType, startY, timeDelay, soundController, level);
            for (int i = 0; i < numEnemies; i++) {
                int x = random.nextInt(SCREEN_WIDTH - 200);
                for (int j = 0; j < 2; j++) {
                    Enemy enemy = new ChickEnemy(x + j * 100, startY, soundController);
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
    }
}
