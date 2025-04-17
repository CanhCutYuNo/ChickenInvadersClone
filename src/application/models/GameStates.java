package application.models;

import java.util.ArrayList;
import java.util.List;

public class GameStates {
    private int level;
    private int foodCounts;
    private List<FloatingText> floatingTexts;
    private boolean levelTransitionTriggered;
    private boolean isDelaying;
    private long delayStartTime;
    private int frameDelay;
    private boolean playerExploded;

    // Constructor
    public GameStates() {
        this.level = 1;
        this.foodCounts = 0;
        this.floatingTexts = new ArrayList<>();
        this.levelTransitionTriggered = false;
        this.isDelaying = false;
        this.delayStartTime = 0;
        this.frameDelay = 0;
        this.playerExploded = false;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFoodCounts() {
        return foodCounts;
    }

    public void setFoodCounts(int foodCounts) {
        this.foodCounts = foodCounts;
    }

    public List<FloatingText> getFloatingTexts() {
        return new ArrayList<>(floatingTexts);
    }

    public void addFloatingText(FloatingText text) {
        this.floatingTexts.add(text);
    }
    
    public void removeExpiredFloatingTexts() {
        floatingTexts.removeIf(FloatingText::isExpired);
    }

    public boolean isLevelTransitionTriggered() {
        return levelTransitionTriggered;
    }

    public void setLevelTransitionTriggered(boolean levelTransitionTriggered) {
        this.levelTransitionTriggered = levelTransitionTriggered;
    }

    public boolean isDelaying() {
        return isDelaying;
    }

    public void setDelaying(boolean isDelaying) {
        this.isDelaying = isDelaying;
    }

    public long getDelayStartTime() {
        return delayStartTime;
    }

    public void setDelayStartTime(long delayStartTime) {
        this.delayStartTime = delayStartTime;
    }

    public int getFrameDelay() {
        return frameDelay;
        
    }

    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }

    public boolean isPlayerExploded() {
        return playerExploded;
    }

    public void setPlayerExploded(boolean playerExploded) {
        this.playerExploded = playerExploded;
    }

    public void reset() {
        this.level = 1;
        this.foodCounts = 0;
        this.floatingTexts.clear();
        this.levelTransitionTriggered = false;
        this.isDelaying = false;
        this.delayStartTime = 0;
        this.frameDelay = 0;
        this.playerExploded = false;
    }
}