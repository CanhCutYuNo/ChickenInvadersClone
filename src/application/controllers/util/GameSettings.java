package application.controllers.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameSettings {
    private static GameSettings instance;
    private static final Object lock = new Object(); 

    private Difficulty difficulty;
    private float backgroundMusicVolume;
    private float soundEffectVolume;
    private boolean muteAudio;
    private int continueLevel;
    private boolean complete;

    private final String SETTINGS_FILE = "settings.properties";

    private List<MuteAudioListener> muteAudioListeners = new ArrayList<>();
    private List<VolumeChangeListener> volumeChangeListeners = new ArrayList<>();

    public interface MuteAudioListener {
        void onMuteAudioChanged(boolean isMuted);
    }

    public interface VolumeChangeListener {
        void onBackgroundMusicVolumeChanged(float volume);
        void onSoundEffectVolumeChanged(float volume);
    }
    
    public enum Difficulty { EASY, NORMAL, HARD, EXTREME; }

    public void addMuteAudioListener(MuteAudioListener listener) {
        muteAudioListeners.add(listener);
    }

    public void addVolumeChangeListener(VolumeChangeListener listener) {
        volumeChangeListeners.add(listener);
    }

    private GameSettings() {
        resetToDefault();
    }

    public static GameSettings getInstance() {
        if(instance == null) {
            synchronized (lock) { 
                if(instance == null) {
                    instance = new GameSettings();
                }
            }
        }
        return instance;
    }

    public void resetToDefault() {
        difficulty = Difficulty.EASY;
        backgroundMusicVolume = 1.0f;
        soundEffectVolume = 1.0f;
        muteAudio = false;
        continueLevel = 1;
        complete = false;
    }

    public void loadSettings() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(SETTINGS_FILE)) {
            props.load(fis);
            difficulty = Difficulty.valueOf(props.getProperty("difficulty", "EASY"));
            backgroundMusicVolume = Float.parseFloat(props.getProperty("backgroundMusicVolume", "1.0"));
            soundEffectVolume = Float.parseFloat(props.getProperty("soundEffectVolume", "1.0"));
            muteAudio = Boolean.parseBoolean(props.getProperty("muteAudio", "false"));
            continueLevel = Integer.parseInt(props.getProperty("continueLevel", "1"));
            complete = Boolean.parseBoolean(props.getProperty("complete", "false"));
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            resetToDefault();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid difficulty value in settings file: " + e.getMessage());
            resetToDefault();
        }
    }

    public void saveSettings() {
        Properties props = new Properties();
        props.setProperty("difficulty", difficulty.name());
        props.setProperty("backgroundMusicVolume", String.valueOf(backgroundMusicVolume));
        props.setProperty("soundEffectVolume", String.valueOf(soundEffectVolume));
        props.setProperty("muteAudio", String.valueOf(muteAudio));
        props.setProperty("continueLevel", String.valueOf(continueLevel));
        props.setProperty("complete", String.valueOf(complete));

        try (FileOutputStream fos = new FileOutputStream(SETTINGS_FILE)) {
            props.store(fos, "Game Settings");
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public float getBackgroundMusicVolume() {
        return backgroundMusicVolume;
    }

    public void setBackgroundMusicVolume(float volume) {
        this.backgroundMusicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        for(VolumeChangeListener listener : volumeChangeListeners) {
            listener.onBackgroundMusicVolumeChanged(backgroundMusicVolume);
        }
    }

    public float getSoundEffectVolume() {
        return soundEffectVolume;
    }

    public void setSoundEffectVolume(float volume) {
        this.soundEffectVolume = Math.max(0.0f, Math.min(1.0f, volume));
        for(VolumeChangeListener listener : volumeChangeListeners) {
            listener.onSoundEffectVolumeChanged(soundEffectVolume);
        }
    }

    public boolean isMuteAudio() {
        return muteAudio;
    }

    public void setMuteAudio(boolean muteAudio) {
        this.muteAudio = muteAudio;
        for(MuteAudioListener listener : muteAudioListeners) {
            listener.onMuteAudioChanged(muteAudio);
        }
    }

    public int getContinueLevel(){
        return continueLevel;
    }

    public void setContinueLevel(int continueLevel){
        this.continueLevel = continueLevel;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}