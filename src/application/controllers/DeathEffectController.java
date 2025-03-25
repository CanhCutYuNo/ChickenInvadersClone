package application.controllers;

import application.models.DeathEffect;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeathEffectController {
    private final List<DeathEffect> deathEffects;

    public DeathEffectController() {
        deathEffects = new ArrayList<>();
    }

    public void add(DeathEffect de) {
        deathEffects.add(de);
    }

    public void update() {
        ArrayList<DeathEffect> deathEffectsToRemove = new ArrayList<>();
        for(DeathEffect deathEffect : deathEffects) {
            if(deathEffect != null) {
                deathEffect.update();
                if(deathEffect.isEnd()) {
                    deathEffectsToRemove.add(deathEffect);
                }
            }
        }
        deathEffects.removeAll(deathEffectsToRemove);
    }

    public void render(Graphics g) {
        for (DeathEffect deathEffect : deathEffects) {
            if(deathEffect != null){
                deathEffect.render(g);
            }
        }
    }

}
