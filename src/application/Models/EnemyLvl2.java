/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.Models;

import java.awt.Image;

/**
 *
 * @author hp
 */
public class EnemyLvl2 extends Enemy {

    private int centerX, centerY;
    private int radius;
    private double theta; // góc quay chuyển động tròn

    public EnemyLvl2(int hp, int PosX, int PosY, int level, Image bodySheet, Image wingsSheet, Image headSheet, Image blinkAnimation) {
        super(hp, PosX, PosY, level, bodySheet, wingsSheet, headSheet, blinkAnimation);

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
