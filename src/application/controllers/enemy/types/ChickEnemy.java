package application.controllers.enemy.types;

import java.awt.Rectangle;

import application.controllers.enemy.EnemyBehavior;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;

public class ChickEnemy implements EnemyBehavior {
    @Override
    public void update(Enemy enemy) {
        if (enemy.getType() != Enemy.EnemyType.CHICK_ENEMY) {
            return;
        }
        // TODO: Thêm logic di chuyển hoặc hành vi nếu cần
    }

    @Override
    public Rectangle getHitbox(Enemy enemy) {
        return new Rectangle(enemy.getPosX(), enemy.getPosY(), enemy.getModelWidth(), enemy.getModelHeight());
    }

    @Override
    public void addSkills(Enemy enemy, SkillType skillType, String imagePath) {
        // Hiện tại không có kỹ năng cho CHICK_ENEMY
    }
}