package application.models.types;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.ImageIcon;

import application.controllers.GameSettings;
import application.controllers.SoundController;
import application.models.DeathEffect;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;

public class ChickEnemy extends Enemy {

    protected Image spriteSheet;

    public ChickEnemy(int PosX, int PosY, SoundController sound) {
        super(getHpByDifficulty(), 46, 54, PosX, PosY, sound, null, null);
        curFrame = (int) (Math.random() % 26);
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

    private static Map<SkillType, String> createSkillImagePaths() {
        return new HashMap<>();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(spriteSheet,
                PosX - offsetX[curFrame] - 15, PosY - 15, PosX + SPRITE[curFrame][2] - offsetX[curFrame] - 15, PosY + SPRITE[curFrame][3] - 15,
                SPRITE[curFrame][0], SPRITE[curFrame][1], SPRITE[curFrame][0] + SPRITE[curFrame][2], SPRITE[curFrame][1] + SPRITE[curFrame][3], null);
    }

    @Override
    public void nextFrame() {
        if (isForward) {
            curFrame++;
            if (curFrame >= 25) {
                isForward = false; // Đổi hướng khi đến cuối mảng
            }
        } else {
            curFrame--;
            if (curFrame <= 0) {
                isForward = true; // Đổi hướng khi về đầu mảng
            }
        }
    }

    @Override
    public void update() {
        // Không có logic cập nhật
    }

    @Override
    public DeathEffect getDeathEffect() {
        return new ChickDeathEffect(getCenterX(), getCenterY());
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT);
    }

    @Override
    public Map<SkillType, String> getSkills() {
        return createSkillImagePaths();
    }

    @Override
    public void addSkills(SkillType skillType, String imagePath) {
        skills.put(skillType, imagePath);
    }

    private static int getHpByDifficulty() {
        switch (GameSettings.getInstance().getDifficulty()) {
            case EASY:
                return 40;
            case NORMAL:
                return 50;
            case HARD:
                return 60;
            case EXTREME:
                return 65;
            default:
                return 50;
        }
    }
}