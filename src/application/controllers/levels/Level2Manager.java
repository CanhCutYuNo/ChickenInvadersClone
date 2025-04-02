package application.controllers.levels;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.controllers.EnemyController;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.types.ChickenEnemy;

public class Level2Manager {

    SoundController sound;
    List<EnemyController> enemyControllers;
    List<Enemy> enemies;
    Random random;

    public Level2Manager(SoundController sound) {
        this.sound = sound;
        this.enemyControllers = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.random = new Random();

        for (int i = 0; i < 20; i++) {
            int posY = random.nextInt(100);
            EnemyController controller = new EnemyControllerLevel2(1, "Chicken", posY - 100, 0.0f + i * 1.0f, sound, 2);

            enemyControllers.add(controller);
            enemies.addAll(controller.getEnemies());
        }

    }

    public void update(float deltaTime) {
        for (EnemyController controller : enemyControllers) {

            controller.update(deltaTime);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void render(Graphics g) {
        for (EnemyController controller : enemyControllers) {
            controller.render(g);
        }
    }

    public void removeEnemy(Enemy enemy) {
        for (EnemyController controller : enemyControllers) {
            if (controller.getEnemies().contains(enemy)) {
                controller.removeEnemy((ChickenEnemy) enemy);
                break;
            }
        }
        enemies.remove(enemy);
    }

    private class EnemyControllerLevel2 extends EnemyController {

        public EnemyControllerLevel2(int numEnemies, String enemyType, int startY, float timeDelay, SoundController soundController, int level) {
            super(numEnemies, enemyType, startY, timeDelay, soundController, level);
            for (int i = 0; i < numEnemies; i++) {
                Enemy enemy = new ChickenEnemy(random.nextInt(SCREEN_WIDTH - 200), startY, soundController);
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
                    if (enemy instanceof ChickenEnemy) {
                        float posY = enemy.getPosY() + 2; // Gà rơi xuống
                        float offsetX = (float) Math.sin(posY * 0.005) * oscillationAmplitude; // Lắc nhẹ
                        float posX = enemy.getPosX() + offsetX * deltaTime * oscillationSpeed; // Lắc mượt hơn

                        enemy.setPosY((int) posY);

                        enemy.setPosX((int) posX);
                        ((ChickenEnemy) enemy).setRotate((float) (20 * Math.sin(0.02 * t))); // Xoay nhẹ
                    }

                    enemy.nextFrame();
                }

                // Kiểm tra nếu gà chạm đáy màn hình, sinh lại từ trên
                for (Enemy enemy : enemies) {
                    if (enemy.getPosY() > 1000) {
                        enemy.setPosY(random.nextInt(200) - 300); // Xuất hiện lại từ trên
                        enemy.setPosX(random.nextInt(1600) + 100); // X ngẫu nhiên
                    }
                }
            }

        }

    }
}
