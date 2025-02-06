package application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLoop {
    private final Timer timer; // Sử dụng Timer để tạo vòng lặp

    public GameLoop(int fps, Runnable update, Runnable render) {
        // Tính toán khoảng thời gian giữa các frame
        int delay = 1000 / fps;

        // Tạo Timer, thực hiện cập nhật và render mỗi khung hình
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update.run();  // Gọi hàm cập nhật logic
                render.run();  // Gọi hàm vẽ lại màn hình
            }
        });
    }

    // Bắt đầu vòng lặp
    public void start() {
        timer.start();
    }

    // Dừng vòng lặp
    public void stop() {
        timer.stop();
    }
}
