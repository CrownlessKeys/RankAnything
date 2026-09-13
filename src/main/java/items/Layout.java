package items;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import app.Main;
import metadata.Metadata;

final class Layout {
    private Layout() {
    }

    static void build(ItemsPanel item) {
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        item.setBackground(item.activated);
        item.setOpaque(true);

        JLabel label = new JLabel("Scores:");

        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));
        leftColumn.setOpaque(false);
        leftColumn.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel dragHandle = new JLabel("⋮⋮");
        dragHandle.setFont(new Font("SansSerif", Font.BOLD, 18));
        dragHandle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        dragHandle.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftColumn.add(Box.createVerticalGlue());
        leftColumn.add(dragHandle);
        leftColumn.add(Box.createVerticalGlue());

        Drag.install(item, dragHandle);

        JPanel mainRow = new JPanel();
        mainRow.setLayout(new BoxLayout(mainRow, BoxLayout.X_AXIS));
        mainRow.setOpaque(false);
        mainRow.add(leftColumn);
        mainRow.add(Box.createHorizontalStrut(10));

        item.headerRow = new JPanel();
        item.headerRow.setLayout(new BoxLayout(item.headerRow, BoxLayout.X_AXIS));
        item.headerRow.setBackground(item.activated);

        ImageIcon trashIcon = new ImageIcon("src/main/resources/iconmonstr-trash-can-filled.png");
        JButton deleteButton = new JButton(trashIcon);
        deleteButton.setPreferredSize(new Dimension(24, 24));
        deleteButton.setMaximumSize(new Dimension(24, 24));
        deleteButton.setMinimumSize(new Dimension(24, 24));
        deleteButton.setBorder(BorderFactory.createEmptyBorder());
        deleteButton.setContentAreaFilled(false);
        deleteButton.setFocusPainted(false);

        item.enabledCheck = new JCheckBox();
        item.enabledCheck.setSelected(true);
        item.enabledCheck.setBackground(item.activated);

        item.collapseButton = new JButton("▼");
        item.collapseButton.setPreferredSize(new Dimension(24, 24));
        item.collapseButton.setMaximumSize(new Dimension(24, 24));
        item.collapseButton.setBorder(BorderFactory.createEmptyBorder());
        item.collapseButton.setContentAreaFilled(false);
        item.collapseButton.setFocusPainted(false);

        item.headerRow.add(item.enabledCheck);
        item.headerRow.add(item.collapseButton);
        item.headerRow.add(Box.createHorizontalStrut(8));
        item.headerRow.add(item.nameField);
        item.headerRow.add(Box.createHorizontalGlue());
        item.headerRow.add(deleteButton);

        item.headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, item.headerRow.getPreferredSize().height));
        item.headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, item.nameField.getPreferredSize().height));
        item.nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        deleteButton.addActionListener(e -> {
            deleteItem(item);
        });

        Scores.buildInitialScores(item);

        // initialize extra info section and return extraInfoToggleRow 
        JPanel extraInfoToggleRow = extraInfo(item);

        JPanel contentColumn = new JPanel();
        contentColumn.setLayout(new BoxLayout(contentColumn, BoxLayout.Y_AXIS));
        contentColumn.setOpaque(false);
        contentColumn.setAlignmentY(Component.TOP_ALIGNMENT);
        contentColumn.add(item.headerRow);

        item.collapsible = new JPanel();
        item.collapsible.setLayout(new BoxLayout(item.collapsible, BoxLayout.Y_AXIS));
        item.collapsible.setOpaque(false);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.scoresColumn.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.collapsible.setAlignmentX(Component.LEFT_ALIGNMENT);

        item.collapsible.add(label);
        item.collapsible.add(Box.createVerticalStrut(10));
        item.collapsible.add(item.scoresColumn);
        item.collapsible.add(item.extraInfoPanel);
        item.collapsible.add(extraInfoToggleRow);

        contentColumn.add(item.collapsible);
        mainRow.add(contentColumn);
        item.add(mainRow);

        item.enabledCheck.addActionListener(e -> item.updateBackground());

        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        item.collapseButton.addActionListener(e -> {
            item.collapsed = !item.collapsed;
            item.collapsible.setVisible(!item.collapsed);
            item.collapseButton.setText(item.collapsed ? "▲" : "▼");
            item.revalidate();
            item.repaint();

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle(Main.frame);
        });

        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    private static JPanel extraInfo(ItemsPanel item) {

        item.extraInfoToggleLabel = new JLabel("More...");
        item.extraInfoToggleLabel.setForeground(Color.GRAY);
        item.extraInfoToggleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.extraInfoToggleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        item.extraInfoPanel = new JPanel();
        item.extraInfoPanel.setLayout(new BoxLayout(item.extraInfoPanel, BoxLayout.Y_AXIS));
        item.extraInfoPanel.setOpaque(false);
        item.extraInfoPanel.setVisible(false);
        item.extraInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel extraInfoToggleRow = new JPanel();
        extraInfoToggleRow.setLayout(new BoxLayout(extraInfoToggleRow, BoxLayout.X_AXIS));
        extraInfoToggleRow.setOpaque(false);
        extraInfoToggleRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        extraInfoToggleRow.add(Box.createHorizontalGlue());
        extraInfoToggleRow.add(item.extraInfoToggleLabel);

        item.extraInfoPanel.add(Box.createVerticalStrut(10));

        item.infoName = new JLabel();
        item.infoName.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoName);

        item.infoWeighted = new JLabel();
        item.infoWeighted.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoWeighted);

        item.infoRank = new JLabel();
        item.infoRank.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoRank);

        item.infoVariance = new JLabel();
        item.infoVariance.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoVariance);

        item.infoCreation = new JLabel();
        item.infoCreation.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoCreation);

        item.extraInfoPanel.add(Box.createVerticalStrut(10));

        item.infoUnweighted = new JLabel();
        item.infoUnweighted.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoUnweighted);

        item.infoUnweightedRank = new JLabel();
        item.infoUnweightedRank.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoUnweightedRank);

        item.extraInfoPanel.add(Box.createVerticalStrut(10));

        JLabel allCriteriaHeader = new JLabel("If all criteria were enabled:");
        allCriteriaHeader.setForeground(Color.GRAY);
        item.extraInfoPanel.add(allCriteriaHeader);

        item.infoWeightedAllCriteria = new JLabel();
        item.infoWeightedAllCriteria.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoWeightedAllCriteria);

        item.infoRankAllCriteria = new JLabel();
        item.infoRankAllCriteria.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoRankAllCriteria);

        item.infoUnweightedAllCriteria = new JLabel();
        item.infoUnweightedAllCriteria.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoUnweightedAllCriteria);

        item.infoUnweightedRankAllCriteria = new JLabel();
        item.infoUnweightedRankAllCriteria.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoUnweightedRankAllCriteria);

        item.extraInfoPanel.add(Box.createVerticalStrut(10));

        JLabel allItemsHeader = new JLabel("If all items were enabled:");
        allItemsHeader.setForeground(Color.GRAY);
        item.extraInfoPanel.add(allItemsHeader);

        item.infoRankAllItems = new JLabel();
        item.infoRankAllItems.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoRankAllItems);

        item.infoUnweightedRankAllItems = new JLabel();
        item.infoUnweightedRankAllItems.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoUnweightedRankAllItems);

        item.extraInfoPanel.add(Box.createVerticalStrut(10));
        
        JLabel allCriteriaAllItemsHeader = new JLabel("If all items AND criteria were enabled:");
        allCriteriaAllItemsHeader.setForeground(Color.GRAY);
        item.extraInfoPanel.add(allCriteriaAllItemsHeader);

        item.infoRankAllCriteriaAllItems = new JLabel();
        item.infoRankAllCriteriaAllItems.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoRankAllCriteriaAllItems);

        item.infoUnweightedRankAllCriteriaAllItems = new JLabel();
        item.infoUnweightedRankAllCriteriaAllItems.setForeground(Color.DARK_GRAY);
        item.extraInfoPanel.add(item.infoUnweightedRankAllCriteriaAllItems);

        ExtraInfo.initialize(item);

        return extraInfoToggleRow;
    }

    private static void deleteItem(ItemsPanel item) {
        JPanel wrapper = (JPanel) item.getParent();
        JPanel column = (JPanel) wrapper.getParent();

        final int frames = 8;
        final int delay = 8;
        final int fullOffset = wrapper.getHeight();
        final Timer[] timerRef = new Timer[1];

        timerRef[0] = new Timer(delay, new ActionListener() {
            int step = 0;

            @Override
            public void actionPerformed(ActionEvent evt) {
                step++;
                int offset = (int)(fullOffset * (step / (float)frames));
                wrapper.setBorder(BorderFactory.createEmptyBorder(-offset, 0, 0, 0));
                wrapper.revalidate();
                wrapper.repaint();

                if (step >= frames) {
                    timerRef[0].stop();
                    column.remove(wrapper);
                    column.revalidate();
                    column.repaint();

                    if (item.onDelete != null) item.onDelete.run();
                }
            }
        });

        timerRef[0].start();
    }
}