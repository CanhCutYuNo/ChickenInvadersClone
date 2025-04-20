package application.views.enemy;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import application.controllers.util.ImageCache;
import application.controllers.util.SoundController;
import application.models.enemy.EnemySkills;
import application.models.enemy.EnemySkills.SkillType;

public class EnemySkillsView {
    private Map<SkillType, Image> skillImages;
    private Image eggSheet;
    private BufferedImage fireballSheet;
    private List<int[]> EggSprites = new ArrayList<>();
    private List<int[]> FireballSprites = new ArrayList<>();
    private SoundController soundController;
    private ImageCache imageCache = ImageCache.getInstance();

    private static final int[][] EggBrokenSprite = {
            {1, 1, 31, 20}, {35, 1, 32, 21}, {69, 1, 34, 22},
            {105, 1, 35, 22}, {143, 1, 37, 23}, {183, 1, 39, 23},
            {225, 1, 40, 24}, {267, 1, 42, 24}, {311, 1, 43, 25},
            {357, 1, 44, 25}, {403, 1, 46, 27}, {451, 1, 47, 27},
            {1, 31, 49, 28}, {53, 31, 49, 28}, {105, 31, 51, 30}, {159, 31, 52, 30},
            {213, 31, 53, 31}, {269, 31, 54, 31}, {325, 31, 56, 33},
            {383, 31, 56, 33}, {441, 31, 58, 33}, {1, 67, 58, 34},
            {61, 67, 59, 34}, {123, 67, 60, 36}, {185, 67, 61, 36},
            {249, 67, 61, 36}, {313, 67, 63, 37}, {379, 67, 63, 37}, {445, 67, 64, 37},
            {1, 107, 65, 37}, {69, 107, 66, 39}, {137, 107, 66, 39},
            {205, 107, 67, 39}, {275, 107, 68, 39}, {345, 107, 68, 40},
            {415, 107, 68, 40}, {1, 149, 69, 40}, {73, 149, 70, 40},
            {145, 149, 70, 40}, {217, 149, 70, 41}, {289, 149, 71, 42},
            {363, 149, 71, 42}, {437, 149, 71, 42}, {1, 193, 71, 42},
            {75, 193, 72, 42}, {149, 193, 72, 42}, {223, 193, 72, 42},
            {297, 193, 72, 42}, {371, 193, 72, 42}
    };

    private static final int[][] BossBulletsSprite = {
            {14, 11, 153, 62}, {194, 11, 159, 62}, {15, 99, 159, 64},
            {193, 99, 149, 64}, {366, 99, 133, 64}, {16, 185, 134, 69},
            {174, 185, 147, 69}, {344, 185, 152, 69}, {14, 266, 164, 74},
            {202, 6, 173, 74}, {19, 357, 161, 70}
    };

