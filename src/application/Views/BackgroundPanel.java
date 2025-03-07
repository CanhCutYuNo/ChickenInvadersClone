/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 *
 * @author hp
 */
public class BackgroundPanel extends javax.swing.JPanel{
    private final Thread updateThread;
    private Image backgroundImage;
    private int y;
    private final int width = 1920;    
    private final int height = 1080;
    private final int fps = 20;
    private boolean isRunning = true;

    public BackgroundPanel() {
        backgroundImage = new ImageIcon(getClass().getResource("/asset/resources/backgrounds/4.png")).getImage();
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
            repaint();
            invokeTime = currentTime;
        }
    }

    private void update() {
        if (y == -height) {
            y = 0;
        }
        y--;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(backgroundImage, 0, y, width, height, null);
        g.drawImage(backgroundImage, 0, y + height, width, height, null);
    }
}
