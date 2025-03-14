package application.controllers;

import javax.swing.*;

import application.views.GamePanel;

public class GameLoop {

    private final Timer gameTimer;
    private int frameCount = 0;
    private int fps = 0;
    private long lastTime = System.nanoTime(); // Lưu thời gian frame trước
    private final GamePanel gamePanel;
    private final Manager gameManager;

    public GameLoop(GamePanel gamePanel, JFrame frame) {
        this.gamePanel = gamePanel;
        this.gameManager = gamePanel.getGameManager(); // Lấy gameManager từ GamePanel

        gameTimer = new Timer(16, e -> {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTime) / 1_000_000_000.0; // Đổi sang giây
            lastTime = currentTime;

            // Cập nhật game logic
            gameManager.update(deltaTime);

            // Kiểm tra nếu hết kẻ thù để chuyển level
            if (gameManager.getEnemies().isEmpty() && !gamePanel.isTransitionActive()) {
                gameManager.setLevel(gameManager.getLevel() + 1);
                gamePanel.updateLevel(); // Cập nhật level và chạy fade
            }

            frame.repaint();

            frameCount++;
        });

        new Timer(1000, e -> {
            fps = frameCount;
            frameCount = 0;
        }).start();
    }

    public void start() {
        lastTime = System.nanoTime();
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
    }

    public int getFPS() {
        return fps;
    }

    // Giả định GamePanel có phương thức isTransitionActive() để kiểm tra trạng thái fade
    // Nếu không có, bạn cần thêm nó vào GamePanel (xem bước 2)
}