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

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JLayeredPane jLayeredPane;
    private GamePanel gamePanel;
    private JPanel backgroundPanel;

    public GameContainerPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
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
        jLayeredPane = new JLayeredPane();
        backgroundPanel = new BackgroundPanel();

        setLayout(new java.awt.GridLayout(1, 1));

        jLayeredPane.setLayout(new javax.swing.OverlayLayout(jLayeredPane));

        jLayeredPane.setLayer(gamePanel, 1);
        jLayeredPane.add(gamePanel);

        add(jLayeredPane);
    }
}
