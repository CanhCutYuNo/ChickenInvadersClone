package application.models.enemy.types;

import application.models.enemy.DeathEffect;
import application.views.types.ChickenDeathEffectView;
import java.awt.Graphics;

public class ChickenDeathEffect extends DeathEffect {

    private final ChickenDeathEffectView chickenDeathEffectView;

    public ChickenDeathEffect(int PosX, int PosY) {
        super(PosX, PosY);
        chickenDeathEffectView = new ChickenDeathEffectView(this);
    }

    @Override
    public void render(Graphics g) {
        chickenDeathEffectView.render(g);
    }

    @Override
    public void update() {
        chickenDeathEffectView.update();
    }
}
