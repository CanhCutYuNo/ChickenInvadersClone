package application.controllers.level;

import java.awt.Graphics;

import application.controllers.enemy.EnemyController;

public interface ILevelManager {
    void update(float deltaTime);

    void render(Graphics g);

    EnemyController getEnemyController();
}