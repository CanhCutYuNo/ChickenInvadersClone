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

    private double scale = 1.0;
    private double angle = 0.0;
    private boolean isScalingUp = true;
    private int maxScaleHoldTime = 3000;
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
        } else if (skillType == SkillType.HOLE) {
            this.width = 300;
            this.height = 300;
        } else if (skillType == SkillType.FIREBALL) {
            this.width = 153;
            this.height = 62;
        }
    }

    public void update() {
        if (!isActive) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (skillType == SkillType.HOLE) {
            if (isHoldingMax) {
                if (currentTime - scaleHoldStartTime >= maxScaleHoldTime) {
                    isHoldingMax = false;
                    isScalingUp = false;
                }
            } else {
                if (isScalingUp) {
                    scale += 0.05;
                    if (scale >= 4.0) {
                        scale = 4.0;
                        isHoldingMax = true;
                        scaleHoldStartTime = currentTime;
                    }
                } else {
                    scale -= 0.05;
                    if (scale <= 1.0) {
                        scale = 1.0;
                        isActive = false;
                        endTime = currentTime;
                        return;
                    }
                }
            }
            angle += Math.toRadians(2);

            if (currentTime - startTime >= duration) {
                isActive = false;
                endTime = currentTime;
            }
        } else if (skillType == SkillType.EGG) {
            if (!isExploding) {
                posY += speedY;
            } else {
                animationFrame++;
            }
        } else if (skillType == SkillType.FIREBALL) {
            posX += speedX;
            posY += speedY;
            animationFrame++;
            angle += Math.toRadians(5);

            if (posX < -100 || posX > 2020 || posY < -100 || posY > 1180) {
                isActive = false;
                endTime = currentTime;
        //        System.out.println("EnemySkills (FIREBALL) disappeared at " + currentTime);
            }
        }
    }

    public boolean isOffScreen() {
        if (skillType == SkillType.EGG) {
            return posY > 1000;
        } else if (skillType == SkillType.FIREBALL) {
            return posX < -100 || posX > 2020 || posY < -100 || posY > 1180;
        }
        return false;
    }

    public boolean removed() {
        return (isExploding && animationFrame > 32);
    }

    public void explode() {
        isExploding = true;
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

        if (skillType == SkillType.FIREBALL) {
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
}