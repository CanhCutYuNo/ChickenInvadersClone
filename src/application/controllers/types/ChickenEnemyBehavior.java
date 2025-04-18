package application.controllers.types;

import java.awt.Rectangle;
import java.util.Random;

import application.controllers.EnemyBehavior;
import application.controllers.EnemySkillsController;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;

public class ChickenEnemyBehavior implements EnemyBehavior {
    private Random random = new Random();
    private static final int MAX_FRAME = 48;

    @Override
    public void update(Enemy enemy) {
        if (enemy.getType() != Enemy.EnemyType.CHICKEN_ENEMY) {
            return;
        }
        enemy.setPosX(enemy.getPosX() + (int) (enemy.getSpeed() * Math.sin(enemy.getFrameCount() * 0.05)));
        enemy.setPosY(enemy.getPosY() + enemy.getSpeed());
        enemy.setFrameCount(enemy.getFrameCount() + 1);
    }

    @Override
    public Rectangle getHitbox(Enemy enemy) {
        return new Rectangle(enemy.getPosX() - 20, enemy.getPosY() - 5, enemy.getModelWidth() + 35, enemy.getModelHeight() + 10);
    }

    @Override
    public void addSkills(Enemy enemy, SkillType skillType, String imagePath) {
        enemy.getSkills().put(skillType, imagePath);
    }

    public void nextFrame(Enemy enemy) {
        if (enemy.getType() != Enemy.EnemyType.CHICKEN_ENEMY) {
            return;
        }
        if (enemy.isForward()) {
            enemy.setCurFrame(enemy.getCurFrame() + 1);
            if (enemy.getCurFrame() >= MAX_FRAME) {
                enemy.setForward(false);
            }
        } else {
            enemy.setCurFrame(enemy.getCurFrame() - 1);
            if (enemy.getCurFrame() <= 0) {
                enemy.setForward(true);
            }
        }
    }

    public void createEggs(Enemy enemy, EnemySkillsController skillsManager) {
        if (enemy.getType() != Enemy.EnemyType.CHICKEN_ENEMY) {
            return;
        }
        if (random.nextDouble() < enemy.getEggProbability()) {
            skillsManager.addSkill(enemy.getPosX(), enemy.getPosY(), 5, enemy.getEggDamage(), SkillType.EGG);
        }
    }
}