package application.models.types;

import application.controllers.SoundController;
import application.models.DeathEffect;
import application.models.Enemy;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;

public class ChickenEnemy extends Enemy {
    protected Image spriteHeadSheet;
    protected Image spriteBodySheet;
    protected Image spriteWingsSheet;
    protected Image blinkAnimation;
    
    protected int[] headSprite;
    protected int[] bodySprite;
    protected List<int[]> wingSprites = new ArrayList<>();
    protected float rotate = 0f;
    private int initialIndex;
    
    private static final int[][] SPRITE_HEAD = {{195, 93, 30, 45}};
    private static final int[][] SPRITE_BODY = {
        {1, 1, 70, 53}, {217, 1, 70, 53}, {433, 1, 70, 53},
        {217, 169, 70, 53}
    };
    private static final int[][] SPRITE_WINGS = {
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

    public ChickenEnemy(int PosX, int PosY, SoundController sound) {
        super(100, 64, 64, PosX, PosY, sound);
        
        spriteBodySheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-body-stripes.png")).getImage();
        spriteWingsSheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-wings.png")).getImage();
        spriteHeadSheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/chicken-face.png")).getImage();
        blinkAnimation = new ImageIcon(getClass().getResource("/asset/resources/gfx/chickenBlink.png")).getImage();

        if (spriteBodySheet == null) {
            System.err.println("Không tải được spriteBodySheet!");
        }
        if (spriteWingsSheet == null) {
            System.err.println("Không tải được spriteWingsSheet!");
        }
        if (spriteHeadSheet == null) {
            System.err.println("Không tải được spriteHeadSheet!");
        }
        if (blinkAnimation == null) {
            System.err.println("Không tải được blinkAnimation!");
        }

        Random random = new Random();
        this.bodySprite = SPRITE_BODY[random.nextInt(SPRITE_BODY.length)];
        this.headSprite = SPRITE_HEAD[0];

        for (int[] frame : SPRITE_WINGS) {
            wingSprites.add(frame);
        }
    }

    public void setRotate(float angle) {
        this.rotate = angle;
    }

    @Override
    public void render(Graphics g) {
        if (spriteBodySheet == null || spriteWingsSheet == null) {
            g.setColor(Color.RED);
            g.fillRect(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT);
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        int centerX = PosX + MODEL_WIDTH / 2;
        int centerY = PosY + MODEL_HEIGHT / 2;
        g2d.rotate(Math.toRadians(rotate), centerX, centerY);

        int[] wingFrame = wingSprites.get(currentFrame);
        int wingWidth = wingFrame[2];
        int wingHeight = wingFrame[3];
        int[][] wingOffsets = {
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
        int[] wingOffset = wingOffsets[currentFrame];
        int offsetX = wingOffset[0];
        int offsetY = wingOffset[1];
        int wingCenterX = PosX + bodySprite[2] / 2;
        int wingCenterY = PosY + bodySprite[3] / 2;

        g2d.drawImage(spriteWingsSheet,
                wingCenterX - wingWidth / 2 + offsetX, wingCenterY - wingHeight / 2 + offsetY - 1,
                wingCenterX + wingWidth / 2 + offsetX, wingCenterY + wingHeight / 2 + offsetY - 1,
                wingFrame[0], wingFrame[1],
                wingFrame[0] + wingWidth, wingFrame[1] + wingHeight, null);

        g2d.drawImage(spriteHeadSheet, PosX + 15, PosY - 50, PosX + headSprite[2] + 15, PosY + headSprite[3] - 50,
                headSprite[0], headSprite[1], headSprite[0] + headSprite[2], headSprite[1] + headSprite[3], null);

        g2d.drawImage(spriteBodySheet, PosX - 5, PosY - 10, PosX + bodySprite[2] - 5, PosY + bodySprite[3] - 10,
                bodySprite[0], bodySprite[1], bodySprite[0] + bodySprite[2], bodySprite[1] + bodySprite[3], null);

        if (frameCount < 20) {
            g2d.drawImage(blinkAnimation, PosX + 23, PosY - 40, 50, 40, null);
        }
        frameCount++;
        if (frameCount > 120) {
            frameCount = 0;
        }

        g2d.dispose();
    }

    @Override
    public void update() {
 
    }

    public int getInitialIndex() {
        return initialIndex;
    }
    
    public void setInitialIndex(int initialIndex) {
        this.initialIndex = initialIndex;
    }
    
    @Override
    public DeathEffect getDeathEffect() {
        return new DeathEffect(getCenterX(), getCenterY());
    }
}