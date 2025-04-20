package application.views.enemy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class DeathEffectView {
    private BufferedImage spriteSheet;
    private int[][] frames;
    private int frameIndex;
    private long frameDelay;
    private long lastFrameTime;
    private int x, y;
    private boolean isFinished;

    public DeathEffectView(BufferedImage spriteSheet, int x, int y) {
        this.spriteSheet = spriteSheet;
        this.x = x;
        this.y = y;
        this.frames = generateFrames();
        this.frameIndex = 0;
        this.frameDelay = 30; // milliseconds per frame
        this.lastFrameTime = System.currentTimeMillis();
        this.isFinished = false;
    }

    private int[][] generateFrames() {
        int[][] f = new int[81][4]; // 9 x 9 grid
        int frameWidth = 64;
        int frameHeight = 64;
        for (int i = 0; i < 81; i++) {
            int fx = (i % 9) * frameWidth;
            int fy = (i / 9) * frameHeight;
            f[i][0] = fx;
            f[i][1] = fy;
            f[i][2] = frameWidth;
            f[i][3] = frameHeight;
        }
        return f;
    }

    // Định nghĩa phương thức render(Graphics)
    public void render(Graphics g) {
        if (!isFinished && frameIndex < frames.length) {
            int[] f = frames[frameIndex];
            g.drawImage(spriteSheet,
                    x, y, x + f[2], y + f[3],  // destination rectangle
                    f[0], f[1], f[0] + f[2], f[1] + f[3], // source rectangle
                    null);
        }
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (!isFinished && now - lastFrameTime >= frameDelay) {
            frameIndex++;
            lastFrameTime = now;
            if (frameIndex >= frames.length) {
                isFinished = true;
            }
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}
