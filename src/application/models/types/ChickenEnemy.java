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

        protected static final int[][] OFFSET = {
        	    {-5, -10}, {-9, -14}, {-14, -19}, {-17, -22}, {-20, -25},
        	    {-22, -27}, {-23, -28}, {-27, -32}, {-29, -34}, {-31, -36},
        	    {-33, -38}, {-34, -39}, {-35, -40}, {-36, -41}, {-39, -44},
        	    {-41, -46}, {-46, -51}, {-47, -52}, {-49, -54}, {-50, -55},
        	    {-50, -55}, {-51, -56}, {-52, -57}, {-52, -57}, {-53, -58},
        	    {-53, -58}, {-54, -59}, {-54, -59}, {-54, -59}, {-55, -60},
        	    {-55, -60}, {-57, -62}, {-58, -63}, {-58, -63}, {-58, -63},
        	    {-59, -64}, {-59, -64}, {-59, -64}, {-59, -64}, {-59, -64},
        	    {-59, -64}, {-59, -64}, {-59, -64}, {-59, -64}, {-59, -64},
        	    {-59, -64}, {-59, -64}, {-59, -64}, {-59, -64}, {-59, -64},
        	    {-59, -64}, {-56, -61}
        	};


        private ChickDeathEffect(int PosX, int PosY) {
            super(PosX, PosY);

            Random random = new Random();
            spriteSheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/smoke4-white.png")).getImage();
            smokes = new ArrayList<>();
            int size = 4;
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
            private final float sizeScale; // Will be random for each smoke

            public Smoke(int PosX, int PosY) {
                Random random = new Random();
                this.PosX = PosX + (random.nextInt() % 4);
                this.PosY = PosY + (random.nextInt() % 4);
                minFrameCount = 0;
                maxFrameCount = 51;
                frameCount = minFrameCount;
                vX = (random.nextInt() % 2);
                vY = (random.nextInt() % 2);
                // Random sizeScale between 0.5 and 1.5 (half size to 1.5x size)
                sizeScale = 0.5f + random.nextFloat() * (1.0f - 0.5f);
            }

            public void render(Graphics g) {
                if (frameCount < maxFrameCount) {
                    int offsetX = OFFSET[frameCount][0];
                    int offsetY = OFFSET[frameCount][1];
                    
                    // Calculate scaled dimensions
                    int scaledWidth = (int)(SPRITE[frameCount][2] * sizeScale);
                    int scaledHeight = (int)(SPRITE[frameCount][3] * sizeScale);
                    
                    g.drawImage(spriteSheet, 
                              PosX + offsetX, 
                              PosY + offsetY, 
                              PosX + offsetX + scaledWidth, 
                              PosY + offsetY + scaledHeight, 
                              SPRITE[frameCount][0], 
                              SPRITE[frameCount][1], 
                              SPRITE[frameCount][0] + SPRITE[frameCount][2], 
                              SPRITE[frameCount][1] + SPRITE[frameCount][3], 
                              null);

//                    int centerX = PosX + offsetX + scaledWidth / 2;
//                    int centerY = PosY + offsetY + scaledHeight / 2;
//
//                    g.setColor(Color.RED);
//                    g.fillOval(centerX - 2, centerY - 2, 4, 4);
//                    
//                    System.out.println("centerX: " + centerX + ", centerY: " + centerY + ", id:" + frameCount + ", sizeScale:" + sizeScale);
//                    
                    frameCount++;
                }
            }

            public boolean isEnd() {
                return (frameCount >= maxFrameCount);
            }

            public void update() {
                PosX = PosX + vX;
                PosY = PosY + vY;
            }
        }
    }
}