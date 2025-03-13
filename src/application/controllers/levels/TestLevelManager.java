package application.controllers.levels;

import application.controllers.LevelManager;
import application.models.Enemy;
import application.models.types.ChickEnemy;
import application.models.types.ChickenEnemy;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class TestLevelManager extends LevelManager {

    public TestLevelManager() {
        super();
    }

    @Override
    protected void initEnemies() {
        int nums = 8;
        int spacing = 200;
        int startX = 100;
        int posY = 100;
        enemies = new ArrayList<>();
        enemies.add(new ChickEnemy(300, 300) {
            @Override
            public void update() {
            }
        });

//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < nums; j++) {
//                int posX = startX + j * spacing;
//                enemies.add(new ChickEnemy(posX, posY) {
//                    @Override
//                    public void update() {
//                    }
//                });
//            }
//            posY += 200;
//        }
    }

}
