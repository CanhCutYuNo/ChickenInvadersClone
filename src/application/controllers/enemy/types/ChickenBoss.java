package application.controllers.enemy.types;

import java.awt.Rectangle;

import application.controllers.enemy.EnemyBehavior;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;

public class ChickenBoss implements EnemyBehavior {
    @Override
    public void update(Enemy enemy) {
        long currentTime = System.currentTimeMillis();
        if (enemy.getType() != Enemy.EnemyType.CHICKEN_BOSS) {
            return;
        }

        // Di chuyển đến trung tâm
        if (enemy.isMovingToCenter()) {
            enemy.setPosY(enemy.getPosY() - enemy.getMoveSpeed());
            if (enemy.getPosY() <= enemy.getTargetY()) {
                enemy.setPosY(enemy.getTargetY());
                enemy.setMovingToCenter(false);
            }
            return;
        }

        if (enemy.getSkillState() == 0) {
            if (currentTime - enemy.getLastHoleSkillTime() >= enemy.getHoleSkillCooldown()) {
                enemy.setShouldCreateHole(true);
                enemy.setLastHoleSkillTime(currentTime);
                enemy.setSkillState(1);
                System.out.println("ChickenBoss requests new HOLE skill at " + currentTime);
            }
        } else if (enemy.getSkillState() == 1) {
            if (currentTime - enemy.getLastHoleSkillTime() >= enemy.getSkillsDelay() &&
                currentTime - enemy.getLastFireballSkillTime() >= enemy.getFireballSkillCooldown()) {
                enemy.setShouldCreateFireballBurst(true);
                enemy.setLastFireballSkillTime(currentTime);
                enemy.setSkillState(0);
                System.out.println("ChickenBoss requests new FIREBALL burst at " + currentTime);
            }
        }
    }

    @Override
    public Rectangle getHitbox(Enemy enemy) {
        return new Rectangle(enemy.getPosX() + 20, enemy.getPosY(), enemy.getModelWidth() - 40, enemy.getModelHeight());
    }

    @Override
    public void addSkills(Enemy enemy, SkillType skillType, String imagePath) {
        enemy.getSkills().put(skillType, imagePath);
    }
}