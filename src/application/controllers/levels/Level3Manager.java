package application.controllers.levels;

import application.controllers.EnemyController;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.types.ChickenEnemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level3Manager {
    SoundController sound;
    List<EnemyController> enemyControllers;
    List<Enemy> enemies;
    Random random;

    public Level3Manager(SoundController sound) {
        this.sound = sound;
        this.enemyControllers = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.random = new Random();

        for(int i = 0; i < 4; i++){
            EnemyController controller1 = new EnemyController(5,"Chicken", 100 * (i) + 100, 0.0f + i*20.f,sound,3);

            enemyControllers.add(controller1);
            enemies.addAll(controller1.getEnemies());
        }


        for(int i = 0; i < 10; i++){
            int posY = random.nextInt(100);
            EnemyController controller = new EnemyController(1,"Chick", posY - 50, 0.0f + i * 2.0f,sound,3);
            enemyControllers.add(controller);
            enemies.addAll(controller.getEnemies());
        }

    }

    public void update(float deltaTime){

        for(EnemyController controller: enemyControllers){

            controller.update2(deltaTime);
        }
    }

    public List<Enemy> getEnemies(){return enemies;}
//    public List<Enemy> getEnemies() {
//        List<Enemy> allEnemies = new ArrayList<>();
//        for (EnemyController controller : enemyControllers) {
//            allEnemies.addAll(controller.getEnemies()); // ✅ Luôn cập nhật danh sách động
//        }
//        System.out.println("📋 Tổng số kẻ địch: " + allEnemies.size());
//        return allEnemies;
//    }
    public void render(Graphics g){
        for(EnemyController controller:enemyControllers){
            controller.render(g);
        }
    }

    public void removeEnemy(Enemy enemy){
        for(EnemyController controller : enemyControllers){
            if(controller.getEnemies().contains(enemy)){
                controller.removeEnemy(enemy);
                break;
            }
        }
        enemies.remove(enemy);
    }
}
