package application.controllers.player;

import java.awt.Point;
import java.awt.Rectangle;

import application.controllers.util.ScreenUtil;
import application.models.player.Player;
import application.views.player.PlayerView;

public class PlayerController {
	
    private long lastMoveTime = 0;
    private static final long MOVE_TIMEOUT = 200;

    private int initialPosX;
    private boolean moving = false;
    private PlayerView playerView;
    private Player player;
    private ScreenUtil screenUtil;

    // Constructor
    public PlayerController(PlayerView playerView) {
        this.player = new Player(100, 50, 0.5, 800, 950);
        this.playerView = playerView; // Gán giá trị
        screenUtil = ScreenUtil.getInstance();
    }

    public double getShootSpeed() {
        return player.getShootSpeed();
    }

    public void setShootSpeed(double shootSpeed) {
        player.setShootSpeed(shootSpeed);
    }

    public int getPosX() {
        return player.getPosX();
    }

    public void setPosX(int PosX) {
        //convert to screen coordinates
        player.setPosX((int)(PosX / screenUtil.getWidth() * 1920f * screenUtil.getScaleX()));
    }

    public int getPosY() {
        return player.getPosY();
    }

    public void setPosY(int PosY) {
        //convert to screen coordinates
        player.setPosY((int)(PosY / screenUtil.getHeight() * 1080f * screenUtil.getScaleY()));
    }

    public Point getPosition() {
        return new Point(player.getPosX(), player.getPosY());
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

    public void isDamaged(int damage){
        player.setHp(player.getHp() - damage);
    }

    public boolean isDead(){
       return player.getHp() <= 0;
    }
    
    public Rectangle getHitbox() {
        return new Rectangle((int) player.getPosX()+9, (int) player.getPosY()+5, 68, 70);
    }

    public void updateDirection(int newX) {
        if(newX < initialPosX) {
            playerView.setCurFrame(Math.max(0, playerView.getCurFrame() - 1));
            moving = true;
        } else if(newX > initialPosX) {
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
        if(System.currentTimeMillis() - lastMoveTime > MOVE_TIMEOUT) {
            moving = false;
        } else {
            moving = true;
        }

        if(!moving && curFrame != 16) {
            if(curFrame < 16) {
                playerView.setCurFrame(curFrame + 1);
            } else {
                playerView.setCurFrame(curFrame - 1);
            }
        }
        
        int exFrame = playerView.getExFrame();
        if(exFrame < 52) {
            playerView.setExFrame(exFrame + 1);
        } else {
            playerView.setExFrame(0);
        }
    }

	public int getHP() {
		return player.getHp();
	}

    public void setHP(int hp){
        player.setHp(hp);

    }
}
