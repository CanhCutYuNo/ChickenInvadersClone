package application.controllers.player;

import application.controllers.bullet.BulletController;
import application.controllers.util.GameSettings;
import application.controllers.util.SoundController;

public class PlayerActionHandler {
    private final PlayerController playerController;
    private final BulletController bullets;
    private final SoundController soundController;

    public PlayerActionHandler(PlayerController playerController, BulletController bullets, SoundController soundController) {
        this.playerController = playerController;
        this.bullets = bullets;
        this.soundController = soundController;
    }

    public void movePlayer(int x, int y) {
        playerController.setPosX(x - 35);
        playerController.setPosY(y - 32);
        playerController.updateDirection(x);
        playerController.setLastMoveTime(System.currentTimeMillis());
    }

    public void shoot() {
        switch (GameSettings.getInstance().getDifficulty()) {
            case EASY:
                bullets.addBullet(playerController.getPosX() + 39, playerController.getPosY(), 40, 1.0, 0.4);
                break;
            case NORMAL:
                bullets.addBullet(playerController.getPosX() + 39, playerController.getPosY(), 30, 1.0, 0.4);
                break;
            case HARD:
            case EXTREME:
                bullets.addBullet(playerController.getPosX() + 39, playerController.getPosY(), 25, 1.0, 0.4);
                break;
        }
        soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/bulletHenSolo.wav").getPath());
    }
}