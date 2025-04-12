package application;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import application.controllers.Controller;
import application.controllers.GameLoop;
import application.controllers.Manager;
import application.controllers.MouseController;
import application.controllers.SoundController;
import application.controllers.ViewController;
import application.views.BackgroundPanel;
import application.views.GameContainerPanel;
import application.views.GamePanel;
import application.views.MenuPanel;
import application.views.MissionSelectionPanel;
import application.views.SettingPanel;

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
    private static MissionSelectionPanel missionSelectionPanel;
    private static GameLoop gameLoop;
    private static ViewController viewController;
    private static MouseController mouseController;
    private static SoundController soundController; 
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Chicken Invaders");
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);

            soundController = new SoundController();
            gameManager = new Manager(cardLayout, mainPanel, null, null, null, soundController, null);

            backgroundPanel = new BackgroundPanel();
            gameManager.setBackgroundPanel(backgroundPanel);

            viewController = new ViewController(cardLayout, mainPanel,
                    null, null, null,
                    backgroundPanel, null, soundController, gameManager);

            mouseController = new MouseController(gamePanel, soundController);

            gamePanel = new GamePanel(gameManager);
            gameManager.setGamePanel(gamePanel);
            gameLoop = new GameLoop(gamePanel, frame);
            gameManager.setGameLoop(gameLoop);

            menuPanel = new MenuPanel(viewController, mouseController, gamePanel);
            gameManager.setMenuPanel(menuPanel);

            settingPanel = new SettingPanel(viewController, soundController);
            gameContainerPanel = new GameContainerPanel(gamePanel);
            missionSelectionPanel = new MissionSelectionPanel(viewController, soundController);

            mainPanel.add(menuPanel, "Menu");
            mainPanel.add(settingPanel, "Setting");
            mainPanel.add(gameContainerPanel, "Game");
            mainPanel.add(missionSelectionPanel, "MissionSelection");

            viewController.setPanels(menuPanel, settingPanel, gameContainerPanel, gamePanel, missionSelectionPanel);

            viewController.switchToMissionSelectionPanel();
            gameLoop.start();

            frame.add(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            gamePanel.addKeyListener(new Controller(null, viewController));
            frame.setFocusable(true);
            frame.requestFocus();
        });
    }
}