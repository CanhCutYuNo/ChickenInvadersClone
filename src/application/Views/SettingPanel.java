/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.Views;

import application.Main;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import javax.imageio.ImageIO;
import java.util.Timer;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 *
 * @author hp
 */
public class SettingPanel extends JPanel {
    public SettingPanel(Runnable onStart) {
        initComponents(onStart);
    }
    
    private void initComponents(Runnable onStart) {
        jLayeredPane = new javax.swing.JLayeredPane();
        containerPanel = new javax.swing.JPanel();
        backgroundPanel = new BackgroundPanel();
        labelPanel = new javax.swing.JPanel();
        label = new javax.swing.JLabel();
        buttonPanel1 = new javax.swing.JPanel();
        button1 = new Button(300, 50);
        buttonPanel2 = new javax.swing.JPanel();
        button2 = new Button(300, 50);
        buttonPanel3 = new javax.swing.JPanel();
        button3 = new Button(300, 50);

        setLayout(new java.awt.GridLayout(1, 1));

        jLayeredPane.setLayout(new javax.swing.OverlayLayout(jLayeredPane));

        jLayeredPane.setLayer(backgroundPanel, 0);
        jLayeredPane.add(backgroundPanel);

        containerPanel.setBackground(new java.awt.Color(255, 0, 0));
        containerPanel.setOpaque(false);
        containerPanel.setPreferredSize(new java.awt.Dimension(485, 410));
        containerPanel.setLayout(new java.awt.GridLayout(4, 1));

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
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        button1.setFont(new java.awt.Font("SansSerif", 0, 30)); // NOI18N
        button1.setText("Audio Settings");
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
        button2.setText("Credit");
        button2.setPreferredSize(new java.awt.Dimension(300, 50));
        buttonPanel2.add(button2, new java.awt.GridBagConstraints());

        containerPanel.add(buttonPanel2);

        //Button 3
        buttonPanel3.setOpaque(false);
        buttonPanel3.setLayout(new java.awt.GridBagLayout());

        //Button 3 On Clicked Event        
        button3.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                onStart.run();
            }
        });

        button3.setFont(new java.awt.Font("SansSerif", 0, 30)); // NOI18N
        button3.setText("Done");
        button3.setPreferredSize(new java.awt.Dimension(300, 50));
        buttonPanel3.add(button3, new java.awt.GridBagConstraints());

        containerPanel.add(buttonPanel3);

        jLayeredPane.setLayer(containerPanel, 1);
        jLayeredPane.add(containerPanel);

        add(jLayeredPane);

    }// </editor-fold>                        

    private javax.swing.JPanel backgroundPanel;
    private Button button1;
    private Button button2;
    private Button button3;
    private javax.swing.JPanel buttonPanel1;
    private javax.swing.JPanel buttonPanel2;
    private javax.swing.JPanel buttonPanel3;
    private javax.swing.JLabel label;
    private javax.swing.JPanel labelPanel;
    private javax.swing.JLayeredPane jLayeredPane;
    private javax.swing.JPanel containerPanel;
}
