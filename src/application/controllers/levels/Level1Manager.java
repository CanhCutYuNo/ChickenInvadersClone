package application.controllers.levels;

import java.util.ArrayList;
import java.util.List;

import application.controllers.EnemyController;
import application.controllers.LevelManager;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;
import application.views.EnemyView;

public class Level1Manager extends LevelManager {
    // Hằng số:
    private static final int SCREEN_LEFT = -50; // Cạnh trái màn hình để đổi hướng.
    private static final int SCREEN_RIGHT = 1920; // Cạnh phải màn hình để đổi hướng.
    private static final int SPACING = 100; // Khoảng cách giữa các gà trong hàng.
    private static final int NUM_ENEMIES_PER_ROW = 10; // Số gà mỗi hàng.
    private static final int[] START_Y = {100, 300, 500}; // Vị trí Y của 3 hàng.
    private static final float[] TIME_DELAYS = {0.0f, 0.0f, 0.0f}; // Thời gian trì hoãn.

    private List<RowState> rowStates; // Danh sách trạng thái các hàng.

    // Lớp lưu trạng thái hàng:
    private class RowState {
        float t; // Thời gian để tính vị trí X.
        int direction; // Hướng di chuyển (1 = phải, -1 = trái).
        float timeElapsed; // Thời gian đã trôi qua.
        boolean isActive; // Hàng đã kích hoạt chưa.
        int startY; // Vị trí Y cố định của hàng.
        float timeDelay; // Thời gian trì hoãn.

        RowState(int startY, float timeDelay) {
            this.t = 0;
            this.direction = 1;
            this.timeElapsed = 0;
            this.isActive = false;
            this.startY = startY;
            this.timeDelay = timeDelay;
        }
    }

    // Khởi tạo:
    public Level1Manager(SoundController soundController, EnemyController enemyController) {
        super(soundController, enemyController); // Gọi hàm cha.
        rowStates = new ArrayList<>(); // Tạo danh sách trạng thái.
        for (int i = 0; i < START_Y.length; i++) { // Khởi tạo 3 hàng.
            rowStates.add(new RowState(START_Y[i], TIME_DELAYS[i]));
        }
    }

    // Tạo gà ban đầu:
    @Override
    protected void initEnemies() {
        for (int row = 0; row < START_Y.length; row++) { // Lặp 3 hàng.
            for (int i = 0; i < NUM_ENEMIES_PER_ROW; i++) { // Lặp 10 gà mỗi hàng.
                // Tạo gà với posX cách nhau 100 pixel, posY tại startY.
                Enemy model = new Enemy(0, 64, 64, -50 - i * SPACING, START_Y[row], 5, Enemy.EnemyType.CHICKEN_ENEMY);
                model.setInitialIndex(i); // Gán chỉ số trong hàng.
                model.getSkills().put(SkillType.EGG, "/asset/resources/gfx/introEgg.png"); // Thêm kỹ năng trứng.
                EnemyView view = new EnemyView(model); // Tạo view.
                enemyController.addEnemy(model, view); // Thêm vào EnemyController.
            }
        }
        System.out.println("Đã tạo " + (START_Y.length * NUM_ENEMIES_PER_ROW) + " con gà");
    }

    // Cập nhật mỗi frame:
    @Override
    public void update(float deltaTime) {
        // Debug: Kiểm tra posY trước khi gọi super.update.
        for (Enemy enemy : enemyController.getEnemyModels()) {
            if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
                System.out.println("Trước super.update: Gà tại x=" + enemy.getPosX() + ", y=" + enemy.getPosY());
            }
        }

        super.update(deltaTime); // Gọi update của EnemyController (có thể thay đổi posY).

        // Debug: Kiểm tra posY sau super.update.
        for (Enemy enemy : enemyController.getEnemyModels()) {
            if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
                System.out.println("Sau super.update: Gà tại x=" + enemy.getPosX() + ", y=" + enemy.getPosY());
            }
        }

        updateRows(deltaTime); // Cập nhật hàng (đặt lại posY).

        // Debug cuối: Kiểm tra posY sau updateRows.
        for (Enemy enemy : enemyController.getEnemyModels()) {
            if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
                System.out.println("Sau updateRows: Gà tại x=" + enemy.getPosX() + ", y=" + enemy.getPosY());
            }
        }
    }

    // Cập nhật vị trí hàng:
    private void updateRows(float deltaTime) {
        for (int row = 0; row < rowStates.size(); row++) { // Lặp 3 hàng.
            RowState state = rowStates.get(row); // Lấy trạng thái hàng.
            state.timeElapsed += deltaTime; // Cộng thời gian trôi qua.

            if (!state.isActive && state.timeElapsed >= state.timeDelay) { // Kích hoạt hàng.
                state.isActive = true;
                System.out.println("Hàng " + row + " tại y=" + state.startY + " kích hoạt");
            }

            if (state.isActive) { // Nếu hàng đang hoạt động:
                state.t += deltaTime * 50 * state.direction; // Cập nhật t để di chuyển ngang.
                float rotate = (float) (20 * Math.sin(0.05 * state.t)); // Tính góc xoay.

                // Tìm gà trong hàng:
                List<Enemy> rowEnemies = new ArrayList<>();
                for (Enemy enemy : enemyController.getEnemyModels()) {
                    if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY && Math.abs(enemy.getPosY() - state.startY) < 20) {
                        rowEnemies.add(enemy); // Thêm gà nếu gần startY (ngưỡng 20 để bắt gà bị lệch).
                    }
                }

                // Debug: In số gà trong hàng.
                System.out.println("Hàng " + row + " có " + rowEnemies.size() + " con gà");

                // Cập nhật từng gà:
                for (Enemy enemy : rowEnemies) {
                    int index = enemy.getInitialIndex();
                    float posX = -1800 + state.t + index * SPACING; // Tính posX.
                    float posY = state.startY; // Ép posY cố định.
                    enemy.setPosX((int) posX); // Đặt posX.
                    enemy.setPosY((int) posY); // Đặt posY.
                    enemy.setRotate(rotate); // Đặt góc xoay.

                    // Debug: Kiểm tra posY trước/sau setPosY.
                    System.out.println("Hàng " + row + " con " + index + ": Trước setPosY y=" + enemy.getPosY() + ", Sau setPosY y=" + posY);
                }

                // Đổi hướng khi chạm cạnh:
                if (!rowEnemies.isEmpty()) {
                    Enemy firstEnemy = rowEnemies.get(0);
                    Enemy lastEnemy = rowEnemies.get(rowEnemies.size() - 1);
                    if (lastEnemy.getPosX() > SCREEN_RIGHT && state.direction == 1) {
                        state.direction = -1;
                        state.t -= 2 * (lastEnemy.getPosX() - SCREEN_RIGHT);
                        System.out.println("Hàng " + row + " quay trái");
                    } else if (firstEnemy.getPosX() < SCREEN_LEFT && state.direction == -1) {
                        state.direction = 1;
                        state.t += 2 * (SCREEN_LEFT - firstEnemy.getPosX());
                        System.out.println("Hàng " + row + " quay phải");
                    }
                }
            }
        }

        // Kiểm tra gà lạc hàng:
        for (Enemy enemy : enemyController.getEnemyModels()) {
            if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
                boolean inRow = false;
                for (int y : START_Y) {
                    if (Math.abs(enemy.getPosY() - y) < 20) {
                        inRow = true;
                        break;
                    }
                }
                if (!inRow) {
                    System.out.println("CẢNH BÁO: Gà lạc hàng tại x=" + enemy.getPosX() + ", y=" + enemy.getPosY());
                }
            }
        }
    }
}