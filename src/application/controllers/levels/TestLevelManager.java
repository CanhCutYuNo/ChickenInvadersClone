package application.controllers.levels;

import application.controllers.LevelManager;
import application.controllers.SoundController;
import application.models.types.EggShellEnemy;
import java.util.ArrayList;

public class TestLevelManager extends LevelManager{
   
	SoundController sound;
	
    public TestLevelManager(SoundController sound) {
        super();
        this.sound = sound;
        initEnemies();
    }

    @Override
    protected void initEnemies() {
        int nums = 8;
        int spacing = 200;
        int startX = 100;
        int posY = 100;
        enemies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < nums; j++) {
                int posX = startX + j * spacing;
                enemies.add(new EggShellEnemy(posX, posY, sound));
            }
            posY += 200;
        }
    }

}
