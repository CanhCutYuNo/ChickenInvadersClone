package application.controllers.level;

import application.controllers.core.GameStateController;
import application.controllers.enemy.EnemyController;
import application.controllers.util.SoundController;

public class LevelTransitionHandler {
    private final EnemyController enemyController;
    private final GameStateController gameStateController;
    private final SoundController soundController;

    public LevelTransitionHandler(EnemyController enemyController, GameStateController gameStateController,
                                  SoundController soundController) {
        this.enemyController = enemyController;
        this.gameStateController = gameStateController;
        this.soundController = soundController;
    }

    public ILevelManager spawnEnemiesAfterFade() {
        enemyController.clear();
        return LevelManagerFactory.createLevelManager(gameStateController.getLevel(), soundController, enemyController);
    }

    public void onTransitionComplete() {
        gameStateController.onTransitionComplete();
    }
}
