/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package application.Views;
import java.awt.*;

/**
 *
 * @author hp
 */
public class Button extends javax.swing.JButton {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isEntered = false;
    public Button(int width, int height) {
        //Xóa Button UI
        setContentAreaFilled(false);
        setBorder(null);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(Color.white);
        setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 24));
        setPreferredSize(new java.awt.Dimension(width, height));
        repaint();
        
        //Event on mouse enter and mouse exit
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isEntered = true;
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                isEntered = false;
                repaint();
            }
            });
                
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        //Button background
        g2D.setColor(new Color(0, 0, 0, 220));
        g2D.fillRect(2, 2, getWidth() - 4, getHeight() - 4);

        //Border
        if(isEntered){
            g2D.setColor(Color.WHITE);
        }
        else{
            g2D.setColor(new Color(80, 80, 80));
        }
        g2D.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2D.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
        
        super.paint(g2D);
    }
    
    

}
