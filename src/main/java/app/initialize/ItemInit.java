package app.initialize;

import java.awt.Color;
import java.awt.Component;
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
import tasks.SelectAll;
import tasks.Sort;

public final class ItemInit {
    private static boolean disabled = false; // used for whether button is "disable all" or "enable all"
    private static boolean collapsed = false; // used for whether button is "collapse all" or "expand all"

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

        // row for the items
            JPanel itemsRow = new JPanel();
            itemsRow.setLayout(new BoxLayout(itemsRow, BoxLayout.Y_AXIS));
            itemsRow.setBackground(itemsPanelColor);
            itemsRow.setOpaque(true);
            itemsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Row for Add Item button
            JButton addItemButton = new JButton("Add item");

            JPanel addItemRow = new JPanel();
            addItemRow.setBackground(itemsPanelColor);
            addItemRow.setLayout(new BoxLayout(addItemRow, BoxLayout.X_AXIS));
            addItemRow.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 
                addItemButton.getPreferredSize().height
            ));
            addItemRow.setAlignmentX(Component.LEFT_ALIGNMENT);

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
            itemsSettingsRow.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 
                preciseToggleItems.getPreferredSize().height
            ));
            itemsSettingsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

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
            itemsSortRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            // layout
            itemsSortRow.add(Box.createHorizontalGlue());
            itemsSortRow.add(itemSortDropdown);
            itemsSortRow.add(Box.createHorizontalStrut(9));
            itemsSortRow.add(itemReverseCheck);
            itemsSortRow.add(Box.createHorizontalStrut(9));
            itemsSortRow.add(itemSortButton);
            itemsSortRow.add(Box.createHorizontalGlue());

        // Row for Select All buttons
            JButton disableAllButton = new JButton("Disable all");
            JButton collapseAllButton = new JButton("Collapse all");

            JPanel selectAllRow = new JPanel();
            selectAllRow.setBackground(itemsPanelColor);
            selectAllRow.setLayout(new BoxLayout(selectAllRow, BoxLayout.X_AXIS));
            selectAllRow.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 
                disableAllButton.getPreferredSize().height
            ));
            selectAllRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            // layout
            selectAllRow.add(Box.createHorizontalGlue());
            selectAllRow.add(disableAllButton);
            selectAllRow.add(Box.createHorizontalGlue());
            selectAllRow.add(collapseAllButton);
            selectAllRow.add(Box.createHorizontalGlue());

        // Items Header
            JPanel itemsHeader = new JPanel();
            itemsHeader.setLayout(new BoxLayout(itemsHeader, BoxLayout.Y_AXIS));
            int height = 10
                    + itemsSortRow.getPreferredSize().height + 10
                    + itemsSettingsRow.getPreferredSize().height + 10
                    + selectAllRow.getPreferredSize().height + 10;
            itemsHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
            itemsHeader.setBackground(itemsPanelColor);
            itemsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

            // layout
            itemsHeader.add(Box.createVerticalStrut(10));
            itemsHeader.add(itemsSortRow);
            itemsHeader.add(Box.createVerticalStrut(10));
            itemsHeader.add(itemsSettingsRow);
            itemsHeader.add(Box.createVerticalStrut(10));
            itemsHeader.add(selectAllRow);
            itemsHeader.add(Box.createVerticalStrut(10));

        // layout
            itemsPanel.add(itemsHeader);
            itemsPanel.add(itemsRow);
            itemsPanel.add(addItemRow);
            itemsPanel.add(Box.createVerticalStrut(50));
        // end

        preciseToggleItems.addActionListener(e -> {
            boolean precise = preciseToggleItems.isSelected();
            Main.updatePrecisionItems(precise);
        });

        itemSortButton.addActionListener(e -> {
            boolean reverse = itemReverseCheck.isSelected();
            String mode = (String)itemSortDropdown.getSelectedItem();

            switch (mode) {
                case "Alphabetical" -> Sort.sortItemsAlphabetically(reverse, itemsRow);
                case "By ranking" -> Sort.sortItemsByRanking(reverse, itemsRow, Main.rankingPanel);
                case "By best score" -> Sort.sortItemsByHighestScore(reverse, itemsRow);
                case "By worst score" -> Sort.sortItemsByLowestScore(reverse, itemsRow);
                case "By variance" -> Sort.sortItemsByScoreVariance(reverse, itemsRow);
                case "By creation" -> Sort.sortItemsByCreationIndex(reverse, itemsRow);
                case "Disabled last" -> Sort.sortItemsDisabledLast(reverse, itemsRow);
            }

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle();
        });

        hideDisabledItems.addActionListener(e -> {
            boolean hide = hideDisabledItems.isSelected();
            Main.updateHideDisabledItems(hide);
        });

        addItemButton.addActionListener(e -> {
            Create.createItem(null);
        });

        disableAllButton.addActionListener(e -> {
            SelectAll.enableAllItems(disabled);
            disabled = !disabled;
            disableAllButton.setText(disabled ? "Enable all" : "Disable all");
        });

        collapseAllButton.addActionListener(e -> {
            SelectAll.collapseAllItems(!collapsed);
            collapseAllButton.setText(collapsed ? "Collapse all" : "Expand all");
            collapsed = !collapsed;
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
        Main.itemsRow = itemsRow;
    }
}