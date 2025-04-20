package application.controllers.enemy.items;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import application.models.enemy.Items;
import application.views.enemy.ItemsView;

public class ItemsController {
    private List<ItemUnit> itemUnits;
    private boolean atomDropped = false;


    public ItemsController(String path) {
        itemUnits = new ArrayList<>();
        atomDropped = false;
    }

    public void addItem(int x, int y, int damage, Items.ItemType type) {
        Items item = new Items(x, y, damage, type);
        itemUnits.add(new ItemUnit(item));
    }

    public void updateItems() {
        Iterator<ItemUnit> iterator = itemUnits.iterator();
        while (iterator.hasNext()) {
            ItemUnit unit = iterator.next();
            unit.update();
            if (unit.isOffScreen()) {
                iterator.remove();
            }
        }
    }

    public void drawItems(Graphics g) {
        for (ItemUnit unit : itemUnits) {
            unit.draw(g);
        }
    }

    public Iterator<Items> iterator() {
        List<Items> itemsOnly = new ArrayList<>();
        for (ItemUnit unit : itemUnits) {
            itemsOnly.add(unit.model);
        }
        return itemsOnly.iterator();
    }


    public boolean hasDroppedAtom() {
        return atomDropped;
    }

    public void markAtomDropped() {
        atomDropped = true;
    }

    public void resetAtomDropped() {
        atomDropped = false;
    }


    public class ItemUnit {
        public Items model;
        public ItemsView view;

        public ItemUnit(Items model) {
            this.model = model;
            this.view = new ItemsView(model);
        }

        public void update() {
            model.update();
        }

        public void draw(Graphics g) {
            view.draw(g, model);
        }

        public boolean isOffScreen() {
            return model.isOffScreen();
        }
    }
    public List<ItemUnit> getItemUnits() {
        return itemUnits;
    }
    public void removeItems(List<ItemUnit> units) {
        itemUnits.removeAll(units);
    }


}