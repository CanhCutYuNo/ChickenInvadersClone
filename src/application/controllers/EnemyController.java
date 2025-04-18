package application.controllers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import application.controllers.types.ChickEnemy;
import application.controllers.types.ChickenBoss;
import application.controllers.types.ChickenEnemy;
import application.controllers.types.EggShellEnemy;
import application.models.DeathEffect;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;
import application.models.types.ChickDeathEffect;
import application.models.types.ChickenDeathEffect;
import application.views.EnemyView;

public class EnemyController {
    private List<Enemy> enemyModels;
    private List<EnemyView> enemyViews;
    private SoundController soundController;
    private Map<Enemy.EnemyType, EnemyBehavior> behaviors;
    private Random random;

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

    public void update() {
        for (int i = 0; i < enemyModels.size(); i++) {
            Enemy enemy = enemyModels.get(i);
            EnemyBehavior behavior = behaviors.get(enemy.getType());
            if (behavior != null) {
                behavior.update(enemy);
                if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
                    ((ChickenEnemy) behavior).createEggs(enemy, skillsManager); // skillsManager cần được truyền vào
                }
            }
            nextFrame(enemy);
        }
    }

    private void nextFrame(Enemy enemy) {
        if (enemy.getType() == Enemy.EnemyType.CHICKEN_BOSS) {
            return; // ChickenBossBehavior đã xử lý frame
        }
        if (enemy.getType() == Enemy.EnemyType.CHICK_ENEMY) {
            ChickEnemy behavior = (ChickEnemy) behaviors.get(Enemy.EnemyType.CHICK_ENEMY);
            behavior.nextFrame(enemy);
            return;
        }
        if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
            ChickenEnemy behavior = (ChickenEnemy) behaviors.get(Enemy.EnemyType.CHICKEN_ENEMY);
            behavior.nextFrame(enemy);
            return;
        }
        if (enemy.isForward()) {
            enemy.setCurFrame(enemy.getCurFrame() + 1);
            if (enemy.getCurFrame() >= 48) {
                enemy.setForward(false);
            }
        } else {
            enemy.setCurFrame(enemy.getCurFrame() - 1);
            if (enemy.getCurFrame() <= 0) {
                enemy.setForward(true);
            }
        }
    }

    public void takeDamage(int index, int damage, String[] hitSounds, String[] deathSounds) {
        Enemy enemy = enemyModels.get(index);
        enemy.setHp(enemy.getHp() - damage);
        if (hitSounds != null && hitSounds.length != 0) {
            soundController.playSoundEffect(getClass().getResource(hitSounds[random.nextInt(hitSounds.length)]).getPath());
        }
        if (enemy.isDead()) {
            String deathSound = (enemy.getType() == Enemy.EnemyType.CHICKEN_BOSS) 
                ? "/asset/resources/sfx/death1.wav" 
                : deathSounds[random.nextInt(deathSounds.length)];
            if (deathSounds != null && deathSounds.length != 0) {
                soundController.playSoundEffect(getClass().getResource(deathSound).getPath());
            }
        }
    }

    public DeathEffect getDeathEffect(int index) {
        Enemy enemy = enemyModels.get(index);
        if (enemy.getType() == Enemy.EnemyType.CHICK_ENEMY) {
            return new ChickDeathEffect(enemy.getCenterX(), enemy.getCenterY());
        } else if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY) {
            return new ChickenDeathEffect(enemy.getCenterX(), enemy.getCenterY());
        }
        return null;
    }

    public Rectangle getHitbox(int index) {
        Enemy enemy = enemyModels.get(index);
        EnemyBehavior behavior = behaviors.get(enemy.getType());
        if (behavior != null) {
            return behavior.getHitbox(enemy);
        }
        return new Rectangle(enemy.getPosX(), enemy.getPosY(), enemy.getModelWidth(), enemy.getModelHeight());
    }

    public void addSkills(int index, SkillType skillType, String imagePath) {
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
        Enemy enemy = enemyModels.get(index);
        if (enemy.getType() == Enemy.EnemyType.CHICKEN_BOSS && enemy.shouldCreateHole()) {
            skillsManager.addSkill(1920 / 2, 1080 / 2, 0, 5000, SkillType.HOLE);
            soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/engineCrab.wav").getPath());
            enemy.setShouldCreateHole(false);
        }
    }

    public void createFireballBurst(int index, EnemySkillsController skillsManager) {
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

    // skillsManager cần được thêm vào constructor hoặc setter
    private EnemySkillsController skillsManager;

    public void setSkillsManager(EnemySkillsController skillsManager) {
        this.skillsManager = skillsManager;
    }
}