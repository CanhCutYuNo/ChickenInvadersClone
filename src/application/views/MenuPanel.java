package application.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;

import application.controllers.MouseController;
import application.controllers.SoundController;
import application.controllers.ViewController;
import application.util.ScreenUtil;


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
    
    private final double scaleX = ScreenUtil.getInstance().getScreenScaleX();
    private final double scaleY = ScreenUtil.getInstance().getScreenScaleY();

    public MenuPanel(ViewController viewController, MouseController mouseController, GamePanel gamePanel) {
        this.viewController = viewController;
        this.sound = new SoundController();
        this.mouseController = mouseController;
        this.gamePanel = gamePanel;
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

    private void initComponents() {
        jLayeredPane = new JLayeredPane();
        containerPanel = new JPanel();
        symbolPanel = new JPanel();
        buttonPanel = new JPanel();

        setLayout(new GridLayout(1, 1));
        jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));

        containerPanel.setOpaque(false);
        //containerPanel.setPreferredSize(new Dimension(985, 1110));
        containerPanel.setLayout(new GridLayout(2, 1));

        symbolPanel.setOpaque(false);
        symbolPanel.setLayout(new GridBagLayout());

        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/asset/resources/gfx/logo5.png"));
        Image rawImage = rawIcon.getImage();

        int newWidth = (int)(rawIcon.getIconWidth() * scaleX);
        int newHeight = (int)(rawIcon.getIconHeight() * scaleY);

        Image scaledImage = rawImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        icon = new JLabel();
        icon.setIcon(scaledIcon);
        icon.setHorizontalTextPosition(SwingConstants.CENTER);
        icon.setVerticalTextPosition(SwingConstants.BOTTOM);

        GridBagConstraints gbcIcon = new GridBagConstraints();
        gbcIcon.gridx = 0;
        gbcIcon.gridy = 0;
        gbcIcon.insets = new Insets((int)(20 * scaleY), (int)(155 * scaleX), (int)(20 * scaleY), (int)(0 * scaleX));
        gbcIcon.anchor = GridBagConstraints.CENTER;
        symbolPanel.add(icon, gbcIcon);

        containerPanel.add(symbolPanel);

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        button1 = createMenuButton("Play", "/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png", () -> {
            sound.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            
            if (viewController != null) {
                // viewController.switchToGameContainerPanel();
                // if (gamePanel != null) {
                //     gamePanel.triggerTransition();
                   
                // }

                viewController.switchToMissionSelectionPanel();
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
        button.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(30 * scaleY)));
        button.setPreferredSize(new Dimension((int)(384 * scaleX), (int)(120 * scaleY)));
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
