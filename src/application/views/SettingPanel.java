package application.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;

import application.controllers.GameSettings;
import application.controllers.GameSettings.Difficulty;
import application.controllers.SoundController;
import application.controllers.ViewController;
import application.util.ScreenUtil;

public class SettingPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private ViewController viewController;
    private SoundController soundClick;
    private JLayeredPane jLayeredPane;
    private JPanel backgroundPanel;
    private Button buttonDifficulty, buttonDone;
    private JSlider backgroundMusicSlider, soundEffectSlider;
    private JLabel backgroundMusicValueLabel, soundEffectValueLabel;
    private final double scaleX = ScreenUtil.getInstance().getScaleX();
    private final double scaleY = ScreenUtil.getInstance().getScaleY();

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
        JLabel headerLabel = new JLabel("Settings");
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(40 * scaleX)));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBounds(scaledBounds(890, 50, 300, 50)); 
        containerPanel.add(headerLabel);

        // Difficulty
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setOpaque(false);
        difficultyPanel.setLayout(null); 
        difficultyPanel.setBounds(scaledBounds(645, 400, 594, 200));

        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(30 * scaleX)));
        difficultyLabel.setBounds(scaledBounds(65, 30, 200, 60)); 
        difficultyPanel.add(difficultyLabel);

        buttonDifficulty = new Button("/asset/resources/gfx/editbox.png", "/asset/resources/gfx/editbox_hover.png");
        buttonDifficulty.setText(getDifficultyString());
        buttonDifficulty.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(30 * scaleX)));
        buttonDifficulty.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            incrementDifficulty();
            buttonDifficulty.setText(getDifficultyString());
            GameSettings.getInstance().saveSettings();
        });
        buttonDifficulty.setBounds(scaledBounds(210, 0, 384, 120)); 
        difficultyPanel.add(buttonDifficulty);

        containerPanel.add(difficultyPanel);

        // Sound Volume Slider (Sound Effect)
        JPanel soundEffectPanel = new JPanel();
        soundEffectPanel.setOpaque(false);
        soundEffectPanel.setLayout(null);
        soundEffectPanel.setBounds(scaledBounds(645, 500, 1094, 200));

        JLabel soundEffectLabel = new JLabel("Sound volume:");
        soundEffectLabel.setForeground(Color.WHITE);
        soundEffectLabel.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(30 * scaleX)));
        soundEffectLabel.setBounds(scaledBounds(10, 30, 400, 60));
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

        Button soundEffectButton = new Button("/asset/resources/gfx/editbox.png", "/asset/resources/gfx/editbox_hover.png");
        soundEffectButton.setLayout(null);
        soundEffectSlider.setBounds(scaledBounds(42, 50, 300, 20));
        soundEffectButton.add(soundEffectSlider);
        soundEffectButton.setBounds(scaledBounds(210, 0, 384, 120));
        soundEffectPanel.add(soundEffectButton);
        
        // Thêm MouseListener cho JSlider
        soundEffectSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                soundEffectButton.enter(); // Kích hoạt hiệu ứng hover của Button
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Kiểm tra xem chuột có còn trong khu vực của Button không
                java.awt.Point mousePos = evt.getPoint();
                SwingUtilities.convertPointToScreen(mousePos, soundEffectSlider);
                java.awt.Rectangle buttonBounds = soundEffectButton.getBounds();
                buttonBounds.setLocation(soundEffectButton.getLocationOnScreen());
                if (!buttonBounds.contains(mousePos)) {
                    soundEffectButton.exit(); // Tắt hiệu ứng hover nếu chuột rời khỏi Button
                }
            }
        });

        containerPanel.add(soundEffectPanel);

        // Music Volume Slider (Background Music)
        JPanel bgMusicPanel = new JPanel();
        bgMusicPanel.setOpaque(false);
        bgMusicPanel.setLayout(null);
        bgMusicPanel.setBounds(scaledBounds(645, 600, 1094, 200)); // x=343, y=450, width=594, height=100

        JLabel bgMusicLabel = new JLabel("Music volume:");
        bgMusicLabel.setForeground(Color.WHITE);
        bgMusicLabel.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(30 * scaleX)));
        bgMusicLabel.setBounds(scaledBounds(15, 30, 400, 60)); // x=0, y=35, width=200, height=30
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

        Button bgMusicButton = new Button("/asset/resources/gfx/editbox.png", "/asset/resources/gfx/editbox_hover.png");
        bgMusicButton.setLayout(null);
        backgroundMusicSlider.setBounds(scaledBounds(42, 50, 300, 20)); // x=42, y=40, width=300, height=20
        bgMusicButton.add(backgroundMusicSlider);
        bgMusicButton.setBounds(scaledBounds(210, 0, 384, 120)); // x=210, y=0, width=384, height=100
        bgMusicPanel.add(bgMusicButton);
        
     // Thêm MouseListener cho JSlider
        backgroundMusicSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bgMusicButton.enter(); // Kích hoạt hiệu ứng hover của Button
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Kiểm tra xem chuột có còn trong khu vực của Button không
                java.awt.Point mousePos = evt.getPoint();
                SwingUtilities.convertPointToScreen(mousePos, soundEffectSlider);
                java.awt.Rectangle buttonBounds = bgMusicButton.getBounds();
                buttonBounds.setLocation(bgMusicButton.getLocationOnScreen());
                if (!buttonBounds.contains(mousePos)) {
                    bgMusicButton.exit(); // Tắt hiệu ứng hover nếu chuột rời khỏi Button
                }
            }
        });

        containerPanel.add(bgMusicPanel);

     // Mute Audio Checkbox
        JPanel muteAudioPanel = new JPanel();
        muteAudioPanel.setOpaque(false);
        muteAudioPanel.setLayout(null);
        muteAudioPanel.setBounds(scaledBounds(645, 700, 900, 400)); // Kích cỡ và vị trí giống trước

        // Tạo JLabel cho chữ "Mute audio"
        JLabel muteAudioLabel = new JLabel("Mute audio:");
        muteAudioLabel.setForeground(Color.WHITE);
        muteAudioLabel.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(30 * scaleX)));
        muteAudioLabel.setBounds(scaledBounds(42, 30, 400, 40));
        muteAudioPanel.add(muteAudioLabel);

     // Tạo Button thay cho JCheckBox
        Button muteAudioButton = new Button("/asset/resources/gfx/checkbox.png", "/asset/resources/gfx/checkbox_hover.png");
        muteAudioButton.setCheckedImage("/asset/resources/gfx/tick.png"); // Đặt hình ảnh dấu tick
        muteAudioButton.setChecked(GameSettings.getInstance().isMuteAudio()); // Khởi tạo trạng thái
        muteAudioButton.setBounds(scaledBounds(230, 5, 100, 100)); 
        muteAudioButton.addActionListener(e -> {
            muteAudioButton.setChecked(!muteAudioButton.isChecked()); // Đảo trạng thái
            GameSettings.getInstance().setMuteAudio(muteAudioButton.isChecked());
            GameSettings.getInstance().saveSettings();
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
        });
        muteAudioPanel.add(muteAudioButton);

        containerPanel.add(muteAudioPanel);

        // Back Button
        buttonDone = new Button("/asset/resources/gfx/button.png", "/asset/resources/gfx/button_hover.png");
        buttonDone.setText("Back");
        buttonDone.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(30 * scaleX)));
        buttonDone.addActionListener(e -> {
            soundClick.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            viewController.switchToMenuPanel();
        });
        buttonDone.setBounds(scaledBounds(50, 950, 280, 110)); // x=466, y=690, width=348, height=70
        containerPanel.add(buttonDone);

        jLayeredPane.setLayer(containerPanel, 1);
        jLayeredPane.add(containerPanel);
        add(jLayeredPane);
    }

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

            @Override
            public void paintFocus(Graphics g) {
               
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