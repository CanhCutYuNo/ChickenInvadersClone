	package application.controllers;
	
	import javax.sound.sampled.*;

import java.io.BufferedInputStream;
import java.io.File;
	import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
	import java.util.concurrent.Executors;
	
	public class SoundController {
	    private Clip clip;
	    private String curTrack;
	    private final ExecutorService ex = Executors.newCachedThreadPool(); // Nhạc nền
	    private final ExecutorService effectEx = Executors.newCachedThreadPool(); // Hiệu ứng âm thanh
	
	    public SoundController(String initialPath) {
	        play(initialPath);
	    }
	
	    public SoundController() {
	    }
	
	    public void play(String path) {
	        ex.submit(() -> {
	            try {
	                if (clip != null && clip.isRunning()) {
	                    clip.stop();
	                }
	
	                if (!path.equals(curTrack)) {
	                    preloadTrack(path);
	                }
	
	                if (clip != null) {
	                    clip.setFramePosition(0);
	                    clip.start();
	                }
	            } catch (Exception e) {
	                System.err.println("Lỗi khi phát nhạc nền: " + e.getMessage());
	            }
	        });
	    }
	
	    private void preloadTrack(String path) {
	        try {
	            File file = new File(path);
	            if (!file.exists()) {
	                System.err.println("Không tìm thấy file nhạc nền: " + path);
	                return;
	            }
	
	            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(file)) {
	                clip = AudioSystem.getClip();
	                clip.open(audioStream);
	                curTrack = path;
	            }
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
	            System.err.println("Lỗi khi tải nhạc nền: " + e.getMessage());
	        }
	    }
	
	    public void stop() {
	        ex.submit(() -> {
	            if (clip != null) {
	                clip.stop();
	            }
	        });
	    }
	
	    public void switchTrack(String newPath) {
	        if (newPath.equals(curTrack)) {
	            return;
	        }
	        stop();
	        play(newPath);
	    }
	
	    public void shutdown() {
	        ex.shutdown();
	        effectEx.shutdown();
	    }
	
	    // 📌 Phát hiệu ứng âm thanh song song (ví dụ: gà bị bắn)
	    public void playEffect(InputStream soundStream) {
	        effectEx.submit(() -> {
	            try {
	                if (soundStream == null) {
	                    System.err.println("❌ Không tìm thấy tệp âm thanh!");
	                    return;
	                }

	                BufferedInputStream bufferedStream = new BufferedInputStream(soundStream);
	                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);
	                Clip effectClip = AudioSystem.getClip();
	                effectClip.open(audioStream);
	                effectClip.start();

	               // System.out.println("🎵 Đang phát âm thanh...");

	                if (effectClip.isRunning()) {
	                    effectClip.stop();
	                    effectClip.setFramePosition(0);
	                }
	                effectClip.start();

	             

	                effectClip.close();
	            } catch (Exception e) {
	                System.err.println("⚠️ Lỗi phát hiệu ứng âm thanh: " + e.getMessage());
	            }
	        });
	    }


	
	    //📌 Điều chỉnh âm lượng (0.0f đến 1.0f)
	    public void setVolume(float volume) {
	        if (clip != null) {
	            try {
	                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	                float min = gainControl.getMinimum(); // Giá trị nhỏ nhất
	                float max = gainControl.getMaximum(); // Giá trị lớn nhất
	                float range = max - min;
	                float gain = min + (range * volume);
	                gainControl.setValue(gain);
	            } catch (IllegalArgumentException e) {
	                System.err.println("🚨 Không thể điều chỉnh âm lượng: " + e.getMessage());
	            }
	        }
	    }
	}
