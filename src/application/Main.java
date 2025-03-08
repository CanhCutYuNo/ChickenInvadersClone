package application;

import javax.swing.*;

import application.Controllers.*;
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
    private static ViewController viewController;

    private static SoundController soundController; // Thêm SoundController

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Chicken Invaders");
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);
//          Khởi tạo SoundController với nhạc nền mặc định (menu)
            soundController = new SoundController(Main.class.getResource("/asset/resources/sfx/CI4Theme.wav").getPath());
        
//            soundController.play(); // Phát nhạc menu khi khởi động game

            gameManager = new Manager(cardLayout, mainPanel, null, null, null,soundController);

            backgroundPanel = new BackgroundPanel();
            gameManager.setBackgroundPanel(backgroundPanel);


            
         
            viewController = new ViewController(cardLayout, mainPanel,
                    null, null, null,
                    backgroundPanel, null, soundController); // Truyền SoundController vào ViewController

            menuPanel = new MenuPanel(viewController);
            gameManager.setMenuPanel(menuPanel);

            gamePanel = new GamePanel(gameManager);
            gameLoop = new GameLoop(gamePanel, frame);
            gameManager.setGameLoop(gameLoop);

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
