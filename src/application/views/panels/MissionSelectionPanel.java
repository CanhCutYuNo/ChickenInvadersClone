package application.views.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

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

public class MissionSelectionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private ViewController viewController;
    private SoundController soundClick;
    private JLayeredPane jLayeredPane;
    private JPanel backgroundPanel;
    private Button backButton, nextButton, switchToLevelSelectionButtonButton;
    private RadioButton continueButton, startNewMissionButton;
    private final GameSettings gameSettings = GameSettings.getInstance();
    private JLabel continueLabel;

    private final double scaleX = ScreenUtil.getInstance().getScreenScaleX();
    private final double scaleY = ScreenUtil.getInstance().getScreenScaleY();

    public MissionSelectionPanel(ViewController viewController, SoundController soundClick) {
        this.viewController = viewController;
        this.soundClick = soundClick;
        GameSettings.getInstance().loadSettings();
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

        // Set null layout để đặt tọa độ
        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(null); 

        // Header
        JLabel headerLabel = new JLabel("Save the World!");
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(40 * scaleX)));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBounds(scaledBounds(800, 50, 400, 50)); 
        containerPanel.add(headerLabel);

        createContinuePanel(containerPanel);

        createStartNewMission(containerPanel);

        createSwtichToLevelSelectionButton(containerPanel);

        createBackButton(containerPanel);

        createNextButton(containerPanel);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(continueButton);
        buttonGroup.add(startNewMissionButton);

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

            viewController.switchToMenuPanel();
        });
        backButton.setBounds(scaledBounds(50, 950, 280, 110));
        containerPanel.add(backButton);
    }

    private void createSwtichToLevelSelectionButton(JPanel containerPanel) {
        switchToLevelSelectionButtonButton = new Button("/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png");
        switchToLevelSelectionButtonButton.setText("Level");
        switchToLevelSelectionButtonButton.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(30 * scaleX)));
        switchToLevelSelectionButtonButton.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());

            viewController.switchToLevelSelectionPanel();
        });
        switchToLevelSelectionButtonButton.setBounds(scaledBounds(820, 950, 280, 110)); 
        containerPanel.add(switchToLevelSelectionButtonButton);
    }    

    private void createNextButton(JPanel containerPanel) {
        nextButton = new Button("/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png");
        nextButton.setText("Next!");
        nextButton.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (30 * scaleX)));
        nextButton.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());

            if(continueButton.isSelected()) {
                gameSettings.setContinueLevel(gameSettings.getContinueLevel());
                viewController.getManager().getGameStateHandler().continueGame();
            } else if(startNewMissionButton.isSelected()) {
            	gameSettings.setContinueLevel(1);
                viewController.getManager().getGameStateHandler().restartGame();
            }
            gameSettings.saveSettings();
            viewController.getManager().getCollisionManager().resetBulletPowerUp();
            viewController.switchToGameContainerPanel();
        });
        nextButton.setBounds(scaledBounds(1590, 950, 280, 110)); // x=466, y=690, width=348, height=70
        containerPanel.add(nextButton);
    }

    private void createContinuePanel(JPanel containerPanel) {
        JPanel continuePanel = new JPanel();
        continuePanel.setOpaque(false);
        continuePanel.setLayout(null);
        continuePanel.setBounds(scaledBounds(550, 500, 900, 100)); // Kích cỡ và vị trí giống trước

        // Tạo Button thay cho JCheckBox
        continueButton = new RadioButton("/asset/resources/gfx/checkbox.png",
                "/asset/resources/gfx/checkbox_hover.png");
        continueButton.setCheckedImage("/asset/resources/gfx/tick.png"); // Đặt hình ảnh dấu tick
        // tạo trạng thái
        continueButton.setBounds(scaledBounds(230, 5, 100, 100));
        continueButton.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());

            continueButton.setSelected(true);
        });
        continuePanel.add(continueButton);

        // Tạo JLabel cho chữ "Continue"
        continueLabel = new JLabel("Continue");
        continueLabel.setForeground(Color.WHITE);
        continueLabel.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (30 * scaleX)));
        continueLabel.setBounds(scaledBounds(380, 30, 400, 40));
        continuePanel.add(continueLabel);

        containerPanel.add(continuePanel);
    }

    private void createStartNewMission(JPanel containerPanel) {
        JPanel startNewMissionPanel = new JPanel();
        startNewMissionPanel.setOpaque(false);
        startNewMissionPanel.setLayout(null);
        startNewMissionPanel.setBounds(scaledBounds(550, 650, 900, 100)); // Kích cỡ và vị trí giống trước

        // Tạo Button thay cho JCheckBox
        startNewMissionButton = new RadioButton("/asset/resources/gfx/checkbox.png",
                "/asset/resources/gfx/checkbox_hover.png");
                startNewMissionButton.setCheckedImage("/asset/resources/gfx/tick.png"); // Đặt hình ảnh dấu tick
        // tạo trạng thái
        startNewMissionButton.setBounds(scaledBounds(230, 5, 100, 100));
        startNewMissionButton.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());

            startNewMissionButton.setSelected(true);
        });
        startNewMissionPanel.add(startNewMissionButton);

        // Tạo JLabel cho chữ "Start new mission"
        JLabel startNewMissionLabel = new JLabel("Start new mission");
        startNewMissionLabel.setForeground(Color.WHITE);
        startNewMissionLabel.setFont(new Font("Comic Sans MS", Font.BOLD, (int) (30 * scaleX)));
        startNewMissionLabel.setBounds(scaledBounds(380, 30, 400, 40));
        startNewMissionPanel.add(startNewMissionLabel);

        containerPanel.add(startNewMissionPanel);
    }    

    public void refresh(){
        if(gameSettings.getContinueLevel() == 1){
            // Trường hợp chưa có màn chơi tiếp theo

            startNewMissionButton.setSelected(true);
            continueButton.setEnabled(false);
            continueLabel.setForeground(new Color(1f, 1f, 1f, 0.2f));
        }
        else{
            continueButton.setEnabled(true);
            continueButton.setSelected(true);
            continueLabel.setForeground(Color.WHITE);
        }
        if(gameSettings.isComplete()){
            switchToLevelSelectionButtonButton.setEnabled(true);
        }
        else{
            switchToLevelSelectionButtonButton.setEnabled(false);
        }
    }

    @Override
    public void paint(Graphics g) {
        paintChildren(g);
    }
}