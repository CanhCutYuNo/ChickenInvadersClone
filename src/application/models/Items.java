package application.models;

public class Items extends EnemyProjectiles {

    public Items(double x, double y, int damage){
        super(x,y,damage);
    }
    @Override
    public void update(){
        posY += 5;
    }

    @Override
    public boolean isOffScreen() {
        return super.isOffScreen();
    }
}
