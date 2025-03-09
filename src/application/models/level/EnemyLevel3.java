package application.models.level;
import application.models.Enemy;

import java.awt.Image;

public class EnemyLevel3 extends Enemy{
    private int direction;
    private int speed;

    public EnemyLevel3(int hp, int PosX, int PosY, int direction, Image bodySheet, Image wingsSheet, Image headSheet, Image blinkAnimation) {
        super(hp, PosX, PosY, bodySheet, wingsSheet, headSheet, blinkAnimation);

        this.speed = 3;
        this.direction = direction;

    }

    @Override
    public void update(){
        PosX += speed ; // Di chuyển ngang theo hướng

        // Khi đến gần trung tâm (hoặc điểm giới hạn), có thể đổi hướng hoặc bắn đạn
        if (( PosX > 400) || (PosX < 400)) {
            PosX // Dừng lại hoặc đổi hướng
        }
    }

}
