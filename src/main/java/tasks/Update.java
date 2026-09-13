package tasks;

import java.awt.Component;

import javax.swing.JPanel;

import app.Main;
import criteria.CriteriaPanel;
import items.ItemsPanel;
import metadata.Metadata;

public final class Update {

    public static void updatePrecisionCriteria(boolean precise) {

        // Update criteria sliders
        for (CriteriaPanel cp : Main.criteriaList) {
            cp.updatePrecision(precise);
        }

        Main.preciseToggleCriteria.setSelected(precise);
    }

    public static void updatePrecisionItems(boolean precise) {

        // Update item sliders
        for (ItemsPanel item : Main.itemsList) {
            item.updatePrecision(precise);
        }

        Main.preciseToggleItems.setSelected(precise);
    }

    public static void updateHideDisabledCriteria(boolean hide) {

        for (Component c : Main.criteriaPanel.getComponents()) {
            if (c instanceof JPanel wrapper &&
                wrapper.getComponentCount() > 0 &&
                wrapper.getComponent(0) instanceof CriteriaPanel cp) {

                wrapper.setVisible(!hide || cp.isEnabledCriterion());
            }
        }

        Main.criteriaPanel.revalidate();
        Main.criteriaPanel.repaint();

        Main.hideDisabledCriteria.setSelected(hide);

        Main.unsavedChanges = true;
        Main.projectDirty = true;
        Metadata.updateWindowTitle(Main.frame);
    }

    public static void updateHideDisabledItems(boolean hide) {

        for (Component c : Main.itemsPanel.getComponents()) {
            if (c instanceof JPanel wrapper &&
                wrapper.getComponentCount() > 0 &&
                wrapper.getComponent(0) instanceof ItemsPanel ip) {

                wrapper.setVisible(!hide || ip.isEnabledItem());
            }
        }

        Main.itemsPanel.revalidate();
        Main.itemsPanel.repaint();

        Main.hideDisabledItems.setSelected(hide);

        Main.unsavedChanges = true;
        Main.projectDirty = true;
        Metadata.updateWindowTitle(Main.frame);
    }    
}