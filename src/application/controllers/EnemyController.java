package application.controllers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.models.Enemy;
import application.models.types.ChickEnemy;
import application.models.types.ChickenBoss;
import application.models.types.ChickenEnemy;

public class EnemyController {
    private List<Enemy> enemies;
    private float timeDelay;
    private float timeElapsed = 0f;
    private boolean isActive = false;
    private float t = 0;
    private int startY;
    private SoundController soundController;
    private float rotate = 0f;
    private static final int SPACING = 150;
    private int direction = 1;
    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_LEFT = 0;
    private static final int SCREEN_RIGHT = SCREEN_WIDTH;
    private String enemyType;
    private int level;

    public EnemyController(int numEnemies, String enemyType, int startY, float timeDelay, SoundController soundController, int level ) {
        this.enemies = new ArrayList<>();
        this.startY = startY;
        this.timeDelay = timeDelay;
        this.enemyType = enemyType;
        this.soundController = soundController;
        this.level = level;

        Random random = new Random();
        for (int i = 0; i < numEnemies; i++) {
            if(level==1){
                Enemy enemy = createEnemy(-50 - i * SPACING, startY);
                enemy.setInitialIndex(i);
                enemies.add(enemy);
            }
            else if(level==2){
                Enemy enemy = createEnemy(random.nextInt(SCREEN_WIDTH-200),startY);
                enemy.setInitialIndex(i);
                enemies.add(enemy);
            }
            else if(level == 3){
                if(enemyType.equals("Chick")){
                    int x = random.nextInt(SCREEN_WIDTH-200);
                    for(int j = 0; j < 2; j++){
                        Enemy enemy = createEnemy(x + j * 100,startY);
                        enemy.setInitialIndex(j);
                        enemies.add(enemy);
                    }
                }
                else if(enemyType.equals("Chicken")){
                    Enemy enemy = createEnemy(-50 - i * SPACING, startY);
                    enemy.setInitialIndex(i);
                    enemies.add(enemy);
                }
            }


        }
    }

    private Enemy createEnemy(int posX, int posY) {
     //   System.out.println("Creating enemy of type: " + enemyType + " at (" + posX + "," + posY + ")");
        switch (enemyType) {
            case "Chick":
                return new ChickEnemy(posX, posY, soundController);
            case "Chicken":
                return new ChickenEnemy(posX, posY, soundController);
            case "Boss":
                // Đặt tọa độ cố định cho ChickenBoss (giữa màn hình)
                return new ChickenBoss(SCREEN_WIDTH / 2 - 150, 100, soundController);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + enemyType);
        }
    }

    public void update(float deltaTime) {
        timeElapsed += deltaTime;
     //   System.out.println("Time elapsed: " + timeElapsed + ", Time delay: " + timeDelay + ", Is active: " + isActive);

        if (!isActive && timeElapsed >= timeDelay) {
            isActive = true;
      //      System.out.println("Row at Y=" + startY + " is now active!");
        }

        if (isActive) {
            t += deltaTime * 100 * direction;
           // rotate = (float) (20 * Math.sin(0.05 * t));

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
                else if (enemy instanceof ChickenBoss) {
                    ((ChickenBoss) enemy).setRotate(rotate);
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

    public void update1(float deltaTime){
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
            float oscillationSpeed = 0.5f; // Tốc độ lắc
            float oscillationAmplitude = 1.0f; // Biên độ lắc
            for (Enemy enemy : enemies) {
                if (enemy instanceof ChickenEnemy) {
                    float posY = enemy.getPosY() + 2; // Gà rơi xuống
                    float offsetX = (float) Math.sin(posY * 0.005) * oscillationAmplitude; // Lắc nhẹ
                    float posX = enemy.getPosX() + offsetX * deltaTime * oscillationSpeed; // Lắc mượt hơn

                    enemy.setPosY((int) posY);

                    enemy.setPosX((int) posX);
                    ((ChickenEnemy) enemy).setRotate((float) (20 * Math.sin(0.05 * t))); // Xoay nhẹ
                }
                else if (enemy instanceof ChickenBoss) {
                    ((ChickenBoss) enemy).setRotate((float) (20 * Math.sin(0.05 * t)));
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

    public void update2(float deltaTime) {
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
            float oscillationSpeed = 0.5f; // Tốc độ lắc
            float oscillationAmplitude = 1.0f; // Biên độ lắc
            for (Enemy enemy : enemies) {
                if (enemy instanceof ChickEnemy) {
                    float posY = enemy.getPosY() + 2; // Gà rơi xuống
                    float offsetX = (float) Math.sin(posY * 0.005) * oscillationAmplitude; // Lắc nhẹ
                    float posX = enemy.getPosX() + offsetX * deltaTime * oscillationSpeed; // Lắc mượt hơn

                    enemy.setPosY((int) posY);

                    enemy.setPosX((int) posX);
                    ((ChickEnemy) enemy).setRotate((float) (20 * Math.sin(0.05 * t))); // Xoay nhẹ
                }
                else if (enemy instanceof ChickenEnemy) {
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
                if(enemy instanceof ChickEnemy){
                    if (enemy.getPosY() > 1000) {
                        enemy.setPosY(random.nextInt(200) - 300); // Xuất hiện lại từ trên
                        enemy.setPosX(random.nextInt(1600) + 100); // X ngẫu nhiên
                    }

                }
                else if(enemy instanceof ChickenEnemy){
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

    public void render(Graphics g) {
        if (isActive) {
            for (Enemy enemy : enemies) {
                if (!enemy.isDead()) {
                    enemy.render(g);
                }
            }
            System.out.println("Rendering row at Y=" + startY + ", Active enemies: " + enemies.size());
        } else {
            System.out.println("Row at Y=" + startY + " not active yet");
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public boolean isActive() {
        return isActive;
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
        System.out.println("Removed enemy from EnemyController at (" + enemy.getPosX() + "," + enemy.getPosY() + "). New size: " + enemies.size());
    }
}