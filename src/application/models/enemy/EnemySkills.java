package application.models.enemy;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class EnemySkills {
    private double posX, posY;
    private double speedX, speedY;
    private boolean isExploding = false;
    private int animationFrame = 0;
    private int damage;
    private boolean hasPlayedSound = false;

    private double scale = 1.0;
    private double angle = 0.0;
    private boolean isScalingUp = true;
    private long scaleHoldStartTime = 0;
    private boolean isHoldingMax = false;
    private boolean isActive = true;
    private long startTime = 0;
    private long duration = 10000;
    private long endTime = 0;

    private int width;
    private int height;

    public enum SkillType {
        EGG,
        HOLE,
        FIREBALL;
    }
    
    private SkillType skillType;

    public EnemySkills(double x, double y, double speedY, int damage, SkillType skillType) {
        this.posX = x;
        this.posY = y;
        this.speedY = speedY;
        this.damage = damage;
        this.skillType = skillType;
        this.startTime = System.currentTimeMillis();
        setDefaultSize();
    }

    public EnemySkills(double x, double y, double speedX, double speedY, int damage, SkillType skillType) {
        this.posX = x;
        this.posY = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.skillType = skillType;
        this.startTime = System.currentTimeMillis();
        setDefaultSize();
    }

    public EnemySkills(double x, double y, double speedX, double speedY, int damage, SkillType skillType, int width, int height) {
        this.posX = x;
        this.posY = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.skillType = skillType;
        this.width = width;
        this.height = height;
        this.startTime = System.currentTimeMillis();
    }

    private void setDefaultSize() {
        if(skillType == SkillType.EGG) {
            this.width = 31;
            this.height = 45;
        } else if(skillType == SkillType.HOLE) {
            this.width = 300;
            this.height = 300;
        } else if(skillType == SkillType.FIREBALL) {
            this.width = 153;
            this.height = 62;
        }
    }

    public boolean isOffScreen() {
        if(skillType == SkillType.EGG) {
            return posY > 1000;
        } else if(skillType == SkillType.FIREBALL) {
            return posX < -100 || posX > 2020 || posY < -100 || posY > 1180;
        }
        return false;
    }

    public boolean removed() {
        return (isExploding && animationFrame > 32);
    }

    public void explode() {
        isExploding = true;
        hasPlayedSound = false;
    }

    public boolean isExploding() {
        return isExploding;
    }

    public int getAnimationFrame() {
        return animationFrame;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getDamage() {
        return damage;
    }

    public double getScale() {
        return scale;
    }

    public double getAngle() {
        return angle;
    }

    public boolean isActive() {
        return isActive;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public long getEndTime() {
        return endTime;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public Shape getHitbox() {
        double scaleFactor = (skillType == SkillType.HOLE) ? scale : 1.0;

        int scaledWidth = (int) (width * scaleFactor);
        int scaledHeight = (int) (height * scaleFactor);

        int hitboxX = (int) (posX - scaledWidth / 2 + 15); 
        int hitboxY = (int) (posY - scaledHeight / 2 + 20);

        Rectangle2D rect = new Rectangle2D.Double(hitboxX, hitboxY, scaledWidth, scaledHeight);

        if(skillType == SkillType.FIREBALL) {
            double angle = Math.atan2(speedY, speedX);
            AffineTransform transform = new AffineTransform();
            transform.rotate(angle, posX, posY);
            return transform.createTransformedShape(rect);
        }

        return rect;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasPlayedSound() {
        return hasPlayedSound;
    }

    public void setHasPlayedSound(boolean hasPlayedSound) {
        this.hasPlayedSound = hasPlayedSound;
    }

    // New getters and setters for variables in update()
    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public void setIsExploding(boolean isExploding) {
        this.isExploding = isExploding;
    }

    public void setAnimationFrame(int animationFrame) {
        this.animationFrame = animationFrame;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setIsScalingUp(boolean isScalingUp) {
        this.isScalingUp = isScalingUp;
    }

    public boolean isScalingUp() {
        return isScalingUp;
    }

    public void setIsHoldingMax(boolean isHoldingMax) {
        this.isHoldingMax = isHoldingMax;
    }

    public boolean isHoldingMax() {
        return isHoldingMax;
    }

    public void setScaleHoldStartTime(long scaleHoldStartTime) {
        this.scaleHoldStartTime = scaleHoldStartTime;
    }

    public long getScaleHoldStartTime() {
        return scaleHoldStartTime;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }
}