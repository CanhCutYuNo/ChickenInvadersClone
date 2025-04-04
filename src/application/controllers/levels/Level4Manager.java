package application.controllers.levels;

import application.controllers.EnemyController;
import application.controllers.LevelManager;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.types.ChickEnemy;
import application.models.types.ChickenEnemy;
import application.models.types.EggShellEnemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level4Manager extends LevelManager{
//    List<EggShellEnemy> eggShellEnemies;
    Random random;

    public Level4Manager(SoundController sound, List<Enemy> enemies) {
        super(sound, enemies);
//        this.eggShellEnemies = new ArrayList<>();
        this.random = new Random();

        for(int i = 0; i < 2; i++){
            int posY = random.nextInt(100);

            addEnemyController(new EnemyControllerLevel4(1, EnemyController.EGG_SHELL, posY - 50, 0.0f + i * 2.0f, sound));
        }
    }

    public void addChickAfterEggShellDeath(int posX, int posY) {
        // Tạo một đối tượng tạm thời để lấy kích thước
        ChickEnemy tempChickEnemy = new ChickEnemy(-1, -1, null);
        EggShellEnemy tempEggShellEnemy = new EggShellEnemy(-1, -1, null);

        addEnemyController(new ChickEnemyControllerLevel4(1, EnemyController.CHICK,
                posX + (tempEggShellEnemy.getMODEL_WIDTH() - tempChickEnemy.getMODEL_WIDTH()) / 2,
                posY + (tempEggShellEnemy.getMODEL_HEIGHT() - tempChickEnemy.getMODEL_WIDTH()) / 2, 0.0f, soundController));
    }

    private class EnemyControllerLevel4 extends EnemyController {
        public EnemyControllerLevel4(int numEnemies, int enemyType, int startY, float timeDelay, SoundController soundController) {
            super(numEnemies, enemyType, startY, timeDelay, soundController);
            Enemy enemy = createEnemy(random.nextInt(SCREEN_WIDTH-200), startY);
            enemy.setInitialIndex(0);
            enemies.add(enemy);
        }

        public void update(float deltaTime){
            timeElapsed += deltaTime;
            if (!isActive && timeElapsed >= timeDelay) {
                isActive = true;
                System.out.println("Row at Y=" + startY + " is now active!");
            }
    
            if (isActive) {
                t += deltaTime * 100 * direction;
                // rotate = (float) (20 * Math.sin(0.05 * t));
                Random random = new Random();
                float gravity = 30.0f; // Tốc độ rơi
    //            float oscillationSpeed = 0.5f; // Tốc độ lắc
    //            float oscillationAmplitude = 1.0f; // Biên độ lắc
                for (Enemy enemy : enemies) {
                    if (enemy instanceof EggShellEnemy) {
                        float posY = enemy.getPosY() + 2; // Gà rơi xuống
    //                    float offsetX = (float) Math.sin(posY * 0.005) * oscillationAmplitude; // Lắc nhẹ
    //                    float posX = enemy.getPosX() + offsetX * deltaTime * oscillationSpeed; // Lắc mượt hơn
    
                        enemy.setPosY((int) posY);
    
    //                    enemy.setPosX((int) posX);
    //                    ((EggShellEnemy) enemy).setRotate((float) (20 * Math.sin(0.05 * t))); // Xoay nhẹ
                    }
                }
            }
        }
    }

    private class ChickEnemyControllerLevel4 extends EnemyController {
        public ChickEnemyControllerLevel4(int numEnemies, int enemyType, int posX, int startY, float timeDelay, SoundController soundController) {
            super(numEnemies, enemyType, startY, timeDelay, soundController);
            Enemy enemy = createEnemy(posX, startY);
            enemy.setInitialIndex(0);
            enemies.add(enemy);
        }

        public void update(float deltaTime){
            timeElapsed += deltaTime;
            
            if (!isActive && timeElapsed >= timeDelay) {
                isActive = true;
                //      System.out.println("Row at Y=" + startY + " is now active!");
            }
            
            if (isActive) {
                
                for (Enemy enemy : enemies) {
                    // Implement here
                    
                    enemy.nextFrame();
                }
            }
        }
    }    
}
