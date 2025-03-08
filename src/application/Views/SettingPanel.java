/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.views;

import java.awt.Graphics;

import java.awt.event.*;
import javax.swing.*;

import application.controllers.ViewController;

/**
 *
 * @author hp
 */
public class SettingPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ViewController viewController;

    public SettingPanel(ViewController viewController) {
        this.viewController = viewController;
        initComponents();
    }

    public void setBackgroundPanel(JPanel backgroundPanel) {
        if (this.backgroundPanel != null) {
            jLayeredPane.remove(this.backgroundPanel);
        }
        this.backgroundPanel = backgroundPanel;
        jLayeredPane.setLayer(this.backgroundPanel, 0);
        jLayeredPane.add(this.backgroundPanel);
    }

    private void initComponents() {
        jLayeredPane = new javax.swing.JLayeredPane();
        containerPanel = new javax.swing.JPanel();
        labelPanel = new javax.swing.JPanel();
        label = new javax.swing.JLabel();
        buttonPanel1 = new javax.swing.JPanel();
        button1 = new Button(300, 50);
        buttonPanel2 = new javax.swing.JPanel();
        button2 = new Button(300, 50);
        buttonPanel3 = new javax.swing.JPanel();
        button3 = new Button(300, 50);
        buttonPanel4 = new javax.swing.JPanel();
        button4 = new Button(300, 50);

        setLayout(new java.awt.GridLayout(1, 1));

        jLayeredPane.setLayout(new javax.swing.OverlayLayout(jLayeredPane));

        containerPanel.setBackground(new java.awt.Color(255, 0, 0));
        containerPanel.setOpaque(false);
        containerPanel.setPreferredSize(new java.awt.Dimension(485, 410));
        containerPanel.setLayout(new java.awt.GridLayout(5, 1));

        labelPanel.setLayout(new java.awt.GridBagLayout());
        labelPanel.setOpaque(false);

        label.setFont(new java.awt.Font("Segoe UI", 0, 42)); // NOI18N
        label.setForeground(new java.awt.Color(255, 255, 255));
        label.setText("Options");
        labelPanel.add(label, new java.awt.GridBagConstraints());

        containerPanel.add(labelPanel);

        //Button 1
        buttonPanel1.setOpaque(false);
        buttonPanel1.setLayout(new java.awt.GridBagLayout());

        //Button 1 On Clicked Event
        button1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                incrementDifficulty();
                throw new UnsupportedOperationException("Chua them tinh nang luu trang thai do kho");
            }
        });

        button1.setFont(new java.awt.Font("SansSerif", 0, 30)); // NOI18N
        button1.setText("Difficulty: " + getDifficultyString());
        buttonPanel1.add(button1, new java.awt.GridBagConstraints());

        containerPanel.add(buttonPanel1);

        //Button 2
        buttonPanel2.setOpaque(false);
        buttonPanel2.setLayout(new java.awt.GridBagLayout());

        //Button 2 On Clicked Event
        button2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        button2.setFont(new java.awt.Font("SansSerif", 0, 30)); // NOI18N
        button2.setText("Audio Settings");
        button2.setPreferredSize(new java.awt.Dimension(300, 50));
        buttonPanel2.add(button2, new java.awt.GridBagConstraints());

        containerPanel.add(buttonPanel2);

        //Button 3
        buttonPanel3.setOpaque(false);
        buttonPanel3.setLayout(new java.awt.GridBagLayout());

        //Button 3 On Clicked Event        
        button3.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        button3.setFont(new java.awt.Font("SansSerif", 0, 30)); // NOI18N
        button3.setText("Credit");
        button3.setPreferredSize(new java.awt.Dimension(300, 50));
        buttonPanel3.add(button3, new java.awt.GridBagConstraints());

        containerPanel.add(buttonPanel3);

        //Button 4
        buttonPanel4.setOpaque(false);
        buttonPanel4.setLayout(new java.awt.GridBagLayout());

        //Button 4 On Clicked Event        
        button4.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                viewController.switchToMenuPanel();
            }
        });

        button4.setFont(new java.awt.Font("SansSerif", 0, 30)); // NOI18N
        button4.setText("Done");
        button4.setPreferredSize(new java.awt.Dimension(300, 50));
        buttonPanel4.add(button4, new java.awt.GridBagConstraints());

        containerPanel.add(buttonPanel4);

        jLayeredPane.setLayer(containerPanel, 1);
        jLayeredPane.add(containerPanel);

        add(jLayeredPane);

    }// </editor-fold>     

    @Override
    public void paint(Graphics g) {
        paintChildren(g);
    }

    public enum Difficulty {
        PEACEFUL, EASY, NORMAL, HARD, EXTREME;
    }

    public String getDifficultyString() {
        switch (Difficulty.values()[difficulty]) {
            case PEACEFUL:
                return "Peaceful";
            case EASY:
                return "Easy";
            case NORMAL:
                return "Normal";
            case HARD:
                return "Hard";
            case EXTREME:
                return "Extreme";
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + difficulty);
        }
    }

    public void incrementDifficulty() {
        difficulty = (difficulty + 1) % Difficulty.values().length;
        button1.setText("Difficulty: " + getDifficultyString());
    }

    private int difficulty;
    private javax.swing.JPanel backgroundPanel;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private javax.swing.JPanel buttonPanel1;
    private javax.swing.JPanel buttonPanel2;
    private javax.swing.JPanel buttonPanel3;
    private javax.swing.JPanel buttonPanel4;
    private javax.swing.JLabel label;
    private javax.swing.JPanel labelPanel;
    private javax.swing.JLayeredPane jLayeredPane;
    private javax.swing.JPanel containerPanel;
}
