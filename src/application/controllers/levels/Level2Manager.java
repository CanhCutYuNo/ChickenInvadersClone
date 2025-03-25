package application.controllers.levels;

import java.util.List;

import application.controllers.EnemyController;
import application.controllers.SoundController;
import application.models.Enemy;


public class Level2Manager {

	SoundController sound;
	List<EnemyController> enemyControllers;
	List<Enemy> enemies;
	
    public Level2Manager(SoundController sound) {
        this.sound = sound;
        
//        enemyControllers.add(new EnemyController(5, -100, 0.0f, sound));
//        enemyControllers.add(new EnemyController(5, -100, 1.5f, sound));
//        enemyControllers.add(new EnemyController(5, -100, 0.0f, sound));
    }

   

}
