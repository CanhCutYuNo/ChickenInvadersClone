package application.Controllers;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundController {
    private Clip clip;

    public SoundController(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Phát âm thanh
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Reset về đầu
            clip.start();
        }
    }

    // Phát âm thanh lặp lại
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Dừng âm thanh
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
