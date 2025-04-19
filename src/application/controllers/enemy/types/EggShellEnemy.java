package application.controllers.enemy.types;

import java.awt.Rectangle;

import application.controllers.enemy.EnemyBehavior;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;

public class EggShellEnemy implements EnemyBehavior {

    @Override
    public void update(Enemy enemy) {
        if (enemy.getType() != Enemy.EnemyType.EGG_SHELL_ENEMY) {
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
        enemy.getSkills().put(skillType, imagePath);
    }

    public void nextFrame(Enemy enemy) {
        if (enemy.getType() != Enemy.EnemyType.EGG_SHELL_ENEMY) {
            return;
        }
        // Không có animation
    }
}