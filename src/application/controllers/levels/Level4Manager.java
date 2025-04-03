package application.controllers.levels;

import application.controllers.EnemyController;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.types.EggShellEnemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level4Manager {
    List<EnemyController> enemyControllers;
    List<Enemy> enemies;
    SoundController sound;
//    List<EggShellEnemy> eggShellEnemies;
    Random random;

    public Level4Manager(SoundController sound){
        this.sound = sound;
//        this.eggShellEnemies = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.random = new Random();
        this.enemyControllers = new ArrayList<>();

        for(int i = 0; i < 2; i++){
            int posY = random.nextInt(100);
            EnemyController controller = new EnemyController(1, EnemyController.EGG_SHELL, posY - 50, 0.0f + i * 2.0f, sound);
            enemyControllers.add(controller);
            enemies.addAll(controller.getEnemies());
        }
    }

    public void update(float deltaTime){
        for(EnemyController controller: enemyControllers){
            controller.update3(deltaTime);
        }
    }

    public List<Enemy> getEnemies(){return enemies;}

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
