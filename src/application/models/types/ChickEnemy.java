/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.models.types;

import application.controllers.SoundController;
import application.models.DeathEffect;
import application.models.Enemy;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author hp
 */
public abstract class ChickEnemy extends Enemy {

    protected Image spriteSheet;

    public ChickEnemy(int PosX, int PosY, SoundController sound) {
        super(50, 46, 54, PosX, PosY, sound);
        currentFrame = (int) (Math.random() % 26);
        frameCount = 0;
        spriteSheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/chick.png")).getImage();

    }

    protected static final int[][] SPRITE = {
        {1, 1, 76, 80}, {79, 1, 76, 80}, {157, 1, 76, 80}, {235, 1, 76, 80}, {313, 1, 76, 79}, {391, 1, 78, 79},
        {1, 83, 78, 79}, {81, 83, 78, 79}, {161, 83, 80, 79}, {243, 83, 80, 78}, {325, 83, 82, 78}, {409, 83, 84, 78},
        {1, 165, 86, 77}, {89, 165, 86, 77}, {177, 165, 88, 77}, {267, 165, 90, 76}, {359, 165, 91, 76},
        {1, 245, 92, 76}, {95, 245, 92, 75}, {189, 245, 92, 75}, {283, 245, 92, 75}, {377, 245, 92, 75},
        {1, 323, 92, 75}, {95, 323, 92, 75}, {189, 323, 92, 75}, {283, 323, 92, 75}
    };

    private static final int[] offsetX = {
        0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8
    };

    @Override
    public void render(Graphics g) {
//        if (frameCount >= SPRITE.length) {
//            frameCount = 0;
//        }
        g.drawImage(spriteSheet,
                PosX - offsetX[currentFrame] - 15, PosY - 15, PosX + SPRITE[currentFrame][2] - offsetX[currentFrame] - 15, PosY + SPRITE[currentFrame][3] - 15,
                SPRITE[currentFrame][0], SPRITE[currentFrame][1], SPRITE[currentFrame][0] + SPRITE[currentFrame][2], SPRITE[currentFrame][1] + SPRITE[currentFrame][3], null);

        //debug
//        g.setColor(Color.WHITE);
//        g.drawRect(PosX, PosY,MODEL_WIDTH, MODEL_HEIGHT);

        //Vẽ chấm đỏ ở trung tâm
//        int centerX = PosX + SPRITE[frameCount][2] / 2;
//        int centerY = PosY + SPRITE[frameCount][3] / 2;
//        g.setColor(Color.RED);
//        g.fillOval(centerX - 2, centerY - 2, 4, 4);


//        System.out.println("x = " + centerX + ", y = " + centerY + ", f = " + frameCount);
//        frameCount++;
    }

