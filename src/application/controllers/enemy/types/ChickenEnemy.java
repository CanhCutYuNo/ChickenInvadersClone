package application.controllers.enemy.types;

import java.awt.Rectangle;
import java.util.Random;

import application.controllers.enemy.EnemyBehavior;
import application.controllers.enemy.skills.EnemySkillsController;
import application.models.enemy.Enemy;
import application.models.enemy.EnemySkills.SkillType;

public class ChickenEnemy implements EnemyBehavior {
    private Random random = new Random();

    @Override
    public void update(Enemy enemy) {
        if (enemy.getType() != Enemy.EnemyType.CHICKEN_ENEMY) {
            return;
        }
        // Sử dụng moveCounter thay vì frameCount
        enemy.setPosX(enemy.getPosX() + (int) (enemy.getSpeed() * Math.sin(enemy.getMoveCounter() * 0.05)));
        enemy.setPosY(enemy.getPosY() + enemy.getSpeed());
        enemy.setMoveCounter(enemy.getMoveCounter() + 1);
    }

    @Override
    public Rectangle getHitbox(Enemy enemy) {
        return new Rectangle(enemy.getPosX() - 20, enemy.getPosY() - 5, enemy.getModelWidth() + 35, enemy.getModelHeight() + 10);
    }

    @Override
    public void addSkills(Enemy enemy, SkillType skillType, String imagePath) {
        enemy.getSkills().put(skillType, imagePath);
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