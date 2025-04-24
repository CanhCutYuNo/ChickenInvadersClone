package application.views.buttons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Button extends JButton {
    private static final long serialVersionUID = 1L;
    private boolean isEntered = false;
    private BufferedImage buttonImage;
    private BufferedImage buttonHoverImage;
    private BufferedImage checkedImage;

    public Button(String normalImagePath, String hoverImagePath) {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(Color.WHITE);
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

        buttonImage = loadImage(normalImagePath);
        buttonHoverImage = loadImage(hoverImagePath);

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isEntered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                isEntered = false;
                repaint();
            }
        });
    }

    private BufferedImage loadImage(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if(is != null) {
                return ImageIO.read(is);
            } else {
                System.out.println("Không tìm thấy file resource: " + resourcePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Không thể tải ảnh: " + resourcePath);
        }
        return null;
    }

    public void setCheckedImage(String checkedImagePath) {
        BufferedImage image = loadImage(checkedImagePath);
        checkedImage = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if(super.isEnabled()){
            paintEnabledComponent(g);
        }
        else{
            paintUnabledComponent(g);
        }

        g2D.dispose();
    }

    private void paintEnabledComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;

        if(isEntered && buttonHoverImage != null) {
            g2D.drawImage(buttonHoverImage, 0, 0, getWidth(), getHeight(), this);
        } else if(buttonImage != null) {
            g2D.drawImage(buttonImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fallback nếu không có ảnh
            g2D.setColor(new Color(0, 0, 0, 200));
            g2D.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        }
    
        if(super.isSelected() && checkedImage != null) {
            int x = (getWidth() - checkedImage.getWidth()) / 2;
            int y = (getHeight() - checkedImage.getHeight()) / 2;
            g2D.drawImage(checkedImage, x, y, this);
        }

        String text = getText();
        if(text != null && !text.isEmpty()) {
            g2D.setFont(getFont());
            FontMetrics fm = g2D.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2 - fm.getDescent();
            g2D.setColor(getForeground());
            g2D.drawString(text, x, y);
        }

    }

    private void paintUnabledComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;

        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        if(buttonImage != null) {
            g2D.drawImage(buttonImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2D.setColor(new Color(0, 0, 0, 200));
            g2D.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        }
    
        if(super.isSelected() && checkedImage != null) {
            int x = (getWidth() - checkedImage.getWidth()) / 2;
            int y = (getHeight() - checkedImage.getHeight()) / 2;
            g2D.drawImage(checkedImage, x, y, this);
        }

        String text = getText();
        if(text != null && !text.isEmpty()) {
            g2D.setFont(getFont());
            FontMetrics fm = g2D.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2 - fm.getDescent();
            g2D.setColor(getForeground());
            g2D.drawString(text, x, y);
        }       
    }

    public void enter() {
        isEntered = true;
        repaint();
    }

    public void exit() {
        isEntered = false;
        repaint();
    }

    public boolean isEntered() {
        return isEntered;
    }
}