/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.Views;

import javax.swing.*;

/**
 *
 * @author hp
 */
public class GameContainerPanel extends JPanel {

    private JLayeredPane jLayeredPane;
    private GamePanel gamePanel;
    private JPanel backgroundPanel;

    public GameContainerPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        initComponents();
    }

    private void initComponents() {
        jLayeredPane = new JLayeredPane();
        backgroundPanel = new BackgroundPanel();
        
        setLayout(new java.awt.GridLayout(1, 1));
        
        jLayeredPane.setLayout(new javax.swing.OverlayLayout(jLayeredPane));

        jLayeredPane.setLayer(backgroundPanel, 0);
        jLayeredPane.add(backgroundPanel);
        
        jLayeredPane.setLayer(gamePanel, 1);
        jLayeredPane.add(gamePanel);
        
        add(jLayeredPane);
    }
}
