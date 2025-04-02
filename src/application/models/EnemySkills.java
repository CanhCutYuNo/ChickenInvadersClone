package application.models;

public class EnemySkills {
    private double posX, posY;
    private double speedX, speedY; // Thay speedY thành speedX, speedY
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
    private long endTime = 0; // Thời gian kết thúc của skill

    public enum SkillType {
        EGG,
        HOLE,
        FIREBALL
    }
    private SkillType skillType;
    
    public EnemySkills(double x, double y, double speedY, int damage, SkillType skillType) {
        this.posX = x;
        this.posY = y;
        this.speedY = speedY;
        this.damage = damage;
        this.skillType = skillType;
        this.startTime = System.currentTimeMillis();
    }
    
    public EnemySkills(double x, double y, double speedX, double speedY, int damage, SkillType skillType) {
        this.posX = x;
        this.posY = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.skillType = skillType;
        this.startTime = System.currentTimeMillis();
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
                        System.out.println("EnemySkills (HOLE) disappeared at " + currentTime);
                        return;
                    }
                }
            }
            angle += Math.toRadians(2);

            if (currentTime - startTime >= duration) {
                isActive = false;
                endTime = currentTime;
                System.out.println("EnemySkills (HOLE) ended at " + currentTime);
            }
        } else if (skillType == SkillType.EGG) {
            if (!isExploding) {
                posY += speedY;
            } else {
                animationFrame++;
            }
        } else if (skillType == SkillType.FIREBALL) {
            // Cập nhật vị trí của FIREBALL dựa trên speedX và speedY
            posX += speedX;
            posY += speedY;
            animationFrame++;
            angle += Math.toRadians(5); // Xoay nhanh hơn cho FIREBALL

            // Kiểm tra nếu FIREBALL ra ngoài màn hình
            if (posX < -100 || posX > 2020 || posY < -100 || posY > 1180) {
                isActive = false;
                endTime = currentTime;
                System.out.println("EnemySkills (FIREBALL) disappeared at " + currentTime);
            }
        }
    }

    public boolean isOffScreen() {
        if (skillType == SkillType.EGG) {
            return posY > 1080;
        } else if (skillType == SkillType.FIREBALL) {
            return posX < -100 || posX > 2020 || posY < -100 || posY > 1180;
        }
        return false;
    }

    public boolean removed() {
        return (isExploding && animationFrame > 32) || !isActive;
    }

    public void explode() {
        isExploding = true;
        animationFrame = 0;
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
		// TODO Auto-generated method stub
		return speedX;
	}

	public double getSpeedY() {
		// TODO Auto-generated method stub
		return speedY;
	}
}