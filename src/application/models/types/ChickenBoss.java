package application.models.types;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import application.controllers.EnemySkillsController;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;

public class ChickenBoss extends Enemy {
    private List<BufferedImage> gifFrames;
    private List<Integer> frameDelays;
    private int currentFrame = 0;
    private long lastFrameTime;
    private float rotate = 0f;
    private int initialIndex;
    private boolean isGifLoaded = false;

    // Quản lý chiêu thức
    private long lastHoleSkillTime = 0;
    private long lastFireballSkillTime = 0;
    private long holeSkillCooldown = 15000; // 15 giây cho HOLE
    private long fireballSkillCooldown = 5000; // 5 giây cho FIREBALL
    private long skillsDelay = 2000; // Delay 2 giây giữa HOLE và FIREBALL

    // Thêm biến để theo dõi trạng thái di chuyển
    private boolean isMovingToCenter = true;
    private static final int START_Y = 1400;
    private static final int TARGET_Y = 0;
    private static final int MOVE_SPEED = 2;

    private Random random = new Random();

    public ChickenBoss(int posX, int posY, SoundController sound) {
        super(1000, 500, 600, posX, START_Y, sound, createSkillImagePaths());
        System.out.println("ChickenBoss created at(" + PosX + "," + PosY + ")");
        
        gifFrames = new ArrayList<>();
        frameDelays = new ArrayList<>();
        lastHoleSkillTime = System.currentTimeMillis();
        lastFireballSkillTime = System.currentTimeMillis();

        new Thread(() -> {
            loadGif("/asset/resources/gfx/boss.gif");
            isGifLoaded = true;
        }).start();
    }

    private static Map<SkillType, String> createSkillImagePaths() {
        Map<SkillType, String> skillImagePaths = new HashMap<>();
        skillImagePaths.put(SkillType.HOLE, "/asset/resources/gfx/hole.png");
        skillImagePaths.put(SkillType.FIREBALL, "/asset/resources/gfx/bullet-bolt1.png");
        return skillImagePaths;
    }

    private void loadGif(String path) {
        long startTime = System.currentTimeMillis();
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            if(inputStream == null) {
                throw new IOException("Cannot find GIF file at path: " + path);
            }
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            reader.setInput(ImageIO.createImageInputStream(inputStream));
            
            int numFrames = reader.getNumImages(true);
            System.out.println("Loading GIF with " + numFrames + " frames...");
            for(int i = 0; i < numFrames; i++) {
                BufferedImage frame = reader.read(i);
                gifFrames.add(frame);

                IIOMetadata metadata = reader.getImageMetadata(i);
                IIOMetadataNode root =(IIOMetadataNode) metadata.getAsTree("javax_imageio_gif_image_1.0");
                IIOMetadataNode gce =(IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
                int delay = Math.min(Integer.parseInt(gce.getAttribute("delayTime")) * 10, 50);
                frameDelays.add(delay);
                System.out.println("Frame " + i + " delay: " + delay + " ms");
            }
        } catch(IOException e) {
            System.err.println("Error loading GIF: " + e.getMessage());
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("GIF loading took " +(endTime - startTime) + " ms");
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d =(Graphics2D) g.create();
        
        if(!isMovingToCenter) {
            skillsController.drawSkills(g);
        }
        
        int centerX = PosX + MODEL_WIDTH / 2;
        int centerY = PosY + MODEL_HEIGHT / 2;
        g2d.rotate(Math.toRadians(rotate), centerX, centerY);
        
        BufferedImage currentFrameImage = gifFrames.get(currentFrame);
        g2d.drawImage(currentFrameImage, 0, PosY, 1920, PosY + 1080, null);
        
        g2d.dispose();
    }

    @Override
    public void nextFrame() {
        if(!isGifLoaded || gifFrames.isEmpty()) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastFrameTime;
        int delay = frameDelays.get(currentFrame);

        if(elapsedTime >= delay) {
            currentFrame++;
            if(currentFrame >= gifFrames.size()) {
                currentFrame = 0;
            }
            lastFrameTime = currentTime;
        }
        
        if(isMovingToCenter) {
            PosY -= MOVE_SPEED;
            if(PosY <= TARGET_Y) {
                PosY = TARGET_Y;
                isMovingToCenter = false;
            } else return;
        }

        // Kiểm tra trạng thái của HOLE và FIREBALL
        boolean hasActiveHole = false;
        boolean hasActiveFireball = false;
        long lastHoleEndTime = 1;
        long lastFireballEndTime = 0;

        for(var skill : skillsController.getSkills()) {
            if(skill.getSkillType() == SkillType.HOLE) {
                if(skill.isActive()) {
                    hasActiveHole = true;
                } else {
                    lastHoleEndTime = Math.max(lastHoleEndTime, skill.getEndTime());
                }
            } else if(skill.getSkillType() == SkillType.FIREBALL) {
                if(skill.isActive()) {
                    hasActiveFireball = true;
                } else {
                    lastFireballEndTime = Math.max(lastFireballEndTime, skill.getEndTime());
                }
            }
        }

        // Tạo HOLE nếu không có HOLE đang hoạt động và FIREBALL vừa kết thúc
        if(!hasActiveHole && !hasActiveFireball && lastFireballEndTime > 0 && 
           (currentTime - lastFireballEndTime >= skillsDelay) && 
           (currentTime - lastHoleSkillTime >= holeSkillCooldown)) {
            skillsController.addSkill(1920 / 2, 1080 / 2, 0, 50, SkillType.HOLE);
            lastHoleSkillTime = currentTime;
            System.out.println("New HOLE skill created at " + currentTime);
        }

        // Tạo FIREBALL nếu không có FIREBALL đang hoạt động và HOLE vừa kết thúc
        if(!hasActiveFireball && !hasActiveHole && lastHoleEndTime > 0 && 
           (currentTime - lastHoleEndTime >= skillsDelay) && 
           (currentTime - lastFireballSkillTime >= fireballSkillCooldown)) {
            createFireballBurst();
            lastFireballSkillTime = currentTime;
            System.out.println("New FIREBALL burst created at " + currentTime);
        }

        skillsController.updateSkills();
    }

    private void createFireballBurst() {
        double centerX = 1920 / 2;
        double centerY = 1080 / 2;
        int damage = 1000;
        double speed = 5;

        for(int i = 0; i < 10; i++) {
            double angleStart = i * 36;
            double angleEnd =(i + 1) * 36;

            for(int j = 0; j < 2; j++) {
                double angle = Math.toRadians(random.nextDouble() *(angleEnd - angleStart) + angleStart);
                double speedX = speed * Math.cos(angle);
                double speedY = speed * Math.sin(angle);
                skillsController.addSkill(centerX, centerY, speedX, speedY, damage, SkillType.FIREBALL);
            }
        }
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if(hp <= 0) {
            sound.playSoundEffect("/asset/resources/sfx/death1.wav");
        }
    }

    @Override
    public void setPosY(int posY) {
        if(isMovingToCenter) {
            System.out.println("Cannot set PosY while ChickenBoss is moving to center. Current Y: " + PosY);
            return;
        }
        this.PosY = posY;
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

    public boolean isGifLoaded() {
        return isGifLoaded;
    }

    public EnemySkillsController getSkillsController() {
        return skillsController;
    }
}