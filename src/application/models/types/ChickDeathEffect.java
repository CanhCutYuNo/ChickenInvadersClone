package application.models.types;

import application.models.DeathEffect;
import application.views.types.ChickDeathEffectView;
import java.awt.Graphics;

public class ChickDeathEffect extends DeathEffect {

    private final ChickDeathEffectView chickDeathEffectView;

    public ChickDeathEffect(int PosX, int PosY) {
        super(PosX, PosY);
        chickDeathEffectView = new ChickDeathEffectView(this);
    }

    @Override
    public void render(Graphics g) {
        chickDeathEffectView.render(g);
    }

    @Override
    public void update() {
        chickDeathEffectView.update();
    }
}
