package application.controllers.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter {
    private ViewController viewController;

    public KeyController(ViewController viewController) {
        this.viewController = viewController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if(viewController == null) {
                return;
            }

            viewController.switchToPausePanel();
        }

    }
}