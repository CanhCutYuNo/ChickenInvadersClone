package application.Controllers;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SoundController {
    private Clip clip;
    private String currentTrack;

    public SoundController(String initialTrackPath) {
        preloadTrack(initialTrackPath);
        play(initialTrackPath);
    }

    // Tải trước track vào bộ nhớ
    private void preloadTrack(String trackPath) {
        try {
            File file = new File(trackPath);
            if (file.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                currentTrack = trackPath;
            } else {
                System.out.println("Không tìm thấy file: " + trackPath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play(String trackPath) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }

        if (!trackPath.equals(currentTrack)) {
            preloadTrack(trackPath);
        }

        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void switchTrack(String newTrackPath) {
        if (newTrackPath.equals(currentTrack)) {
            return; // Nếu bài nhạc đang phát đúng rồi thì không cần đổi
        }

        stop();
        play(newTrackPath);
    }
}
