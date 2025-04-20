package application.views.enemy;

import javax.swing.*;

import application.controllers.util.ImageCache;
import application.models.enemy.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnemyView {
    private Enemy enemyModel;
    private Map<Enemy.EnemyType, Image[]> images;
    private int curFrame;
    private int frameCount;
    private int spriteDelay;
    private long lastFrameTime;
    private boolean isForward; // Thêm để điều khiển hướng animation
    private int[] bodySprite;
    private int[] headSprite;
    private List<int[]> wingSprites;
    private int[][] wingOffsets;
    private ImageCache imageCache = ImageCache.getInstance();

    private static final int[][] SPRITE_HEAD = {{195, 93, 30, 45}};
    private static final int[][] SPRITE_BODY = {
        {1, 1, 70, 53}, {217, 1, 70, 53}, {433, 1, 70, 53}, {217, 169, 70, 53}
    };
    private static final int[][] SPRITE_WINGS = {
        {1, 1, 126, 112}, {129, 1, 126, 112}, {257, 1, 126, 112}, {385, 1, 126, 112}, {513, 1, 126, 112},
        {641, 1, 126, 111}, {769, 1, 126, 111}, {897, 1, 126, 111}, {1025, 1, 126, 110}, {1153, 1, 128, 110},
        {1283, 1, 128, 108}, {1413, 1, 128, 108}, {1543, 1, 130, 107}, {1675, 1, 130, 106}, {1807, 1, 130, 106},
        {1, 115, 132, 105}, {135, 115, 132, 104}, {269, 115, 132, 103}, {403, 115, 134, 101}, {539, 115, 136, 99},
        {677, 115, 137, 98}, {817, 115, 138, 96}, {957, 115, 140, 94}, {1099, 115, 140, 93}, {1241, 115, 142, 90},
        {1385, 115, 142, 88}, {1529, 115, 144, 86}, {1675, 115, 144, 85}, {1821, 115, 144, 83}, {1, 223, 146, 82},
        {149, 223, 148, 80}, {299, 223, 150, 79}, {451, 223, 150, 78}, {603, 223, 152, 77}, {757, 223, 152, 77},
        {911, 223, 152, 76}, {1065, 223, 154, 75}, {1221, 223, 154, 75}, {1377, 223, 154, 75}, {1533, 223, 154, 74},
        {1689, 223, 154, 74}, {1845, 223, 154, 74}, {1, 307, 154, 73}, {157, 307, 154, 73}, {313, 307, 154, 73},
        {469, 307, 153, 73}, {625, 307, 152, 73}, {779, 307, 152, 73}, {933, 307, 152, 73}, {1087, 307, 153, 73}
    };
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
        this.curFrame = 0;
        this.frameCount = 0;
        this.spriteDelay = (enemyModel.getType() == Enemy.EnemyType.CHICKEN_BOSS) ? 30 : 0;
        this.lastFrameTime = System.currentTimeMillis();
        this.isForward = true; // Khởi tạo hướng animation
        this.wingSprites = new ArrayList<>();
        initializeSprites();
        loadImages();
    }

    private void initializeSprites() {
        if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
            this.bodySprite = SPRITE_BODY[(int) (Math.random() * SPRITE_BODY.length)];
            this.headSprite = SPRITE_HEAD[0];
            for (int[] frame : SPRITE_WINGS) {
                this.wingSprites.add(frame);
            }
            this.wingOffsets = new int[][] {
                {-5, -10}, {-5, -10}, {-5, -10}, {-5, -10}, {-5, -10}, {-5, -10}, {-5, -10}, {-5, -10},
                {-5, -9}, {-5, -9}, {-5, -9}, {-5, -9}, {-5, -9}, {-5, -8}, {-5, -8}, {-5, -8},
                {-5, -7}, {-5, -7}, {-5, -6}, {-5, -6}, {-5, -5}, {-5, -4}, {-5, -3}, {-5, -3},
                {-5, -2}, {-5, -1}, {-5, 0}, {-5, 0}, {-5, 1}, {-5, 2}, {-5, 2}, {-5, 2},
                {-5, 3}, {-5, 3}, {-5, 3}, {-5, 4}, {-5, 4}, {-5, 4}, {-5, 4}, {-5, 5},
                {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}
            };
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICK_ENEMY) {
            this.curFrame = (int) (Math.random() * SPRITE_CHICK.length);
        }
    }

    private void loadImages() {
        if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
            Image[] chickenImages = new Image[4];
            try {
                chickenImages[0] = imageCache.getResourceImage("/asset/resources/gfx/chicken-body-stripes.png");
                chickenImages[1] = imageCache.getResourceImage("/asset/resources/gfx/chicken-wings.png");
                chickenImages[2] = imageCache.getResourceImage("/asset/resources/gfx/chicken-face.png");
                chickenImages[3] = imageCache.getResourceImage("/asset/resources/gfx/chickenBlink.png");
            } catch (Exception e) {
                System.out.println("Error: Could not load chicken images");
                e.printStackTrace();
            }
            images.put(Enemy.EnemyType.CHICKEN_ENEMY, chickenImages);
        } else if (enemyModel.getType() == Enemy.EnemyType.EGG_SHELL_ENEMY) {
            Image[] eggShellImages = new Image[1];
            try {
                eggShellImages[0] = imageCache.getResourceImage("/asset/resources/gfx/eggShell.png");
            } catch (Exception e) {
                System.out.println("Error: Could not load egg shell sprite sheet");
                e.printStackTrace();
            }
            images.put(Enemy.EnemyType.EGG_SHELL_ENEMY, eggShellImages);
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_BOSS) {
            Image[] bossImages = new Image[1];
            try {
                bossImages[0] = imageCache.getResourceImage("/asset/resources/gfx/boss.png");
            } catch (Exception e) {
                System.out.println("Error: Could not load boss sprite sheet");
                e.printStackTrace();
            }
            images.put(Enemy.EnemyType.CHICKEN_BOSS, bossImages);
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICK_ENEMY) {
            Image[] chickImages = new Image[1];
            try {
                chickImages[0] = imageCache.getResourceImage("/asset/resources/gfx/chick.png");
            } catch (Exception e) {
                System.out.println("Error: Could not load chick sprite sheet");
                e.printStackTrace();
            }
            images.put(Enemy.EnemyType.CHICK_ENEMY, chickImages);
        }
    }

    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime < spriteDelay) {
            return;
        }

        if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
            if (isForward) {
                if (curFrame < 48) { // Check before incrementing
                    curFrame+=2;
                } else {
                    isForward = false;
                }
            } else {
                if (curFrame > 0) { // Check before decrementing
                    curFrame-=2;
                } else {
                    isForward = true;
                }
            }
            frameCount = (frameCount + 1) % 120;
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICK_ENEMY) {
            if (isForward) {
                if (curFrame < SPRITE_CHICK.length - 1) {
                    curFrame++;
                } else {
                    isForward = false;
                }
            } else {
                if (curFrame > 0) {
                    curFrame--;
                } else {
                    isForward = true;
                }
            }
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_BOSS) {
            curFrame = (curFrame + 1) % SPRITE_BOSS.length;
        }

        lastFrameTime = currentTime;
    }
    
    public void render(Graphics g) {
        updateAnimation();

        if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
            Image[] chickenImages = images.get(Enemy.EnemyType.CHICKEN_ENEMY);
            if (chickenImages != null && chickenImages[0] != null && chickenImages[1] != null && chickenImages[2] != null && chickenImages[3] != null) {
                int centerX = enemyModel.getPosX() + enemyModel.getModelWidth() / 2;
                int centerY = enemyModel.getPosY() + enemyModel.getModelHeight() / 2;
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.rotate(Math.toRadians(enemyModel.getRotate()), centerX, centerY);

                int frame = Math.min(curFrame, wingSprites.size() - 1);
                frame = Math.max(frame, 0);
                int[] wingFrame = wingSprites.get(frame);
                int wingWidth = wingFrame[2];
                int wingHeight = wingFrame[3];
                int[] wingOffset = wingOffsets[frame];
                int offsetX = wingOffset[0];
                int offsetY = wingOffset[1];
                int wingCenterX = enemyModel.getPosX() + bodySprite[2] / 2;
                int wingCenterY = enemyModel.getPosY() + bodySprite[3] / 2;

                g.drawImage(chickenImages[1],
                        wingCenterX - wingWidth / 2 + offsetX, wingCenterY - wingHeight / 2 + offsetY - 1,
                        wingCenterX + wingWidth / 2 + offsetX, wingCenterY + wingHeight / 2 + offsetY - 1,
                        wingFrame[0], wingFrame[1],
                        wingFrame[0] + wingWidth, wingFrame[1] + wingHeight, null);

                g.drawImage(chickenImages[2],
                        enemyModel.getPosX() + 15, enemyModel.getPosY() - 50,
                        enemyModel.getPosX() + headSprite[2] + 15, enemyModel.getPosY() + headSprite[3] - 50,
                        headSprite[0], headSprite[1],
                        headSprite[0] + headSprite[2], headSprite[1] + headSprite[3], null);

                g.drawImage(chickenImages[0],
                        enemyModel.getPosX() - 5, enemyModel.getPosY() - 10,
                        enemyModel.getPosX() + bodySprite[2] - 5, enemyModel.getPosY() + bodySprite[3] - 10,
                        bodySprite[0], bodySprite[1],
                        bodySprite[0] + bodySprite[2], bodySprite[1] + bodySprite[3], null);

                if (frameCount < 20) {
                    g.drawImage(chickenImages[3], enemyModel.getPosX() + 23, enemyModel.getPosY() - 40, 50, 40, null);
                }

                g2d.rotate(-Math.toRadians(enemyModel.getRotate()), centerX, centerY);
                g2d.dispose();
            } else {
                g.setColor(Color.RED);
                g.fillRect(enemyModel.getPosX(), enemyModel.getPosY(), enemyModel.getModelWidth(), enemyModel.getModelHeight());
            }
        } else if (enemyModel.getType() == Enemy.EnemyType.EGG_SHELL_ENEMY) {
            Image[] eggShellImages = images.get(Enemy.EnemyType.EGG_SHELL_ENEMY);
            if (eggShellImages != null && eggShellImages[0] != null) {
                // Xác định sprite dựa trên HP
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

                int spriteX = SPRITE_EGG_SHELL[state][0];
                int spriteY = SPRITE_EGG_SHELL[state][1];
                int spriteWidth = SPRITE_SIZE_EGG_SHELL[0];
                int spriteHeight = SPRITE_SIZE_EGG_SHELL[1];

                // Tính tâm để xoay
                int centerX = enemyModel.getPosX() + enemyModel.getModelWidth() / 2;
                int centerY = enemyModel.getPosY() + enemyModel.getModelHeight() / 2;
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.rotate(Math.toRadians(enemyModel.getRotate()), centerX, centerY);

                // Vẽ sprite egg shell
                g.drawImage(eggShellImages[0],
                        enemyModel.getPosX(), enemyModel.getPosY(),
                        enemyModel.getPosX() + 75, enemyModel.getPosY() + 97,
                        spriteX, spriteY,
                        spriteX + spriteWidth, spriteY + spriteHeight, null);

                g2d.dispose();
            } else {
                g.setColor(Color.RED);
                g.fillRect(enemyModel.getPosX(), enemyModel.getPosY(), enemyModel.getModelWidth(), enemyModel.getModelHeight());
            }
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICKEN_BOSS) {
            Image[] bossImages = images.get(Enemy.EnemyType.CHICKEN_BOSS);
            if (bossImages != null && bossImages[0] != null) {
                int frame = Math.min(curFrame, SPRITE_BOSS.length - 1);
                frame = Math.max(frame, 0);
                int spriteX = SPRITE_BOSS[frame][0] * SPRITE_SIZE_BOSS[0];
                int spriteY = SPRITE_BOSS[frame][1] * SPRITE_SIZE_BOSS[1];
                int spriteWidth = SPRITE_SIZE_BOSS[0];
                int spriteHeight = SPRITE_SIZE_BOSS[1];

                // Tính tâm để xoay
                int centerX = enemyModel.getPosX() + enemyModel.getModelWidth() / 2;
                int centerY = enemyModel.getPosY() + enemyModel.getModelHeight() / 2;
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.rotate(Math.toRadians(enemyModel.getRotate()), centerX, centerY);

                // Vẽ sprite boss
                g.drawImage(bossImages[0],
                    enemyModel.getPosX() - 300, enemyModel.getPosY() - 100, enemyModel.getPosX() + enemyModel.getModelWidth() + 300, enemyModel.getPosY() + enemyModel.getModelHeight() + 100,
                        spriteX, spriteY,
                        spriteX + spriteWidth, spriteY + spriteHeight, null);

                g2d.dispose();
            } else {
                g.setColor(Color.RED);
                g.fillRect(enemyModel.getPosX(), enemyModel.getPosY(), enemyModel.getModelWidth(), enemyModel.getModelHeight());
            }
        } else if (enemyModel.getType() == Enemy.EnemyType.CHICK_ENEMY) {
            Image[] chickImages = images.get(Enemy.EnemyType.CHICK_ENEMY);
            if (chickImages != null && chickImages[0] != null) {
                int frame = Math.min(curFrame, SPRITE_CHICK.length - 1);
                frame = Math.max(frame, 0);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.rotate(Math.toRadians(enemyModel.getRotate()), enemyModel.getPosX() + enemyModel.getModelWidth() / 2, enemyModel.getPosY() + enemyModel.getModelHeight() / 2);
                g.drawImage(chickImages[0],
                        enemyModel.getPosX() - OFFSET_X_CHICK[frame] - 15, enemyModel.getPosY() - 15,
                        enemyModel.getPosX() + SPRITE_CHICK[frame][2] - OFFSET_X_CHICK[frame] - 15, enemyModel.getPosY() + SPRITE_CHICK[frame][3] - 15,
                        SPRITE_CHICK[frame][0], SPRITE_CHICK[frame][1],
                        SPRITE_CHICK[frame][0] + SPRITE_CHICK[frame][2], SPRITE_CHICK[frame][1] + SPRITE_CHICK[frame][3], null);
                g2d.dispose();
            }
        }
        g.setColor(Color.WHITE);
        g.drawRect(enemyModel.getPosX(), enemyModel.getPosY(), enemyModel.getModelWidth(), enemyModel.getModelHeight());
    }
}