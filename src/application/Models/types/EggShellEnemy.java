package application.models.types;

import application.controllers.SoundController;
import application.models.Enemy;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class EggShellEnemy extends Enemy {

    protected Image spriteSheet;

    public EggShellEnemy(int PosX, int PosY, SoundController sound) {
        super(400, SPRITE_SIZE[0], SPRITE_SIZE[1], PosX, PosY, sound);
        currentFrame = 0;
        frameCount = 0;
        spriteSheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/eggShell.png")).getImage();
    }

    protected static final int[][] SPRITE = {
        {1, 1}, {153, 1}, {305, 1}, {1, 197}
    };

    protected static final int[] SPRITE_SIZE = {150, 194};

    @Override
    public void render(Graphics g) {
        int state;
        if (getHp() >= 300) {
            state = 0;
        } else if (getHp() >= 200) {
            state = 1;
        } else if (getHp() >= 100) {
            state = 2;
        } else {
            state = 3;
        }
        g.drawImage(spriteSheet,
                PosX, PosY, PosX + SPRITE_SIZE[0], PosY + SPRITE_SIZE[1],
                SPRITE[state][0], SPRITE[state][1], SPRITE[state][0] + SPRITE_SIZE[0], SPRITE[state][1] + SPRITE_SIZE[1], null);

        //Hitbox
        g.setColor(Color.WHITE);
        g.drawRect(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT);
    }

    @Override
    public void nextFrame() {
        // Do nothing
    }

    @Override
    public void update() {

    }
}
