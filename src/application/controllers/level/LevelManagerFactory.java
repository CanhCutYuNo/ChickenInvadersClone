package application.controllers.level;

import application.controllers.enemy.EnemyController;
import application.controllers.level.levels.Level1Manager;
import application.controllers.level.levels.Level2Manager;
import application.controllers.level.levels.Level3Manager;
import application.controllers.level.levels.Level4Manager;
import application.controllers.level.levels.Level5Manager;
import application.controllers.util.SoundController;

public class LevelManagerFactory {
    public static ILevelManager createLevelManager(int level, SoundController soundController, EnemyController enemyController) {
        switch (level) {
            case 1:
                return new Level1Manager(soundController, enemyController);
            case 2:
                return new Level2Manager(soundController, enemyController);
            case 3:
                return new Level3Manager(soundController, enemyController);
            case 4:
                return new Level4Manager(soundController, enemyController);
            case 5:
                return new Level5Manager(soundController, enemyController);
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }
    }
}