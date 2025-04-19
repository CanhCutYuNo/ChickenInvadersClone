package application.controllers.core;

import application.controllers.bullet.BulletController;
import application.controllers.enemy.EnemyController;
import application.controllers.enemy.skills.EnemySkillsController;
import application.controllers.level.ILevelManager;
import application.controllers.player.PlayerController;
import application.controllers.util.GameSettings;
import application.controllers.util.ViewController;

public class GameStateHandler {
    private final GameStateController gameStateController;
    private final EnemyController enemyController;
    private final EnemySkillsController skillsManager;
    private final PlayerController playerController;
    private final BulletController bullets;
    private final ViewController viewController;
    private ILevelManager levelManager;

    public GameStateHandler(GameStateController gameStateController, EnemyController enemyController,
                            EnemySkillsController skillsManager, PlayerController playerController,
                            BulletController bullets, ViewController viewController) {
        this.gameStateController = gameStateController;
        this.enemyController = enemyController;
        this.skillsManager = skillsManager;
        this.playerController = playerController;
        this.bullets = bullets;
        this.viewController = viewController;
    }

    public void restartGame() {
        gameStateController.resetGame();
        enemyController.clear();
        skillsManager.clear();
        playerController.setPosX(800);
        playerController.setPosY(950);
        bullets.clear();
        levelManager = null;
        viewController.showMenu();
        playerController.setHP();
    }

    public void load() {
        gameStateController.loadLevel(GameSettings.getInstance().getContinueLevel());
    }

    public void setLevelManager(ILevelManager levelManager) {
        this.levelManager = levelManager;
    }

	public ILevelManager getLevelManager() {
		return levelManager;
	}
}