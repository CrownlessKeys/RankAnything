package app.initialize;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import app.Main;
import app.Settings;
import criteria.CriteriaPanel;
import items.ItemsPanel;
import metadata.Metadata;
import tasks.Create;
import tasks.Sort;

public final class ItemInit {
    public static void build() {
        Color itemsPanelColor = new Color(175, 205, 205);
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Items"));
        itemsPanel.setBackground(itemsPanelColor);
        itemsPanel.setOpaque(true);

        JScrollPane itemsScroll = new JScrollPane(itemsPanel);
        itemsScroll.getVerticalScrollBar().setUnitIncrement(16);
        itemsScroll.getHorizontalScrollBar().setUnitIncrement(16);

        // Row for Add Item button
            JButton addItemButton = new JButton("Add item");

            JPanel addItemRow = new JPanel();
            addItemRow.setBackground(itemsPanelColor);
            addItemRow.setLayout(new BoxLayout(addItemRow, BoxLayout.X_AXIS));
            addItemRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, addItemButton.getPreferredSize().height));

            // layout
            addItemRow.add(Box.createHorizontalGlue());
            addItemRow.add(addItemButton);
            addItemRow.add(Box.createHorizontalGlue());

        // Settings Row for precise sliders and hide disabled items toggles
            JCheckBox preciseToggleItems = new JCheckBox("Precise sliders");
            preciseToggleItems.setSelected(Settings.preciseSlidersItems); // default from settings
            preciseToggleItems.setBackground(itemsPanelColor);

            JCheckBox hideDisabledItems = new JCheckBox("Hide disabled");
            hideDisabledItems.setSelected(Settings.hideDisabledItems); // default from settings
            hideDisabledItems.setBackground(itemsPanelColor);

            JPanel itemsSettingsRow = new JPanel();
            itemsSettingsRow.setBackground(itemsPanelColor);
            itemsSettingsRow.setLayout(new BoxLayout(itemsSettingsRow, BoxLayout.X_AXIS));
            itemsSettingsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, preciseToggleItems.getPreferredSize().height));

            // layout
            itemsSettingsRow.add(Box.createHorizontalGlue());
            itemsSettingsRow.add(preciseToggleItems);
            itemsSettingsRow.add(Box.createHorizontalGlue());
            itemsSettingsRow.add(hideDisabledItems);
            itemsSettingsRow.add(Box.createHorizontalGlue());

        // Sorting Row for everything sorting-related
            String[] itemSortModes = {
                "Alphabetical",
                "By ranking",
                "By best score",
                "By worst score",
                "By variance",
                "By creation",
                "Disabled last"
            };

            JComboBox<String> itemSortDropdown = new JComboBox<>(itemSortModes);

            JCheckBox itemReverseCheck = new JCheckBox("Reverse");
            itemReverseCheck.setBackground(itemsPanelColor);

            JButton itemSortButton = new JButton("Sort");

            JPanel itemsSortRow = new JPanel();
            itemsSortRow.setLayout(new BoxLayout(itemsSortRow, BoxLayout.X_AXIS));
            itemsSortRow.setBackground(itemsPanelColor);

            // layout
            itemsSortRow.add(Box.createHorizontalGlue());
            itemsSortRow.add(itemSortDropdown);
            itemsSortRow.add(Box.createHorizontalStrut(9));
            itemsSortRow.add(itemReverseCheck);
            itemsSortRow.add(Box.createHorizontalStrut(9));
            itemsSortRow.add(itemSortButton);
            itemsSortRow.add(Box.createHorizontalGlue());

        // Items Header
            JPanel itemsHeader = new JPanel();
            itemsHeader.setLayout(new BoxLayout(itemsHeader, BoxLayout.Y_AXIS));
            itemsHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            itemsHeader.setBackground(itemsPanelColor);

            // layout
            itemsHeader.add(Box.createVerticalStrut(10));
            itemsHeader.add(itemsSortRow);
            itemsHeader.add(Box.createVerticalStrut(10));
            itemsHeader.add(itemsSettingsRow);
            itemsHeader.add(Box.createVerticalStrut(10));
            itemsHeader.add(addItemRow);
            itemsHeader.add(Box.createVerticalStrut(10));

            // add header to the panel
            itemsPanel.add(itemsHeader);
        // end

        preciseToggleItems.addActionListener(e -> {
            boolean precise = preciseToggleItems.isSelected();
            Main.updatePrecisionItems(precise);
        });

        itemSortButton.addActionListener(e -> {
            boolean reverse = itemReverseCheck.isSelected();
            String mode = (String)itemSortDropdown.getSelectedItem();

            switch (mode) {
                case "Alphabetical" -> Sort.sortItemsAlphabetically(reverse, itemsPanel);
                case "By ranking" -> Sort.sortItemsByRanking(reverse, itemsPanel, Main.rankingPanel);
                case "By best score" -> Sort.sortItemsByHighestScore(reverse, itemsPanel);
                case "By worst score" -> Sort.sortItemsByLowestScore(reverse, itemsPanel);
                case "By variance" -> Sort.sortItemsByScoreVariance(reverse, itemsPanel);
                case "By creation" -> Sort.sortItemsByCreationIndex(reverse, itemsPanel);
                case "Disabled last" -> Sort.sortItemsDisabledLast(reverse, itemsPanel);
            }

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle(Main.frame);
        });

        hideDisabledItems.addActionListener(e -> {
            boolean hide = hideDisabledItems.isSelected();
            Main.updateHideDisabledItems(hide);
        });

        addItemButton.addActionListener(e -> {
            Create.createItem(null, Main.frame);
        });

        // Attach callback to ALL criteria
        for (CriteriaPanel cp : Main.criteriaList) {

            // Structural changes: add, delete, reorder, enable/disable
            cp.setOnStructureChanged(() -> {
                for (ItemsPanel item : Main.itemsList) {
                    item.refreshCriteria();
                    item.refreshExtraInfo();
                }
                Main.rankingPanel.updateAllScores();
            });

            // Value changes: weight, name, number box
            cp.setOnCriteriaChanged(() -> {
                for (ItemsPanel item : Main.itemsList) {
                    item.refreshCriteria();
                    item.refreshExtraInfo();
                }
                Main.rankingPanel.updateAllScores(); 
            });
        }

        Main.itemsPanel = itemsPanel;
        Main.itemsScroll = itemsScroll;
        Main.preciseToggleItems = preciseToggleItems;
        Main.hideDisabledItems = hideDisabledItems;
    }
}