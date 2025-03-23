//package application.views;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.image.BufferedImage;
//
//class imgButton extends JButton {
//    private Image leftCap, middlePart, rightCap;
//    private Image hoverLeftCap, hoverMiddlePart, hoverRightCap;
//    private boolean isHovered = false;
//    
//    private int leftWidth, middleWidth, rightWidth, buttonHeight;
//
//    public imgButton(String path, String hoverPath, int stretchWidth) {
//        ImageIcon icon = new ImageIcon(getClass().getResource(path));
//        ImageIcon hoverIcon = new ImageIcon(getClass().getResource(hoverPath));
//        
//        Image fullImage = icon.getImage();
//        Image fullHoverImage = hoverIcon.getImage();
//
//        // Xác định kích thước
//        leftWidth = fullImage.getWidth(null) / 4;
//        rightWidth = leftWidth;
//        middleWidth = fullImage.getWidth(null) - leftWidth - rightWidth;
//        buttonHeight = fullImage.getHeight(null);
//
//        // Cắt ảnh thành 3 phần
//        leftCap = cropImage(fullImage, 0, leftWidth);
//        middlePart = cropImage(fullImage, leftWidth, middleWidth);
//        rightCap = cropImage(fullImage, leftWidth + middleWidth, rightWidth);
//
//        // Cắt ảnh hover
//        hoverLeftCap = cropImage(fullHoverImage, 0, leftWidth);
//        hoverMiddlePart = cropImage(fullHoverImage, leftWidth, middleWidth);
//        hoverRightCap = cropImage(fullHoverImage, leftWidth + middleWidth, rightWidth);
//
//        // Đặt kích thước mới
//        int newWidth = leftWidth + stretchWidth + rightWidth;
//        setPreferredSize(new Dimension(newWidth, buttonHeight));
//        setContentAreaFilled(false);
//        setBorderPainted(false);
//        setFocusPainted(false);
//        setOpaque(false);
//
//        // Xử lý hover
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                isHovered = true;
//                repaint();
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                isHovered = false;
//                repaint();
//            }
//        });
//    }
//
//    private Image cropImage(Image source, int x, int width) {
//        BufferedImage bi = new BufferedImage(width, buttonHeight, BufferedImage.TYPE_INT_ARGB);
//        Graphics g = bi.getGraphics();
//        g.drawImage(source, 0, 0, width, buttonHeight, x, 0, x + width, buttonHeight, null);
//        g.dispose();
//        return bi;
//    } 
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        int width = getWidth();
//
//        Image left = isHovered ? hoverLeftCap : leftCap;
//        Image middle = isHovered ? hoverMiddlePart : middlePart;
//        Image right = isHovered ? hoverRightCap : rightCap;
//
//        // Vẽ phần trái
//        g2d.drawImage(left, 0, 0, leftWidth, buttonHeight, this);
//
//        // Vẽ phần giữa (co giãn)
//        g2d.drawImage(middle, leftWidth, 0, width - leftWidth - rightWidth, buttonHeight, this);
//
//        // Vẽ phần phải
//        g2d.drawImage(right, width - rightWidth, 0, rightWidth, buttonHeight, this);
//
//        super.paintComponent(g);
//    }
//}
