package application.controllers;

import java.awt.Graphics;

import application.views.EnemyView;

public abstract class LevelManager {
    protected EnemyController enemyController;
    protected SoundController soundController;

    public LevelManager(SoundController soundController, EnemyController enemyController) {
        this.soundController = soundController;
        this.enemyController = enemyController;
        initEnemies();
    }

    protected abstract void initEnemies();

    public void update(float deltaTime) {
        enemyController.update();
    }

    public void render(Graphics g) {
        for(EnemyView view : enemyController.getEnemyViews()) {
            view.render(g);
        }
    }

    public void checkEnemyDeath() {
   
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}