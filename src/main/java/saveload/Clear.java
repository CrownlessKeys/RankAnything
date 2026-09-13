package saveload;

import app.Main;

final class Clear {
    static void clearCriteriaPanel() {
        while (Main.criteriaPanel.getComponentCount() > 1) {
            Main.criteriaPanel.remove(1);
        }
        Main.criteriaPanel.revalidate();
        Main.criteriaPanel.repaint();
    }

    static void clearItemsPanel() {
        while (Main.itemsPanel.getComponentCount() > 1) {
            Main.itemsPanel.remove(1);
        }
        Main.itemsPanel.revalidate();
        Main.itemsPanel.repaint();
    }
}
