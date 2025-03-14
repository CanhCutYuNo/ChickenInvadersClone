/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.models.level;

import application.models.Enemy;
import java.awt.Image;
import application.controllers.*;
/**
 *
 * @author hp
 */
public class EnemyLevel2 extends Enemy {

    private int centerX, centerY;
    private int radius;
    private double theta; // góc quay chuyển động tròn

    public EnemyLevel2(int hp, int PosX, int PosY, Image bodySheet, Image wingsSheet, Image headSheet, Image blinkAnimation, SoundController soundController) {
        super(hp, PosX, PosY, bodySheet, wingsSheet, headSheet, blinkAnimation, soundController);

        this.theta = Math.random() * 2 * Math.PI;
        this.centerX = PosX;
        this.centerY = PosY;
        this.radius = 100;
    }

    @Override
    public void update() {
        theta += 0.01;
        PosX = centerX + (int) (radius * Math.cos(theta));
        PosY = centerY + (int) (radius * Math.sin(theta));
    }

}
