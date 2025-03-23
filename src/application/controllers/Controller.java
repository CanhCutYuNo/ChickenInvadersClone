package application.controllers;

import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Controller extends KeyAdapter {

    private JFrame main;
    private ViewController viewController;

    public Controller(JFrame _main, CardLayout _cardLayout, JPanel _mainPanel, ViewController _viewController) {
        this.main = _main;
        this.viewController = _viewController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            int res = JOptionPane.showConfirmDialog(
                    main,
                    "Do you want to return to the menu?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (res == JOptionPane.YES_OPTION) {
                viewController.switchToMenuPanel();
            }
        }
    }

}
