package application.views;

import application.models.Enemy;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EnemyView {
    private Enemy enemyModel;
    private Map<Enemy.EnemyType, Image[]> images;
    private static final int[][] SPRITE_BOSS = {
        {0,0}, {1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, {7,0}, {8,0}, {9,0},
        {0,1}, {1,1}, {2,1}, {3,1}, {4,1}, {5,1}, {6,1}, {7,1}, {8,1}, {9,1},
        {0,2}, {1,2}, {2,2}, {3,2}, {4,2}, {5,2}, {6,2}, {7,2}, {8,2}, {9,2},
        {0,3}, {1,3}, {2,3}, {3,3}, {4,3}, {5,3}, {6,3}, {7,3}, {8,3}, {9,3},
        {0,4}, {1,4}, {2,4}, {3,4}, {4,4}, {5,4}, {6,4}, {7,4}, {8,4}, {9,4},
        {0,5}, {1,5}, {2,5}, {3,5}, {4,5}, {5,5}, {6,5}, {7,5}, {8,5}, {9,5},
    };
    private static final int[] SPRITE_SIZE_BOSS = {400, 376};
    private static final int[][] SPRITE_CHICK = {
        {1, 1, 76, 80}, {79, 1, 76, 80}, {157, 1, 76, 80}, {235, 1, 76, 80}, {313, 1, 76, 79}, {391, 1, 78, 79},
        {1, 83, 78, 79}, {81, 83, 78, 79}, {161, 83, 80, 79}, {243, 83, 80, 78}, {325, 83, 82, 78}, {409, 83, 84, 78},
        {1, 165, 86, 77}, {89, 165, 86, 77}, {177, 165, 88, 77}, {267, 165, 90, 76}, {359, 165, 91, 76},
        {1, 245, 92, 76}, {95, 245, 92, 75}, {189, 245, 92, 75}, {283, 245, 92, 75}, {377, 245, 92, 75},
        {1, 323, 92, 75}, {95, 323, 92, 75}, {189, 323, 92, 75}, {283, 323, 92, 75}
    };
    private static final int[] OFFSET_X_CHICK = {
        0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8
    };
    private static final int[][] SPRITE_EGG_SHELL = {
        {1, 1}, {153, 1}, {305, 1}, {1, 197}
    };
    private static final int[] SPRITE_SIZE_EGG_SHELL = {150, 194};

    public EnemyView(Enemy enemyModel) {
        this.enemyModel = enemyModel;
        this.images = new HashMap<>();
        loadImages();
    }

    private void loadImages() {
        if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
            Image[] chickenImages = new Image[4]; // 0: body, 1: wings, 2: head, 3: blink
            try {
                chickenImages[0] = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-body-stripes.png")).getImage();
                chickenImages[1] = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-wings.png")).getImage();
                chickenImages[2] = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-face.png")).getImage();
                chickenImages[3] = new ImageIcon(getClass().getResource("/asset/resources/gfx/chickenBlink.png")).getImage();
            } catch (Exception e) {
                System.out.println("Error: Could not load chicken images");
                e.printStackTrace();
            }
            images.put(Enemy.EnemyType.CHICKEN_ENEMY, chickenImages);
        } else if (enemyModel.getType() == Enemy.EnemyType.EGG_SHELL_ENEMY) {
            Image[] eggShellImages = new Image[1];
            try {
                eggShellImages[0] = new ImageIcon(getClass().getResource("/asset/resources/gfx/eggShell.png")).getImage();
            } catch (Exception e) {
                System.out.println("Error: Could not load egg shell sprite sheet");
                e.printStackTrace();
            }
            images.put(Enemy.EnemyType.EGG_SHELL_ENEMY, eggShellImages);
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_BOSS) {
            Image[] bossImages = new Image[1];
            try {
                bossImages[0] = new ImageIcon(getClass().getResource("/asset/resources/gfx/boss.png")).getImage();
            } catch (Exception e) {
                System.out.println("Error: Could not load boss sprite sheet");
                e.printStackTrace();
            }
            images.put(Enemy.EnemyType.CHICKEN_BOSS, bossImages);
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICK_ENEMY) {
            Image[] chickImages = new Image[1];
            try {
                chickImages[0] = new ImageIcon(getClass().getResource("/asset/resources/gfx/chick.png")).getImage();
            } catch (Exception e) {
                System.out.println("Error: Could not load chick sprite sheet");
                e.printStackTrace();
            }
            images.put(Enemy.EnemyType.CHICK_ENEMY, chickImages);
        }
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
            Image[] chickenImages = images.get(Enemy.EnemyType.CHICKEN_ENEMY);
            if (chickenImages != null && chickenImages[0] != null && chickenImages[1] != null && chickenImages[2] != null && chickenImages[3] != null) {
                int centerX = enemyModel.getPosX() + enemyModel.getModelWidth() / 2;
                int centerY = enemyModel.getPosY() + enemyModel.getModelHeight() / 2;
                g2d.rotate(Math.toRadians(enemyModel.getRotate()), centerX, centerY);

                int frame = Math.min(enemyModel.getCurFrame(), enemyModel.getWingSprites().size() - 1);
                int[] wingFrame = enemyModel.getWingSprites().get(frame);
                int wingWidth = wingFrame[2];
                int wingHeight = wingFrame[3];
                int[] wingOffset = enemyModel.getWingOffsets()[frame];
                int offsetX = wingOffset[0];
                int offsetY = wingOffset[1];
                int wingCenterX = enemyModel.getPosX() + enemyModel.getBodySprite()[2] / 2;
                int wingCenterY = enemyModel.getPosY() + enemyModel.getBodySprite()[3] / 2;

                g2d.drawImage(chickenImages[1],
                        wingCenterX - wingWidth / 2 + offsetX, wingCenterY - wingHeight / 2 + offsetY - 1,
                        wingCenterX + wingWidth / 2 + offsetX, wingCenterY + wingHeight / 2 + offsetY - 1,
                        wingFrame[0], wingFrame[1],
                        wingFrame[0] + wingWidth, wingFrame[1] + wingHeight, null);

                g2d.drawImage(chickenImages[2],
                        enemyModel.getPosX() + 15, enemyModel.getPosY() - 50,
                        enemyModel.getPosX() + enemyModel.getHeadSprite()[2] + 15, enemyModel.getPosY() + enemyModel.getHeadSprite()[3] - 50,
                        enemyModel.getHeadSprite()[0], enemyModel.getHeadSprite()[1],
                        enemyModel.getHeadSprite()[0] + enemyModel.getHeadSprite()[2], enemyModel.getHeadSprite()[1] + enemyModel.getHeadSprite()[3], null);

                g2d.drawImage(chickenImages[0],
                        enemyModel.getPosX() - 5, enemyModel.getPosY() - 10,
                        enemyModel.getPosX() + enemyModel.getBodySprite()[2] - 5, enemyModel.getPosY() + enemyModel.getBodySprite()[3] - 10,
                        enemyModel.getBodySprite()[0], enemyModel.getBodySprite()[1],
                        enemyModel.getBodySprite()[0] + enemyModel.getBodySprite()[2], enemyModel.getBodySprite()[1] + enemyModel.getBodySprite()[3], null);

                if (enemyModel.getFrameCount() < 20) {
                    g2d.drawImage(chickenImages[3], enemyModel.getPosX() + 23, enemyModel.getPosY() - 40, 50, 40, null);
                }

                g2d.rotate(-Math.toRadians(enemyModel.getRotate()), centerX, centerY);
            } else {
                g2d.setColor(Color.RED);
                g2d.fillRect(enemyModel.getPosX(), enemyModel.getPosY(), enemyModel.getModelWidth(), enemyModel.getModelHeight());
            }
        } else if (enemyModel.getType() == Enemy.EnemyType.EGG_SHELL_ENEMY) {
            Image[] eggShellImages = images.get(Enemy.EnemyType.EGG_SHELL_ENEMY);
            if (eggShellImages != null && eggShellImages[0] != null) {
                int state;
                if (enemyModel.getHp() >= 300) {
                    state = 0;
                } else if (enemyModel.getHp() >= 200) {
                    state = 1;
                } else if (enemyModel.getHp() >= 100) {
                    state = 2;
                } else {
                    state = 3;
                }
                g2d.drawImage(eggShellImages[0],
                        enemyModel.getPosX(), enemyModel.getPosY(),
                        enemyModel.getPosX() + enemyModel.getModelWidth(), enemyModel.getPosY() + enemyModel.getModelHeight(),
                        SPRITE_EGG_SHELL[state][0], SPRITE_EGG_SHELL[state][1],
                        SPRITE_EGG_SHELL[state][0] + SPRITE_SIZE_EGG_SHELL[0], SPRITE_EGG_SHELL[state][1] + SPRITE_SIZE_EGG_SHELL[1], null);
                // Vẽ hitbox để debug (tùy chọn)
                // g2d.setColor(Color.WHITE);
                // g2d.drawRect(enemyModel.getPosX(), enemyModel.getPosY(), enemyModel.getModelWidth(), enemyModel.getModelHeight());
            }
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_BOSS) {
            Image[] bossImages = images.get(Enemy.EnemyType.CHICKEN_BOSS);
            if (bossImages != null && bossImages[0] != null) {
                g2d.rotate(enemyModel.getRotate(), enemyModel.getPosX() + enemyModel.getModelWidth() / 2, enemyModel.getPosY() + enemyModel.getModelHeight() / 2);
                g.drawImage(bossImages[0],
                        enemyModel.getPosX() - 80, enemyModel.getPosY(),
                        enemyModel.getPosX() + enemyModel.getModelWidth() + 80, enemyModel.getPosY() + enemyModel.getModelHeight(),
                        SPRITE_BOSS[enemyModel.getCurFrame()][0] * SPRITE_SIZE_BOSS[0], SPRITE_BOSS[enemyModel.getCurFrame()][1] * SPRITE_SIZE_BOSS[1],
                        (SPRITE_BOSS[enemyModel.getCurFrame()][0] + 1) * SPRITE_SIZE_BOSS[0], (SPRITE_BOSS[enemyModel.getCurFrame()][1] + 1) * SPRITE_SIZE_BOSS[1], null);
                g2d.rotate(-enemyModel.getRotate(), enemyModel.getPosX() + enemyModel.getModelWidth() / 2, enemyModel.getPosY() + enemyModel.getModelHeight() / 2);
                g2d.setColor(Color.RED);
                Rectangle hitbox = new Rectangle(enemyModel.getPosX() + 20, enemyModel.getPosY(), enemyModel.getModelWidth() - 40, enemyModel.getModelHeight());
                g2d.draw(hitbox);
            }
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICK_ENEMY) {
            Image[] chickImages = images.get(Enemy.EnemyType.CHICK_ENEMY);
            if (chickImages != null && chickImages[0] != null) {
                int frame = Math.min(enemyModel.getCurFrame(), SPRITE_CHICK.length - 1);
                g2d.rotate(enemyModel.getRotate(), enemyModel.getPosX() + enemyModel.getModelWidth() / 2, enemyModel.getPosY() + enemyModel.getModelHeight() / 2);
                g.drawImage(chickImages[0],
                        enemyModel.getPosX() - OFFSET_X_CHICK[frame] - 15, enemyModel.getPosY() - 15,
                        enemyModel.getPosX() + SPRITE_CHICK[frame][2] - OFFSET_X_CHICK[frame] - 15, enemyModel.getPosY() + SPRITE_CHICK[frame][3] - 15,
                        SPRITE_CHICK[frame][0], SPRITE_CHICK[frame][1],
                        SPRITE_CHICK[frame][0] + SPRITE_CHICK[frame][2], SPRITE_CHICK[frame][1] + SPRITE_CHICK[frame][3], null);
                g2d.rotate(-enemyModel.getRotate(), enemyModel.getPosX() + enemyModel.getModelWidth() / 2, enemyModel.getPosY() + enemyModel.getModelHeight() / 2);
            }
        }
        g2d.dispose();
    }
}