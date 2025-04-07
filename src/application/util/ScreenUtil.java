package application.util;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;

public final class ScreenUtil {
    private static ScreenUtil instance;
    private static final Object lock = new Object(); 

    GraphicsEnvironment ge;
    GraphicsDevice gd;
    GraphicsConfiguration gc;
    AffineTransform at;

    private ScreenUtil() { 
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gc = gd.getDefaultConfiguration();
        at = gc.getDefaultTransform();
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
