package application.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.OverlayLayout;
import javax.swing.event.ChangeListener;

import application.controllers.GameSettings;
import application.controllers.GameSettings.Difficulty;
import application.controllers.SoundController;
import application.controllers.ViewController;

public class SettingPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private ViewController viewController;
    private SoundController soundClick;
    private JLayeredPane jLayeredPane;
    private JPanel backgroundPanel;
    private Button buttonDifficulty, buttonDone;
    private JSlider backgroundMusicSlider, soundEffectSlider;
    private JCheckBox muteAudioCheckBox;
    private JLabel backgroundMusicValueLabel, soundEffectValueLabel;

    public SettingPanel(ViewController viewController, SoundController soundClick) {
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

    private void initComponents() {
        jLayeredPane = new JLayeredPane();
        setLayout(new GridLayout(1, 1));
        jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));

        // Sử dụng null layout để đặt tọa độ tuyệt đối
        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(null); // Tắt GridBagLayout

        // Header: Settings
        JLabel headerLabel = new JLabel("Settings");
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBounds(890, 50, 300, 50); // x=490, y=50, width=300, height=50
        containerPanel.add(headerLabel);

        // Difficulty Section
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setOpaque(false);
        difficultyPanel.setLayout(null); // Tắt GridBagLayout cho panel con
        difficultyPanel.setBounds(645, 400, 594, 200); // x=343, y=150, width=594, height=100

        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        difficultyLabel.setBounds(65, 15, 200, 60); // x=0, y=35 (căn giữa theo chiều dọc), width=200, height=30
        difficultyPanel.add(difficultyLabel);

        buttonDifficulty = new Button(384, 100, "/asset/resources/gfx/editbox.png", "/asset/resources/gfx/editbox.png");
        buttonDifficulty.setText(getDifficultyString());
        buttonDifficulty.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        buttonDifficulty.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            incrementDifficulty();
            buttonDifficulty.setText(getDifficultyString());
            GameSettings.getInstance().saveSettings();
        });
        buttonDifficulty.setBounds(210, 0, 384, 100); // x=210 (sau difficultyLabel), y=0, width=384, height=100
        difficultyPanel.add(buttonDifficulty);

        containerPanel.add(difficultyPanel);

        // Sound Volume Slider (Sound Effect)
        JPanel soundEffectPanel = new JPanel();
        soundEffectPanel.setOpaque(false);
        soundEffectPanel.setLayout(null);
        soundEffectPanel.setBounds(645, 480, 1094, 200); // x=343, y=300, width=594, height=100

        JLabel soundEffectLabel = new JLabel("Sound volume:");
        soundEffectLabel.setForeground(Color.WHITE);
        soundEffectLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        soundEffectLabel.setBounds(10, 15, 400, 60); // x=0, y=35, width=200, height=30
        soundEffectPanel.add(soundEffectLabel);

        soundEffectSlider = createCustomSlider((int) (GameSettings.getInstance().getSoundEffectVolume() * 100));
        soundEffectValueLabel = new JLabel(String.valueOf(soundEffectSlider.getValue()) + "%");
        soundEffectValueLabel.setForeground(Color.WHITE);
        soundEffectValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        ChangeListener soundEffectChangeListener = e -> {
            int value = soundEffectSlider.getValue();
            soundEffectValueLabel.setText(value + "%");
            GameSettings.getInstance().setSoundEffectVolume(value / 100.0f);
            if (!soundEffectSlider.getValueIsAdjusting()) {
                GameSettings.getInstance().saveSettings();
            }
        };
        soundEffectSlider.addChangeListener(soundEffectChangeListener);

        Button soundEffectButton = new Button(384, 100, "/asset/resources/gfx/editbox.png", "/asset/resources/gfx/editbox.png");
        soundEffectButton.setLayout(null);
        soundEffectSlider.setBounds(42, 40, 300, 20); // x=42, y=40 (căn giữa trong button), width=300, height=20
        soundEffectButton.add(soundEffectSlider);
        soundEffectButton.setBounds(210, 0, 384, 100); // x=210, y=0, width=384, height=100
        soundEffectPanel.add(soundEffectButton);

        containerPanel.add(soundEffectPanel);

        // Music Volume Slider (Background Music)
        JPanel bgMusicPanel = new JPanel();
        bgMusicPanel.setOpaque(false);
        bgMusicPanel.setLayout(null);
        bgMusicPanel.setBounds(645, 560, 1094, 200); // x=343, y=450, width=594, height=100

        JLabel bgMusicLabel = new JLabel("Music volume:");
        bgMusicLabel.setForeground(Color.WHITE);
        bgMusicLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        bgMusicLabel.setBounds(15, 15, 400, 60); // x=0, y=35, width=200, height=30
        bgMusicPanel.add(bgMusicLabel);

        backgroundMusicSlider = createCustomSlider((int) (GameSettings.getInstance().getBackgroundMusicVolume() * 100));
        backgroundMusicValueLabel = new JLabel(String.valueOf(backgroundMusicSlider.getValue()) + "%");
        backgroundMusicValueLabel.setForeground(Color.WHITE);
        backgroundMusicValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        ChangeListener bgMusicChangeListener = e -> {
            int value = backgroundMusicSlider.getValue();
            backgroundMusicValueLabel.setText(value + "%");
            GameSettings.getInstance().setBackgroundMusicVolume(value / 100.0f);
            if (!backgroundMusicSlider.getValueIsAdjusting()) {
                GameSettings.getInstance().saveSettings();
            }
        };
        backgroundMusicSlider.addChangeListener(bgMusicChangeListener);

        Button bgMusicButton = new Button(384, 100, "/asset/resources/gfx/editbox.png", "/asset/resources/gfx/editbox.png");
        bgMusicButton.setLayout(null);
        backgroundMusicSlider.setBounds(42, 40, 300, 20); // x=42, y=40, width=300, height=20
        bgMusicButton.add(backgroundMusicSlider);
        bgMusicButton.setBounds(210, 0, 384, 100); // x=210, y=0, width=384, height=100
        bgMusicPanel.add(bgMusicButton);

        containerPanel.add(bgMusicPanel);

     // Mute Audio Checkbox
        JPanel muteAudioPanel = new JPanel();
        muteAudioPanel.setOpaque(false);
        muteAudioPanel.setLayout(null);
        muteAudioPanel.setBounds(645, 640, 200, 40); // Kích cỡ và vị trí giống trước

        // Tạo JLabel cho chữ "Mute audio"
        JLabel muteAudioLabel = new JLabel("Mute audio");
        muteAudioLabel.setForeground(Color.WHITE);
        muteAudioLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        muteAudioLabel.setBounds(0, 0, 150, 40); // x=0, y=0, width=150, height=40
        muteAudioPanel.add(muteAudioLabel);

        // Tạo JCheckBox cho tick
        muteAudioCheckBox = new JCheckBox("", GameSettings.getInstance().isMuteAudio());
        muteAudioCheckBox.setOpaque(false);
        java.net.URL uncheckedUrl = getClass().getResource("/asset/resources/gfx/checkbox_unchecked.png");
        java.net.URL checkedUrl = getClass().getResource("/asset/resources/gfx/checkbox_checked.png");
        if (uncheckedUrl != null && checkedUrl != null) {
            muteAudioCheckBox.setIcon(new ImageIcon(uncheckedUrl));
            muteAudioCheckBox.setSelectedIcon(new ImageIcon(checkedUrl));
        } else {
            System.err.println("Không tìm thấy hình ảnh checkbox: /asset/resources/gfx/checkbox_unchecked.png hoặc /asset/resources/gfx/checkbox_checked.png");
        }
        muteAudioCheckBox.setBounds(150, 5, 40, 30); // x=150 (sau JLabel), y=5 (căn giữa theo chiều dọc), width=40, height=30
        muteAudioCheckBox.addActionListener(e -> {
            GameSettings.getInstance().setMuteAudio(muteAudioCheckBox.isSelected());
            GameSettings.getInstance().saveSettings();
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
        });
        muteAudioPanel.add(muteAudioCheckBox);

        // Thêm hiệu ứng hover
        muteAudioPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                muteAudioLabel.setForeground(Color.YELLOW); // Đổi màu chữ thành vàng khi hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                muteAudioLabel.setForeground(Color.WHITE); // Đổi màu chữ về trắng khi không hover
            }
        });

        containerPanel.add(muteAudioPanel);

        // Back Button
        buttonDone = new Button(348, 70, "/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png");
        buttonDone.setText("Back");
        buttonDone.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        buttonDone.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            viewController.switchToMenuPanel();
        });
        buttonDone.setBounds(466, 690, 348, 70); // x=466, y=690, width=348, height=70
        containerPanel.add(buttonDone);

        jLayeredPane.setLayer(containerPanel, 1);
        jLayeredPane.add(containerPanel);
        add(jLayeredPane);
    }

    // Phương thức tạo JSlider tùy chỉnh
    private JSlider createCustomSlider(int initialValue) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, initialValue);
        
        // Tùy chỉnh giao diện
        slider.setOpaque(false);
        slider.setForeground(Color.lightGray);
        slider.setBackground(new Color(0, 0, 0, 0));
        slider.setPreferredSize(new Dimension(300, 20));

        // Tùy chỉnh track và thumb
        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 102, 204));
                g2d.fillRect(trackRect.x, trackRect.y + trackRect.height / 4, trackRect.width, trackRect.height / 2);
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width - 2, thumbRect.height - 2);
            }
        });

        return slider;
    }

    private String getDifficultyString() {
        Difficulty difficulty = GameSettings.getInstance().getDifficulty();
        return difficulty.name().charAt(0) + difficulty.name().substring(1).toLowerCase();
    }

    private void incrementDifficulty() {
        Difficulty currentDifficulty = GameSettings.getInstance().getDifficulty();
        int nextIndex = (currentDifficulty.ordinal() + 1) % Difficulty.values().length;
        GameSettings.getInstance().setDifficulty(Difficulty.values()[nextIndex]);
    }

    @Override
    public void paint(Graphics g) {
        paintChildren(g);
    }
}