package application.controllers.enemy.items;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import application.models.enemy.Items;
import application.views.enemy.ItemsView;

public class ItemsController implements IItemsController {
    private List<Items> items;
    private ItemsView itemsView;

    public ItemsController(String path) {
        items = new ArrayList<>();
        itemsView = new ItemsView(path);
    }
    
    @Override
    public void addItem(int x, int y, int damage) {
        items.add(new Items(x, y, damage));
    }

    @Override
    public void updateItems() {
        Iterator<Items> iterator = items.iterator();
        while (iterator.hasNext()) {
            Items item = iterator.next();
            item.update();
            if (item.isOffScreen()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void drawItems(Graphics g) {
        for (Items item : items) {
            itemsView.draw(g, item);
        }
    }

    @Override
    public Iterator<Items> iterator() {
        return items.iterator();
    }
}