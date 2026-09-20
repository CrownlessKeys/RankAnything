package tasks;

import app.Main;
import criteria.CriteriaPanel;
import items.ItemsPanel;

public class SelectAll {
    public static void enableAllCriteria(boolean enabled) {
        for (CriteriaPanel cp : Main.criteriaList) {
            cp.setEnabledCriterion(enabled);
        }
    }

    public static void enableAllItems(boolean enabled) {
        for (ItemsPanel item : Main.itemsList) {
            item.setEnabledItem(enabled);
        }
    }

    public static void collapseAllItems(boolean collapsed) {
        for (ItemsPanel item : Main.itemsList) {
            item.setCollapsed(collapsed);
        }
    }
}