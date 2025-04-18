package application.models;

import application.controllers.GameSettings;
import application.models.EnemySkills.SkillType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Enemy {
    public enum EnemyType {
        CHICKEN_ENEMY, EGG_SHELL_ENEMY, CHICKEN_BOSS, CHICK_ENEMY
    }

    protected int hp;
    protected int posX;
    protected int posY;
    protected int speed;
    protected int initialIndex;
    protected float rotate;
    protected int curFrame;
    protected int frameCount;
    protected boolean isForward;
    protected int modelWidth;
    protected int modelHeight;
    protected Map<SkillType, String> skills;
    protected static final int MAP_WIDTH = 1900;
    protected EnemyType type;
    protected int chickCount;
    protected boolean isMovingToCenter;
    protected int startY;
    protected int targetY;
    protected int moveSpeed;
    protected long lastHoleSkillTime;
    protected long lastFireballSkillTime;
    protected long holeSkillCooldown;
    protected long fireballSkillCooldown;
    protected long skillsDelay;
    protected int skillState;
    protected boolean shouldCreateHole;
    protected boolean shouldCreateFireballBurst;
    protected long lastFrameTime;
    protected int spriteDelay;
    // Thuộc tính bổ sung cho ChickenEnemy
    protected int[] bodySprite;
    protected int[] headSprite;
    protected List<int[]> wingSprites;
    protected int[][] wingOffsets;
    protected float eggProbability;
    protected int eggDamage;

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

    public Enemy(int hp, int modelWidth, int modelHeight, int posX, int posY, int speed, EnemyType type) {
        this.hp = (type == EnemyType.EGG_SHELL_ENEMY) ? 400 : 
                 (type == EnemyType.CHICKEN_ENEMY || type == EnemyType.CHICK_ENEMY) ? getHpByDifficulty(type) : hp;
        this.modelWidth = (type == EnemyType.EGG_SHELL_ENEMY) ? 75 : modelWidth;
        this.modelHeight = (type == EnemyType.EGG_SHELL_ENEMY) ? 97 : modelHeight;
        this.posX = posX;
        this.posY = posY;
        this.speed = speed;
        this.initialIndex = 0;
        this.rotate = 0f;
        this.curFrame = (type == EnemyType.CHICK_ENEMY) ? (int) (Math.random() % 26) : 
                       (type == EnemyType.EGG_SHELL_ENEMY) ? 0 : (int) (Math.random() * 40);
        this.frameCount = (type == EnemyType.CHICK_ENEMY || type == EnemyType.EGG_SHELL_ENEMY) ? 0 : (int) (Math.random() * 120);
        this.isForward = true;
        this.skills = new HashMap<>();
        this.type = type;
        this.chickCount = -1;
        this.isMovingToCenter = type == EnemyType.CHICKEN_BOSS;
        this.startY = type == EnemyType.CHICKEN_BOSS ? 1400 : posY;
        this.targetY = type == EnemyType.CHICKEN_BOSS ? (1080 - 600) / 2 : posY;
        this.moveSpeed = type == EnemyType.CHICKEN_BOSS ? 2 : 0;
        this.lastHoleSkillTime = type == EnemyType.CHICKEN_BOSS ? System.currentTimeMillis() : 0;
        this.lastFireballSkillTime = type == EnemyType.CHICKEN_BOSS ? System.currentTimeMillis() : 0;
        this.holeSkillCooldown = type == EnemyType.CHICKEN_BOSS ? 15000 : 0;
        this.fireballSkillCooldown = type == EnemyType.CHICKEN_BOSS ? 5000 : 0;
        this.skillsDelay = type == EnemyType.CHICKEN_BOSS ? 5000 : 0;
        this.skillState = 0;
        this.shouldCreateHole = false;
        this.shouldCreateFireballBurst = false;
        this.lastFrameTime = System.currentTimeMillis();
        this.spriteDelay = type == EnemyType.CHICKEN_BOSS ? 30 : 0;

        // Khởi tạo cho ChickenEnemy
        if (type == EnemyType.CHICKEN_ENEMY) {
            this.bodySprite = SPRITE_BODY[(int) (Math.random() * SPRITE_BODY.length)];
            this.headSprite = SPRITE_HEAD[0];
            this.wingSprites = new ArrayList<>();
            for (int[] frame : SPRITE_WINGS) {
                this.wingSprites.add(frame);
            }
            this.wingOffsets = new int[][] {
                {-5, -10}, {-5, -10}, {-5, -10}, {-5, -10}, {-5, -10},
                {-5, -10}, {-5, -10}, {-5, -10}, {-5, -9}, {-5, -9},
                {-5, -9}, {-5, -9}, {-5, -9}, {-5, -8}, {-5, -8},
                {-5, -8}, {-5, -7}, {-5, -7}, {-5, -6}, {-5, -6},
                {-5, -5}, {-5, -4}, {-5, -3}, {-5, -3}, {-5, -2},
                {-5, -1}, {-5, 0}, {-5, 0}, {-5, 1}, {-5, 2},
                {-5, 2}, {-5, 2}, {-5, 3}, {-5, 3}, {-5, 3},
                {-5, 4}, {-5, 4}, {-5, 4}, {-5, 4}, {-5, 5},
                {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5},
                {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}, {-5, 5}
            };
            switch (GameSettings.getInstance().getDifficulty()) {
                case EASY:
                    this.eggProbability = 0.0002f;
                    this.eggDamage = 30;
                    break;
                case NORMAL:
                    this.eggProbability = 0.0003f;
                    this.eggDamage = 40;
                    break;
                case HARD:
                    this.eggProbability = 0.0006f;
                    this.eggDamage = 50;
                    break;
                case EXTREME:
                    this.eggProbability = 0.0008f;
                    this.eggDamage = 60;
                    break;
                default:
                    this.eggProbability = 0.0003f;
                    this.eggDamage = 40;
            }
        }
    }

    private int getHpByDifficulty(EnemyType type) {
        if (type == EnemyType.CHICK_ENEMY) {
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
        } else if (type == EnemyType.CHICKEN_ENEMY) {
            switch (GameSettings.getInstance().getDifficulty()) {
                case EASY:
                    return 90;
                case NORMAL:
                    return 100;
                case HARD:
                    return 120;
                case EXTREME:
                    return 130;
                default:
                    return 100;
            }
        }
        return hp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public int getCenterX() {
        return (posX + modelWidth / 2);
    }

    public int getCenterY() {
        return (posY + modelHeight / 2);
    }

    public EnemyType getType() {
        return type;
    }

    public int getChickCount() {
        return chickCount;
    }

    public void setChickCount(int chickCount) {
        this.chickCount = chickCount;
    }

    public boolean isMovingToCenter() {
        return isMovingToCenter;
    }

    public void setMovingToCenter(boolean movingToCenter) {
        this.isMovingToCenter = movingToCenter;
    }

    public int getStartY() {
        return startY;
    }

    public int getTargetY() {
        return targetY;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public long getLastHoleSkillTime() {
        return lastHoleSkillTime;
    }

    public void setLastHoleSkillTime(long lastHoleSkillTime) {
        this.lastHoleSkillTime = lastHoleSkillTime;
    }

    public long getLastFireballSkillTime() {
        return lastFireballSkillTime;
    }

    public void setLastFireballSkillTime(long lastFireballSkillTime) {
        this.lastFireballSkillTime = lastFireballSkillTime;
    }

    public long getHoleSkillCooldown() {
        return holeSkillCooldown;
    }

    public long getFireballSkillCooldown() {
        return fireballSkillCooldown;
    }

    public long getSkillsDelay() {
        return skillsDelay;
    }

    public int getSkillState() {
        return skillState;
    }

    public void setSkillState(int skillState) {
        this.skillState = skillState;
    }

    public boolean shouldCreateHole() {
        return shouldCreateHole;
    }

    public void setShouldCreateHole(boolean shouldCreateHole) {
        this.shouldCreateHole = shouldCreateHole;
    }

    public boolean shouldCreateFireballBurst() {
        return shouldCreateFireballBurst;
    }

    public void setShouldCreateFireballBurst(boolean shouldCreateFireballBurst) {
        this.shouldCreateFireballBurst = shouldCreateFireballBurst;
    }

    public long getLastFrameTime() {
        return lastFrameTime;
    }

    public void setLastFrameTime(long lastFrameTime) {
        this.lastFrameTime = lastFrameTime;
    }

    public int getSpriteDelay() {
        return spriteDelay;
    }

    public int[] getBodySprite() {
        return bodySprite;
    }

    public int[] getHeadSprite() {
        return headSprite;
    }

    public List<int[]> getWingSprites() {
        return wingSprites;
    }

    public int[][] getWingOffsets() {
        return wingOffsets;
    }

    public float getEggProbability() {
        return eggProbability;
    }

    public int getEggDamage() {
        return eggDamage;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getInitialIndex() {
        return initialIndex;
    }

    public void setInitialIndex(int initialIndex) {
        this.initialIndex = initialIndex;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public int getCurFrame() {
        return curFrame;
    }

    public void setCurFrame(int curFrame) {
        this.curFrame = curFrame;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public boolean isForward() {
        return isForward;
    }

    public void setForward(boolean forward) {
        isForward = forward;
    }

    public int getModelWidth() {
        return modelWidth;
    }

    public int getModelHeight() {
        return modelHeight;
    }

    public Map<SkillType, String> getSkills() {
        return skills;
    }

    public static int getMapWidth() {
        return MAP_WIDTH;
    }
}