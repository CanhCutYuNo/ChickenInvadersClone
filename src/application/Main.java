package application;

import java.util.*;
import javax.swing.*;

import application.Controllers.*;
import application.Models.*;
import application.Views.*;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
    	    JFrame frame = new JFrame("Chicken Invaders");
    	    frame.setUndecorated(true);
    	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	    
    	    CardLayout cardLayout = new CardLayout();
    	    JPanel mainPanel = new JPanel(cardLayout);

    	    Manager gameManager = new Manager(cardLayout, mainPanel);
    	    GamePanel gamePanel = new GamePanel(gameManager);

    	    MenuPanel menuPanel = new MenuPanel(() -> {
    	        cardLayout.show(mainPanel, "Game");
    	    }, frame::dispose);

    	    mainPanel.add(menuPanel, "Menu");
    	    mainPanel.add(gamePanel, "Game");

    	    frame.add(mainPanel);
    	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	    frame.setVisible(true);
    	    
    	    frame.addKeyListener(new Controller(frame, cardLayout, mainPanel));
            frame.setFocusable(true);
            frame.requestFocus();
    	    
    	    GameLoop gameLoop = new GameLoop(100, gameManager::update, gamePanel::repaint);
        	gameLoop.start();
    	});
    }
}