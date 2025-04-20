package application.controllers.core;

import application.controllers.util.GameSettings;
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

    // Kiểm tra và kích hoạt chuyển level
    public void checkLevelTransition(boolean isEnemiesEmpty) {
        if (isEnemiesEmpty && !gameStates.isLevelTransitionTriggered() && !gameStates.isDelaying()) {
            gameStates.setLevel(gameStates.getLevel() + 1);
            gameStates.setDelaying(true);
            gameStates.setDelayStartTime(System.currentTimeMillis());

            GameSettings gameSettings = GameSettings.getInstance();
            gameSettings.setContinueLevel(gameStates.getLevel());
            gameSettings.saveSettings();
        }
    }

    // Cập nhật trạng thái chuyển level (xử lý thời gian chờ)
    public boolean updateLevelTransition() {
        if (gameStates.isDelaying()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - gameStates.getDelayStartTime() >= DELAY_DURATION) {
                gameStates.setDelaying(false);
                gameStates.setLevelTransitionTriggered(true);

                return true; // Báo hiệu rằng đã sẵn sàng để chuyển cảnh
            }
        }
        return false;
    }

    // Hoàn tất chuyển cảnh
    public void onTransitionComplete() {
        gameStates.setLevelTransitionTriggered(false);
    }

    // Cập nhật văn bản nổi (FloatingText)
    public void updateFloatingTexts() {
        for (BulletDame floatingText : gameStates.getFloatingTexts()) {
            floatingText.update();
        }
        gameStates.removeExpiredFloatingTexts();
    }

    // Thêm văn bản nổi
    public void spawnFloatingText(int x, int y, String text, Color color) {
        gameStates.addFloatingText(new BulletDame(x, y, text, color));
    }

    // Reset trạng thái game
    public void resetGame() {
        gameStates.reset();
    }

    // Getters để truy cập trạng thái
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

    // Load level từ settings (nếu cần)
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