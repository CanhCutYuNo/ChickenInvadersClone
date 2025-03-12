package application.controllers.levels;

import application.controllers.LevelManager;
import application.models.types.ChickEnemy;
import application.models.Enemy;
import application.models.types.ChickenEnemy;

import java.awt.*;
import java.util.ArrayList;

public class Level3Manager extends LevelManager{
    public Level3Manager(){
        super();
    }

    @Override
    protected void initEnemies(){
        int nums = 5;
        int spacing = 400;
        int startY = 150;
        enemies = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int direction = (i % 2 == 0) ? 1 : -1; // Hướng bay vào trung tâm
            for(int j = 0; j < nums; j++){
                int startX = 100 + j * spacing;
                ChickenEnemyLvl3 enemyLvl3 = new ChickenEnemyLvl3(startX, startY,direction);
                enemies.add(enemyLvl3);
                for(int k = 0; k < 4; k++){
                    double angleOffSet = Math.toRadians(k*90); //Góc lệch
                    enemies.add(new ChickEnemiesLV3(enemyLvl3.getPosX(), enemyLvl3.getPosY(), 150, angleOffSet,enemyLvl3)); //60-ban kinh
                }
            }
            startY+=350;


        }
    }

    private class ChickenEnemyLvl3 extends ChickenEnemy{
        private int direction;
        private int speed = 3;
//        private ArrayList<ChickEnemiesLV3> childChicken; // Danh sách gà con
//        private double angel = 0; //Góc quay ban đầu của gà
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

//            //Cap nhat vi tri ga con xoay quanh ga lon
//            angel += 0.01; //Toc do quay
//            for(ChickEnemiesLV3 chick : childChicken){
//                chick.update(PosX, PosY, angel);
//            }

        }

//        @Override
//        public void render(Graphics g){
//            super.render(g);
//            for(ChickEnemiesLV3 chick : childChicken){
//                chick.render(g);
//            }
//        }
    }

    private class ChickEnemiesLV3 extends ChickEnemy{
        private double radius; // Bán kính quay
        private double angleOffset;// Góc ban đầu
        private double angle = 0;
        private boolean isOrphan = false;//Xac dinh ga con co mat me khong
        private ChickenEnemyLvl3 mother; //Tham chieu ga me
        public ChickEnemiesLV3(int centerX, int centerY, double radius, double angleOffset, ChickenEnemyLvl3 mother) {
            super(centerX, centerY);
            this.radius = radius;
            this.angleOffset = angleOffset;
            this.mother = mother;
        }


        public void update() {
            if(mother != null && enemies.contains(mother)){
                angle += 0.01;
                PosX = mother.getPosX() + (int) (radius * Math.cos(angle + angleOffset));
                PosY = mother.getPosY() + (int) (radius * Math.sin(angle + angleOffset));
            }
            else{
                // Khi gà mẹ chết, gà con sẽ bay tự do theo hướng ngẫu nhiên
                if(!isOrphan){
                    isOrphan = true;
                }
                PosX += Math.random() * 2 - 1; // Tạo chuyển động ngẫu nhiên
                PosY += Math.random() * 2 - 1;
            }
        }

    }
}
