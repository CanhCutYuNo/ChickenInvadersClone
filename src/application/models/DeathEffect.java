package application.models;

import application.views.DeathEffectView;
import java.awt.Graphics;

public class DeathEffect {

    private final DeathEffectView deathEffectView;
    protected int PosX;
    protected int PosY;
    protected boolean end;
    protected final int MAP_WIDTH = 1900;
//    protected final int MAP_HEIGHT = 1080; 

    public DeathEffect(int PosX, int PosY, DeathEffectView deathEffectView) {
        this.PosX = PosX;
        this.PosY = PosY;
        end = false;

        this.deathEffectView = deathEffectView;

    }

    public DeathEffect(int PosX, int PosY) {
        this.PosX = PosX;
        this.PosY = PosY;
        end = false;

        this.deathEffectView = new DeathEffectView(this);
    }

    public boolean isEnd() {
        return end;
    }

    public void setEndToTrue() {
        end = true;
    }

    public int getPosX() {
        return PosX;
    }

    public int getPosY() {
        return PosY;
    }

    public void render(Graphics g) {
        
        deathEffectView.render(g);
    }

    public void update() {
        deathEffectView.update();
    }
}
