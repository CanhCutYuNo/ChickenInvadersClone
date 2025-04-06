package application.models.types;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    // Quản lý thời gian và trạng thái để thông báo khi tạo kỹ năng
    private long lastHoleSkillTime = 0;
    private long lastFireballSkillTime = 0;
    private long holeSkillCooldown = 15000; // 15 giây cho HOLE
    private long fireballSkillCooldown = 5000; // 5 giây cho FIREBALL
    private long skillsDelay = 5000; // Delay 5 giây giữa HOLE và FIREBALL
    private int skillState = 0; // 0: Tạo HOLE, 1: Tạo FIREBALL
    private boolean shouldCreateHole = false; // Cờ để thông báo tạo HOLE
    private boolean shouldCreateFireballBurst = false; // Cờ để thông báo tạo FIREBALL burst


    // Thêm biến để theo dõi trạng thái di chuyển
    private boolean isMovingToCenter = true;
    private static final int START_Y = 1400;
    private static final int TARGET_Y = 0;
    private static final int MOVE_SPEED = 2;


    //Quan ly thoi gian BOSS an hien
    private boolean isVisible = true;
    private long lastTeleportTime = 0;
    private long teleportInterval = 15000; // 15 giây giữa mỗi lần teleport
    private long disappearDuration = 3000; // Biến mất trong 3 giây
    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_HEIGHT = 1080;

    private Random random = new Random();

    public ChickenBoss(int posX, int posY, SoundController sound) {
        super(1000, 500, 600, (1920- 500)/2, START_Y, sound);
        System.out.println("ChickenBoss created at (" + PosX + "," + PosY + ")");

        addSkills(SkillType.HOLE, "/asset/resources/gfx/hole.png");
        addSkills(SkillType.FIREBALL, "/asset/resources/gfx/bullet-bolt1.png");

        gifFrames = new ArrayList<>();
        frameDelays = new ArrayList<>();
        lastHoleSkillTime = System.currentTimeMillis();
        lastFireballSkillTime = System.currentTimeMillis();

        new Thread(() -> {
            loadGif("/asset/resources/gfx/boss.gif");
            isGifLoaded = true;
        }).start();
    }

    private void loadGif(String path) {
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

    @Override
    public void render(Graphics g) {

        if(!isVisible) return;
        Graphics2D g2d = (Graphics2D) g.create();

        int centerX = PosX + MODEL_WIDTH / 2;
        int centerY = PosY + MODEL_HEIGHT / 2;
        g2d.rotate(Math.toRadians(rotate), centerX, centerY);

        if (isGifLoaded && !gifFrames.isEmpty()) {
            BufferedImage currentFrameImage = gifFrames.get(currentFrame);
            g2d.drawImage(currentFrameImage, PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT, null);
        } else {
            // Vẽ placeholder nếu GIF chưa tải
            g2d.setColor(Color.RED);
            g2d.fillRect(PosX, PosY, MODEL_WIDTH-50, MODEL_HEIGHT-50);
        }

        // Vẽ hitbox để debug
        g2d.setColor(Color.RED);
        Shape hitbox = getHitbox();
        g2d.draw(hitbox);

        g2d.dispose();
    }

    @Override
    public void nextFrame() {
        if (!isGifLoaded || gifFrames.isEmpty()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastFrameTime;
        int delay = frameDelays.get(currentFrame);

        if (elapsedTime >= delay) {
            currentFrame++;
            if (currentFrame >= gifFrames.size()) {
                currentFrame = 0;
            }
            lastFrameTime = currentTime;
        }

        if (isMovingToCenter) {
            PosY -= MOVE_SPEED;
            if (PosY <= TARGET_Y) {
                PosY = TARGET_Y;
                isMovingToCenter = false;
            } else {
                return;
            }
        }

        // Kiểm tra thời gian để thông báo tạo kỹ năng
        if (skillState == 0) {
            // Kiểm tra nếu đã qua cooldown để tạo HOLE
            if (currentTime - lastHoleSkillTime >= holeSkillCooldown) {
                shouldCreateHole = true; // Đặt cờ để Manager tạo HOLE
                lastHoleSkillTime = currentTime;
                skillState = 1; // Chuyển sang trạng thái tạo FIREBALL
                System.out.println("ChickenBoss requests new HOLE skill at " + currentTime);
            }
        } else if (skillState == 1) {
            // Kiểm tra nếu đã qua delay kể từ HOLE và qua cooldown của FIREBALL
            if (currentTime - lastHoleSkillTime >= skillsDelay &&
                currentTime - lastFireballSkillTime >= fireballSkillCooldown) {
                if(isVisible){
                    shouldCreateFireballBurst = true; // Đặt cờ để Manager tạo FIREBALL burst
                    lastFireballSkillTime = currentTime;
                    skillState = 0; // Chuyển sang trạng thái tạo HOLE
                    System.out.println("ChickenBoss requests new FIREBALL burst at " + currentTime);

                }

            }
        }

        //Visible
        if (isVisible && currentTime - lastTeleportTime >= teleportInterval) {
            isVisible = false;
            lastTeleportTime = currentTime; // Đánh dấu thời gian bắt đầu biến mất
        } else if (!isVisible && currentTime - lastTeleportTime >= disappearDuration) {
            teleportToRandomPosition();  // Xuất hiện ở vị trí ngẫu nhiên
            isVisible = true;
            lastTeleportTime = currentTime; // Đặt lại thời gian cho lần sau
        }
    }

    //Phuong thuc dich chuyen
    private void teleportToRandomPosition() {
        int newX = random.nextInt(SCREEN_WIDTH - MODEL_WIDTH);
        int newY = random.nextInt(SCREEN_HEIGHT - MODEL_HEIGHT);
        this.PosX = newX;
        this.PosY = newY;
        System.out.println("ChickenBoss teleported to: (" + PosX + ", " + PosY + ")");
    }

    // Phương thức để Manager gọi khi cần tạo FIREBALL burst
    public void createFireballBurst(EnemySkillsController skillsManager) {
        double centerX = PosX + MODEL_WIDTH / 2;
        double centerY = PosY + MODEL_HEIGHT / 2;
        int damage = 1000;
        double speed = 5;

        for (int i = 0; i < 10; i++) {
            double angleStart = i * 36;
            double angleEnd = (i + 1) * 36;

            for (int j = 0; j < 2; j++) {
                double angle = Math.toRadians(random.nextDouble() * (angleEnd - angleStart) + angleStart);
                double speedX = speed * Math.cos(angle);
                double speedY = speed * Math.sin(angle);
                skillsManager.addSkill(centerX, centerY, speedX, speedY, damage, SkillType.FIREBALL);
            }
        }
        shouldCreateFireballBurst = false; // Reset cờ sau khi tạo
    }

    // Phương thức để Manager gọi khi cần tạo HOLE
    public void createHoleSkill(EnemySkillsController skillsManager) {
        skillsManager.addSkill(PosX, PosY, 0, 5000, SkillType.HOLE);
        //skillsManager.addSkillImagePath(SkillType.HOLE, "/asset/resources/gfx/hole.png");
        shouldCreateHole = false; // Reset cờ sau khi tạo
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            sound.playSoundEffect("/asset/resources/sfx/death1.wav");
        }
    }

    @Override
    public void setPosY(int posY) {
        if (isMovingToCenter) {
            System.out.println("Cannot set PosY while ChickenBoss is moving to center. Current Y: " + PosY);
            return;
        }
        this.PosY = posY;
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT);
    }

    @Override
    public Map<SkillType, String> getSkills() {
        return super.getSkills();
    }

//    @Override
//    public DeathEffect getDeathEffect() {
//        return new DeathEffect(PosX, PosY, "/asset/resources/gfx/explosion.png", 5);
//    }

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

    public boolean shouldCreateHole() {
        return shouldCreateHole;
    }

    public boolean shouldCreateFireballBurst() {
        return shouldCreateFireballBurst;
    }

    public boolean isMovingToCenter() {
        return isMovingToCenter;
    }
    
    @Override
    public void addSkills(SkillType skillType, String imagePath) {
        skills.put(skillType, imagePath);
    }
}