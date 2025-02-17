package application.Views;

import application.Main;
import application.Controllers.*;
import application.Models.*;
import application.Views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuPanel extends BasePanel {
	
    public MenuPanel(Runnable onStart, Runnable onExit) {
        super("/asset/resources/background.png");

        setLayout(new BorderLayout());

        // Menu title
        JLabel titleLabel = new JLabel("Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

     // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Transparent button panel
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(200, 50));
        startButton.addActionListener(e -> {
        	hideCursor();
        	onStart.run();
        });

        JButton settingButton = new JButton("Setting");
        settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingButton.setMaximumSize(new Dimension(200, 50));
        settingButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chưa làm", "Setting", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(200, 50));
        exitButton.addActionListener(e -> System.exit(0));

        // add buttons to buttonPanel
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(settingButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalGlue());

        add(buttonPanel, BorderLayout.CENTER);

    }
	
	private void hideCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "InvisibleCursor");

        setCursor(invisibleCursor);
    }

}
