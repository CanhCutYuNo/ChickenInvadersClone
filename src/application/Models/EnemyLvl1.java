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
public class EnemyLvl1 extends Enemy {

    private boolean movingRight;

    public EnemyLvl1(int hp, int PosX, int PosY, int level, Image bodySheet, Image wingsSheet, Image headSheet, Image blinkAnimation) {
        super(hp, PosX, PosY, level, bodySheet, wingsSheet, headSheet, blinkAnimation);
        this.movingRight = true;
    }

    @Override
    public void update() {
        if (movingRight) {
            PosX += speed;
            if (PosX >= MAP_WIDTH - MODEL_WIDTH) {
                movingRight = false;
            }
        } else {
            PosX -= speed;
            if (PosX <= 0) {
                movingRight = true;
            }
        }
    }
}
