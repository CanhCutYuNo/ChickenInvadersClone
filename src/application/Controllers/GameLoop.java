package application.Controllers;

import javax.swing.*;
import application.Views.GamePanel;

public class GameLoop {
    private final Timer gameTimer;

    public GameLoop(GamePanel gamePanel) {
        gameTimer = new Timer(8, e -> {
            gamePanel.getGameManager().update(); // Cập nhật logic game
            gamePanel.repaint(); // Vẽ lại màn hình
        });
    }

    public void start() {
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
    }
}
