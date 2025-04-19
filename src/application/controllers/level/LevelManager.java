package application.controllers.level;

import java.awt.Graphics;

import application.controllers.enemy.EnemyController;
import application.controllers.util.SoundController;
import application.views.enemy.EnemyView;

public abstract class LevelManager implements ILevelManager {
    protected EnemyController enemyController;
    protected SoundController soundController;

    public LevelManager(SoundController soundController, EnemyController enemyController) {
        this.soundController = soundController;
        this.enemyController = enemyController;
        initEnemies();
    }

    protected abstract void initEnemies();

    @Override
    public void update(float deltaTime) {
        enemyController.update();
    }

    @Override
    public void render(Graphics g) {
        for (EnemyView view : enemyController.getEnemyViews()) {
            view.render(g);
        }
    }

    @Override
    public EnemyController getEnemyController() {
        return enemyController;
    }
}