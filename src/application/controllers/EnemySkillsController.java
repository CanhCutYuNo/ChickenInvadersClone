package application.controllers;

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

    public EnemySkillsController(Map<SkillType, String> skillImagePaths) {
        this.skills = new ArrayList<>();
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