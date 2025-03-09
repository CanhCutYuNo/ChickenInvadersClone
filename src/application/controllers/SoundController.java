package application.controllers;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundController {
    private Clip backgroundClip;
    private String currentTrack;
    private final Map<String, Clip> soundCache = new HashMap<>(); // Cache âm thanh

    public SoundController(String initialTrackPath) {
        playBackground(initialTrackPath);
    }
    public SoundController() {
    }

    // Phát nhạc nền (lặp lại)
    public void playBackground(String trackPath) {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }

        backgroundClip = loadClip(trackPath);
        if (backgroundClip != null) {
            backgroundClip.setFramePosition(0);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();
            currentTrack = trackPath;
        }
    }

    public void stopBackground() {
        if (backgroundClip != null) {
            backgroundClip.stop();
        }
    }

    public void switchBackground(String newTrackPath) {
        if (newTrackPath.equals(currentTrack)) return; // Nếu trùng bài thì không đổi

        stopBackground();
        playBackground(newTrackPath);
    }

   
 // Phát hiệu ứng âm thanh (không lặp lại)
    public void playSound(String soundPath) {
        new Thread(() -> {
            Clip effectClip = loadClip(soundPath);
            if (effectClip != null) {
                effectClip.setFramePosition(0);
                effectClip.start();
                effectClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        effectClip.close(); // Đóng clip để giải phóng tài nguyên
                    }
                });
            }
        }).start();
    }


    // Hàm tải clip
    private Clip loadClip(String path) {
        if (soundCache.containsKey(path)) {
            return soundCache.get(path);
        }

        try {
            File file = new File(path);
            if (file.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                soundCache.put(path, clip);
                return clip;
            } else {
                System.out.println("Không tìm thấy file: " + path);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }
}
