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
    private SoundController soundController;
    Map<SkillType, String> skillImagePaths;

    public EnemySkillsController(SoundController soundController) {
        this.skills = new ArrayList<>();
        skillImagePaths = new HashMap<>();
        skillImagePaths.put(SkillType.HOLE, "/asset/resources/gfx/hole.png");
        skillImagePaths.put(SkillType.FIREBALL, "/asset/resources/gfx/fireball.png");
        skillImagePaths.put(SkillType.EGG, "/asset/resources/gfx/introEgg.png");
        this.view = new EnemySkillsView(skillImagePaths, soundController);
        this.soundController = soundController;
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