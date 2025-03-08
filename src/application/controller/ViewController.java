package application.controllers;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Point;
import java.awt.Robot;
import javax.swing.JPanel;

import application.views.*;

public class ViewController {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private SettingPanel settingPanel;
    private GameContainerPanel gameContainerPanel;
    private BackgroundPanel backgroundPanel;
    private GamePanel gamePanel;
    private SoundController soundController;

    public ViewController(CardLayout cardLayout, JPanel mainPanel, 
                          MenuPanel menuPanel, SettingPanel settingPanel, 
                          GameContainerPanel gameContainerPanel, 
                          BackgroundPanel backgroundPanel,
                          GamePanel gamePanel, SoundController soundController) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.menuPanel = menuPanel;
        this.settingPanel = settingPanel;
        this.gameContainerPanel = gameContainerPanel;
        this.backgroundPanel = backgroundPanel;
        this.gamePanel = gamePanel;
        this.soundController = soundController;
    }

    public void setPanels(MenuPanel menuPanel, SettingPanel settingPanel, 
                          GameContainerPanel gameContainerPanel, GamePanel gamePanel) {
        this.menuPanel = menuPanel;
        this.settingPanel = settingPanel;
        this.gameContainerPanel = gameContainerPanel;
        this.gamePanel = gamePanel;
    }

    public void switchToMenuPanel() {
        cardLayout.show(mainPanel, "Menu");        
        menuPanel.setBackgroundPanel(backgroundPanel);
   
        soundController.switchTrack(getClass().getResource("/asset/resources/sfx/CI4Theme.wav").getPath());
    }

    public void switchToSettingPanel() {
        cardLayout.show(mainPanel, "Setting");
        settingPanel.setBackgroundPanel(backgroundPanel);
        
        // Phát hiệu ứng chuyển đổi (SFX)
        //SoundController.playSFX("assets/sounds/click.wav");
    }

    public void switchToGameContainerPanel() {
        cardLayout.show(mainPanel, "Game");
        gameContainerPanel.setBackgroundPanel(backgroundPanel);
        centerMouseOnPlayer();
        
        // Phát nhạc nền khi vào game
        //soundController.switchTrack("assets/sounds/game_music.wav");
    }

    private void centerMouseOnPlayer() {
        try {
            Robot robot = new Robot();
            Point playerPos = gamePanel.getGameManager().getPlayerPosition();
            Point screenPos = gamePanel.getLocationOnScreen();
            int mouseX = screenPos.x + playerPos.x;
            int mouseY = screenPos.y + playerPos.y;
            robot.mouseMove(mouseX, mouseY);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
