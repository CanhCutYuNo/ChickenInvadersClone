package application.controllers;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundController {
    private Clip clip;
    private String curTrack;
    private final ExecutorService ex = Executors.newCachedThreadPool(); // Chỉ chạy 1 track 1 lúc

    public SoundController(String initialPath) {
        play(initialPath);
    }

    public void play(String path) {
//        path = path.replace("%20", " ");
        ex.submit(() -> {
            try {
                if(clip != null && clip.isRunning()) {
                    clip.stop();
                }

                if(!path.equals(curTrack)) {
                    preloadTrack(path);
                }

                if(clip != null) {
                    clip.setFramePosition(0);
                    //clip.loop(Clip.LOOP_CONTINUOUSLY);
                    clip.start();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void preloadTrack(String path) {
        path = path.replace("%20", " ");
        try {
            File file = new File(path);
            if(file.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                curTrack = path;
            } else {
                System.out.println("Không tìm thấy file: " + path);
            }
        } catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Dừng nhạc
    public void stop() {
        ex.submit(() -> {
            if(clip != null) {
                clip.stop();
            }
        });
    }

    // Đổi bài hát(dùng thread)
    public void switchTrack(String newPath) {
        newPath = newPath.replace("%20", " ");
        if(newPath.equals(curTrack)) {
            return; // Nếu bài nhạc đang phát đúng rồi thì không cần đổi
        }
        stop();
        play(newPath);
    }

    // Dọn dẹp tài nguyên khi đóng ứng dụng
    public void shutdown() {
        ex.shutdown();
    }
}
