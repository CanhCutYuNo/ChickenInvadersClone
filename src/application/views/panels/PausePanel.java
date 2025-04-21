package application.views.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import application.controllers.util.ScreenUtil;
import application.controllers.util.SoundController;
import application.controllers.util.ViewController;
import application.views.buttons.Button;

public class PausePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private ViewController viewController;
    private SoundController soundController;
    private JLayeredPane jLayeredPane;
    private JPanel containerPanel;
    private JLabel pauseBackgroundLayer;
    private JLabel titleLabel;
    private Button resumeButton;
    private Button exitButton;
    private final double scaleX = ScreenUtil.getInstance().getScreenScaleX();
    private final double scaleY = ScreenUtil.getInstance().getScreenScaleY();

    private final String backgroundImageResourcePath = "/asset/resources/gfx/pause_background.png";

    public PausePanel(ViewController viewController, SoundController soundController) {
        if(viewController == null || soundController == null) {
            throw new IllegalArgumentException("ViewController and ISoundController cannot be null");
        }
        this.viewController = viewController;
        this.soundController = soundController;
        initComponents();
    }

    private void initComponents() {
        jLayeredPane = new JLayeredPane();
        containerPanel = new JPanel();
        pauseBackgroundLayer = new JLabel();
        titleLabel = new JLabel("Pause");
        resumeButton = createMenuButton("Resume", "/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png",() -> {
            playSoundSafe("/asset/resources/sfx/clickXP.wav");
            if(viewController != null) {
                viewController.resumeGame();
            }
        });
        exitButton = createMenuButton("Exit to Menu", "/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png",() -> {
            playSoundSafe("/asset/resources/sfx/clickXP.wav");
            if(viewController != null) {
                viewController.switchToMenuPanel();
            }
        });

        setLayout(new BorderLayout());
        setOpaque(false);
        jLayeredPane.setLayout(null); 
        containerPanel.setOpaque(false);
        containerPanel.setLayout(null); 

        Image backgroundImage = null;
        try {
            URL imageUrl = getClass().getResource(backgroundImageResourcePath);
            if(imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            } else {
                System.err.println("PausePanel: Background resource not found: " + backgroundImageResourcePath);
            }
        } catch(IOException e) {
            System.err.println("PausePanel: Failed to load background image.");
        }

        if(backgroundImage != null) {
            Image scaledImage = backgroundImage.getScaledInstance(
               (int)(1400 * scaleX),
               (int)(788 * scaleY),
                Image.SCALE_SMOOTH
            );
            ImageIcon backgroundIcon = new ImageIcon(scaledImage);
            pauseBackgroundLayer.setIcon(backgroundIcon);
            pauseBackgroundLayer.setBounds((int)(235 * scaleX),(int)(90 * scaleY),(int)(1400 * scaleX),(int)(788 * scaleY));
        } else {
            System.out.println("PausePanel: Using fallback background color.");
            pauseBackgroundLayer.setBackground(new Color(0, 0, 0, 180));
            pauseBackgroundLayer.setOpaque(true);
            pauseBackgroundLayer.setBounds(0, 0,(int)(1400 * scaleX),(int)(788 * scaleY));
        }
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD,(int)(60 * scaleY)));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        int titleWidth =(int)(400 * scaleX);
        int titleHeight =(int)(80 * scaleY);
        titleLabel.setBounds(
           (int)((1920 * scaleX - titleWidth) / 2),
           (int)(200 * scaleY),
            titleWidth,
            titleHeight
        );

        resumeButton.setPreferredSize(new Dimension((int)(384 * scaleX),(int)(120 * scaleY)));
        resumeButton.setMaximumSize(new Dimension((int)(384 * scaleX),(int)(120 * scaleY)));
        resumeButton.setBounds(
           (int)((1920 * scaleX - 384 * scaleX) / 2),
           (int)(400 * scaleY),
           (int)(384 * scaleX),
           (int)(120 * scaleY)
        );

        exitButton.setPreferredSize(new Dimension((int)(384 * scaleX),(int)(120 * scaleY)));
        exitButton.setMaximumSize(new Dimension((int)(384 * scaleX),(int)(120 * scaleY)));
        exitButton.setBounds(
           (int)((1920 * scaleX - 384 * scaleX) / 2),
           (int)(500 * scaleY),
           (int)(384 * scaleX),
           (int)(120 * scaleY)
        );

        containerPanel.add(titleLabel);
        containerPanel.add(resumeButton);
        containerPanel.add(exitButton);
        containerPanel.setBounds(0, 0,(int)(1920 * scaleX),(int)(1080 * scaleY));

        jLayeredPane.add(pauseBackgroundLayer, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane.add(containerPanel, JLayeredPane.PALETTE_LAYER);

        add(jLayeredPane, BorderLayout.CENTER);

        jLayeredPane.setPreferredSize(new Dimension((int)(1920 * scaleX),(int)(1080 * scaleY)));
    }

    private Button createMenuButton(String text, String imagePath, String hoverImagePath, Runnable action) {
        Button button = new Button(imagePath, hoverImagePath);
        button.setText(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD,(int)(30 * scaleY)));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                if(SwingUtilities.isLeftMouseButton(evt) && action != null) {
                    action.run();
                }
            }
        });
        return button;
    }

    private void playSoundSafe(String resourcePath) {
        if(soundController == null) return;
        try {
            URL soundUrl = getClass().getResource(resourcePath);
            if(soundUrl != null) {
                soundController.playSoundEffect(soundUrl.getPath());
            } else {
                System.err.println("PausePanel: Sound resource not found: " + resourcePath);
            }
        } catch(Exception ex) {
            System.err.println("PausePanel: Error playing sound " + resourcePath + " - " + ex.getMessage());
        }
    }

    public void onPausePanelShown() {
        if(resumeButton != null) {
            resumeButton.requestFocusInWindow();
        }
    }
}