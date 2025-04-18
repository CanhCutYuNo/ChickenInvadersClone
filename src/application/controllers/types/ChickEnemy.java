package application.controllers.types;

import java.awt.Rectangle;

import application.controllers.EnemyBehavior;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;

public class ChickEnemy implements EnemyBehavior {
    private static final int MAX_FRAME = 25;

    @Override
    public void update(Enemy enemy) {
        if (enemy.getType() != Enemy.EnemyType.CHICK_ENEMY) {
            return;
        }
        // Không có logic di chuyển
    }

    @Override
    public Rectangle getHitbox(Enemy enemy) {
        return new Rectangle(enemy.getPosX(), enemy.getPosY(), enemy.getModelWidth(), enemy.getModelHeight());
    }

    @Override
    public void addSkills(Enemy enemy, SkillType skillType, String imagePath) {
        // ChickEnemy không có kỹ năng
    }

    public void nextFrame(Enemy enemy) {
        if (enemy.getType() != Enemy.EnemyType.CHICK_ENEMY) {
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
}