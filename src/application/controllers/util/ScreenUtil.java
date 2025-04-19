package application.controllers.util;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

public final class ScreenUtil {
    private static ScreenUtil instance;
    private static final Object lock = new Object(); 
    
    private int screenWidth;
    private int screenHeight;

    GraphicsEnvironment ge;
    GraphicsDevice gd;
    GraphicsConfiguration gc;
    AffineTransform at;

    private ScreenUtil() { 
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gc = gd.getDefaultConfiguration();
        at = gc.getDefaultTransform();
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
        System.out.println(screenWidth + ", " + screenHeight);
    }

    public static ScreenUtil getInstance() { 
        if (instance == null) {
            synchronized (lock) { 
                if (instance == null) {
                    instance = new ScreenUtil();
                }
            }
        }
        return instance;
    }

    public double getScreenScaleX() {
        return screenWidth / 1920.0;
    }

    public double getScreenScaleY() {
        return screenHeight / 1080.0;
    }
    
    public double getScaleX() {
        return at.getScaleX();
    }

    public double getScaleY() {
        return at.getScaleY();
    }


    public double getWidth() {
        return (double)gd.getDisplayMode().getWidth();
    }

    public double getHeight() {
        return (double)gd.getDisplayMode().getHeight();
    }
    
}