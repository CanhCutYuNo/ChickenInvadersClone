/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.Views;

import application.Main;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 *
 * @author hp
 */
public class MenuPanel extends javax.swing.JPanel {

    public MenuPanel(Runnable onStart1, Runnable onStart2){
        initComponents(onStart1, onStart2);
    }
    
    private void initComponents(Runnable onStart1, Runnable onStart2) {
        lineBorder1 = (javax.swing.border.LineBorder) javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0));
        jLayeredPane = new javax.swing.JLayeredPane();
        containerPanel = new javax.swing.JPanel();
        backgroundPanel = new BackgroundPanel();
        symbolPanel = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
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
        containerPanel.setLayout(new java.awt.GridLayout(2, 1));

        symbolPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        symbolPanel.setOpaque(false);
        symbolPanel.setLayout(new java.awt.GridBagLayout());

        icon.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        icon.setForeground(new java.awt.Color(255, 255, 255));
        icon.setText("GAME");
        icon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        symbolPanel.add(icon, new java.awt.GridBagConstraints());

        containerPanel.add(symbolPanel);

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new java.awt.GridLayout(3, 1));

        //Button 1
        buttonPanel1.setOpaque(false);
        buttonPanel1.setLayout(new java.awt.GridBagLayout());

        //Button 1 On Clicked Event
        button1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                onStart1.run();                
            }
        });

        button1.setText("Play");
        button1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        buttonPanel1.add(button1, new java.awt.GridBagConstraints());

        buttonPanel.add(buttonPanel1);

        //Button 2
        buttonPanel2.setOpaque(false);
        buttonPanel2.setLayout(new java.awt.GridBagLayout());

        //Button 2 On Clicked Event
        button2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                onStart2.run();
            }
        });

        button2.setText("Options");
        button2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        buttonPanel2.add(button2, new java.awt.GridBagConstraints());

        buttonPanel.add(buttonPanel2);

        //Button 3
        buttonPanel3.setOpaque(false);
        buttonPanel3.setLayout(new java.awt.GridBagLayout());

        //Button 3 On Clicked Event        
        button3.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                System.exit(0);
            }
        });

        button3.setText("Quit Game");
        button3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        buttonPanel3.add(button3, new java.awt.GridBagConstraints());

        buttonPanel.add(buttonPanel3);

        containerPanel.add(buttonPanel);

        jLayeredPane.setLayer(containerPanel, 1);
        jLayeredPane.add(containerPanel);

        add(jLayeredPane);
    }// </editor-fold>                        

    private javax.swing.JPanel backgroundPanel;
    private Button button1;
    private Button button2;
    private Button button3;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel buttonPanel1;
    private javax.swing.JPanel buttonPanel2;
    private javax.swing.JPanel buttonPanel3;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JLabel icon;
    private javax.swing.JLayeredPane jLayeredPane;
    private javax.swing.border.LineBorder lineBorder1;
    private javax.swing.JPanel symbolPanel;
    // End of variables declaration  
}
