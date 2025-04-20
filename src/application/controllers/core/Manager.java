package application.controllers.core;

import java.awt.Color;

import application.controllers.bullet.BulletController;
import application.controllers.enemy.EnemyController;
import application.controllers.enemy.death.DeathEffectController;
import application.controllers.enemy.items.ItemsController;
import application.controllers.enemy.skills.EnemySkillsController;
import application.controllers.level.ILevelManager;
import application.controllers.level.LevelTransitionHandler;
import application.controllers.player.PlayerActionHandler;
import application.controllers.player.PlayerController;
import application.controllers.util.SoundController;
import application.controllers.util.ViewController;
import application.models.core.GameStates;
import application.views.panels.GamePanel;
import application.views.player.PlayerView;

public class Manager {
    private PlayerView playerView;
    private PlayerController playerController;
    private BulletController bullets;
    private EnemyController enemyController;
    private EnemySkillsController skillsManager;
    private DeathEffectController deathEffectController;
    private ItemsController items;
    private CollisionManager collisionManager;
    private GameStateController gameStateController;
    private SoundController soundController;
    private GameLoop gameLoop;
    private GamePanel gamePanel;
    private ViewController viewController;
    
    private PlayerActionHandler playerActionHandler;
    private GameStateHandler gameStateHandler;
    private final LevelTransitionHandler levelTransitionHandler;



    private String[] deathSounds = {
            "/asset/resources/sfx/chickDie3.wav",
            "/asset/resources/sfx/chickDie4.wav",
            "/asset/resources/sfx/chickDie5.wav",
            "/asset/resources/sfx/chickDie6.wav",
            "/asset/resources/sfx/chicken1a(die).wav",
            "/asset/resources/sfx/chicken2b(die).wav",
            "/asset/resources/sfx/chicken3a(die).wav"
    };
    private String[] hitSounds = {
            "/asset/resources/sfx/chicken1b1(pluck).wav",
            "/asset/resources/sfx/chicken1b2(pluck).wav",
            "/asset/resources/sfx/chicken2a1(pluck).wav",
            "/asset/resources/sfx/chicken3b1(pluck).wav",
            "/asset/resources/sfx/chicken3b2(pluck).wav",
            "/asset/resources/sfx/chicken5b(pluck).wav"
    };

    public Manager(GameLoop _gameLoop, SoundController _soundController, GamePanel _gamePanel) {
        this.soundController = _soundController;
        GameStates gameStates = new GameStates();
        this.gameStateController = new GameStateController(gameStates);
        this.bullets = new BulletController();
        this.enemyController = new EnemyController(soundController);
        this.skillsManager = new EnemySkillsController(soundController);
        this.enemyController.setSkillsManager(skillsManager);
        this.deathEffectController = new DeathEffectController();
        this.playerController = new PlayerController(null);
        this.playerView = new PlayerView(playerController);
        this.playerController.setPlayerView(playerView);
        this.items = new ItemsController("/asset/resources/gfx/flareSmall~1.png");
        this.gameLoop = _gameLoop;
        this.gamePanel = _gamePanel;
        this.collisionManager = new CollisionManager(playerController, enemyController, bullets,
                skillsManager, items, gameStateController, soundController, hitSounds, deathSounds);
        this.playerActionHandler = new PlayerActionHandler(playerController, bullets, soundController);
        this.gameStateHandler = null;
        this.levelTransitionHandler = new LevelTransitionHandler(enemyController, gameStateController, soundController);
    }

    public PlayerController getPlayer() {
        return playerController;
    }

    public GameStateController getGameStateController() { // Thêm getter cho GameStateController
        return gameStateController;
    }

    public BulletController getBulletController() {
        return bullets;
    }

    public EnemySkillsController getSkillsManager() {
        return skillsManager;
    }

    public ILevelManager getLevelManager() {
        return gameStateHandler.getLevelManager();
    }

    public DeathEffectController getDeathEffectController() {
        return deathEffectController;
    }

    public ItemsController getItemsController() {
        return items;
    }

    public PlayerView getPlayerView() {
        return playerController.getPlayerView();
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }
    
    public PlayerActionHandler getPlayerActionHandler() {
    	return playerActionHandler;
    }

    public void onTransitionComplete() {
        levelTransitionHandler.onTransitionComplete();
    }

    public void update(double deltaTime) {
        if (playerView.isExploding()) {
            playerView.updateExplosion();
            if (52 < playerView.getExFrame()) {
            	gameStateHandler.restartGame();
            }
            return;
        }
        
        updateBullets();
        playerController.update();
        items.updateItems();
        skillsManager.updateSkills();
        deathEffectController.update();

        if (gamePanel.isTransitionActive()) {
            return;
        }

        if (gameStateController.updateLevelTransition()) {
            collisionManager.resetBulletPowerUp();
            gamePanel.triggerTransition();
        }

        if (gameStateController.isDelaying()) {
            return;
        }

        enemyController.update();
        

        ILevelManager levelManager = gameStateHandler.getLevelManager();
        if (levelManager != null) {
            levelManager.update((float) deltaTime);
        }

        collisionManager.checkCollisions();
        gameStateController.updateFloatingTexts();

        gameStateController.checkLevelTransition(enemyController.getEnemyModels().isEmpty());
    }

    private void updateBullets() {
        bullets.update();
    }

    public void spawnEnemiesAfterFade() {
        ILevelManager newLevelManager = levelTransitionHandler.spawnEnemiesAfterFade();
        gameStateHandler.setLevelManager(newLevelManager);
    }

    public void spawnFloatingText(int x, int y, String text, Color color) {
        gameStateController.spawnFloatingText(x, y, text, color);
    }

    public void setGamePanel(GamePanel _gamePanel) {
        this.gamePanel = _gamePanel;
    }

    public void load() {
        gameStateHandler.load();
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

	public void setViewController(ViewController _viewController) {
		this.viewController = _viewController;
		this.gameStateHandler = new GameStateHandler(gameStateController, enemyController, skillsManager,
                playerController, bullets, viewController);
	}
}