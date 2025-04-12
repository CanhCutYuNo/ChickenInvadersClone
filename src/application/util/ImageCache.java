package application.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.swing.ImageIcon;

public class ImageCache {
    private static ImageCache instance;
    private static final Object lock = new Object(); 

    private final Map<String, ImageIcon> cache;

    private List<BufferedImage> gifFrames;
    private List<Integer> frameDelays;
        
    private ImageCache() {
        cache = new HashMap<>();
        gifFrames = new ArrayList<>();
        frameDelays = new ArrayList<>();

        loadImageAsset();

        Thread t = new Thread(() -> {
            loadBossGifAsset();
        });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
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

    private void loadBossGifAsset(){
        String path = "/asset/resources/gfx/boss.gif";

        long startTime = System.currentTimeMillis();
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            if (inputStream == null) {
                throw new IOException("Cannot find GIF file at path: " + path);
            }
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            reader.setInput(ImageIO.createImageInputStream(inputStream));

            int numFrames = reader.getNumImages(true);
            System.out.println("Loading GIF with " + numFrames + " frames...");
            for (int i = 0; i < numFrames; i++) {
                BufferedImage frame = reader.read(i);
                gifFrames.add(frame);

                IIOMetadata metadata = reader.getImageMetadata(i);
                IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree("javax_imageio_gif_image_1.0");
                IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
                int delay = Math.min(Integer.parseInt(gce.getAttribute("delayTime")) * 10, 50);
                frameDelays.add(delay);
                System.out.println("Frame " + i + " delay: " + delay + " ms");
            }
        } catch (IOException e) {
            System.err.println("Error loading GIF: " + e.getMessage());
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("GIF loading took " + (endTime - startTime) + " ms");
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

    public List<BufferedImage> getBossImages(){
        return gifFrames;
    }
}