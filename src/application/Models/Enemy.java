package application.Models;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Enemy {
    private Image spriteBodySheet;
    private Image spriteWingsSheet;
    private int currentFrame = 0;
    private int[] bodySprite; // Lưu tọa độ body
    private List<int[]> wingSprites = new ArrayList<>();
    private int hp;
    private int posX;
    private int posY;
    private boolean isForward = true; // Biến để theo dõi hướng di chuyển của animation
    private static final int MODEL_WIDTH = 64;
    private static final int MODEL_HEIGHT = 64;
    private static final int MAP_WIDTH = 1900;
    private static final int MAP_HEIGHT = 1080;

    // Danh sách tọa độ của các body trên sprite sheet
    private static final int[][] SPRITE_BODY = {
        {1, 1, 67, 108}, {71, 1, 67, 108}, {141, 1, 67, 108},
        {211, 1, 67, 108}
    };

    // Danh sách các vị trí của cánh trên sprite sheet
//    private static final int[][] SPRITE_WINGS = {
//        {1, 1, 125, 107}, {129, 1, 125, 107}, {257, 1, 125, 107},
//        {385, 1, 125, 107}, {513, 1, 125, 107}, {641, 1, 125, 106},
//        {769, 1, 125, 106}, {897, 1, 127, 106}, {1027, 1, 127, 105},
//        {1157, 1, 127, 105}, {1287, 1, 127, 104}, {1, 111, 129, 104},
//        {133, 111, 129, 103}, {265, 111, 129, 102}, {397, 111, 131, 101},
//        {531, 111, 131, 100}, {665, 111, 131, 99}, {799, 111, 133, 98},
//        {935, 111, 133, 96}, {1071, 111, 135, 95}, {1209, 111, 137, 94},
//        {1349, 111, 139, 92}, {1, 217, 139, 90}, {143, 217, 141, 88},
//        {287, 217, 142, 86}, {431, 217, 143, 84}, {577, 217, 143, 82},
//        {723, 217, 145, 81}, {871, 217, 145, 79}, {1019, 217, 147, 78},
//        {1169, 217, 149, 77}, {1321, 217, 149, 75}, {1, 309, 151, 74}, 
//        {155, 309, 151, 73}, {309, 309, 153, 73}, {465, 309, 153, 73}, 
//        {621, 309, 153, 72}, {777, 309, 153, 71}, {933, 309, 154, 71},
//        {1089, 309, 153, 71}, {1245, 309, 153, 70}, {1, 385, 153, 70},
//        {157, 385, 153, 70}, {313, 385, 153, 70}, {469, 285, 153, 70},
//        {625, 385, 153, 70}, {781, 385, 153, 69}, {937, 385, 153, 69},
//        {1093, 385, 153, 69}, {1249, 385, 153, 69}
//    };
    
    private static final int[][] SPRITE_WINGS = {
            {1, 1, 126, 112}, {129, 1, 126, 112}, {257, 1, 126, 112},
            {385, 1, 126, 112}, {513, 1, 126, 112}, {641, 1, 126, 111},
            {769, 1, 126, 111}, {897, 1, 126, 111}, {1025, 1, 126, 110},
            {1153, 1, 128, 110}, {1283, 1, 128, 108}, {1413, 1, 128, 108}, 
            {1543, 1, 130, 107}, {1675, 1, 130, 106}, {1807, 1, 130, 106}, {1, 115, 132, 105},
            {135, 115, 132, 104}, {269, 115, 132, 103}, {403, 115, 134, 101},
            {539, 115, 136, 99}, {677, 115, 137, 98}, {817, 115, 138, 96},
            {957, 115, 140, 94}, {1099, 115, 140, 93}, {1241, 115, 142, 90},
            {1385, 115, 142, 88},  {1529, 115, 144, 86}, {1675, 115, 144, 85}, {1821, 115, 144, 83},
            {1, 223, 146, 82}, {149, 223, 148, 80},  {299, 223, 150, 79},
            {451, 223, 150, 78}, {603, 223, 152, 77}, {757, 223, 152, 77},
            {911, 223, 152, 76}, {1065, 223, 154, 75}, {1221, 223, 154, 75},
            {1377, 223, 154, 75}, {1533, 223, 154, 74}, {1689, 223, 154, 74}, 
            {1845, 223, 154, 74}, {1, 307, 154, 73}, {157, 307, 154, 73}, 
            {313, 307, 154, 73}, {469, 307, 153, 73}, {625, 307, 152, 73},
            {779, 307, 152, 73}, {933, 307, 152, 73}, {1087, 307, 153, 73},
        };

    public Enemy(int hp, Image bodySheet, Image wingsSheet) {
        this.hp = hp;
        this.spriteBodySheet = bodySheet;
        this.spriteWingsSheet = wingsSheet;

        Random random = new Random();
        this.posX = random.nextInt(MAP_WIDTH);
        this.posY = random.nextInt(MAP_HEIGHT / 2);

        // Chọn ngẫu nhiên một phần của body
        this.bodySprite = SPRITE_BODY[random.nextInt(SPRITE_BODY.length)];

        // Thêm tất cả các frame của cánh vào danh sách
        for(int[] frame : SPRITE_WINGS) {
            wingSprites.add(frame);
        }
    }

    public void render(Graphics g) {
        if(spriteBodySheet != null && spriteWingsSheet != null) {
        	int[] wingFrame = wingSprites.get(currentFrame);
        	int wingWidth = wingFrame[2];
        	int wingHeight = wingFrame[3]; 
        	int[][] wingOffsets = {
        			//1-4
        		    { -5, -10 }, { -5, -10 }, { -5, -10 }, { -5, -10 }, { -5, -10 },
        		    //5-9
        		    { -5, -10 }, { -5, -10 }, { -5, -10 }, { -5, -9 }, { -5, -9 },
        		    //10-14
        		    { -5, -9 }, { -5, -9 }, { -5, -9 }, { -5, -8 }, { -5, -8 },
        		    //15-19
        		    { -5, -8 }, { -5, -7 }, { -5, -7 }, { -5, -6 }, { -5, -6 },
        		    //20-24
        		    { -5, -5 }, { -5, -4 }, { -5, -3 }, { -5, -3 }, { -5, -2 },
        		    //25-29
        		    { -5, -1 }, { -5, 0 }, { -5, 0 }, { -5, 1 }, { -5, 2 },
        		    //30-34
        		    { -5, 2 }, { -5, 2 }, { -5, 3 }, { -5, 3 }, { -5, 3 },
        		    //35-39
        		    { -5, 4 }, { -5, 4 }, { -5, 4 }, { -5, 4 }, { -5, 5 },
        		    //40-44
        		    { -5, 5 }, { -5, 5 }, { -5, 5 }, { -5, 5 }, { -5, 5 },
        		    //45-49
        		    { -5, 5 }, { -5, 5 }, { -5, 5 }, { -5, 5 }, { -5, 5 },
        		};
        	// Lấy offset của frame hiện tại
        	int[] wingOffset = wingOffsets[currentFrame];
        	int offsetX = wingOffset[0];
        	int offsetY = wingOffset[1];
        	int centerX = posX + bodySprite[2] / 2;
        	int centerY = posY + bodySprite[3] / 2;


        	// Vẽ cánh với offset
        	g.drawImage(spriteWingsSheet, 
        	    centerX - wingWidth / 2 + offsetX, centerY - wingHeight / 2 + offsetY - 1,  
        	    centerX + wingWidth / 2 + offsetX, centerY + wingHeight / 2 + offsetY - 1,
        	    wingFrame[0], wingFrame[1], 
        	    wingFrame[0] + wingWidth, wingFrame[1] + wingHeight, null);
        	
//        	// Debug: Vẽ khung viền đỏ quanh cánh 
//        	g.setColor(Color.RED);
//        	g.drawRect(centerX - wingWidth / 2 + offsetX, centerY - wingHeight / 2 + offsetY, wingWidth, wingHeight);
//
//        	// Debug: Vẽ tâm (dấu chấm đỏ) ở trung tâm cánh
//        	g.setColor(Color.RED);
//        	g.fillOval(centerX + offsetX - 2, centerY + offsetY - 2, 4, 4);
      
            // Vẽ body
            g.drawImage(spriteBodySheet, posX - 5, posY - 21, posX + bodySprite[2] - 5, posY + bodySprite[3] - 21,
                    bodySprite[0], bodySprite[1], bodySprite[0] + bodySprite[2], bodySprite[1] + bodySprite[3], null);
//           g.drawImage(spriteBodySheet, posX, posY, posX, posY,
//                    bodySprite[0], bodySprite[1], bodySprite[0] + bodySprite[2], bodySprite[1] + bodySprite[3], null);
//            g.setColor(Color.RED);
//        	g.drawRect(posX, posY, bodySprite[2], bodySprite[3]);
        } else {
            g.setColor(Color.RED);
            g.fillRect(posX, posY, MODEL_WIDTH, MODEL_HEIGHT);
        }
    }

    public void nextFrame() {
        if(isForward) {
            currentFrame++;
            if(currentFrame >= 48) {
                isForward = false; // Đổi hướng khi đến cuối mảng
            }
        } else {
            currentFrame--;
            if(currentFrame <= 0) {
                isForward = true; // Đổi hướng khi về đầu mảng
            }
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(posX, posY, MODEL_WIDTH, MODEL_HEIGHT);
    }

    public int getHp() {
        return hp;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
    
    public int getCurrentFrame() {
    	return currentFrame;
    }
}