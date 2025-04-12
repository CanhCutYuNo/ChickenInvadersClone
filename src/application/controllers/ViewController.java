package application.controllers;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Robot;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JPanel;

import application.views.BackgroundPanel;
import application.views.GameContainerPanel;
import application.views.GamePanel;
import application.views.MenuPanel;
import application.views.MissionSelectionPanel;
import application.views.SettingPanel;

public class ViewController {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private SettingPanel settingPanel;
    private GameContainerPanel gameContainerPanel;
    private BackgroundPanel backgroundPanel;
    private GamePanel gamePanel;
    private MissionSelectionPanel missionSelectionPanel;
    private SoundController soundController; 
    private Manager manager;
    private GameLoop gameLoop;

    public ViewController(CardLayout cardLayout, JPanel mainPanel,
                          MenuPanel menuPanel, SettingPanel settingPanel,
                          GameContainerPanel gameContainerPanel, 
                          BackgroundPanel backgroundPanel,
                          GamePanel gamePanel, SoundController soundController, Manager manager,
                          MissionSelectionPanel missionSelectionPanel, GameLoop gameLoop) {

        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.menuPanel = menuPanel;
        this.settingPanel = settingPanel;
        this.gameContainerPanel = gameContainerPanel; 
        this.backgroundPanel = backgroundPanel;
        this.gamePanel = gamePanel;
        this.manager = manager;
        this.soundController = soundController;
        this.missionSelectionPanel = missionSelectionPanel; 
        this.gameLoop = gameLoop;
    }

    
    public void setPanels(MenuPanel menuPanel, SettingPanel settingPanel,
                          GameContainerPanel gameContainerPanel, GamePanel gamePanel,
                          MissionSelectionPanel missionSelectionPanel, GameLoop gameLoop) {
        this.menuPanel = menuPanel;
        this.settingPanel = settingPanel;
        this.gameContainerPanel = gameContainerPanel;
        this.gamePanel = gamePanel;
        this.missionSelectionPanel = missionSelectionPanel;
        this.gameLoop = gameLoop;
    }

    public void switchToMenuPanel() {
        System.out.println("VC: Switching to Menu");
        if (checkComponents("Menu", menuPanel, backgroundPanel)) {
            cardLayout.show(mainPanel, "Menu");
            menuPanel.setBackgroundPanel(backgroundPanel);
            soundController.stopBackgroundMusic();
            playSoundSafe("/asset/resources/sfx/CI4Theme.wav", true);
            if(menuPanel != null) {
            	
            }
        }
    }

    public void switchToSettingPanel() {
        System.out.println("VC: Switching to Settings");
         if (checkComponents("Settings", settingPanel, backgroundPanel)) {
            cardLayout.show(mainPanel, "Setting");
            settingPanel.setBackgroundPanel(backgroundPanel);
        }
    }

     public void switchToMissionSelectionPanel() {
         System.out.println("VC: Switching to Mission Selection");
         if (checkComponents("MissionSelection", missionSelectionPanel, backgroundPanel)) {
             cardLayout.show(mainPanel, "MissionSelection");
             missionSelectionPanel.setBackgroundPanel(backgroundPanel);
             if(missionSelectionPanel != null) missionSelectionPanel.refresh();
         }
     }


    public void switchToGameContainerPanel() {
        System.out.println("VC: Switching to Game Container");
        if (checkComponents("Game", gameContainerPanel, backgroundPanel)) {
            cardLayout.show(mainPanel, "Game"); 
            gameContainerPanel.setBackgroundPanel(backgroundPanel);
            centerMouseOnPlayer();
            soundController.stopBackgroundMusic();
            playSoundSafe("/asset/resources/sfx/CI4Ingame2.wav", true);
            if(manager != null) manager.load();
            if(gamePanel != null) {
                gamePanel.requestFocusInWindow();
                gamePanel.triggerTransition();
            }
        }
    }

    public void switchToPausePanel() {
        if(gameContainerPanel != null) {
            gameContainerPanel.showPauseOverlay();
            if(gameLoop != null) {
                gameLoop.pause();
            }
        }
    }

    public void resumeGame() {
        if(gameContainerPanel != null) {
            gameContainerPanel.hidePauseOverlay();
            if(gamePanel != null) {
            	centerMouseOnPlayer();
            //	gamePanel.triggerTransition();
                gamePanel.requestFocusInWindow();
            }
            if(gameLoop != null) {
                gameLoop.resume();
            }
        }
    }

   
    private void centerMouseOnPlayer() {
    	if(gamePanel == null || manager == null) {
             return;
        }
        try {
            Point playerPos = manager.getPlayerPosition();
            Point screenPos = gamePanel.getLocationOnScreen();
             int playerWidth = 54; 
             int playerHeight = 50;
             int playerCenterX = playerPos.x + playerWidth / 2;
             int playerCenterY = playerPos.y + playerHeight / 2;

            int mouseX = screenPos.x + playerCenterX;
            int mouseY = screenPos.y + playerCenterY;

            Robot robot = new Robot();
            System.out.println("VC: Centering mouse to Player at screen coordinates: " + mouseX + ", " + mouseY);
            robot.mouseMove(mouseX, mouseY);
        } catch (AWTException | IllegalComponentStateException e) { 
            System.err.println("VC: Failed to center mouse - " + e.getMessage());
        } catch (Exception e) {
             System.err.println("VC: Unexpected error centering mouse - " + e.getMessage());
        }
    }
    
    private boolean checkComponents(String targetPanel, JComponent... components) {
         for (JComponent comp : components) {
            if (comp == null) {
                System.err.println("ViewController Error: Cannot switch to " + targetPanel + " - Required component is null: " + comp);
                return false;
            }
        }
         if (cardLayout == null || mainPanel == null) {
              System.err.println("ViewController Error: Cannot switch to " + targetPanel + " - CardLayout or MainPanel is null.");
              return false;
         }
        return true;
    }

    private void playSoundSafe(String resourcePath, boolean isBackground) {
         if (soundController == null) return;
         try {
             URL soundUrl = getClass().getResource(resourcePath);
             if (soundUrl != null) {
                 if (isBackground) {
                     soundController.playBackgroundMusic(soundUrl.getPath());
                 } else {
                     soundController.playSoundEffect(soundUrl.getPath());
                 }
             } else {
                 System.err.println("ViewController: Sound resource not found: " + resourcePath);
             }
         } catch (Exception ex) {
             System.err.println("ViewController: Error playing sound " + resourcePath + " - " + ex.getMessage());
         }
    }
}
