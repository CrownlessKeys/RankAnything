package tasks;

import app.Main;
import criteria.CriteriaPanel;
import items.ItemsPanel;

public final class Count {
    public static int countEnabledItems() {
        int count = 0;
        for (ItemsPanel item : Main.itemsList) {
            if (item.isEnabledItem()) count++;
        }
        return count;
    }
    
    public static int countEnabledCriteria() {
        int count = 0;
        for (CriteriaPanel cp : Main.criteriaList) {
            if (cp.isEnabledCriterion()) count++;
        }
        return count;
    }
}