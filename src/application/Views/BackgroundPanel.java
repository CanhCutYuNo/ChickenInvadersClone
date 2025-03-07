/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.Views;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author hp
 */
public class BackgroundPanel extends javax.swing.JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Thread drawThread;
    private Image backgroundImage;
    private int y1;
    private int y2;
    private final int width = 1920;    
    private final int height = 1080;
    private final int fps = 20;
    private boolean isRunning = true;

    public BackgroundPanel() {
        backgroundImage = new ImageIcon(getClass().getResource("/asset/resources/backgrounds/4.png")).getImage();
        y1 = 0;
        y2 = height;
        drawThread = new Thread(() -> {
            drawLoop();
        });
        drawThread.start();
    }

    public void stop() {
        isRunning = false;
    }
    
    private void drawLoop() {
        long currentTime;
        long invokeTime;
        invokeTime = currentTime = System.nanoTime();
        while (isRunning) {
            while (currentTime - invokeTime < 1000000000 / fps) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                currentTime = System.nanoTime();
            }
            update();
            repaint();
            invokeTime = currentTime;
        }
    }

    private void update() {
        if (y1 == -height) {
            y1 = height;
        }
        if (y2 == -height) {
            y2 = height;
        }
        y1--;
        y2--;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(backgroundImage, 0, y1, width, height, null);
        g.drawImage(backgroundImage, 0, y2, width, height, null);
    }
}
