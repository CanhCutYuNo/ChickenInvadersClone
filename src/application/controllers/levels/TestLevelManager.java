package application.controllers.levels;

import application.controllers.LevelManager;
import application.controllers.SoundController;
import application.models.types.EggShellEnemy;
import java.util.ArrayList;

public class TestLevelManager extends LevelManager{
   
	SoundController sound;
	
    public TestLevelManager(SoundController sound) {
        super(sound);
    }


}
