package application.controllers.core;

import application.models.bullet.BulletDame;
import application.models.core.GameStates;

import java.awt.Color;
import java.util.List;

public class GameStateController {
    private final GameStates gameStates;
    private static final long DELAY_DURATION = 2000;

    public GameStateController(GameStates gameStates) {
        this.gameStates = gameStates;
    }

    public void checkLevelTransition(boolean isEnemiesEmpty) {
        if (isEnemiesEmpty && !gameStates.isLevelTransitionTriggered() && !gameStates.isDelaying()) {
            gameStates.setLevel(gameStates.getLevel() + 1);
            gameStates.setDelaying(true);
            gameStates.setDelayStartTime(System.currentTimeMillis());

        }
    }

    public boolean updateLevelTransition() {
        if (gameStates.isDelaying()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - gameStates.getDelayStartTime() >= DELAY_DURATION) {
                gameStates.setDelaying(false);
                gameStates.setLevelTransitionTriggered(true);

                return true;
            }
        }
        return false;
    }

    public void onTransitionComplete() {
        gameStates.setLevelTransitionTriggered(false);
    }

    public void updateFloatingTexts() {
        for (BulletDame floatingText : gameStates.getFloatingTexts()) {
            floatingText.update();
        }
        gameStates.removeExpiredFloatingTexts();
    }

    public void spawnFloatingText(int x, int y, String text, Color color) {
        gameStates.addFloatingText(new BulletDame(x, y, text, color));
    }

    public void clearAllFloatingTexts() {
        gameStates.getFloatingTexts().clear();
    }
    
    public void resetGame() {
        gameStates.reset();
    }

    public int getLevel() {
        return gameStates.getLevel();
    }

    public int getFoodCounts() {
        return gameStates.getFoodCounts();
    }

    public void setFoodCounts(int foodCounts) {
        gameStates.setFoodCounts(foodCounts);
    }

    public boolean isPlayerExploded() {
        return gameStates.isPlayerExploded();
    }

    public void setPlayerExploded(boolean playerExploded) {
        gameStates.setPlayerExploded(playerExploded);
    }

    public boolean isDelaying() {
        return gameStates.isDelaying();
    }

    public boolean isLevelTransitionTriggered() {
        return gameStates.isLevelTransitionTriggered();
    }

    public void loadLevel(int level) {
        gameStates.setLevel(level);
    }

	public List<BulletDame> getFloatingTexts() {
		return gameStates.getFloatingTexts();
	}
	
	public GameStates getGameStates() {
		return gameStates;
	}
}