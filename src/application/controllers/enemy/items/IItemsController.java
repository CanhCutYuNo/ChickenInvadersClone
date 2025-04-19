package application.controllers.enemy.items;

import java.awt.Graphics;
import java.util.Iterator;

import application.models.enemy.Items;

public interface IItemsController {
    void updateItems();
    void drawItems(Graphics g);
    void addItem(int x, int y, int damage);
    Iterator<Items> iterator();
}