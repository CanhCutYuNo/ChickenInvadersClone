package application.views.player;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Rectangle;


import javax.swing.ImageIcon;

import application.controllers.player.PlayerController;
import application.controllers.util.ImageCache;

public class PlayerView {

    private Image spriteSheet;
    private Image exhaustImage;
    private Image explosionSheet;
    private PlayerController playerController;
    private ImageCache imageCache = ImageCache.getInstance();

    private int[][] spriteData = {
        {1, 1135, 104, 114}, {1, 1019, 104, 114}, {1, 903, 104, 114},
        {1, 787, 105, 114}, {1, 672, 106, 113}, {1, 556, 107, 113},
        {1, 440, 109, 113}, {1, 325, 110, 112}, {1, 212, 111, 111},
        {1, 98, 112, 111}, {115, 1139, 114, 110}, {115, 1028, 115, 109},
        {115, 917, 116, 108}, {115, 808, 117, 107}, {115, 699, 117, 106},
        {115, 591, 117, 106}, {115, 375, 117, 106}, {115, 267, 117, 106},
        {115, 158, 117, 107}, {115, 47, 116, 108}, {235, 1140, 114, 109},
        {235, 1027, 114, 110}, {235, 915, 112, 110}, {235, 802, 111, 111},
        {235, 687, 110, 112}, {235, 573, 109, 112}, {235, 458, 107, 113},
        {235, 342, 106, 113}, {235, 225, 105, 114}, {235, 109, 104, 114},
        {351, 1135, 104, 114}, {351, 1019, 104, 114}
    };
    

    private int[][] explosionData = {
        {1, 1, 21, 21}, {25, 1, 29, 32}, {57, 1, 38, 38}, {97, 1, 45, 42}, {145, 1, 52, 49}, {199, 1, 55, 54},
        {260, 1, 57, 60}, {319, 1, 65, 64}, {387, 1, 69, 67}, {459, 1, 74, 72}, {535, 1, 76, 77}, {613, 1, 79, 80},
        {695, 1, 81, 82}, {779, 1, 84, 86}, {865, 1, 90, 88}, {1, 91, 95, 90}, {99, 91, 104, 93}, {205, 91, 106, 95},
        {313, 91, 109, 99}, {425, 91, 110, 102}, {537, 100, 112, 104}, {651, 98, 113, 106}, {767, 91, 115, 115}, {885, 91, 116, 117},
        {1, 211, 117, 119}, {121, 211, 118, 120}, {241, 211, 118, 120}, {361, 211, 119, 121}, {483, 211, 119, 123}, {605, 211, 120, 123},
        {727, 211, 122, 125}, {851, 211, 126, 124}, {1, 339, 127, 125}, {131, 339, 127, 124}, {261, 339, 128, 122}, {391, 339, 128, 124},
        {521, 339, 128, 124}, {651, 339, 128, 127}, {781, 339, 128, 128}, {1, 469, 128, 128}, {131, 469, 128, 128}, {261, 469, 128, 128},
        {391, 469, 128, 128}, {521, 469, 128, 127}, {651, 469, 128, 126}, {781, 469, 128, 127}, {1, 599, 128, 126}, {131, 599, 128, 125},
        {261, 599, 128, 126}, {391, 599, 128, 124}, {521, 599, 128, 123}, {651, 599, 128, 122}, {781, 599, 128, 119}
    };

    private int curFrame = 16;
    private int exFrame = 0;
    private boolean exploding = false;

    public PlayerView(PlayerController _playerController) {
        this.playerController = _playerController;

        try {
            spriteSheet = imageCache.getResourceImage("/asset/resources/gfx/spaceship.png");
            exhaustImage = imageCache.getResourceImage("/asset/resources/gfx/exhaust4.png");
            explosionSheet = imageCache.getResourceImage("/asset/resources/gfx/explosion4.png");
        } catch (Exception e) {
            System.out.println("Error: Could not load player sprite sheet or exhaust image.");
            e.printStackTrace();
        }
    }

    public int getCurFrame() {
        return curFrame;
    }

    public int getExFrame() {
        return exFrame;
    }

    public int setCurFrame(int _curFrame) {
        return curFrame = _curFrame;
    }

    public int setExFrame(int _exFrame) {
        return exFrame = _exFrame;
    }

