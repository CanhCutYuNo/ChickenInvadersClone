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
import java.util.Objects;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author hp
 */
public class ChickEnemy extends Enemy {

    protected Image spriteSheet;

    public ChickEnemy(int PosX, int PosY, SoundController sound) {
        super(50, 46, 54, PosX, PosY, sound);
        currentFrame = (int) (Math.random() % 26);
        frameCount = 0;
        spriteSheet = new ImageIcon(Objects.requireNonNull(getClass().getResource("/asset/resources/gfx/chick.png"))).getImage();

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
}
