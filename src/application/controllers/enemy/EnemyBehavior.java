package application.controllers;

import application.models.Enemy;
import application.models.EnemySkills.SkillType;

import java.awt.Rectangle;

public interface EnemyBehavior {
    void update(Enemy enemy);
    Rectangle getHitbox(Enemy enemy);
    void addSkills(Enemy enemy, SkillType skillType, String imagePath);
}