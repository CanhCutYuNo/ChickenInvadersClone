package application.controllers.levels;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.controllers.EnemyController;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.types.ChickenEnemy;

public class Level2Manager {

	SoundController sound;
	List<EnemyController> enemyControllers;
	List<Enemy> enemies;
    Random random;
	
    public Level2Manager(SoundController sound) {
        this.sound = sound;
        this.enemyControllers = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.random = new Random();

        for(int i = 0; i < 20; i++){
            int posY = random.nextInt(100);
            EnemyController controller = new EnemyController(1,"Chicken", posY-100, 0.0f + i * 1.0f,sound,2);

            enemyControllers.add(controller);
            enemies.addAll(controller.getEnemies());
        }

    }

    public void update(float deltaTime){
        for(EnemyController controller: enemyControllers){

            controller.update1(deltaTime);
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
                controller.removeEnemy((ChickenEnemy) enemy);
                break;
            }
        }
        enemies.remove(enemy);
    }

}
