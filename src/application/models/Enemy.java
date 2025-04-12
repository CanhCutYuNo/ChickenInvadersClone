package application.models;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import application.controllers.SoundController;
import application.models.EnemySkills.SkillType;

public abstract class Enemy {
    protected int curFrame = (int) (Math.random() * 40);
    protected int frameCount = (int) (Math.random() * 120);

    protected int hp;
    protected int PosX;
    protected int PosY;
    protected int speed;
    protected int initialIndex = 0;
    protected float rotate = 0f;

    protected SoundController sound;
    protected boolean isForward = true;
    protected int MODEL_WIDTH;
    protected int MODEL_HEIGHT;
    protected static final int MAP_WIDTH = 1900;
    protected Map<SkillType, String> skills; 

    public Enemy(int hp, int MODEL_WIDTH, int MODEL_HEIGHT, int PosX, int PosY, SoundController sound) {
        this.hp = hp;
        this.MODEL_WIDTH = MODEL_WIDTH;
        this.MODEL_HEIGHT = MODEL_HEIGHT;
        this.PosX = PosX;
        this.PosY = PosY;
        this.sound = sound;
        this.skills = new HashMap<>();     }

    String[] deathSounds = {
        "/asset/resources/sfx/chickDie3.wav",
        "/asset/resources/sfx/chickDie4.wav",
        "/asset/resources/sfx/chickDie5.wav",
        "/asset/resources/sfx/chickDie6.wav",
        "/asset/resources/sfx/chicken1a(die).wav",
        "/asset/resources/sfx/chicken2b(die).wav",
        "/asset/resources/sfx/chicken3a(die).wav"
    };

    String[] hitSounds = {
        "/asset/resources/sfx/chicken1b1(pluck).wav",
        "/asset/resources/sfx/chicken1b2(pluck).wav",
        "/asset/resources/sfx/chicken2a1(pluck).wav",
        "/asset/resources/sfx/chicken3b1(pluck).wav",
        "/asset/resources/sfx/chicken3b2(pluck).wav",
        "/asset/resources/sfx/chicken5b(pluck).wav"
    };

    public abstract void render(Graphics g);

    public void nextFrame() {
        if (isForward) {
            curFrame++;
            if (curFrame >= 48) {
                isForward = false;
            }
        } else {
            curFrame--;
            if (curFrame <= 0) {
                isForward = true;
            }
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
        Random random = new Random();
        sound.playSoundEffect(getClass().getResource(hitSounds[random.nextInt(hitSounds.length)]).getPath());
        if (hp <= 0) {
            sound.playSoundEffect(getClass().getResource(deathSounds[random.nextInt(deathSounds.length)]).getPath());
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public abstract Rectangle getHitbox();

    public int getHp() {
        return hp;
    }

    public int getPosX() {
        return PosX;
    }

    public void setPosX(int posX) {
        this.PosX = posX;
    }

    public void setPosY(int posY) {
        this.PosY = posY;
    }

    public int getPosY() {
        return PosY;
    }

    public int getMODEL_WIDTH() {
        return MODEL_WIDTH;
    }

    public int getMODEL_HEIGHT() {
        return MODEL_HEIGHT;
    }

    public int getCenterX() {
        return (PosX + MODEL_WIDTH / 2);
    }

    public int getCenterY() {
        return (PosY + MODEL_HEIGHT / 2);
    }

    public int getcurFrame() {
        return curFrame;
    }

    public DeathEffect getDeathEffect() {
        return null;
    }

    // Loại bỏ abstract khỏi update(), để trống vì di chuyển do EnemyController xử lý
    public void update() {
        // Không tự di chuyển, để EnemyController điều chỉnh
    }

    public void setInitialIndex(int i) {
        initialIndex = i;
    }

    public int getInitialIndex() {
        return initialIndex;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public Map<SkillType, String> getSkills() {
        return skills;
    }

    public abstract void addSkills(SkillType skillType, String imagePath);


}