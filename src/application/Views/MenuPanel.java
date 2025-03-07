package application.Views;

import application.Controllers.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MenuPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SoundController backgroundMenuMusic;
    private ViewController viewController;
    private JPanel backgroundPanel;

    public MenuPanel(ViewController viewController) {
        this.viewController = viewController;
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
        //lineBorder1 = new javax.swing.border.LineBorder(new Color(0, 0, 0));
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

        icon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        icon.setForeground(Color.WHITE);
        icon.setText("GAME");
        icon.setHorizontalTextPosition(SwingConstants.CENTER);
        symbolPanel.add(icon, new GridBagConstraints());

        containerPanel.add(symbolPanel);
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(3, 1));

        // Button 1 (Play)
        buttonPanel1.setOpaque(false);
        buttonPanel1.setLayout(new GridBagLayout());

        button1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (backgroundMenuMusic != null) {
                    backgroundMenuMusic.stop(); // Dừng nhạc nền
                }
                if (viewController != null) {
                    viewController.switchToGameContainerPanel();
                }
            }
        });

        button1.setText("Play");
        button1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        buttonPanel1.add(button1, new GridBagConstraints());
        buttonPanel.add(buttonPanel1);

        // Button 2 (Options)
        buttonPanel2.setOpaque(false);
        buttonPanel2.setLayout(new GridBagLayout());

        button2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
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

        try {
            backgroundMenuMusic = new SoundController(getClass().getResource("/asset/resources/sfx/CI4Theme.wav").getPath());
            backgroundMenuMusic.loop();
        } catch (Exception e) {
            System.err.println("Lỗi khi tải file nhạc: " + e.getMessage());
        }
     
    }// </editor-fold>      
    
    @Override
    public void paint(Graphics g) {
        paintChildren(g);
    }

    private Button button1, button2, button3;
    private JPanel buttonPanel, buttonPanel1, buttonPanel2, buttonPanel3, containerPanel, symbolPanel;
    private JLabel icon;
    private JLayeredPane jLayeredPane;
    //private javax.swing.border.LineBorder lineBorder1;
}
