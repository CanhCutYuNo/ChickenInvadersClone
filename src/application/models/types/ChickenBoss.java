package application.models.types;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import application.controllers.SoundController;
import application.models.Enemy;

public class ChickenBoss extends Enemy {
    private List<BufferedImage> gifFrames; // Danh sách các frame của GIF
    private List<Integer> frameDelays; // Thời gian delay giữa các frame (tính bằng mili giây)
    private int currentFrame = 0; // Frame hiện tại
    private long lastFrameTime = 0; // Thời gian cuối cùng chuyển frame
    private float rotate = 0f;
    private int initialIndex;
    private boolean isForward = true;

    public ChickenBoss(int posX, int posY, SoundController sound) {
        super(1000, 300, 400, posX, posY, sound);
        System.out.println("ChickenBoss created at (" + posX + "," + posY + ")");
        
        // Tải GIF và các frame
        gifFrames = new ArrayList<>();
        frameDelays = new ArrayList<>();
        loadGif("/asset/resources/gfx/22.gif"); // Đường dẫn đến file GIF
    }

    private void loadGif(String path) {
        try {
            // Đọc GIF từ resource
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            reader.setInput(ImageIO.createImageInputStream(getClass().getResourceAsStream(path)));
            
            int numFrames = reader.getNumImages(true);
            for (int i = 0; i < numFrames; i++) {
                // Đọc frame
                BufferedImage frame = reader.read(i);
                gifFrames.add(frame);

                // Đọc metadata để lấy delay của frame
                IIOMetadata metadata = reader.getImageMetadata(i);
                IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree("javax_imageio_gif_image_1.0");
                IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
                int delay = Integer.parseInt(gce.getAttribute("delayTime")) * 10; // Chuyển từ 1/100 giây sang mili giây
                frameDelays.add(delay);
            }
        } catch (IOException e) {
            System.err.println("Error loading GIF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void render(Graphics g) {
        System.out.println("Rendering ChickenBoss at (" + PosX + "," + PosY + "), Frame: " + currentFrame);
        Graphics2D g2d = (Graphics2D) g.create();

        // Xoay nếu cần
        int centerX = PosX + MODEL_WIDTH / 2;
        int centerY = PosY + MODEL_HEIGHT / 2;
        g2d.rotate(Math.toRadians(rotate), centerX, centerY);

        // Vẽ frame hiện tại của GIF
        if (!gifFrames.isEmpty()) {
            BufferedImage currentFrameImage = gifFrames.get(currentFrame);
            g2d.drawImage(currentFrameImage, 0, 0, 1920, 1080, null);
        } else {
            System.err.println("No GIF frames to render!");
        }

        g2d.dispose();
    }

    @Override
    public void nextFrame() {
        if (gifFrames.isEmpty()) {
            return;
        }

        // Kiểm tra thời gian để chuyển frame
        long currentTime = System.currentTimeMillis();
        int delay = frameDelays.get(currentFrame);
        if (currentTime - lastFrameTime >= delay) {
            currentFrame++;
            if (currentFrame >= gifFrames.size()) {
                currentFrame = 0; // Quay lại frame 0 khi đến cuối
            }
            lastFrameTime = currentTime;
            System.out.println("Advanced to frame: " + currentFrame);
        }
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            sound.playSoundEffect("/asset/resources/sfx/death1.wav");
        }
    }

    public int getInitialIndex() {
        return initialIndex;
    }

    public void setInitialIndex(int initialIndex) {
        this.initialIndex = initialIndex;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }
}