package application.controllers;

import java.awt.*;

import application.views.*;

public class PlayerController {

    private double shootSpeed;
    private int PosX;
    private int PosY;
    private long lastMoveTime = 0;
    private static final long MOVE_TIMEOUT = 200;

    private int initialPosX;
    private boolean moving = false;

    private PlayerView playerView;

    // Constructor
    public PlayerController(double shootSpeed, int PosX, int PosY, PlayerView playerView) {
        this.shootSpeed = shootSpeed;
        this.PosX = PosX;
        this.PosY = PosY;
        this.initialPosX = PosX;
        this.playerView = playerView; // Gán giá trị
    }

    // Getter & Setter
    public double getShootSpeed() {
        return shootSpeed;
    }

    public void setShootSpeed(double shootSpeed) {
        this.shootSpeed = shootSpeed;
    }

    public int getPosX() {
        return PosX;
    }

    public void setPosX(int PosX) {
        this.PosX = PosX;
    }

    public int getPosY() {
        return PosY;
    }

    public void setPosY(int PosY) {
        this.PosY = PosY;
    }

    public Point getPosition() {
        return new Point(PosX, PosY);
    }

    public boolean getMoving() {
        return moving;
    }

    public void setLastMoveTime(long LMT) {
        this.lastMoveTime = LMT;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public PlayerView setPlayerView(PlayerView _playerView) {
        return playerView = _playerView;
    }

    public void updateDirection(int newX) {
        if (newX < initialPosX) {
            playerView.setCurFrame(Math.max(0, playerView.getCurFrame() - 1));
            moving = true;
        } else if (newX > initialPosX) {
            playerView.setCurFrame(Math.min(31, playerView.getCurFrame() + 1));
            moving = true;
        } else {
            moving = false;
        }
        initialPosX = newX;
        lastMoveTime = System.currentTimeMillis();
    }

    public void update() {
        int curFrame = playerView.getCurFrame();
        if (System.currentTimeMillis() - lastMoveTime > MOVE_TIMEOUT) {
            moving = false;
        } else {
            moving = true;
        }

        if (!moving && curFrame != 16) {
            if (curFrame < 16) {
                playerView.setCurFrame(curFrame + 1);
            } else {
                playerView.setCurFrame(curFrame - 1);
            }
        }
        int exFrame = playerView.getExFrame();
        //        if(curFrame >= 15 && curFrame < 32) {
//        	curFrame++;
//        }
        if (exFrame < 52) {
            playerView.setExFrame(exFrame + 1);
        } else {
            playerView.setExFrame(0);
        }
    }
}
