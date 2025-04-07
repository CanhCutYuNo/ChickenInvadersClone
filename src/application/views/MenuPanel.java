package application.views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import application.controllers.SoundController;
import application.controllers.ViewController;
import application.controllers.MouseController;


public class MenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private ViewController viewController;
    private JPanel backgroundPanel;
    private SoundController sound;
    private MouseController mouseController;
    private GamePanel gamePanel;
    private JLayeredPane jLayeredPane;

    private JPanel containerPanel, symbolPanel, buttonPanel;
    private JLabel icon;
    private Button button1, button2, button3;

    public MenuPanel(ViewController viewController, MouseController mouseController, GamePanel gamePanel) {
        this.viewController = viewController;
        this.sound = new SoundController();
        this.mouseController = mouseController;
        this.gamePanel = gamePanel;
        initComponents();


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                sound.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            }
        });
    }

    public void setBackgroundPanel(JPanel backgroundPanel) {
        if (this.backgroundPanel != null) {
            jLayeredPane.remove(this.backgroundPanel);
        }
        this.backgroundPanel = backgroundPanel;
        jLayeredPane.setLayer(this.backgroundPanel, 0);
        jLayeredPane.add(this.backgroundPanel);
    }

    private void initComponents() {
        jLayeredPane = new JLayeredPane();
        containerPanel = new JPanel();
        symbolPanel = new JPanel();
        buttonPanel = new JPanel();

        setLayout(new GridLayout(1, 1));
        jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));

        containerPanel.setOpaque(false);
    //    containerPanel.setPreferredSize(new Dimension(485, 410));
        containerPanel.setLayout(new GridLayout(2, 1));

        symbolPanel.setOpaque(false);
        symbolPanel.setLayout(new GridBagLayout());

        ImageIcon gameIcon = new ImageIcon(getClass().getResource("/asset/resources/gfx/logo5.png"));
        icon = new JLabel();
        icon.setIcon(gameIcon);
        icon.setHorizontalTextPosition(SwingConstants.CENTER);
        icon.setVerticalTextPosition(SwingConstants.BOTTOM);

        GridBagConstraints gbcIcon = new GridBagConstraints();
        gbcIcon.gridx = 0;
        gbcIcon.gridy = 0;
        gbcIcon.insets = new Insets(20, 155, 20, 0);
        gbcIcon.anchor = GridBagConstraints.CENTER;
        symbolPanel.add(icon, gbcIcon);

        containerPanel.add(symbolPanel);

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        button1 = createMenuButton("Play", "/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png", () -> {
            sound.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            
            if (viewController != null) {
                viewController.switchToGameContainerPanel();
                if (gamePanel != null) {
                    gamePanel.triggerTransition();
                   
                }
            }
        });

        button2 = createMenuButton("Options", "/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png", () -> {
            sound.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            if (viewController != null) {
                viewController.switchToSettingPanel();
            }
        });

        button3 = createMenuButton("Quit Game", "/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png", () -> {
            sound.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            System.exit(0);
        });

        addButtonToPanel(button1);
        addButtonToPanel(button2);
        addButtonToPanel(button3);

        containerPanel.add(buttonPanel);
        jLayeredPane.setLayer(containerPanel, 1);
        jLayeredPane.add(containerPanel);
        add(jLayeredPane);
    }

    private Button createMenuButton(String text, String imagePath, String hoverImagePath, Runnable action) {
        Button button = new Button(imagePath, hoverImagePath);
        button.setText(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        button.setPreferredSize(new Dimension(384, 120));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                action.run();
            }
        });
        return button;
    }

    private void addButtonToPanel(Button button) {
        JPanel panelWrapper = new JPanel(new GridBagLayout());
        panelWrapper.setOpaque(false);
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0;
        gbcButton.gridy = 0;
        gbcButton.anchor = GridBagConstraints.CENTER;
        panelWrapper.add(button, gbcButton);
        buttonPanel.add(panelWrapper);
    }
}
