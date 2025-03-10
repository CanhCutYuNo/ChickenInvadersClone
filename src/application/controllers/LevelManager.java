package application.controllers;

import application.models.*;
import application.models.types.ChickEnemy;
import application.views.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public abstract class LevelManager {

    protected ArrayList<Enemy> enemies;

    public LevelManager() {
        enemies = new ArrayList<>();
        initEnemies();
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    protected abstract void initEnemies();
}
