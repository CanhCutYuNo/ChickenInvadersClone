package application;

import javax.swing.*;

import application.controllers.*;
import application.views.*;

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
    private static ViewController viewController;
    private static SoundController soundController; 
  
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Chicken Invaders");
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);
            
            soundController = new SoundController();

            gameManager = new Manager(cardLayout, mainPanel, null, null, null, null, soundController);

            backgroundPanel = new BackgroundPanel();
            gameManager.setBackgroundPanel(backgroundPanel);
         
            viewController = new ViewController(cardLayout, mainPanel,
                    null, null, null,
                    backgroundPanel, null, soundController);

            menuPanel = new MenuPanel(viewController);
            gameManager.setMenuPanel(menuPanel);

            gamePanel = new GamePanel(gameManager);
            gameLoop = new GameLoop(gamePanel, frame);
            gameManager.setGameLoop(gameLoop);
            gameManager.setGamePanel(gamePanel);

            settingPanel = new SettingPanel(viewController);
            gameContainerPanel = new GameContainerPanel(gamePanel);

            mainPanel.add(menuPanel, "Menu");
            mainPanel.add(settingPanel, "Setting");
            mainPanel.add(gameContainerPanel, "Game");

            viewController.setPanels(menuPanel, settingPanel, gameContainerPanel, gamePanel);

            viewController.switchToMenuPanel();
            gameLoop.start();

            frame.add(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            frame.addKeyListener(new Controller(frame, cardLayout, mainPanel, viewController));
            frame.setFocusable(true);
            frame.requestFocus();
        });
    }
}
