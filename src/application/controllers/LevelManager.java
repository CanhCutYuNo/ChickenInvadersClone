package application.controllers;

import java.util.ArrayList;

import application.models.Enemy;

public abstract class LevelManager {

    protected ArrayList<Enemy> enemies;

    public LevelManager() {
        enemies = new ArrayList<>();
        //initEnemies();
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    protected abstract void initEnemies();
}