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
public class EnemyLevel1 extends Enemy {

    private boolean movingRight;

    public EnemyLevel1(int hp, int PosX, int PosY, Image bodySheet, Image wingsSheet, Image headSheet, Image blinkAnimation, SoundController soundController) {
        super(hp, PosX, PosY, bodySheet, wingsSheet, headSheet, blinkAnimation, soundController);
        this.movingRight = true;
    }

    @Override
    public void update() {

        if(movingRight) {
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
