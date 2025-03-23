package application.models.types;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import application.controllers.SoundController;
import application.models.Enemy;

public class ChickenBoss extends Enemy {
    protected Image spriteBody;
    protected Image spriteBodySheet;
    protected Image spriteLimbs;
    
    protected List<int[]> bodySprites = new ArrayList<>();
    
    protected float rotate = 0f;
    private int initialIndex;
    private int currentFrame = 0; // Thêm biến currentFrame
    private static final int FRAME_COUNT = 50; // Số frame trong sprite sheet
    
    private static final int[][] SPRITE_BODY = {
            {1, 1, 126, 112}, {129, 1, 126, 112}, {257, 1, 126, 112},
            {385, 1, 126, 112}, {513, 1, 126, 112}, {641, 1, 126, 111},
            {769, 1, 126, 111}, {897, 1, 126, 111}, {1025, 1, 126, 110},
            {1153, 1, 128, 110}, {1283, 1, 128, 108}, {1413, 1, 128, 108},
            {1543, 1, 130, 107}, {1675, 1, 130, 106}, {1807, 1, 130, 106}, {1, 115, 132, 105},
            {135, 115, 132, 104}, {269, 115, 132, 103}, {403, 115, 134, 101},
            {539, 115, 136, 99}, {677, 115, 137, 98}, {817, 115, 138, 96},
            {957, 115, 140, 94}, {1099, 115, 140, 93}, {1241, 115, 142, 90},
            {1385, 115, 142, 88}, {1529, 115, 144, 86}, {1675, 115, 144, 85}, {1821, 115, 144, 83},
            {1, 223, 146, 82}, {149, 223, 148, 80}, {299, 223, 150, 79},
            {451, 223, 150, 78}, {603, 223, 152, 77}, {757, 223, 152, 77},
            {911, 223, 152, 76}, {1065, 223, 154, 75}, {1221, 223, 154, 75},
            {1377, 223, 154, 75}, {1533, 223, 154, 74}, {1689, 223, 154, 74},
            {1845, 223, 154, 74}, {1, 307, 154, 73}, {157, 307, 154, 73},
            {313, 307, 154, 73}, {469, 307, 153, 73}, {625, 307, 152, 73},
            {779, 307, 152, 73}, {933, 307, 152, 73}, {1087, 307, 153, 73}
    };
    
    private static final int[][] SPRITE_LIMBS = {
            {1, 1, 201, 191}, {205, 1, 201, 191}, {1, -195, 284, 185}
    };

    public ChickenBoss(int PosX, int PosY, SoundController sound) {
        super(1000, 300, 400, PosX, PosY, sound); // Khớp với constructor của Enemy
        System.out.println("ChickenBoss created at (" + PosX + "," + PosY + ")");
        spriteBodySheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-wings.png")).getImage();
        spriteBody = new ImageIcon(getClass().getResource("/asset/resources/gfx/henperor-body.png")).getImage();
        spriteLimbs = new ImageIcon(getClass().getResource("/asset/resources/gfx/henperor-limbs.png")).getImage();
        
        for (int[] frame : SPRITE_BODY) {
            bodySprites.add(frame);
        }
    }
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int centerX = PosX + MODEL_WIDTH / 2;
        int centerY = PosY + MODEL_HEIGHT / 2;
       // g2d.rotate(Math.toRadians(rotate), centerX, centerY);

        int[] bodyFrame = bodySprites.get(currentFrame);
        int bodyWidth = bodyFrame[2];
        int bodyHeight = bodyFrame[3];
        int[][] bodyOffsets = {
            {-5, -10}, {-5, -10}, {-5, -10}, {-5, -10}, {-5, -10},
            {-5, -10}, {-5, -10}, {-5, -10}, {-5, -9}, {-5, -9},
            {-5, -9}, {-5, -9}, {-5, -9}, {-5, -8}, {-5, -8},
            {-5, -8}, {-5, -7}, {-5, -7}, {-5, -6}, {-5, -6},
            {-5, -5}, {-5, -4}, {-5, -3}, {-5, -3}, {-5, -2},
            {-5, -1}, {-5, 0}, {-5, 0}, {-5, 1}, {-5, 2},
            {-5, 2}, {-5, 2}, {-5, 3}, {-5, 3}, {-5, 3},
            {-5, 4}, {-5, 4}, {-5, 4}, {-5, 4}, {-5, 5},
            {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5},
            {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5},
        };
        int[] bodyOffset = bodyOffsets[currentFrame];
        int offsetX = bodyOffset[0];
        int offsetY = bodyOffset[1];
        int bodyCenterX = PosX;
        int bodyCenterY = PosY;

        g2d.drawImage(spriteBodySheet,
                bodyCenterX - bodyWidth / 2 + offsetX, bodyCenterY - bodyHeight / 2 + offsetY - 1,
                bodyCenterX + bodyWidth / 2 + offsetX, bodyCenterY + bodyHeight / 2 + offsetY - 1,
                bodyFrame[0], bodyFrame[1],
                bodyFrame[0] + bodyWidth, bodyFrame[1] + bodyHeight, null);

        // Sửa cách vẽ spriteBody
        g2d.drawImage(spriteBody, PosX, PosX - 50, PosX + MODEL_WIDTH, PosX - 50 + MODEL_HEIGHT, null);

        g2d.drawImage(spriteLimbs, PosX - 50, PosX - 20, PosX - 50 + 201, PosX - 20 + 191, 1, 1, 1 + 201, 1 + 191, null);

        g2d.drawImage(spriteLimbs, PosX + 50, PosX - 20, PosX + 50 + 201, PosX - 20 + 191, 205, 1, 205 + 201, 1 + 191, null);

        g2d.drawImage(spriteLimbs, PosX - 70, PosX + 50, PosX - 70 + 284, PosX + 50 + 185, 1, -195, 1 + 284, -195 + 185, null);              

        g2d.dispose();
    }

    @Override
    public void nextFrame() {
    	if(isForward) {
            currentFrame++;
            if (currentFrame >= 48) {
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
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            // Phát âm thanh chết
            sound.playSoundEffect("/asset/resources/sfx/death1.wav");
        }
    }

    public int getInitialIndex() {
        return initialIndex;
    }
    
    public void setInitialIndex(int initialIndex) {
        this.initialIndex = initialIndex;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }
}