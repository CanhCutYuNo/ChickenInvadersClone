package application.controllers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import application.models.Items;
import application.views.ItemsView;

public class ItemsController {
    private List<Items> items;  // Danh sách vật phẩm thay vì một vật phẩm duy nhất
    private ItemsView itemsView;

    public ItemsController(String path) {
        items = new ArrayList<>();
        itemsView = new ItemsView(path);
    }

    // Thêm vật phẩm vào danh sách
    public void addItem(int x, int y, int damage) {
        items.add(new Items(x, y, damage));
//        System.out.println("Item added: ");
    }

    // Cập nhật danh sách vật phẩm
    public void updateItems() {
        Iterator<Items> iterator = items.iterator();
        while (iterator.hasNext()) {
            Items item = iterator.next();
            item.update();

            // Nếu vật phẩm ra khỏi màn hình, xóa khỏi danh sách
            if (item.isOffScreen()) {
                iterator.remove();
            }
        }
    }

    // Vẽ tất cả vật phẩm lên màn hình
    public void drawItems(Graphics g) {
        for (Items item : items) {
            itemsView.draw(g, item);
        }
    }

    // Trả về iterator hợp lệ để duyệt danh sách vật phẩm
    public Iterator<Items> iterator() {
        return items.iterator();
    }

//    // Thêm vật phẩm vào danh sách tại vị trí của kẻ địch
//    public void spawnItem(Enemy enemy) {
//        if (enemy != null) {
//            items.add(new Items(enemy.getPosX(), enemy.getPosY(), 10));  // 10 là giá trị mặc định, có thể thay đổi
//        }
//    }
}