    public EnemySkillsView(Map<SkillType, String> skillImagePaths, SoundController soundController) {
    	this.soundController = soundController;
        skillImages = new HashMap<>();
        try {     	
            for (Map.Entry<SkillType, String> entry : skillImagePaths.entrySet()) {
                SkillType skillType = entry.getKey();
                String path = entry.getValue();

                if (skillType == SkillType.EGG) {
                    Image image = imageCache.getResourceImage(path);
                    skillImages.put(skillType, image);
                    eggSheet = imageCache.getResourceImage("/asset/resources/gfx/eggbreak~1.png");
                    
                } else if (skillType == SkillType.FIREBALL) {
                    InputStream inputStream = getClass().getResourceAsStream("/asset/resources/gfx/bullet-bolt1.png");
                    if (inputStream == null) {
                        throw new IOException("Không tìm thấy hình ảnh: /asset/resources/gfx/bullet-bolt1.png");
                    }
                    fireballSheet = ImageIO.read(inputStream);
                    inputStream.close();
                    for (int[] frame : BossBulletsSprite) {
                        FireballSprites.add(frame);
                    }
                } else {
                    InputStream inputStream = getClass().getResourceAsStream(path);
                    if (inputStream == null) {
                        throw new IOException("Không tìm thấy hình ảnh: " + path);
                    }
                    BufferedImage originalImage = ImageIO.read(inputStream);
                    inputStream.close();

                    int newWidth = 500;
                    int newHeight = 500;
                    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = (Graphics2D) resizedImage.getGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                    g2d.dispose();

                    skillImages.put(skillType, resizedImage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            for (SkillType skillType : skillImagePaths.keySet()) {
                if (!skillImages.containsKey(skillType)) {
                    if (skillType == SkillType.EGG) {
                        skillImages.put(skillType, new BufferedImage(40, 80, BufferedImage.TYPE_INT_ARGB));
                    } else {
                        skillImages.put(skillType, new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB));
                    }
                }
            }
        }
    }

    public void draw(Graphics g, EnemySkills skill) {
        if (!skill.isActive()) {
            return;
        }

        if (skill.getSkillType() == SkillType.EGG) {
            if (!skill.isExploding()) {
                Image eggImage = skillImages.get(SkillType.EGG);
                if (eggImage != null) {
                    g.drawImage(eggImage, (int) skill.getPosX(), (int) skill.getPosY(), 40, 80, null);
                }
            } else {
            	 if (skill.getAnimationFrame() == 0) {
            		 soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/eggSplat.wav").getPath());	
                 }
                drawEggBroken(g, skill, skill.getAnimationFrame());
            }
        } else if (skill.getSkillType() == SkillType.FIREBALL) {
            drawFireball(g, skill, skill.getAnimationFrame());
        } else {
            Image skillImage = skillImages.get(skill.getSkillType());
            if (skillImage != null) {
                drawSkill(g, skill.getPosX(), skill.getPosY(), skill.getScale(), skill.getAngle(), skillImage);
            }
        }

//        // Vẽ hitbox
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Color.RED);
//        Shape hitbox = skill.getHitbox();
//        g2d.draw(hitbox);
    }

    private void drawSkill(Graphics g, double posX, double posY, double scale, double angle, Image skillImage) {
        Graphics2D g2d = (Graphics2D) g.create();

        int newWidth = (int) (skillImage.getWidth(null) * scale);
        int newHeight = (int) (skillImage.getHeight(null) * scale);

        int x = (int) (posX - newWidth / 2);
        int y = (int) (posY - newHeight / 2);

        double centerX = x + newWidth / 2.0;
        double centerY = y + newHeight / 2.0;

        java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
        transform.translate(centerX, centerY);
        transform.rotate(angle);
        transform.scale(scale, scale);
        transform.translate(-skillImage.getWidth(null) / 2.0, -skillImage.getHeight(null) / 2.0);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(skillImage, transform, null);

        g2d.dispose();
    }

    public void drawEggBroken(Graphics g, EnemySkills skill, int eFrame) {
        for (int[] frame : EggBrokenSprite) {
            EggSprites.add(frame);
        }
        
           
        
        if (eggSheet != null) {
      
            int[] eggFrame = EggSprites.get(eFrame);
            int ex = eggFrame[0], ey = eggFrame[1], ew = eggFrame[2], eh = eggFrame[3];

            int[][] offsets = {
                    {-15, -3}, {-16, -3}, {-17, -4}, {-18, -4}, {-19, -5}, {-21, -5}, {-22, -6}, {-23, -6},
                    {-24, -6}, {-25, -6}, {-26, -8}, {-27, -8}, {-28, -9}, {-28, -9}, {-30, -10}, {-31, -10},
                    {-31, -11}, {-32, -11}, {-34, -12}, {-34, -12}, {-35, -12}, {-35, -13}, {-36, -13}, {-37, -15},
                    {-37, -15}, {-37, -15}, {-39, -15}, {-39, -15}, {-40, -15}, {-40, -15}, {-41, -17}, {-41, -17}, {-42, -17}
            };

            int offsetX = offsets[eFrame][0];
            int offsetY = offsets[eFrame][1];

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            double scale = 2.0;

            g2d.drawImage(eggSheet,
                    (int) (skill.getPosX() + offsetX + 15), (int) (skill.getPosY() + offsetY),
                    (int) (skill.getPosX() + offsetX + 15 + (ew * scale) / 2), (int) (skill.getPosY() + offsetY + (eh * scale) / 2),
                    ex, ey, ex + ew, ey + eh,
                    null);
        }
    }

    public void drawFireball(Graphics g, EnemySkills skill, int fFrame) {
        int frameIndex = fFrame % FireballSprites.size();
        int[] fireballFrame = FireballSprites.get(frameIndex);
        int fx = fireballFrame[0], fy = fireballFrame[1], fw = fireballFrame[2], fh = fireballFrame[3];

        if (fw <= 0 || fh <= 0) {
            return;
        }

        double posX = skill.getPosX();
        double posY = skill.getPosY();
        int x = (int) (posX - fw / 2);
        int y = (int) (posY - fh / 2);
        int centerX = x + fw / 2;
        int centerY = y + fh / 2;

        double speedX = skill.getSpeedX();
        double speedY = skill.getSpeedY();
        double angle = Math.atan2(speedY, speedX);

        BufferedImage fireballFrameImage;
        fireballFrameImage = fireballSheet.getSubimage(fx, fy, fw, fh);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
        transform.translate(centerX, centerY);
        transform.rotate(angle);
        transform.translate(-fw / 2.0, -fh / 2.0);

        g2d.drawImage(fireballFrameImage, transform, null);

        g2d.dispose();
    }

//    private int frameIndex = 0;
//
//    private void updateFrameIndex() {
//        frameIndex = (frameIndex + 1) % FireballSprites.size();
//    }
}