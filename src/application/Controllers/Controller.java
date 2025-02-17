package application.Controllers;

import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Controller extends KeyAdapter {
	
	private JFrame main;
	
	
	public Controller(JFrame _main) {
		this.main = _main;
	}
	
	 @Override
	    public void keyPressed(KeyEvent e) {
	        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                int result = JOptionPane.showConfirmDialog(
	                        main,
	                        "Do you want to return to the menu?",
	                        "Confirm",
	                        JOptionPane.YES_NO_OPTION
	                );
	        }
	    }
}
