package application.views.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import application.controllers.util.GameSettings;
import application.controllers.util.ScreenUtil;
import application.controllers.util.SoundController;
import application.controllers.util.ViewController;
import application.views.buttons.Button;
import application.views.buttons.RadioButton;

public class LevelSelectionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private ViewController viewController;
    private SoundController soundClick;
    private JLayeredPane jLayeredPane;
    private JPanel backgroundPanel;
    private Button backButton, nextButton;
    private final GameSettings gameSettings = GameSettings.getInstance();
    private Vector<RadioButton> levelButtons; ButtonGroup buttonGroup;

    private final double scaleX = ScreenUtil.getInstance().getScreenScaleX();
    private final double scaleY = ScreenUtil.getInstance().getScreenScaleY();

    public LevelSelectionPanel(ViewController viewController, SoundController soundClick) {
        this.viewController = viewController;
        this.soundClick = soundClick;
        levelButtons = new Vector<>(0);
        initComponents();
    }

    public void setBackgroundPanel(JPanel backgroundPanel) {
        if(this.backgroundPanel != null) {
            jLayeredPane.remove(this.backgroundPanel);
        }
        this.backgroundPanel = backgroundPanel;
        jLayeredPane.setLayer(this.backgroundPanel, 0);
        jLayeredPane.add(this.backgroundPanel);
    }
    
    private java.awt.Rectangle scaledBounds(int x, int y, int width, int height) {
        return new java.awt.Rectangle(
            (int)(x * scaleX),
            (int)(y * scaleY),
            (int)(width * scaleX),
            (int)(height * scaleY)
        );
    }

    private void initComponents() {
        jLayeredPane = new JLayeredPane();
        setLayout(new GridLayout(1, 1));
        jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));

        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(null); 

        JLabel headerLabel = new JLabel("Choose your level!");
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(40 * scaleX)));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBounds(scaledBounds(800, 50, 400, 50)); 
        containerPanel.add(headerLabel);

        createLevelSelectionPanels(containerPanel);

        createBackButton(containerPanel);

        createNextButton(containerPanel);

        buttonGroup = new ButtonGroup();
        for(RadioButton radioButton : levelButtons) {
            buttonGroup.add(radioButton);
        }

        jLayeredPane.setLayer(containerPanel, 1);
        jLayeredPane.add(containerPanel);
        add(jLayeredPane);
    }

    private void createBackButton(JPanel containerPanel) {
        backButton = new Button("/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png");
        backButton.setText("Back");
        backButton.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(30 * scaleX)));
        backButton.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());

            viewController.switchToMissionSelectionPanel();
        });
        backButton.setBounds(scaledBounds(50, 950, 280, 110));
        containerPanel.add(backButton);
    }

    private void createNextButton(JPanel containerPanel) {
        nextButton = new Button("/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png");
        nextButton.setText("Next!");
        nextButton.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (30 * scaleX)));
        nextButton.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());

            gameSettings.setContinueLevel(this.getLevel());
            gameSettings.saveSettings();
            
            viewController.getManager().getGameStateHandler().continueGame();
            viewController.getManager().getCollisionManager().resetBulletPowerUp();
            viewController.switchToGameContainerPanel();
        });
        nextButton.setBounds(scaledBounds(1590, 950, 280, 110));
        containerPanel.add(nextButton);
    }

    private void createLevelSelectionPanels(JPanel containerPanel) {
        for(int i = 1; i <= 5; i++) {
            JPanel levelPanel = new JPanel();
            levelPanel.setOpaque(false);
            levelPanel.setLayout(null);
            levelPanel.setBounds(scaledBounds(550, 200 + 100 * i, 900, 100)); 

            RadioButton levelButton;
            levelButton = new RadioButton("/asset/resources/gfx/checkbox.png",
                    "/asset/resources/gfx/checkbox_hover.png");
            levelButton.setCheckedImage("/asset/resources/gfx/tick.png");
            levelButton.setBounds(scaledBounds(230, 5, 100, 100));
            levelButton.addActionListener(e -> {
                soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
    
                levelButton.setSelected(true);
            });
            levelPanel.add(levelButton);

            levelButtons.add(levelButton);

            JLabel levelLanel = new JLabel("Level " + i);
            levelLanel.setForeground(Color.WHITE);
            levelLanel.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (30 * scaleX)));
            levelLanel.setBounds(scaledBounds(380, 30, 400, 40));
            levelPanel.add(levelLanel);
    
            containerPanel.add(levelPanel);

        }
    }

    public int getLevel(){
        for(RadioButton levelButton : levelButtons) {
            if(levelButton.isSelected())return (levelButtons.indexOf(levelButton) + 1);
        }
        return 1;
    }

    public void refresh(){
        if(!levelButtons.isEmpty() && levelButtons.get(0) != null){
            levelButtons.get(0).setSelected(true);
        }
    }

    @Override
    public void paint(Graphics g) {
        paintChildren(g);
    }
}