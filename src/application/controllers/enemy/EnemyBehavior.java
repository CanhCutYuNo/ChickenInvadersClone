package application.controllers.enemy;

import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;

import java.awt.Rectangle;

public interface EnemyBehavior {
    void update(Enemy enemy);
    Rectangle getHitbox(Enemy enemy);
    void addSkills(Enemy enemy, SkillType skillType, String imagePath);
}