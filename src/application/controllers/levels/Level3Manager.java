package application.controllers.levels;

import application.controllers.LevelManager;
import application.models.types.ChickEnemy;
import application.models.Enemy;
import application.models.types.ChickenEnemy;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Level3Manager extends LevelManager{
    public Level3Manager(){
        super();
    }

    @Override
    protected void initEnemies(){
        int nums = 5;
        int spacing = 400;
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
        private ArrayList<ChickEnemy> childChicken; // Danh sách gà con
        private double angel = 0; //Góc quay ban đầu của gà
        public ChickenEnemyLvl3(int PosX, int PosY, int direction){
            super(PosX,PosY);
            this.direction = direction;
            this.childChicken = new ArrayList<>();

            for(int i = 0; i < 4; i++){
                double angleOffSet = Math.toRadians(i*90); //Góc lệch
                childChicken.add(new chickEnemiesLV3(PosX, PosY, 60, angleOffSet)); //60-ban kinh
            }

        }

        @Override
        public void update(){
            PosX += speed * direction; // Di chuyển ngang theo hướng
            if (PosX < 0 || PosX > 1900) { // Nếu ra ngoài màn hình, đổi hướng
                direction *= -1;
            }

            //Cap nhat vi tri ga con xoay quanh ga lon
            angel += 0.05; //Toc do quay
            for(ChickEnemy chick : childChicken){
                chick.update();
            }

        }

        @Override
        public void render(Graphics g){
            super.render(g);
            for(ChickEnemy chick : childChicken){
                chick.render(g);
            }
        }
    }

    private class chickEnemiesLV3 extends ChickEnemy{
        private double radius; // Bán kính quay
        private double angleOffset; // Góc ban đầu

        public chickEnemiesLV3(int centerX, int centerY, double radius, double angleOffset) {
            super(centerX, centerY); // Gà con có tốc độ 1.5
            this.radius = radius;
            this.angleOffset = angleOffset;
        }


        public void update(int centerX, int centerY, double angle) {
            // Cập nhật vị trí dựa trên gà lớn
            PosX = centerX + (int) (radius * Math.cos(angle + angleOffset));
            PosY = centerY + (int) (radius * Math.sin(angle + angleOffset));
        }

    }
}
