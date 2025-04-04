package application.models.types;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import application.controllers.SoundController;
import application.models.Enemy;
import application.models.EnemySkills.SkillType;

public class EggShellEnemy extends Enemy {

    protected Image spriteSheet;

    public EggShellEnemy(int PosX, int PosY, SoundController sound) {
        super(400, SPRITE_SIZE[0], SPRITE_SIZE[1], PosX, PosY, sound);
        curFrame = 0;
        frameCount = 0;
        spriteSheet = new ImageIcon(getClass().getResource("/asset/resources/gfx/eggShell.png")).getImage();
        MODEL_HEIGHT = 97;
        MODEL_WIDTH = 75;
    }

    protected static final int[][] SPRITE = {
        {1, 1}, {153, 1}, {305, 1}, {1, 197}
    };

    protected static final int[] SPRITE_SIZE = {150, 194};

    private static Map<SkillType, String> createSkillImagePaths() {
        return new HashMap<>();
    }
    
    @Override
    public void render(Graphics g) {
        int state;
        if (getHp() >= 300) {
            state = 0;
        } else if (getHp() >= 200) {
            state = 1;
        } else if (getHp() >= 100) {
            state = 2;
        } else {
            state = 3;
        }
        g.drawImage(spriteSheet,
                PosX, PosY, PosX + MODEL_WIDTH, PosY + MODEL_HEIGHT,
                SPRITE[state][0], SPRITE[state][1], SPRITE[state][0] + SPRITE_SIZE[0], SPRITE[state][1] + SPRITE_SIZE[1], null);

        //Hitbox
//        g.setColor(Color.WHITE);
//        g.drawRect(PosX, PosY, 75, 97);
    }

    @Override
    public void nextFrame() {
       
    }

    @Override
    public void update() {

    }
    
    @Override
    public Rectangle getHitbox() {
        return new Rectangle(PosX, PosY, MODEL_WIDTH, MODEL_HEIGHT);
    }
    
    @Override
    public Map<SkillType, String> getSkills() {
    	return createSkillImagePaths();
    }
    
    @Override
    public void addSkills(SkillType skillType, String imagePath) {
        skills.put(skillType, imagePath);
    }
//	@Override
//	public EnemySkillsController getSkillsController() {
//		return null;
//	}
}
