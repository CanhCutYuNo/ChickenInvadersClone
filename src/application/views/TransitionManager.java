package application.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class TransitionManager {

    public static void applyFadeTransition(String imagePath, int duration, JComponent component) {
        if (component == null) {
            System.err.println("Error: Component is null.");
            return;
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            System.err.println("Error: Image not found - " + imagePath);
            return;
        }

        ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
        Image image = imageIcon.getImage();
        if (image.getWidth(null) <= 0 || image.getHeight(null) <= 0) {
            System.err.println("Error: Invalid image file - " + imagePath);
            return;
        }

        if (component.getWidth() <= 0 || component.getHeight() <= 0) {
            System.err.println("Error: Component has invalid size.");
            return;
        }

        // Định nghĩa class nội tại FadePanel
        class FadePanel extends JPanel {
            private float alpha = 0.0f;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            }

            public void setAlpha(float value) {
                alpha = value;
                repaint();
            }
        }

        final FadePanel backgroundPanel = new FadePanel();
        backgroundPanel.setBounds(0, 0, component.getWidth(), component.getHeight());
        backgroundPanel.setOpaque(false);
        component.add(backgroundPanel);
        component.setComponentZOrder(backgroundPanel, 0);

        // Khai báo và khởi tạo fadeOutTimer trước
        Timer fadeOutTimer = new Timer(20, new AbstractAction() {
            private float alpha = 1.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 0.05f;
                if (alpha <= 0.0f) {
                    alpha = 0.0f;
                    fadeOutTimer.stop();
                    component.remove(backgroundPanel);
                    component.repaint();
                }
                backgroundPanel.setAlpha(alpha);
            }
        });

        // Khai báo và khởi tạo fadeInTimer
        Timer fadeInTimer = new Timer(20, new AbstractAction() {
            private float alpha = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.05f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    fadeInTimer.stop();
                    Timer delayTimer = new Timer(duration, e1 -> fadeOutTimer.start());
                    delayTimer.setRepeats(false);
                    delayTimer.start();
                }
                backgroundPanel.setAlpha(alpha);
            }
        });

        fadeInTimer.start();
    }

    // Để test code
    public static void main(String[] args) {
        JFrame frame = new JFrame("Fade Transition Test");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setVisible(true);
        applyFadeTransition("path/to/your/image.jpg", 2000, panel); // Thay bằng đường dẫn ảnh thực tế
    }
}