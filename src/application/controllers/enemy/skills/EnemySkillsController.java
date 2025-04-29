package application.controllers.enemy.skills;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import application.controllers.util.SoundController;
import application.models.enemy.EnemySkills;
import application.models.enemy.EnemySkills.SkillType;
import application.views.enemy.EnemySkillsView;

public class EnemySkillsController {
    private List<EnemySkills> skills;
    private EnemySkillsView view;
    Map<SkillType, String> skillImagePaths;
    
    private int maxScaleHoldTime = 3000;

    public EnemySkillsController(SoundController soundController) {
        this.skills = new ArrayList<>();
        skillImagePaths = new HashMap<>();
        skillImagePaths.put(SkillType.HOLE, "/asset/resources/gfx/hole.png");
        skillImagePaths.put(SkillType.FIREBALL, "/asset/resources/gfx/fireball.png");
        skillImagePaths.put(SkillType.EGG, "/asset/resources/gfx/introEgg.png");
        this.view = new EnemySkillsView(skillImagePaths, soundController);
    }


	public void addSkill(double posX, double posY, double speedY, int dame, SkillType skillType) {
        EnemySkills skill = new EnemySkills(posX, posY, speedY, dame, skillType);
        skills.add(skill);
    }
    
    public void addSkill(double posX, double posY, double speedX, double speedY, int dame, SkillType skillType) {
    	EnemySkills skill = new EnemySkills(posX, posY, speedX, speedY, dame, skillType);
        skills.add(skill);
	}

    public void updateSkills() {
        Iterator<EnemySkills> iterator = skills.iterator();
        while(iterator.hasNext()) {
            EnemySkills skill = iterator.next();
            update(skill);
            if(skill.isOffScreen()) {
                skill.explode();
            }
            if(skill.removed()) {
                iterator.remove();
            }
        }
    }
    
    public void update(EnemySkills skill) {
        if (!skill.isActive()) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (skill.getSkillType() == SkillType.HOLE) {
            if (skill.isHoldingMax()) {
                if (currentTime - skill.getScaleHoldStartTime() >= maxScaleHoldTime) {
                    skill.setIsHoldingMax(false);
                    skill.setIsScalingUp(false);
                }
            } else {
                if (skill.isScalingUp()) {
                    skill.setScale(skill.getScale() + 0.05);
                    if (skill.getScale() >= 4.0) {
                        skill.setScale(4.0);
                        skill.setIsHoldingMax(true);
                        skill.setScaleHoldStartTime(currentTime);
                    }
                } else {
                    skill.setScale(skill.getScale() - 0.05);
                    if (skill.getScale() <= 1.0) {
                        skill.setScale(1.0);
                        skill.setIsActive(false);
                        skill.setEndTime(currentTime);
                        return;
                    }
                }
            }
            skill.setAngle(skill.getAngle() + Math.toRadians(2));

            if (currentTime - skill.getStartTime() >= skill.getDuration()) {
                skill.setIsActive(false);
                skill.setEndTime(currentTime);
            }
        } else if (skill.getSkillType() == SkillType.EGG) {
            if (!skill.isExploding()) {
                skill.setPosY(skill.getPosY() + skill.getSpeedY());
            } else {
                skill.setAnimationFrame(skill.getAnimationFrame() + 1);
            }
        } else if (skill.getSkillType() == SkillType.FIREBALL) {
            skill.setPosX(skill.getPosX() + skill.getSpeedX());
            skill.setPosY(skill.getPosY() + skill.getSpeedY());
            skill.setAnimationFrame(skill.getAnimationFrame() + 1);
            skill.setAngle(skill.getAngle() + Math.toRadians(5));

            if (skill.getPosX() < -100 || skill.getPosX() > 2020 || skill.getPosY() < -100 || skill.getPosY() > 1180) {
                skill.setIsActive(false);
                skill.setEndTime(currentTime);
            }
        }
    }

    public void drawSkills(Graphics g) {
        for(EnemySkills skill : skills) {
            view.draw(g, skill);
        }
    }

    public List<EnemySkills> getSkills() {
        return skills;
    }

    public void clear() {
        skills.clear();
        view.clear();
    }

	
}