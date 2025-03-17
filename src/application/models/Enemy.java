package application.models;

import java.awt.*;
import java.util.Random;

import application.controllers.SoundController;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy {

    protected int currentFrame = (int) (Math.random() * 40);
    protected int frameCount = (int) (Math.random() * 120);

    private int hp;
    protected int PosX;
    protected int PosY;
    protected int speed;
    
    protected SoundController sound;    
    protected boolean isForward = true; // Biến để theo dõi hướng di chuyển của animation
    protected final int MODEL_WIDTH;
    protected final int MODEL_HEIGHT;
    protected static final int MAP_WIDTH = 1900;
    
    public Enemy(int hp, int MODEL_WIDTH, int MODEL_HEIGHT, int PosX, int PosY, SoundController sound) {
        this.hp = hp;       
        this.MODEL_WIDTH = MODEL_WIDTH;
        this.MODEL_HEIGHT = MODEL_HEIGHT;
        this.PosX = PosX;
        this.PosY = PosY;
        this.sound = sound;
    }
    
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

    // Cập nhật animation frame
    public void nextFrame() {
        if (isForward) {
            currentFrame++;
            if (currentFrame >= 48) {
                isForward = false; // Đổi hướng khi đến cuối mảng
            }
        } else {
            currentFrame--;
            if (currentFrame <= 0) {
                isForward = true; // Đổi hướng khi về đầu mảng
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
        if (hp <= 0) {
            return true;
        }
        return false;
    }

    public Rectangle getBounds() {
        return new Rectangle(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT);
    }

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
    
    public int getCurrentFrame() {
        return currentFrame;
    }

    // Loại bỏ abstract khỏi update(), để trống vì di chuyển do EnemyController xử lý
    public void update() {
        // Không tự di chuyển, để EnemyController điều chỉnh
    }
}