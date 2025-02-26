package application.Controllers;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Controller extends KeyAdapter {
	private JFrame main;
	private CardLayout cardLayout;
	private JPanel mainPanel;
	
	public Controller(JFrame _main, CardLayout _cardLayout, JPanel _mainPanel) {
		this.main = _main;
		this.cardLayout = _cardLayout;
		this.mainPanel = _mainPanel;
	}
	
	 @Override
	    public void keyPressed(KeyEvent e) {
	        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                int res = JOptionPane.showConfirmDialog(
	                        main,
	                        "Do you want to return to the menu?",
	                        "Confirm",
	                        JOptionPane.YES_NO_OPTION
	                );
	                
	                if(res == JOptionPane.YES_OPTION) {
	                	cardLayout.show(mainPanel, "Menu");
	                }
	        }
	    }
	 
	
}
