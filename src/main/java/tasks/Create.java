package tasks;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONObject;

import app.Main;
import criteria.CriteriaPanel;
import items.ItemsPanel;
import metadata.Metadata;

public final class Create {

    public static CriteriaPanel createCriterion(JSONObject data, JFrame frame) {
        
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        CriteriaPanel bubble = new CriteriaPanel();
        bubble.setAlignmentX(Component.LEFT_ALIGNMENT);
        bubble.initialize();

        // If loading from JSON
        if (data != null) {
            bubble.creationIndex = data.getInt("creationIndex");
            bubble.rename(data.getString("name"));
            bubble.setEnabledCriterion(data.getBoolean("enabled"));
            bubble.weightSlider.setValue((int)Math.round(data.getDouble("weight") * (Main.isCriteriaPrecise() ? 10 : 2)));
            bubble.valueField.setText(String.valueOf(data.getDouble("weight")));
        } else {
            bubble.creationIndex = ++Main.criteriaCreationCounter;
            bubble.rename("Criterion #" + bubble.creationIndex);
        }

        Main.criteriaList.add(bubble);
        bubble.setCriteriaList(Main.criteriaList);
        bubble.refreshExtraInfo();
        bubble.updateBackground();

        bubble.setOnEnabledChanged(() -> {
            if (Main.hideDisabledCriteria.isSelected()) {
                wrapper.setVisible(bubble.isEnabledCriterion());
            }

            for (ItemsPanel item : Main.itemsList) {
                item.refreshExtraInfo();
            }

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle();
        });

        bubble.setOnStructureChanged(() -> {
            for (ItemsPanel item : Main.itemsList) {
                item.refreshCriteria();
            }
            Main.rankingPanel.updateAllScores();

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle();
        });

        bubble.setOnCriteriaChanged(() -> {
            for (ItemsPanel item : Main.itemsList) {
                item.refreshCriteria();
            }
            Main.rankingPanel.updateAllScores();

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle();
        });

        bubble.setOnDelete(() -> {
            Main.criteriaList.remove(bubble);

            for (ItemsPanel item : Main.itemsList) {
                item.refreshCriteria();
            }

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle();
        });

        wrapper.add(bubble);
        wrapper.add(Box.createVerticalStrut(10));
        Main.criteriaRow.add(wrapper);
        Main.criteriaPanel.revalidate();
        Main.criteriaPanel.repaint();

        for (CriteriaPanel cp : Main.criteriaList) {
            cp.setOnStructureChanged(() -> {
                for (ItemsPanel item : Main.itemsList) {
                    item.refreshCriteria();
                }
                Main.rankingPanel.updateAllScores();

                Main.unsavedChanges = true;
                Main.projectDirty = true;
                Metadata.updateWindowTitle();
            });

            cp.setOnCriteriaChanged(() -> {
                for (ItemsPanel item : Main.itemsList) {
                    item.refreshCriteria();
                }
                Main.rankingPanel.updateAllScores();

                Main.unsavedChanges = true;
                Main.projectDirty = true;
                Metadata.updateWindowTitle();
            });
        }

        for (ItemsPanel item : Main.itemsList) {
            item.refreshCriteria();
            item.refreshExtraInfo();
        }

        for (CriteriaPanel cp : Main.criteriaList) {
            cp.refreshExtraInfo();
        }

        Main.rankingPanel.updateAllScores();

        Main.unsavedChanges = true;
        Main.projectDirty = true;
        Metadata.updateWindowTitle();

        return bubble;
    }

    public static ItemsPanel createItem(JSONObject data, JFrame frame) {
        
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        ItemsPanel bubble = new ItemsPanel(Main.criteriaList);
        bubble.initialize();
        bubble.setRankingPanel(Main.rankingPanel);
        bubble.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (data != null) {
            bubble.creationIndex = data.getInt("creationIndex");
            bubble.rename(data.getString("name"));
            bubble.setEnabledItem(data.getBoolean("enabled"));
            bubble.setCollapsed(data.getBoolean("collapsed"));
        } else {
            bubble.creationIndex = ++Main.itemsCreationCounter;
            bubble.rename("Item #" + bubble.creationIndex);
        }

        Main.itemsList.add(bubble);
        Main.rankingPanel.addItemRow(bubble);
        bubble.refreshExtraInfo();
        bubble.updateBackground();

        bubble.setOnEnabledChanged(() -> {
            if (Main.hideDisabledItems.isSelected()) {
                wrapper.setVisible(bubble.isEnabledItem());
            }

            for (CriteriaPanel cp : Main.criteriaList) {
                cp.refreshExtraInfo();
            }

            for (ItemsPanel item : Main.itemsList) {
                item.refreshExtraInfo();
            }

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle();
        });

        bubble.setOnScoresChanged(() -> {
            Main.rankingPanel.updateItemScore(bubble);
            Main.rankingPanel.updateItemRank(bubble);
            bubble.refreshExtraInfo();

            for (CriteriaPanel cp : Main.criteriaList) {
                cp.refreshExtraInfo();
            }

            for (ItemsPanel item : Main.itemsList) {
                item.refreshExtraInfo();
            }

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle();
        });

        bubble.setOnNameChanged(() -> {
            Main.rankingPanel.updateItemName(bubble);
            bubble.refreshExtraInfo();

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle();
        });

        bubble.setOnDelete(() -> {
            Main.itemsList.remove(bubble);
            Main.rankingPanel.removeItemRow(bubble);
            for (ItemsPanel ip : Main.itemsList) ip.refreshExtraInfo();

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle();
        });

        wrapper.add(bubble);
        wrapper.add(Box.createVerticalStrut(10));

        Main.itemsRow.add(wrapper);
        Main.itemsPanel.revalidate();
        Main.itemsPanel.repaint();

        for (CriteriaPanel cp : Main.criteriaList) {
            cp.refreshExtraInfo();
        }

        for (ItemsPanel item : Main.itemsList) {
            item.refreshExtraInfo();
        }

        Main.unsavedChanges = true;
        Main.projectDirty = true;
        Metadata.updateWindowTitle();

        return bubble;
    }
}