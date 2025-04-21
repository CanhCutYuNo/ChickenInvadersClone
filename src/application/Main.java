package application;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import application.controllers.core.GameLoop;
import application.controllers.core.Manager;
import application.controllers.util.Controller;
import application.controllers.util.ImageCache;
import application.controllers.util.SoundController;
import application.controllers.util.ViewController;
import application.views.panels.BackgroundPanel;
import application.views.panels.GameContainerPanel;
import application.views.panels.GamePanel;
import application.views.panels.LevelSelectionPanel;
import application.views.panels.MenuPanel;
import application.views.panels.MissionSelectionPanel;
import application.views.panels.PausePanel;
import application.views.panels.SettingPanel;

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
    private static LevelSelectionPanel levelSelectionPanel;
    private static GameLoop gameLoop;
    private static ViewController viewController;
    private static SoundController soundController; 
    private static PausePanel pausePanel;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageCache.getInstance();

            frame = new JFrame("Chicken Invaders");
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);

            soundController = new SoundController();
            gameManager = new Manager(null, soundController, null);

            backgroundPanel = new BackgroundPanel();

            viewController = new ViewController(cardLayout, mainPanel,
                    null, null, null,
                    backgroundPanel, null, soundController, gameManager, null, null);
            
            gameManager.setViewController(viewController);
            viewController.setBackgroundPanel(backgroundPanel);
            
            pausePanel = new PausePanel(viewController, soundController);

            gamePanel = new GamePanel(gameManager, soundController);
            gameManager.setGamePanel(gamePanel);
            gameLoop = new GameLoop(gamePanel, frame);
            gameManager.setGameLoop(gameLoop);

            menuPanel = new MenuPanel(viewController);
            viewController.setMenuPanel(menuPanel);

            settingPanel = new SettingPanel(viewController, soundController);
            gameContainerPanel = new GameContainerPanel(gamePanel, pausePanel);
            missionSelectionPanel = new MissionSelectionPanel(viewController, soundController);
            levelSelectionPanel = new LevelSelectionPanel(viewController, soundController);

            mainPanel.add(menuPanel, "Menu");
            mainPanel.add(settingPanel, "Setting");
            mainPanel.add(gameContainerPanel, "Game");
            mainPanel.add(missionSelectionPanel, "MissionSelection");
            mainPanel.add(levelSelectionPanel, "LevelSelection");

            viewController.setPanels(menuPanel, settingPanel, gameContainerPanel, gamePanel, missionSelectionPanel, levelSelectionPanel,
                                     gameLoop);

            viewController.switchToMenuPanel();
            gameLoop.start();

            frame.add(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            gamePanel.addKeyListener(new Controller(viewController));
            frame.setFocusable(true);
            frame.requestFocus();
        });
    }
}