package application.controllers.level.levels;

public class RowState {
    public float t;
    public int direction;
    public float timeElapsed;
    public boolean isActive;
    public int startY;
    public float timeDelay;

    public RowState(int startY, float timeDelay) {
        this.t = 0;
        this.direction = 1;
        this.timeElapsed = 0;
        this.isActive = false;
        this.startY = startY;
        this.timeDelay = timeDelay;
    }

    public RowState(int startY, float timeDelay, int initialDirection) {
        this.t = 0;
        this.direction = initialDirection;
        this.timeElapsed = 0;
        this.isActive = false;
        this.startY = startY;
        this.timeDelay = timeDelay;
    }
}