package application.controllers;
	
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundController {
    private final ExecutorService ex = Executors.newCachedThreadPool();
    private final List<Clip> clips = new ArrayList<>(); // Lưu danh sách Clip cho hiệu ứng âm thanh
    private Clip backgroundClip; // Clip riêng cho nhạc nền

    public void playBackgroundMusic(String path) {

        ex.submit(() -> {
            try {
                stopBackgroundMusic(); // Dừng nhạc nền cũ trước khi phát bài mới

                File file = new File(path);
                if (!file.exists()) {
                  //  System.out.println("Không tìm thấy file: " + path);
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioStream);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Lặp vô hạn
                backgroundClip.start();
                
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }

    // 🛑 Dừng nhạc nền
    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }
    }

    // 🔊 Phát hiệu ứng âm thanh (không ảnh hưởng đến nhạc nền)
    public void playSoundEffect(String path) {
        ex.submit(() -> {
            try {
                File file = new File(path);
                if (!file.exists()) {
                    System.out.println("Không tìm thấy file: " + path);
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip effectClip = AudioSystem.getClip();
                effectClip.open(audioStream);
                
                // Thêm Clip vào danh sách để quản lý
                synchronized (clips) {
                    clips.add(effectClip);
                }

                effectClip.start();

                // Khi phát xong, tự động xóa khỏi danh sách
                effectClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        effectClip.close();
                        synchronized (clips) {
                            clips.remove(effectClip);
                        }
                    }
                });

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }

    // 🛑 Dừng tất cả âm thanh (hiệu ứng + nhạc nền)
    public void stopAll() {
        stopBackgroundMusic();
        synchronized (clips) {
            for (Clip clip : clips) {
                clip.stop();
                clip.close();
            }
            clips.clear();
        }
    }

    // 🧹 Dọn dẹp tài nguyên khi đóng ứng dụng
    public void shutdown() {
        stopAll();
        ex.shutdown();
    }
}
