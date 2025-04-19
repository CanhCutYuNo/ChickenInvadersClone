/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.views.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import application.controllers.util.ScreenUtil;

/**
 *
 * @author hp
 */
public class BackgroundPanel extends javax.swing.JPanel {

    /**
	 * 
	 */
    private final ScreenUtil screenUtil;
	private static final long serialVersionUID = 1L;
	private final Thread updateThread;
    private Image backgroundImage;
    private int y;
    private final int width = 1920;
    private final int height = 1080;
    private final int fps = 60;
    private boolean isRunning = true;

    public BackgroundPanel() {
        screenUtil = ScreenUtil.getInstance();
        backgroundImage = new ImageIcon(getClass().getResource("/asset/resources/backgrounds/starfield5-ci5.png")).getImage();
        y = 0;
        updateThread = new Thread(() -> {
            updateLoop();
        });
        updateThread.start();
    }

    public void stop() {
        isRunning = false;
    }

    private void updateLoop() {
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
            invokeTime = currentTime;
        }
    }

    private void update() {
        if(y >= height) {
            y = 0;
        }
        y++;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.scale(screenUtil.getWidth() / 1920f / screenUtil.getScaleX(),
                screenUtil.getHeight() / 1080f / screenUtil.getScaleY());

        g.drawImage(backgroundImage, 0, y, width, height, null);
        g.drawImage(backgroundImage, 0, y - height, width, height, null);

        g2D.dispose();
    }

}
