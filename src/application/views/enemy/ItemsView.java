package application.views.enemy;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import application.models.enemy.Items;

public class ItemsView {
    private BufferedImage atomSpritesheet;
    private BufferedImage[] atomSprites;
    private Image foodImage;
    private Image itemImage;
    private int currentFrame = 0;
    private int frameCounter = 0;
    private static final int FRAME_DELAY = 5;

    private static final int[][] SPRITE_ATOM = {
            {5, 9, 60, 68}, {72, 9, 68, 73}, {143, 8, 68, 81}, {212, 6, 68, 81},
            {288, 6, 68, 81}, {371, 4, 68, 81}, {11, 96, 68, 81}, {103, 95, 68, 81},
            {207, 92, 68, 81}, {305, 89, 68, 81}, {399, 87, 68, 81}, {13, 178, 68, 81},
            {100, 183, 68, 81}, {183, 185, 68, 81}, {251, 189, 68, 81}, {320, 191, 68, 81},
            {391, 190, 68, 81}, {14, 285, 68, 81}, {97, 283, 68, 81}, {191, 282, 68, 81},
            {284, 275, 68, 81}, {382, 272, 68, 81}, {15, 370, 68, 81}, {110, 369, 68, 81},
            {198, 368, 68, 81}
    };

    public ItemsView(Items items) {
        try {
            atomSpritesheet = ImageIO.read(getClass().getResource("/asset/resources/gfx/atom_1.png"));
            foodImage = new ImageIcon(getClass().getResource("/asset/resources/gfx/food_thighs.PNG")).getImage();

            atomSprites = new BufferedImage[SPRITE_ATOM.length];
            for(int i = 0; i < SPRITE_ATOM.length; i++) {
                int[] s = SPRITE_ATOM[i];
                atomSprites[i] = atomSpritesheet.getSubimage(s[0], s[1], s[2], s[3]);
            }

            if(items.getType() == Items.ItemType.FOOD) {
                itemImage = foodImage;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, Items item) {
        if(item.getType() == Items.ItemType.ATOM && atomSprites != null) {
            frameCounter++;
            if(frameCounter >= FRAME_DELAY) {
                currentFrame = (currentFrame + 1) % atomSprites.length;
                frameCounter = 0;
            }
            g.drawImage(atomSprites[currentFrame], (int) item.getPosX(), (int) item.getPosY(), null);
        } else if(item.getType() == Items.ItemType.FOOD && itemImage != null) {
            g.drawImage(itemImage, (int) item.getPosX(), (int) item.getPosY(), null);

//            Rectangle hitbox = item.getHitbox();
//            g.setColor(Color.RED);
//            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }
    }

    
}
