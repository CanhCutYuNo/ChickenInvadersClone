package application.controllers.util;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundController implements GameSettings.MuteAudioListener, GameSettings.VolumeChangeListener {
    private final ExecutorService ex = Executors.newCachedThreadPool();
    private final List<Clip> clips = new ArrayList<>();
    private Clip backgroundClip;

    public SoundController() {
        GameSettings.getInstance().addMuteAudioListener(this);
        GameSettings.getInstance().addVolumeChangeListener(this);
    }

    public void playBackgroundMusic(String path) {
        ex.submit(() -> {
            try {
                stopBackgroundMusic();

                File file = new File(path.replaceAll("%20", " "));
                if(!file.exists()) {
                    System.err.println("Không tìm thấy file âm thanh: " + path);
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioStream);

                applyVolumeToBackgroundClip();

                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundClip.start();

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Lỗi khi phát nhạc nền từ file " + path + ": " + e.getMessage());
            }
        });
    }

    public void stopBackgroundMusic() {
        if(backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }
    }

    public void playSoundEffect(String path) {
        ex.submit(() -> {
            try {
                File file = new File(path.replaceAll("%20", " "));
                if(!file.exists()) {
                    System.err.println("Không tìm thấy file âm thanh: " + path);
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip effectClip = AudioSystem.getClip();
                effectClip.open(audioStream);

                applyVolumeToEffectClip(effectClip);

                synchronized (clips) {
                    clips.add(effectClip);
                }
                effectClip.start();

                effectClip.addLineListener(event -> {
                    if(event.getType() == LineEvent.Type.STOP) {
                        effectClip.close();
                        synchronized (clips) {
                            clips.remove(effectClip);
                        }
                    }
                });

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Lỗi khi phát âm thanh từ file " + path + ": " + e.getMessage());
            }
        });
    }

    public void stopAll() {
        stopBackgroundMusic();
        synchronized (clips) {
            for(Clip clip : clips) {
                clip.stop();
                clip.close();
            }
            clips.clear();
        }
    }

    public void shutdown() {
        stopAll();
        ex.shutdown();
    }

    private void applyVolumeToBackgroundClip() {
        if(backgroundClip != null && backgroundClip.isOpen()) {
                FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                float volume = GameSettings.getInstance().isMuteAudio() ? 0.0f : GameSettings.getInstance().getBackgroundMusicVolume();
                float dB = (float) (Math.log(volume == 0.0 ? 0.0001 : volume) / Math.log(10.0) * 20.0);
                volumeControl.setValue(dB);
        }
    }

    private void applyVolumeToEffectClip(Clip clip) {
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float volume = GameSettings.getInstance().isMuteAudio() ? 0.0f : GameSettings.getInstance().getSoundEffectVolume();
        float dB = (float) (Math.log(volume == 0.0 ? 0.0001 : volume) / Math.log(10.0) * 20.0);
        volumeControl.setValue(dB);
    }

    private void applyVolumeToClip(Clip clip, float volume) {
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(volume == 0.0 ? 0.0001 : volume) / Math.log(10.0) * 20.0);
        volumeControl.setValue(dB);
    }

    @Override
    public void onMuteAudioChanged(boolean isMuted) {
        float backgroundVolume = isMuted ? 0.0f : GameSettings.getInstance().getBackgroundMusicVolume();
        float effectVolume = isMuted ? 0.0f : GameSettings.getInstance().getSoundEffectVolume();

        ex.submit(() -> {
            if(backgroundClip != null && backgroundClip.isOpen()) {
                applyVolumeToClip(backgroundClip, backgroundVolume);
            }

            synchronized (clips) {
                for(Clip clip : clips) {
                    if(clip.isOpen()) {
                        applyVolumeToClip(clip, effectVolume);
                    }
                }
            }
        });
    }

    @Override
    public void onBackgroundMusicVolumeChanged(float volume) {
        if(!GameSettings.getInstance().isMuteAudio()) {
            ex.submit(() -> {
                applyVolumeToClip(backgroundClip, volume);
            });
        }
    }

    @Override
    public void onSoundEffectVolumeChanged(float volume) {
        if(!GameSettings.getInstance().isMuteAudio()) {
            ex.submit(() -> {
                synchronized (clips) {
                    for(Clip clip : clips) {
                        if(clip.isOpen()) {
                            applyVolumeToClip(clip, volume);
                        }
                    }
                }
            });
        }
    }
}