package application.controllers;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import application.models.EnemySkills;
import application.models.EnemySkills.SkillType;
import application.views.EnemySkillsView;

public class EnemySkillsController {
    private List<EnemySkills> skills;
    private EnemySkillsView view;
    Map<SkillType, String> skillImagePaths;

    public EnemySkillsController(Map<SkillType, String> _skillImagePaths) {
        this.skills = new ArrayList<>();
        skillImagePaths = _skillImagePaths;
        // Thêm path thủ công để test
        skillImagePaths.put(SkillType.HOLE, "/asset/resources/gfx/hole.png"); // Thay bằng path thật
        skillImagePaths.put(SkillType.FIREBALL, "/asset/resources/gfx/fireball.png"); // Thay bằng path thật
        skillImagePaths.put(SkillType.EGG, "/asset/resources/gfx/introEgg.png"); // Thay bằng path thật
        System.out.println("Skill image paths: " + skillImagePaths);
        this.view = new EnemySkillsView(skillImagePaths);
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
            skill.update();
            if(skill.isOffScreen()) {
                skill.explode();
            }
            if(skill.removed()) {
                iterator.remove();
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
    }

	
}