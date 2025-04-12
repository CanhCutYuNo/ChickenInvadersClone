package application.views;

import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.*;

public class GameContainerPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLayeredPane jLayeredPane;
    private GamePanel gamePanel;
    private PausePanel pausePanel;
    private JPanel backgroundPanel; 

    public GameContainerPanel(GamePanel gamePanel, PausePanel pausePanel) {
        this.gamePanel = gamePanel;
        this.pausePanel = pausePanel;
        initComponents();
    }

    public void setBackgroundPanel(JPanel backgroundPanel) {
        if (backgroundPanel == null) return;

        if (this.backgroundPanel != null) {
            jLayeredPane.remove(this.backgroundPanel);
        }
        this.backgroundPanel = backgroundPanel;
        jLayeredPane.add(this.backgroundPanel, JLayeredPane.DEFAULT_LAYER); 
        jLayeredPane.setLayer(this.backgroundPanel, JLayeredPane.DEFAULT_LAYER); 
    }

    private void initComponents() {
        jLayeredPane = new JLayeredPane();
        setLayout(new BorderLayout());
        add(jLayeredPane, BorderLayout.CENTER);

         jLayeredPane.setLayout(new OverlayLayout(jLayeredPane));
        if (gamePanel != null) {
            gamePanel.setOpaque(false);
            jLayeredPane.add(gamePanel, JLayeredPane.PALETTE_LAYER); 
            jLayeredPane.setLayer(gamePanel, JLayeredPane.PALETTE_LAYER);
        }

        if (pausePanel != null) {
             pausePanel.setOpaque(false);
            jLayeredPane.add(pausePanel, JLayeredPane.MODAL_LAYER);
            jLayeredPane.setLayer(pausePanel, JLayeredPane.MODAL_LAYER);
            pausePanel.setVisible(false); 
        }
    }

    public void showPauseOverlay() {
        if(pausePanel != null) {
            pausePanel.setVisible(true);
            pausePanel.requestFocusInWindow();
        }
    }

    public void hidePauseOverlay() {
        if (pausePanel != null) {
            pausePanel.setVisible(false);
        }
        if (gamePanel != null) {
            gamePanel.requestFocusInWindow();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
       

}
