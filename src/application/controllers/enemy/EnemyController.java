package application.controllers.enemy;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import application.controllers.enemy.death.DeathEffectController;
import application.controllers.enemy.death.EnemyDeathListener;
import application.controllers.enemy.skills.EnemySkillsController;
import application.controllers.enemy.types.ChickEnemy;
import application.controllers.enemy.types.ChickenBoss;
import application.controllers.enemy.types.ChickenEnemy;
import application.controllers.enemy.types.EggShellEnemy;
import application.controllers.util.SoundController;
import application.models.enemy.DeathEffect;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;
import application.models.enemy.types.ChickDeathEffect;
import application.models.enemy.types.ChickenDeathEffect;
import application.views.enemy.EnemyView;

public class EnemyController {
    private List<Enemy> enemyModels;
    private List<EnemyView> enemyViews;
    private SoundController soundController;
    private Map<Enemy.EnemyType, EnemyBehavior> behaviors;
    private Random random;
    private EnemySkillsController skillsManager;
    private DeathEffectController deathEffectController;
    private List<EnemyDeathListener> deathListeners = new ArrayList<>();

    public EnemyController(SoundController soundController) {
        this.enemyModels = new ArrayList<>();
        this.enemyViews = new ArrayList<>();
        this.soundController = soundController;
        this.random = new Random();
        this.behaviors = new HashMap<>();
        behaviors.put(Enemy.EnemyType.CHICKEN_ENEMY, new ChickenEnemy());
        behaviors.put(Enemy.EnemyType.EGG_SHELL_ENEMY, new EggShellEnemy());
        behaviors.put(Enemy.EnemyType.CHICKEN_BOSS, new ChickenBoss());
        behaviors.put(Enemy.EnemyType.CHICK_ENEMY, new ChickEnemy());
    }

    public void addEnemy(Enemy enemyModel, EnemyView enemyView) {
        enemyModels.add(enemyModel);
        enemyViews.add(enemyView);
    }
    
    public void addDeathListener(EnemyDeathListener listener) {
        deathListeners.add(listener);
    }

    public void update() {
        for (int i = 0; i < enemyModels.size(); i++) {
            Enemy enemy = enemyModels.get(i);
            EnemyBehavior behavior = behaviors.get(enemy.getType());
            if (behavior != null) {
                behavior.update(enemy);
                if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
                    ((ChickenEnemy) behavior).createEggs(enemy, skillsManager);
                }
            }
        }
    }
    
    public void checkAndNotifyDeaths() {
        for (int i = enemyModels.size() - 1; i >= 0; i--) {
            Enemy enemy = enemyModels.get(i);
            if (enemy.isDead()) {
                // Phát sự kiện trước khi xóa
                for (EnemyDeathListener listener : deathListeners) {
                    listener.onEnemyDeath(enemy, i);
                }
                getDeathEffect(i);
                removeEnemy(i);
            }
        }
    }

    public void takeDamage(int index, int damage, String[] hitSounds, String[] deathSounds) {
        if (index < 0 || index >= enemyModels.size()) {
            return;
        }
        Enemy enemy = enemyModels.get(index);
        enemy.setHp(enemy.getHp() - damage);
        if (hitSounds != null && hitSounds.length > 0) {
            soundController.playSoundEffect(getClass().getResource(hitSounds[random.nextInt(hitSounds.length)]).getPath());
        }
        if (enemy.isDead()) {
            deathEffectController.add(getDeathEffect(index));
            String deathSound = (deathSounds != null && deathSounds.length > 0) ? deathSounds[random.nextInt(deathSounds.length)] : null;
            if (deathSound != null) {
                soundController.playSoundEffect(getClass().getResource(deathSound).getPath());
            }
        }
        checkAndNotifyDeaths();
    }

    public DeathEffect getDeathEffect(int index) {
        if (index < 0 || index >= enemyModels.size()) {
            return null;
        }
        Enemy enemy = enemyModels.get(index);
        if (enemy.getType() == Enemy.EnemyType.CHICK_ENEMY) {
            return new ChickDeathEffect(enemy.getCenterX(), enemy.getCenterY());
        } else if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
            return new ChickenDeathEffect(enemy.getCenterX(), enemy.getCenterY());
        }
        return null;
    }

    public Rectangle getHitbox(int index) {
        if (index < 0 || index >= enemyModels.size()) {
            return null;
        }
        Enemy enemy = enemyModels.get(index);
        EnemyBehavior behavior = behaviors.get(enemy.getType());
        if (behavior != null) {
            return behavior.getHitbox(enemy);
        }
        return new Rectangle(enemy.getPosX(), enemy.getPosY(), enemy.getModelWidth(), enemy.getModelHeight());
    }

    public void addSkills(int index, SkillType skillType, String imagePath) {
        if (index < 0 || index >= enemyModels.size()) {
            return;
        }
        Enemy enemy = enemyModels.get(index);
        EnemyBehavior behavior = behaviors.get(enemy.getType());
        if (behavior != null) {
            behavior.addSkills(enemy, skillType, imagePath);
        }
    }

    public void removeEnemy(int index) {
        if (index >= 0 && index < enemyModels.size()) {
            enemyModels.remove(index);
            enemyViews.remove(index);
        }
    }

    public void clear() {
        enemyModels.clear();
        enemyViews.clear();
    }

    public List<Enemy> getEnemyModels() {
        return new ArrayList<>(enemyModels);
    }

    public List<EnemyView> getEnemyViews() {
        return new ArrayList<>(enemyViews);
    }

    public void createHoleSkill(int index, EnemySkillsController skillsManager) {
        if (index < 0 || index >= enemyModels.size()) {
            return;
        }
        Enemy enemy = enemyModels.get(index);
        if (enemy.getType() == Enemy.EnemyType.CHICKEN_BOSS && enemy.shouldCreateHole()) {
            skillsManager.addSkill(1920 / 2, 1080 / 2, 0, 100, SkillType.HOLE);
            soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/engineCrab.wav").getPath());
            enemy.setShouldCreateHole(false);
        }
    }

    public void createFireballBurst(int index, EnemySkillsController skillsManager) {
        if (index < 0 || index >= enemyModels.size()) {
            return;
        }
        Enemy enemy = enemyModels.get(index);
        if (enemy.getType() == Enemy.EnemyType.CHICKEN_BOSS && enemy.shouldCreateFireballBurst()) {
            double centerX = 1920 / 2;
            double centerY = 1080 / 2;
            int damage = 1000;
            double speed = 5;

            for (int i = 0; i < 10; i++) {
                double angleStart = i * 36;
                double angleEnd = (i + 1) * 36;

                for (int j = 0; j < 2; j++) {
                    double angle = Math.toRadians(random.nextDouble() * (angleEnd - angleStart) + angleStart);
                    double speedX = speed * Math.cos(angle);
                    double speedY = speed * Math.sin(angle);
                    skillsManager.addSkill(centerX, centerY, speedX, speedY, damage, SkillType.FIREBALL);
                }
            }
            soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/cannonFire.wav").getPath());
            enemy.setShouldCreateFireballBurst(false);
        }
    }

    public void setSkillsManager(EnemySkillsController skillsManager) {
        this.skillsManager = skillsManager;
    }

	public EnemySkillsController getSkillsManager() {
		return skillsManager;
	}

    public void setDeathEffectController(DeathEffectController deathEffectController) {
        this.deathEffectController = deathEffectController;
    }

    public DeathEffectController getDeathEffectController() {
        return deathEffectController;
    }
}