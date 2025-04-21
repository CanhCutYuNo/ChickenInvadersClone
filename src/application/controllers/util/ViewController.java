package application.controllers.util;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Robot;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JPanel;

import application.controllers.core.GameLoop;
import application.controllers.core.Manager;
import application.views.panels.BackgroundPanel;
import application.views.panels.GameContainerPanel;
import application.views.panels.GamePanel;
import application.views.panels.LevelSelectionPanel;
import application.views.panels.MenuPanel;
import application.views.panels.MissionSelectionPanel;
import application.views.panels.SettingPanel;

public class ViewController {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private SettingPanel settingPanel;
    private GameContainerPanel gameContainerPanel;
    private BackgroundPanel backgroundPanel;
    private GamePanel gamePanel;
    private MissionSelectionPanel missionSelectionPanel;
    private LevelSelectionPanel levelSelectionPanel;
    private SoundController soundController;
    private Manager manager;
    private GameLoop gameLoop;

    public ViewController(CardLayout cardLayout, JPanel mainPanel, MenuPanel menuPanel, SettingPanel settingPanel,
                     GameContainerPanel gameContainerPanel, BackgroundPanel backgroundPanel, GamePanel gamePanel,
                     SoundController soundController, Manager manager, MissionSelectionPanel missionSelectionPanel,
                     GameLoop gameLoop) {
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

    public void setBackgroundPanel(BackgroundPanel backgroundPanel) {
        this.backgroundPanel = backgroundPanel;
    }

    public void setMenuPanel(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }

    public void showMenu() {
        switchToMenuPanel();
    }

    public void setPanels(MenuPanel menuPanel, SettingPanel settingPanel, GameContainerPanel gameContainerPanel,
                          GamePanel gamePanel, MissionSelectionPanel missionSelectionPanel, LevelSelectionPanel levelSelectionPanel,
                          GameLoop gameLoop) {
        this.menuPanel = menuPanel;
        this.settingPanel = settingPanel;
        this.gameContainerPanel = gameContainerPanel;
        this.gamePanel = gamePanel;
        this.missionSelectionPanel = missionSelectionPanel;
        this.levelSelectionPanel = levelSelectionPanel;
        this.gameLoop = gameLoop;
    }

    public void switchToMenuPanel() {
        if (checkComponents("Menu", menuPanel, backgroundPanel)) {
            cardLayout.show(mainPanel, "Menu");
            gameContainerPanel.hidePauseOverlay();
            menuPanel.setBackgroundPanel(backgroundPanel);
            if (soundController != null) {
                soundController.stopBackgroundMusic();
                try {
                    URL soundUrl = getClass().getResource("/asset/resources/sfx/CI4Theme.wav");
                    if (soundUrl != null) {
                        soundController.playBackgroundMusic(soundUrl.getPath());
                    } else {
                        System.err.println("UIManager: Sound resource not found: /asset/resources/sfx/CI4Theme.wav");
                    }
                } catch (Exception ex) {
                    System.err.println("UIManager: Error playing menu music - " + ex.getMessage());
                }
            }
            gameLoop.start();
        }
    }

    public void switchToSettingPanel() {
        if (checkComponents("Settings", settingPanel, backgroundPanel)) {
            cardLayout.show(mainPanel, "Setting");
            settingPanel.setBackgroundPanel(backgroundPanel);
        }
    }

    public void switchToMissionSelectionPanel() {
        if (checkComponents("MissionSelection", missionSelectionPanel, backgroundPanel)) {
            cardLayout.show(mainPanel, "MissionSelection");
            missionSelectionPanel.setBackgroundPanel(backgroundPanel);
            if (missionSelectionPanel != null) missionSelectionPanel.refresh();
        }
    }

    public void switchToLevelSelectionPanel(){
        if (checkComponents("LevelSelection", levelSelectionPanel, backgroundPanel)) {
            cardLayout.show(mainPanel, "LevelSelection");
            levelSelectionPanel.setBackgroundPanel(backgroundPanel);
            if (levelSelectionPanel != null) levelSelectionPanel.refresh();
        }        
    }

    public void switchToGameContainerPanel() {
        if (checkComponents("Game", gameContainerPanel, backgroundPanel)) {
            cardLayout.show(mainPanel, "Game");
            gameContainerPanel.setBackgroundPanel(backgroundPanel);
            centerMouseOnPlayer();
            if (soundController != null) {
                soundController.stopBackgroundMusic();
                try {
                    URL soundUrl = getClass().getResource("/asset/resources/sfx/CI4Ingame2.wav");
                    if (soundUrl != null) {
                        soundController.playBackgroundMusic(soundUrl.getPath());
                    } else {
                        System.err.println("UIManager: Sound resource not found: /asset/resources/sfx/CI4Ingame2.wav");
                    }
                } catch (Exception ex) {
                    System.err.println("UIManager: Error playing game music - " + ex.getMessage());
                }
            }
            if (manager != null) manager.load();
            if (gamePanel != null) {
                gamePanel.requestFocusInWindow();
                gamePanel.triggerTransition();
            }
        }
    }

    public void switchToPausePanel() {
        if (gameContainerPanel != null) {
            gameContainerPanel.showPauseOverlay();
            if (gameLoop != null) {
                gameLoop.pause();
            }
        }
    }

    public void resumeGame() {
        if (gameContainerPanel != null) {
            gameContainerPanel.hidePauseOverlay();
            if (gamePanel != null) {
                centerMouseOnPlayer();
                gamePanel.requestFocusInWindow();
            }
            if (gameLoop != null) {
                gameLoop.resume();
            }
        }
    }

    private void centerMouseOnPlayer() {
        if (gamePanel == null || manager == null) {
            return;
        }
        try {
            Point playerPos = manager.getPlayer().getPosition();
            Point screenPos = gamePanel.getLocationOnScreen();
            int playerWidth = 54;
            int playerHeight = 50;
            int playerCenterX = playerPos.x + playerWidth / 2;
            int playerCenterY = playerPos.y + playerHeight / 2;

            int mouseX = screenPos.x + playerCenterX;
            int mouseY = screenPos.y + playerCenterY;

            Robot robot = new Robot();
            System.out.println("UIManager: Centering mouse to Player at screen coordinates: " + mouseX + ", " + mouseY);
            robot.mouseMove(mouseX, mouseY);
        } catch (AWTException | IllegalComponentStateException e) {
            System.err.println("UIManager: Failed to center mouse - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("UIManager: Unexpected error centering mouse - " + e.getMessage());
        }
    }

    private boolean checkComponents(String targetPanel, JComponent... components) {
        for (JComponent comp : components) {
            if (comp == null) {
                System.err.println("UIManager Error: Cannot switch to " + targetPanel + " - Required component is null: " + comp);
                return false;
            }
        }
        if (cardLayout == null || mainPanel == null) {
            System.err.println("UIManager Error: Cannot switch to " + targetPanel + " - CardLayout or MainPanel is null.");
            return false;
        }
        return true;
    }

	public void setManager(Manager gameManager) {
		this.manager = gameManager;
	}
}