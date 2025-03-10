package application.controllers.levels;

import application.controllers.LevelManager;
import application.models.types.ChickEnemy;
import application.models.Enemy;
import application.models.types.ChickenEnemy;

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
        int startY = 100;
        enemies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int direction = (i % 2 == 0) ? 1 : -1; // Hướng bay vào trung tâm
            for(int j = 0; j < nums; j++){
                int startX = 100 + j * spacing;
                enemies.add(new ChickenEnemyLvl3(startX, startY,direction));
            }
            startY+=200;
        }
    }

    private class ChickenEnemyLvl3 extends ChickenEnemy{
        private int direction;

        public ChickenEnemyLvl3(int PosX, int PosY, int direction){
            super(PosX,PosY);
            this.direction = direction;

        }

        @Override
        public void update(){
            PosX += speed * direction; // Di chuyển ngang theo hướng
            if (PosX < 0 || PosX > 1900) { // Nếu ra ngoài màn hình, đổi hướng
                direction *= -1;
            }
        }
    }
}
