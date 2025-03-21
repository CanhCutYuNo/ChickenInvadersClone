package application.controllers;

import application.models.EnemyProjectiles;
import application.views.EnemyProjectilesView;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyProjectilesController {
    private List<EnemyProjectiles> projectiles;
    private EnemyProjectilesView projectileView;

    public EnemyProjectilesController(String path) {
        projectiles = new ArrayList<>();
        projectileView = new EnemyProjectilesView(path);
    }

    public void addProjectile(double x, double y, int damage) {
        projectiles.add(new EnemyProjectiles(x, y,damage));
    }

    public void updateProjectiles() {
        Iterator<EnemyProjectiles> iterator = projectiles.iterator();
        while(iterator.hasNext()) {
            EnemyProjectiles projectile = iterator.next();
            projectile.update();

            if(!projectile.isExploding() && projectile.isOffScreen()) {
                projectile.explode(); 
            }

            if(projectile.removed()) {
                iterator.remove();
            }
        }
    }


    public void drawProjectiles(Graphics g) {
        for (EnemyProjectiles projectile : projectiles) {
            if(projectile.isExploding()) {
                projectileView.drawEggBroken(g, projectile, projectile.getAnimationFrame());
            } else {
                projectileView.draw(g, projectile);
            }
        }
    }


    public List<EnemyProjectiles> getProjectiles() {
        return projectiles;
    }
    
    public void clear() {
    	projectiles.clear();
    }
    
    
}
