package application.controllers.enemy.death;

import application.models.enemy.Enemy;

public interface EnemyDeathListener {
    void onEnemyDeath(Enemy enemy, int index);
}
