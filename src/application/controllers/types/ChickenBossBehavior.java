package application.controllers.types;

import java.awt.Rectangle;
import java.util.Random;

import application.controllers.EnemyBehavior;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;

public class ChickenBossBehavior implements EnemyBehavior {
    private Random random = new Random();
    private static final int[][] SPRITE = {
        {0,0}, {1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, {7,0}, {8,0}, {9,0},
        {0,1}, {1,1}, {2,1}, {3,1}, {4,1}, {5,1}, {6,1}, {7,1}, {8,1}, {9,1},
        {0,2}, {1,2}, {2,2}, {3,2}, {4,2}, {5,2}, {6,2}, {7,2}, {8,2}, {9,2},
        {0,3}, {1,3}, {2,3}, {3,3}, {4,3}, {5,3}, {6,3}, {7,3}, {8,3}, {9,3},
        {0,4}, {1,4}, {2,4}, {3,4}, {4,4}, {5,4}, {6,4}, {7,4}, {8,4}, {9,4},
        {0,5}, {1,5}, {2,5}, {3,5}, {4,5}, {5,5}, {6,5}, {7,5}, {8,5}, {9,5},
    };

    @Override
    public void update(Enemy enemy) {
        long currentTime = System.currentTimeMillis();
        if (enemy.getType() != Enemy.EnemyType.CHICKEN_BOSS) {
            return;
        }

        // Cập nhật frame
        if (currentTime - enemy.getLastFrameTime() >= enemy.getSpriteDelay()) {
            enemy.setCurFrame(enemy.getCurFrame() + 1);
            if (enemy.getCurFrame() >= SPRITE.length) {
                enemy.setCurFrame(0);
            }
            enemy.setLastFrameTime(currentTime);
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

        // Kiểm tra thời gian để thông báo tạo kỹ năng
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