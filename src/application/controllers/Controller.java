package application.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    private ViewController viewController;

    public Controller(ViewController viewController) {
        if(viewController == null) {
             throw new IllegalArgumentException("ViewController cannot be null in Controller");
        }
        this.viewController = viewController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (viewController == null) {
                return;
            }

            viewController.switchToPausePanel();
        }

    }
}
