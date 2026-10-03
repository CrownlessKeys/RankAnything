package saveload;

import app.Main;

final class Clear {

    static void clearCriteriaPanel() {
        Main.criteriaRow.removeAll();
        Main.criteriaRow.revalidate();
        Main.criteriaRow.repaint();
    }

    static void clearItemsPanel() {
        Main.itemsRow.removeAll();
        Main.itemsRow.revalidate();
        Main.itemsRow.repaint();
    }
}