package application.models;

import java.awt.Graphics;

public class DeathEffect {
    protected int PosX;
    protected int PosY;
    protected boolean end;
    protected final int MAP_WIDTH = 1900;
    //protected final int MAP_HEIGHT = 1080; 
    
    public DeathEffect(int PosX, int PosY){
        this.PosX = PosX;
        this.PosY = PosY;
        end = false;
    }
    
    public boolean isEnd() {
        return end;
    }    
    
    public int getPosX() {
        return PosX;
    }

    public int getPosY() {
        return PosY;
    }    
    
    public void render(Graphics g){
        
    }
    
    public void update(){
        
    }
}
