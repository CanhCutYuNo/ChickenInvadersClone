package application.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TransitionManager extends JPanel {
    private float alpha = 0f; // Độ trong suốt (0.0f đến 1.0f)
    private Timer timer;
    private boolean fadeIn = true;
    private BufferedImage levelImage;
    private Runnable onComplete; // Callback khi hiệu ứng hoàn tất

    public TransitionManager(String imagePath, Runnable onComplete) {
        this.onComplete = onComplete;
        setOpaque(false); // Làm trong suốt nền
        setLayout(new BorderLayout());

        // Tải ảnh PNG từ đường dẫn
        try {
            levelImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể tải ảnh: " + imagePath);
            levelImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_ARGB); // Ảnh mặc định nếu lỗi
        }

        // Thiết lập timer cho hiệu ứng
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fadeIn) {
                    alpha += 0.05f; // Tăng độ trong suốt
                    if (alpha >= 1f) {
                        fadeIn = false; // Chuyển sang fade out
                        timer.setDelay(1000); // Đợi 1 giây trước khi fade out
                    }
                } else {
                    alpha -= 0.05f; // Giảm độ trong suốt
                    if (alpha <= 0f) {
                        timer.stop(); // Dừng timer khi fade out hoàn tất
                        setVisible(false); // Ẩn panel
                        getParent().remove(TransitionManager.this); // Xóa panel khỏi container
                        getParent().revalidate();
                        getParent().repaint();
                        if (onComplete != null) {
                            onComplete.run(); // Gọi callback để tiếp tục logic
                        }
                    }
                }
                repaint(); // Cập nhật giao diện
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        if (levelImage != null) {
            g2d.drawImage(levelImage, 0, 0, getWidth(), getHeight(), this);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // Khôi phục
    }

    public void startFade() {
        alpha = 0f;
        fadeIn = true;
        setVisible(true);
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return levelImage != null ? new Dimension(levelImage.getWidth(), levelImage.getHeight()) : new Dimension(400, 200);
    }
}