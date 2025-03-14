package application.views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import application.Main;
import application.controllers.*;

public class MenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private ViewController viewController;
    private JPanel backgroundPanel;
    private SoundController sound;
    private MouseController mouseController;
    private GamePanel gamePanel; // Tham chiếu đến GamePanel để lấy tọa độ màn hình

    public MenuPanel(ViewController viewController, MouseController mouseController, GamePanel gamePanel) {
        this.viewController = viewController;
        this.sound = new SoundController();
        this.mouseController = mouseController;
        this.gamePanel = gamePanel; // Lưu tham chiếu
        initComponents();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                sound.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
            }
        });
    }

    // Phương thức sử dụng Robot để gán con trỏ chuột vào vị trí player
    private void snapMouseToPlayer() {
        if (gamePanel == null) {
            System.err.println("gamePanel là null, không thể gán con trỏ chuột!");
            return;
        }

        try {
            Robot robot = new Robot();
            // Tọa độ logic của player (cố định như bạn đã chỉ định)
            int playerX = 800 + 35; // 835
            int playerY = 950 + 32; // 982

            // Chuyển đổi tọa độ logic thành tọa độ màn hình
            Point panelLocation = gamePanel.getLocationOnScreen();
            System.out.println("Panel Location: (" + panelLocation.x + ", " + panelLocation.y + ")");
            int screenX = panelLocation.x + playerX;
            int screenY = panelLocation.y + playerY;
            System.out.println("Tọa độ trước khi gán: (" + screenX + ", " + screenY + ")");

            // Đặt con trỏ chuột vào vị trí của player
            robot.mouseMove(screenX, screenY);
            System.out.println("Đã gán con trỏ chuột vào player tại: (" + screenX + ", " + screenY + ")");
        } catch (AWTException e) {
            System.err.println("Lỗi khi sử dụng Robot: " + e.getMessage());
            e.printStackTrace();
        }
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
        icon = new JLabel();
        buttonPanel = new JPanel();
        buttonPanel1 = new JPanel();
        button1 = new Button(300, 50);
        buttonPanel2 = new JPanel();
        button2 = new Button(300, 50);
        buttonPanel3 = new JPanel();
        button3 = new Button(300, 50);

        setLayout(new GridLayout(1, 1));
        jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));

        containerPanel.setOpaque(false);
        containerPanel.setPreferredSize(new Dimension(485, 410));
        containerPanel.setLayout(new GridLayout(2, 1));

        symbolPanel.setOpaque(false);
        symbolPanel.setLayout(new GridBagLayout());

        ImageIcon gameIcon = new ImageIcon(getClass().getResource("/asset/resources/gfx/logo5.png"));
        icon.setIcon(gameIcon);
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        icon.setForeground(Color.WHITE);
        icon.setHorizontalTextPosition(SwingConstants.CENTER);
        icon.setVerticalTextPosition(SwingConstants.BOTTOM);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 155, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        symbolPanel.add(icon, gbc);

        containerPanel.add(symbolPanel);
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(3, 1));

        buttonPanel1.setOpaque(false);
        buttonPanel1.setLayout(new GridBagLayout());

        button1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (viewController != null) {
                    sound.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
                    viewController.switchToGameContainerPanel();

                    // Kích hoạt transition khi chuyển sang GamePanel
                    if (gamePanel != null) {
                        gamePanel.triggerTransition();

                        // Sử dụng invokeLater để đảm bảo GamePanel đã hiển thị
                        SwingUtilities.invokeLater(() -> {
                            snapMouseToPlayer();
                        });
                    }
                }
            }
        });

        button1.setText("Play");
        button1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        buttonPanel1.add(button1, new GridBagConstraints());
        buttonPanel.add(buttonPanel1);

        buttonPanel2.setOpaque(false);
        buttonPanel2.setLayout(new GridBagLayout());

        button2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                sound.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
                if (viewController != null) {
                    viewController.switchToSettingPanel();
                }
            }
        });

        button2.setText("Options");
        button2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        buttonPanel2.add(button2, new GridBagConstraints());
        buttonPanel.add(buttonPanel2);

        buttonPanel3.setOpaque(false);
        buttonPanel3.setLayout(new GridBagLayout());

        button3.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                sound.playSoundEffect(getClass().getResource("/asset/resources/sfx/clickXP.wav").getPath());
                System.exit(0);
            }
        });

        button3.setText("Quit Game");
        button3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        buttonPanel3.add(button3, new GridBagConstraints());
        buttonPanel.add(buttonPanel3);

        containerPanel.add(buttonPanel);
        jLayeredPane.setLayer(containerPanel, 1);
        jLayeredPane.add(containerPanel);
        add(jLayeredPane);
    }

    @Override
    public void paint(Graphics g) {
        paintChildren(g);
    }

    private Button button1, button2, button3;
    private JPanel buttonPanel, buttonPanel1, buttonPanel2, buttonPanel3, containerPanel, symbolPanel;
    private JLabel icon;
    private JLayeredPane jLayeredPane;
}