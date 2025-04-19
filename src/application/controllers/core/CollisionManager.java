package application.controllers.core;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Random;

import application.controllers.bullet.BulletController;
import application.controllers.enemy.EnemyController;
import application.controllers.enemy.items.ItemsController;
import application.controllers.enemy.skills.EnemySkillsController;
import application.controllers.player.PlayerController;
import application.controllers.util.GameSettings;
import application.controllers.util.SoundController;
import application.models.bullet.Bullet;
import application.models.bullet.BulletDame;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills;
import application.models.enemy.Items;

public class CollisionManager {
    private final PlayerController playerController;
    private final EnemyController enemyController;
    private final BulletController bulletController;
    private final EnemySkillsController skillsManager;
    private final ItemsController itemsController;
    private final GameStateController gameStates;
    private final SoundController soundController;
    private final String[] hitSounds;
    private final String[] deathSounds;
    private boolean isCollidingAtom = false;

    public CollisionManager(PlayerController playerController, EnemyController enemyController,
                               BulletController bulletController, EnemySkillsController skillsManager,
                               ItemsController itemsController, GameStateController gameStates, SoundController soundController,
                               String[] hitSounds, String[] deathSounds) {
        this.playerController = playerController;
        this.enemyController = enemyController;
        this.bulletController = bulletController;
        this.skillsManager = skillsManager;
        this.itemsController = itemsController;
        this.gameStates = gameStates;
        this.soundController = soundController;
        this.hitSounds = hitSounds;
        this.deathSounds = deathSounds;
    }

    public void checkCollisions() {
        checkBulletEnemyCollisions();
        checkPlayerCollisionsWithEnemies();
        checkPlayerCollisionsWithSkills();
        checkPlayerCollisionsWithItems();
    }

    private void checkBulletEnemyCollisions() {
        for (int i = bulletController.getBullets().size() - 1; i >= 0; i--) {
            Bullet bullet = bulletController.getBullets().get(i);
            if(isCollidingAtom){
                bullet.transformToStrongerBullet();
                bulletController.removeBullet(i);
                bulletController.addBullet(bullet.getX(), bullet.getY(), bullet.getDamage(), bullet.getSpeedY(), bullet.getAcceleration(),bullet.getType());
            }

            for (int j = enemyController.getEnemyModels().size() - 1; j >= 0; j--) {
                if (isColliding(bullet, j)) {
                    Enemy enemy = enemyController.getEnemyModels().get(j);
                    spawnFloatingText(enemy.getPosX() - 2, enemy.getPosY(), "- " + String.valueOf(bullet.getDamage()), Color.RED);
                    if (enemy.getType() == Enemy.EnemyType.CHICKEN_ENEMY || enemy.getType() == Enemy.EnemyType.CHICK_ENEMY) {
                        enemyController.takeDamage(j, bullet.getDamage(), hitSounds, deathSounds);
                    } else {
                        enemyController.takeDamage(j, bullet.getDamage(), null, null);
                        soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/eggshellCrack.wav").getPath());                        
                    }
                    bulletController.removeBullet(i);
                    if (enemy.isDead()) {
                        Random random = new Random();
                        Items.ItemType itemType = null;
                        float chance = 0;
                        int damageItem = 0;
                        switch (GameSettings.getInstance().getDifficulty()) {
                            case EASY:
                                chance = 0.5F;
                                damageItem = -15;
                                break;
                            case NORMAL:
                                chance = 0.3F;
                                damageItem = -10;
                                break;
                            case HARD:
                                chance = 0.2F;
                                damageItem = -5;
                                break;
                            case EXTREME:
                                chance = 0.15F;
                                damageItem = -5;
                                break;
                        }
                        if (random.nextDouble() < chance) {
                            if (!itemsController.hasDroppedAtom()) {
                                itemType = Items.ItemType.ATOM;
                                itemsController.markAtomDropped();
                            } else {
                                itemType = Items.ItemType.FOOD;

                            }
                            itemsController.addItem(enemy.getPosX(), enemy.getPosY() - 15, damageItem, itemType);
                        }
                    }
                    break;
                }
            }
        }
    }

    private void checkPlayerCollisionsWithItems() {
        Iterator<Items> iterator = itemsController.iterator();
        while (iterator.hasNext()) {
            Items item = iterator.next();
            if (isColliding(playerController, item)) {
                if(item.getType() == Items.ItemType.FOOD){
                    playerController.isDamaged(item.getDamage());
                    spawnFloatingText(playerController.getPosX(), playerController.getPosY() - 10, "+ " + String.valueOf(Math.abs(item.getDamage())), Color.GREEN);
                    gameStates.setFoodCounts(gameStates.getFoodCounts() + 1);
                }
                else{
                    isCollidingAtom = true;
                }
                soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/(eating1).wav").getPath());
                iterator.remove();
            }
        }
    }

    private void checkPlayerCollisionsWithEnemies() {
        for (int i = 0; i < enemyController.getEnemyModels().size(); i++) {
            if (isColliding(playerController, i)) {
                if (!gameStates.isPlayerExploded()) {
                    playerController.getPlayerView().startExplosion();
                    soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/explosionPlayer.wav").getPath());
                    gameStates.setPlayerExploded(true);
                }
                break;
            }
        }
    }

    private void checkPlayerCollisionsWithSkills() {
        Iterator<EnemySkills> skillIterator = skillsManager.getSkills().iterator();
        while (skillIterator.hasNext()) {
            EnemySkills skill = skillIterator.next();
            if (skill.isActive() && isColliding(playerController, skill)) {
                playerController.isDamaged(skill.getDamage());
                spawnFloatingText(playerController.getPosX(), playerController.getPosY() - 10, "- " + String.valueOf(skill.getDamage()), Color.RED);
                soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/eggshellCrack.wav").getPath());
                if (playerController.getHP() <= 0) {
                    playerController.getPlayerView().startExplosion();
                    soundController.playSoundEffect(getClass().getResource("/asset/resources/sfx/explosionPlayer.wav").getPath());
                    gameStates.setPlayerExploded(true);
                }
                skillIterator.remove();
            }
        }
    }

    private void spawnFloatingText(int x, int y, String text, Color color) {
        gameStates.getGameStates().addFloatingText(new BulletDame(x, y, text, color));
    }

    private boolean isColliding(Bullet bullet, int enemyIndex) {
        Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 9, 52);
        return enemyController.getHitbox(enemyIndex).intersects(bulletBounds);
    }

    private boolean isColliding(PlayerController player, int enemyIndex) {
        return player.getHitbox().intersects(enemyController.getHitbox(enemyIndex));
    }

    private boolean isColliding(PlayerController player, EnemySkills skill) {
        return skill.getHitbox().intersects(player.getHitbox());
    }

    private boolean isColliding(PlayerController player, Items item) {
        return player.getHitbox().intersects(item.getHitbox());
    }
}