    public void render(Graphics g) {
        if (spriteSheet != null) {
            int[] data = spriteData[curFrame];
            int sx = data[0], sy = data[1], sw = data[2], sh = data[3];

            int[][] Offsets = {
                {-15, -3}, {-15, -3}, {-15, -3}, {-15, -3}, {-15, -4},
                {-15, -4}, {-13, -4}, {-13, -4}, {-13, -4}, {-12, -4},
                {-11, -5}, {-11, -5}, {-10, -6}, {-9, -7}, {-9, -7},
                {-8, -7}, {-6, -7}, {-5, -7}, {-5, -7}, {-5, -6},
                {-5, -5}, {-3, -4}, {-3, -4}, {-3, -4}, {-3, -4},
                {-2, -4}, {0, -4}, {0, -4}, {0, -4}, {-0, -4},
                {-0, -4}, {0, -4}, {-5, 3}
            };

            int offsetX = Offsets[curFrame][0];
            int offsetY = Offsets[curFrame][1];

            g.drawImage(spriteSheet,
                    playerController.getPosX() + offsetX, playerController.getPosY() + offsetY,
                    playerController.getPosX() + offsetX + 100, playerController.getPosY() + offsetY + 100,
                    sx, sy, sx + sw, sy + sh, null);

            if (exhaustImage != null) {
                int[] exhaustData = {1, 271, 80, 240};
                int ex_sx = exhaustData[0], ex_sy = exhaustData[1];
                int ex_sw = exhaustData[2], ex_sh = exhaustData[3];

                int jitterX = (int) (Math.random() * 6) - 3;
                int jitterY = (int) (Math.random() * 6) - 3;

                float alpha = (float) (Math.random() * 0.5 + 0.5);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

                double scale = 1 + (Math.random() * 0.2);
                int scaledWidth = (int) (ex_sw * scale);
                int scaledHeight = (int) (ex_sh * scale);

                g2d.drawImage(exhaustImage,
                        playerController.getPosX() + 22 + jitterX, playerController.getPosY() + 70 + jitterY,
                        playerController.getPosX() + 22 + scaledWidth / 2 + jitterX, playerController.getPosY() + 70 + scaledHeight / 2 + jitterY,
                        ex_sx, ex_sy, ex_sx + ex_sw, ex_sy + ex_sh, null);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

        } else {
            g.setColor(Color.RED);
            g.fillRect(playerController.getPosX(), playerController.getPosY(), 64, 64);
        }
        // Vẽ hitbox
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Color.RED);
//        Rectangle hitbox = playerController.getHitbox();
//        g2d.draw(hitbox);



    }

    public boolean isExploding() {
        return exploding;
    }

    public void startExplosion() {
        exploding = true;
        exFrame = 0;
    }

    public void updateExplosion() {
        if (exploding) {
            exFrame++;
            if (exFrame >= 53) {
                exploding = false;
            }
        }
    }

    public void explosionRender(Graphics g) {
        if (explosionSheet != null) {
            int[] eData = explosionData[exFrame];
            int ex = eData[0], ey = eData[1], ew = eData[2], eh = eData[3];

            int[][] eOffsets = {
                {0, -3}, {-16, -25}, {-34, -37}, {-48, -45}, {-62, -59}, {-68, -69}, {-72, -81},
                {-88, -89}, {-96, -95}, {-106, -105}, {-110, -115}, {-116, -121}, {-120, -125},
                {-126, -133}, {-138, -137}, {-148, -141}, {-166, -147}, {-170, -151}, {-176, -159},
                {-178, -165}, {-182, -169}, {-184, -173}, {-188, -191}, {-190, -195}, {-192, -199},
                {-194, -201}, {-194, -201}, {-196, -203}, {-196, -207}, {-198, -207}, {-202, -211},
                {-210, -209}, {-212, -211}, {-212, -209}, {-214, -205}, {-214, -209}, {-214, -209},
                {-214, -215}, {-214, -217}, {-214, -217}, {-214, -217}, {-214, -217}, {-214, -217},
                {-214, -215}, {-214, -213}, {-214, -215}, {-214, -213}, {-214, -211}, {-214, -213},
                {-214, -209}, {-214, -207}, {-214, -205}, {-214, -199}
            };

            int eOffsetX = eOffsets[exFrame][0];
            int eOffsetY = eOffsets[exFrame][1];

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            double scale = 8.0;

            g2d.drawImage(explosionSheet,
                    (int) (playerController.getPosX() + eOffsetX), (int) (playerController.getPosY() + eOffsetY),
                    (int) (playerController.getPosX() + eOffsetX + (ew * scale) / 2), (int) (playerController.getPosY() + eOffsetY + (eh * scale) / 2),
                    ex, ey, ex + ew, ey + eh,
                    null);
        }
    }
    
    public PlayerController getPlayer() {
    	return playerController;
    }
}
