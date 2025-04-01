package application.controllers.levels;

import application.controllers.EnemyController;
import application.controllers.SoundController;
import application.models.Enemy;
import application.models.types.EggShellEnemy;

import java.util.List;

public class Level4Manager {
    List<EnemyController> enemyControllers;
    List<Enemy> enemies;
    SoundController sound;
    List<EggShellEnemy> eggShellEnemies;

}
