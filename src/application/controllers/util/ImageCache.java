package application.controllers.util;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class ImageCache {
    private static ImageCache instance;
    private static final Object lock = new Object(); 

    private final Map<String, ImageIcon> cache;

    private ImageCache() {
        cache = new HashMap<>();

        loadImageAsset();
    }

    private void loadImageAsset(){
        String basePath = "/asset/resources/gfx";
        File directory = new File(getClass().getResource(basePath).getFile());
        
        // Fix lỗi space trong đường dẫn
        directory = new File(directory.getAbsolutePath().replaceAll("%20", " ")); 

        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile() && (file.getName().endsWith(".png") )|| file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
                    String path = basePath + "/" + file.getName();
                    cache.put(path, new ImageIcon(ImageCache.class.getResource(path)));
                }
            }
        }
    }

    public static ImageCache getInstance() {
        if (instance == null) {
            synchronized (lock) { 
                if (instance == null) {
                    instance = new ImageCache();
                }
            }
        }
        return instance;
    }

    public Image getResourceImage(String path) {
        if (!cache.containsKey(path)) {
            cache.put(path, new ImageIcon(ImageCache.class.getResource(path)));
        }
        return cache.get(path).getImage();
    }

    public ImageIcon getResourceImageIcon(String path) {
        if (!cache.containsKey(path)) {
            cache.put(path, new ImageIcon(ImageCache.class.getResource(path)));
        }
        return cache.get(path);
    }

}