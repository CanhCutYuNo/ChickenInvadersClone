//package application.controllers;
//
//import application.models.Enemy;
//import application.models.types.ChickenEnemy; // Import ChickenEnemy
//
//import java.awt.Graphics;
//import java.util.ArrayList;
//import java.util.List;
//
//public class EnemyController {
//    private List<Enemy> enemies;
//    private int speedX = 3; // Tốc độ di chuyển ngang của hàng
//    private int startX, startY; // Vị trí ban đầu của hàng
////    private float timeDelay; // Thời gian trì hoãn trước khi hàng xuất hiệ
//    private float timeElapsed = 0f; // Thời gian đã trôi qua
//    private boolean isActive = false; // Hàng đã xuất hiện chưa
//    private static final int SCREEN_WIDTH = 1920; // Chiều rộng màn hình
//    private SoundController soundController; // Thêm để truyền vào ChickenEnemy
//
//    public EnemyController(int numEnemies, int startX, int startY, float timeDelay, SoundController soundController) {
//        this.enemies = new ArrayList<>();
//        this.startX = startX;
//        this.startY = startY;
//        this.timeDelay = timeDelay;
//        this.soundController = soundController;
//
//        // Tạo các con gà trong hàng, bắt đầu từ ngoài màn hình (bên trái)
//        for (int i = 0; i < numEnemies; i++) {
//            enemies.add(new ChickenEnemy(startX + i * 60 - SCREEN_WIDTH, startY, soundController));
//        }
//    }
//
//    public void update(float deltaTime) {
//        // Tăng thời gian đã trôi qua
//        timeElapsed += deltaTime;
//
//        // Kích hoạt hàng khi đủ thời gian trì hoãn
//        if (!isActive && timeElapsed >= timeDelay) {
//            isActive = true;
//        }
//
//        if (isActive) {
//            // Di chuyển hàng vào vị trí ban đầu
//            if (enemies.get(0).getPosX() < startX) {
//                for (Enemy enemy : enemies) {
//                    enemy.setPosX(enemy.getPosX() + 5); // Tốc độ bay vào
//                }
//            } else {
//                // Di chuyển qua lại
//                for (Enemy enemy : enemies) {
//                    enemy.setPosX(enemy.getPosX() + speedX);
//                }
//
//                // Kiểm tra biên màn hình và đổi hướng
//                int leftMostX = enemies.get(0).getPosX();
//                int rightMostX = enemies.get(enemies.size() - 1).getPosX() + 50; // 50 là chiều rộng gà
//
//                if (leftMostX <= 0 || rightMostX >= SCREEN_WIDTH) {
//                    speedX = -speedX; // Đổi hướng
//                }
//            }
//        }
//
//        // Cập nhật animation frame của từng con gà
//        enemies.forEach(Enemy::nextFrame);
//    }
//
//    public void render(Graphics g) {
//        enemies.forEach(enemy -> enemy.render(g));
//    }
//
//    public List<Enemy> getEnemies() {
//        return enemies;
//    }
//
//    public boolean isActive() {
//        return isActive;
//    }
//}