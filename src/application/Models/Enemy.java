package application.models;

import java.awt.*;
import java.util.Random;

import application.controllers.SoundController;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy {

    protected int currentFrame = (int) (Math.random() * 40);
    protected int frameCount = (int) (Math.random() * 120);

    protected int hp;
    protected int PosX;
    protected int PosY;
    protected int speed;
    
    private SoundController sound;    
//    protected boolean alive; //Ktra ga die chua
//	protected int level = 1;
    protected boolean isForward = true; // Biến để theo dõi hướng di chuyển của animation
    protected static final int MODEL_WIDTH = 64;
    protected static final int MODEL_HEIGHT = 64;
    protected static final int MAP_WIDTH = 1900;
    //protected static final int MAP_HEIGHT = 1080;
    
    public Enemy(int hp, int PosX, int PosY, int speed, SoundController sound) {
        this.hp = hp;       
        this.PosX = PosX;
        this.PosY = PosY;
        this.speed = speed;
        this.sound = sound;
//        this.alive = true;
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

    // ?
    public void nextFrame() {
        if(isForward) {
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
      //  soundController.playEffect(getClass().getResource(hitSounds[random.nextInt(hitSounds.length)]).getPath());
        if (hp <= 0) {
            sound.playSoundEffect(getClass().getResource(deathSounds[random.nextInt(deathSounds.length)]).getPath());
        }
    }

    public boolean isDead() {
        if (hp <= 0) {
           // Random random = new Random();
            //sound.playSoundEffect(getClass().getResource(deathSounds[random.nextInt(deathSounds.length)]).getPath());
            //soundController.playEffect(getClass().getResource(deathSounds[random.nextInt(deathSounds.length)]).getPath());
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

    public int getPosY() {
        return PosY;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }


    public abstract void update();
}
