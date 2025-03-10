package application.controllers.levels;

import application.controllers.LevelManager;
import application.models.types.ChickEnemy;
import application.models.Enemy;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Level3Manager extends LevelManager{
    public Level3Manager(){
        super();
    }

    @Override
    protected void initEnemies(){
        int nums = 8;
        int spacing = 200;
        int starty = 100;
        for (int i = 0; i < nums; i++) {
            int startX = (i % 2 == 0) ? -50 : 850; // Gà bên trái (-50) hoặc bên phải (850)
            int direction = (i % 2 == 0) ? 1 : -1; // Hướng bay vào trung tâm

            enemies.add(new ChickenEnemyLvl3(100, startX, startY, direction));

            if (i % (nums / 2) == 0) {
                startY += spacing; // Tạo hàng mới
            }
        }
    }

    private class ChickenEnemyLvl3 extends ChickEnemy{
        private int direction;

        public ChickenEnemyLvl3(int PosX, int PosY, int direction){
            super(PosX,PosY);
            this.direction = direction;

        }
    }

    @Override
    public void update(){
        PosX += speed * direction; // Di chuyển ngang theo hướng

        // Khi đến gần trung tâm (hoặc điểm giới hạn), có thể đổi hướng hoặc bắn đạn
        if ((direction == -1 && PosX > 400) || (direction == 1 && PosX < 400)) {
            direction = 0; // Dừng lại hoặc đổi hướng
        }
    }

}
