package application.views;

import application.controllers.SoundController;
import application.controllers.ViewController;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Properties;

public class SettingPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private ViewController viewController;
    private SoundController soundClick;
    private JLayeredPane jLayeredPane;
    private JPanel backgroundPanel;
    private Button buttonDifficulty, buttonAudio, buttonCredit, buttonDone;
    private int difficulty;
    private final File settingsFile = new File("settings.properties");

    public SettingPanel(ViewController viewController, SoundController soundClick) {
        this.viewController = viewController;
        this.soundClick = soundClick;
        loadSettings();
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
        setLayout(new GridLayout(1, 1));
        jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));

        JPanel containerPanel = new JPanel(new GridLayout(5, 1));
        containerPanel.setOpaque(false);

        // Label
        JPanel labelPanel = new JPanel(new GridBagLayout());
        labelPanel.setOpaque(false);
        JLabel label = new JLabel("Options");
        label.setFont(new Font("Segoe UI", Font.BOLD, 60));
        label.setForeground(Color.WHITE);
        labelPanel.add(label);
        containerPanel.add(labelPanel);

        // Difficulty Button
        buttonDifficulty = createButton("Difficulty: " + getDifficultyString(), e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            incrementDifficulty();
            saveSettings();
        });
        containerPanel.add(wrapButtonPanel(buttonDifficulty));

        // Audio Settings Button
        buttonAudio = createButton("Audio Settings", e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            JOptionPane.showMessageDialog(this, "Audio Settings will be available soon.");
        });
        containerPanel.add(wrapButtonPanel(buttonAudio));

        // Credit Button
        buttonCredit = createButton("Credit", e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            JOptionPane.showMessageDialog(this, "Game developed by [Your Name Here].");
        });
        containerPanel.add(wrapButtonPanel(buttonCredit));

        // Done Button
        buttonDone = createButton("Done", e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            viewController.switchToMenuPanel();
        });
        containerPanel.add(wrapButtonPanel(buttonDone));

        jLayeredPane.setLayer(containerPanel, 1);
        jLayeredPane.add(containerPanel);
        add(jLayeredPane);
    }

    private Button createButton(String text, java.awt.event.ActionListener action) {
        Button button = new Button(400, 80, "/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png");
        button.setText(text);
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, 36));
        button.addActionListener(action);
        return button;
    }

    private JPanel wrapButtonPanel(Button button) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.add(button, new GridBagConstraints());
        return panel;
    }

    public enum Difficulty { PEACEFUL, EASY, NORMAL, HARD, EXTREME; }

    private String getDifficultyString() {
        return Difficulty.values()[difficulty].name().charAt(0) + Difficulty.values()[difficulty].name().substring(1).toLowerCase();
    }

    private void incrementDifficulty() {
        difficulty = (difficulty + 1) % Difficulty.values().length;
        buttonDifficulty.setText("Difficulty: " + getDifficultyString());
    }

    private void loadSettings() {
        Properties props = new Properties();
        if (settingsFile.exists()) {
            try (FileInputStream fis = new FileInputStream(settingsFile)) {
                props.load(fis);
                difficulty = Integer.parseInt(props.getProperty("difficulty", "0"));
            } catch (IOException e) {
                difficulty = 0;
            }
        } else {
            difficulty = 0;
        }
    }

    private void saveSettings() {
        Properties props = new Properties();
        props.setProperty("difficulty", String.valueOf(difficulty));
        try (FileOutputStream fos = new FileOutputStream(settingsFile)) {
            props.store(fos, "Game Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        paintChildren(g);
    }
}
