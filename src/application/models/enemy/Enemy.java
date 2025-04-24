package application.models.enemy;

import application.controllers.util.GameSettings;
import application.models.enemy.EnemySkills.SkillType;

import java.util.HashMap;
import java.util.Map;

public class Enemy {
    public enum EnemyType {
        CHICKEN_ENEMY, EGG_SHELL_ENEMY, CHICKEN_BOSS, CHICK_ENEMY
    }
    
    public static class EnemyState {
        public float timeElapsed;
        public boolean isActive;
        public float timeDelay;
        public float t;

        public EnemyState(float timeDelay) {
            this.timeElapsed = 0;
            this.isActive = false;
            this.timeDelay = timeDelay;
            this.t = 0;
        }
    }

    protected int hp;
    protected int posX;
    protected int posY;
    protected int speed;
    protected int initialIndex;
    protected float rotate;
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
    protected float eggProbability;
    protected int eggDamage;
    protected int moveCounter;

    public EnemyState state;

    public Enemy(int modelWidth, int modelHeight, int posX, int posY, int speed, EnemyType type) {
        this.hp = getHpByDifficulty(type);
        this.modelWidth = (type == EnemyType.EGG_SHELL_ENEMY) ? 75 : modelWidth;
        this.modelHeight = (type == EnemyType.EGG_SHELL_ENEMY) ? 97 : modelHeight;
        this.posX = posX;
        this.posY = posY;
        this.speed = speed;
        this.initialIndex = 0;
        this.rotate = 0f;
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
        this.moveCounter = 0; // Khởi tạo moveCounter
        this.state = null;

        if(type == EnemyType.CHICKEN_ENEMY) {
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
        if(type == EnemyType.CHICK_ENEMY) {
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
        } else if(type == EnemyType.CHICKEN_ENEMY) {
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
        } else if(type == EnemyType.CHICKEN_BOSS) {
            switch (GameSettings.getInstance().getDifficulty()) {
            case EASY:
                return 1000;
            case NORMAL:
                return 1500;
            case HARD:
                return 2000;
            case EXTREME:
                return 3500;
            default:
                return 1000;
            }
        }
            
        return 400;
    }

    public boolean isDead() { return hp <= 0; }
    public int getCenterX() { return posX + modelWidth / 2; }
    public int getCenterY() { return posY + modelHeight / 2; }
    public EnemyType getType() { return type; }
    public int getChickCount() { return chickCount; }
    public void setChickCount(int chickCount) { this.chickCount = chickCount; }
    public boolean isMovingToCenter() { return isMovingToCenter; }
    public void setMovingToCenter(boolean movingToCenter) { this.isMovingToCenter = movingToCenter; }
    public int getStartY() { return startY; }
    public int getTargetY() { return targetY; }
    public int getMoveSpeed() { return moveSpeed; }
    public long getLastHoleSkillTime() { return lastHoleSkillTime; }
    public void setLastHoleSkillTime(long lastHoleSkillTime) { this.lastHoleSkillTime = lastHoleSkillTime; }
    public long getLastFireballSkillTime() { return lastFireballSkillTime; }
    public void setLastFireballSkillTime(long lastFireballSkillTime) { this.lastFireballSkillTime = lastFireballSkillTime; }
    public long getHoleSkillCooldown() { return holeSkillCooldown; }
    public long getFireballSkillCooldown() { return fireballSkillCooldown; }
    public long getSkillsDelay() { return skillsDelay; }
    public int getSkillState() { return skillState; }
    public void setSkillState(int skillState) { this.skillState = skillState; }
    public boolean shouldCreateHole() { return shouldCreateHole; }
    public void setShouldCreateHole(boolean shouldCreateHole) { this.shouldCreateHole = shouldCreateHole; }
    public boolean shouldCreateFireballBurst() { return shouldCreateFireballBurst; }
    public void setShouldCreateFireballBurst(boolean shouldCreateFireballBurst) { this.shouldCreateFireballBurst = shouldCreateFireballBurst; }
    public float getEggProbability() { return eggProbability; }
    public int getEggDamage() { return eggDamage; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = posX; }
    public int getPosY() { return posY; }
    public void setPosY(int posY) { this.posY = posY; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public int getInitialIndex() { return initialIndex; }
    public void setInitialIndex(int initialIndex) { this.initialIndex = initialIndex; }
    public float getRotate() { return rotate; }
    public void setRotate(float rotate) { this.rotate = rotate; }
    public int getModelWidth() { return modelWidth; }
    public int getModelHeight() { return modelHeight; }
    public Map<SkillType, String> getSkills() { return skills; }
    public static int getMapWidth() { return MAP_WIDTH; }
    public int getInitialY() { return (type == EnemyType.CHICKEN_BOSS) ? startY : posY; }
    public void setState(EnemyState state) { this.state = state; }
    public int getMoveCounter() { return moveCounter; }
    public void setMoveCounter(int moveCounter) { this.moveCounter = moveCounter; }
}