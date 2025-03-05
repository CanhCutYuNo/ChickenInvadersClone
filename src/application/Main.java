package application;

import java.util.*;
import javax.swing.*;

import application.Controllers.*;
import application.Models.*;
import application.Views.*;

import java.awt.*;

public class Main {
    
    private static JFrame frame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private static Manager gameManager;
    private static GamePanel gamePanel;
    private static BackgroundPanel backgroundPanel;
    private static MenuPanel menuPanel;
    private static SettingPanel settingPanel;
    private static GameContainerPanel gameContainerPanel;
    private static GameLoop gameLoop;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Chicken Invaders");
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            
            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);
            
            gameManager = new Manager(cardLayout, mainPanel);
            gamePanel = new GamePanel(gameManager);
            
            backgroundPanel = new BackgroundPanel();
            
            menuPanel = new MenuPanel();
            
            settingPanel = new SettingPanel();
            
            gameContainerPanel = new GameContainerPanel(gamePanel);
            
            mainPanel.add(menuPanel, "Menu");
            mainPanel.add(settingPanel, "Setting");
            mainPanel.add(gameContainerPanel, "Game");
            
            switchToMenuPanel();
            
            frame.add(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            
            frame.addKeyListener(new Controller(frame, cardLayout, mainPanel));
            frame.setFocusable(true);
            frame.requestFocus();
            
            gameLoop = new GameLoop(gamePanel);
            gameLoop.start();
        });
    }
    
    public static void switchToMenuPanel() {
        cardLayout.show(mainPanel, "Menu");        
        menuPanel.setBackgroundPanel(backgroundPanel);
    }
    
    public static void switchToSettingPanel() {
        cardLayout.show(mainPanel, "Setting");
        settingPanel.setBackgroundPanel(backgroundPanel);
    }
    
    public static void switchToGameContainerPanel() {
        cardLayout.show(mainPanel, "Game");
        gameContainerPanel.setBackgroundPanel(backgroundPanel);
    }
}