    @Override
    public void nextFrame() {
        if (isForward) {
            currentFrame++;
            if (currentFrame >= 25) {
                isForward = false; // Đổi hướng khi đến cuối mảng
            }
        } else {
            currentFrame--;
            if (currentFrame <= 0) {
                isForward = true; // Đổi hướng khi về đầu mảng
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public DeathEffect getDeathEffect() {
        return new ChickDeathEffect(getCenterX(), getCenterY());
    }

    private class ChickDeathEffect extends DeathEffect {

        private ArrayList<Smoke> smokes;
        protected Image spriteSheet;
        protected static final int[][] SPRITE = {
            {1, 1, 20, 20}, {23, 1, 29, 29}, {55, 1, 38, 38}, {95, 1, 45, 45},
            {143, 1, 51, 51}, {197, 1, 54, 54}, {253, 1, 57, 57}, {313, 1, 64, 64},
            {379, 1, 68, 68}, {449, 1, 72, 72}, {523, 1, 76, 76}, {601, 1, 78, 78},
            {681, 1, 80, 80}, {763, 1, 82, 82}, {847, 1, 89, 89}, {1, 91, 93, 93},
            {97, 91, 102, 102}, {201, 91, 105, 105}, {309, 91, 108, 108}, {419, 91, 110, 110},
            {531, 97, 111, 111}, {645, 97, 113, 113}, {761, 97, 114, 114}, {877, 91, 115, 115},
            {1, 211, 116, 116}, {119, 211, 117, 117}, {239, 211, 118, 118}, {359, 211, 118, 118},
            {479, 211, 119, 119}, {601, 211, 120, 120}, {723, 211, 121, 121}, {847, 211, 125, 125},
            {1, 335, 126, 126}, {129, 335, 126, 126}, {257, 335, 127, 127}, {387, 335, 128, 128},
            {517, 335, 128, 128}, {647, 335, 128, 128}, {777, 335, 128, 128}, {1, 461, 128, 128},
            {131, 461, 128, 128}, {261, 461, 128, 128}, {391, 461, 128, 128}, {521, 461, 128, 128},
            {651, 461, 128, 128}, {781, 461, 128, 128}, {1, 589, 128, 128}, {131, 589, 128, 128},
            {261, 589, 128, 128}, {391, 589, 128, 128}, {521, 589, 128, 128}, {651, 589, 123, 123}, {777, 591, 118, 118}};

        protected static final int[] OFFSET = {
            0, 4, 9, 12, 15, 17, 18, 22, 24, 26, 28, 29, 30, 31, 34, 36, 41, 42, 44, 45, 45, 46, 47, 47, 48, 48, 49, 49, 49, 50, 50, 52, 53, 53, 53, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 51, 49
        };

        private ChickDeathEffect(int PosX, int PosY) {
            super(PosX, PosY);

            Random random = new Random();
            spriteSheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/smoke4-yellow.png")).getImage();
            smokes = new ArrayList<>();
            int size = Math.abs(random.nextInt()) % 3 + 1;
            for (int i = 0; i < size; i++) {
                smokes.add(new Smoke(PosX, PosY));
            }
        }

        @Override
        public void render(Graphics g) {
            for (Smoke smoke : smokes) {
                smoke.render(g);
            }

            //Vẽ chấm đỏ tìm PosX, PosY
//        g.setColor(Color.RED);
//        g.fillOval(PosX, PosY, 4, 4);
        }

        @Override
        public void update() {
            if (smokes.isEmpty()) {
                end = true;
            }
            ArrayList<Smoke> smokesToRemove = new ArrayList<>();
            for (Smoke smoke : smokes) {
                smoke.update();
                if (smoke.isEnd()) {
                    smokesToRemove.add(smoke);
                }
            }
            smokes.removeAll(smokesToRemove);
            
            
        }

        private class Smoke {

            private int PosX;
            private int PosY;
            private int frameCount;
            private final int minFrameCount;
            private final int maxFrameCount;
            private final int vX;
            private final int vY;

            public Smoke(int PosX, int PosY) {
                Random random = new Random();

                this.PosX = PosX + (random.nextInt() % 4);
                this.PosY = PosY + (random.nextInt() % 4);
                minFrameCount = Math.abs(random.nextInt() % 20);
                maxFrameCount = Math.abs(random.nextInt() % 27) + 26;
                frameCount = minFrameCount;
                vX = (random.nextInt() % 3);
                vY = (random.nextInt() % 3);
            }

            public void render(Graphics g) {
                if (frameCount != maxFrameCount) {
                    int offsetValue = -OFFSET[frameCount] - 10;
                    g.drawImage(spriteSheet, PosX + offsetValue, PosY + offsetValue, PosX + SPRITE[frameCount][2] + offsetValue, PosY + SPRITE[frameCount][3] + offsetValue, SPRITE[frameCount][0], SPRITE[frameCount][1], SPRITE[frameCount][0] + SPRITE[frameCount][2], SPRITE[frameCount][1] + SPRITE[frameCount][3], null);
                }
                frameCount++;
            }

            public boolean isEnd() {
                return (maxFrameCount == frameCount);
            }

            public void update() {
                PosX = PosX + vX;
                PosY = PosY + vY;
            }
        }
    }
}
