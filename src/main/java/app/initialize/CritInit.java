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
import metadata.Metadata;
import tasks.Create;
import tasks.Sort;

public final class CritInit {
    public static void build() {
        
        Color criteriaPanelColor = new Color(180, 200, 210);
        JPanel criteriaPanel = new JPanel();
        criteriaPanel.setLayout(new BoxLayout(criteriaPanel, BoxLayout.Y_AXIS));
        criteriaPanel.setBorder(BorderFactory.createTitledBorder("Criteria"));
        criteriaPanel.setBackground(criteriaPanelColor); 
        criteriaPanel.setOpaque(true);
        
        // Row for the Add Criterion Button
            JButton addCriterionButton = new JButton("Add Criterion");

            JPanel addCriterionRow = new JPanel();
            addCriterionRow.setBackground(criteriaPanelColor);
            addCriterionRow.setLayout(new BoxLayout(addCriterionRow, BoxLayout.X_AXIS));
            addCriterionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, addCriterionButton.getPreferredSize().height));

            // layout
            addCriterionRow.add(Box.createHorizontalGlue());
            addCriterionRow.add(addCriterionButton);
            addCriterionRow.add(Box.createHorizontalGlue());

        // Settings Row for precise sliders and hide disabled criteria toggles
            JCheckBox preciseToggleCriteria = new JCheckBox("Precise Sliders");
            preciseToggleCriteria.setSelected(false); // default coarse mode
            preciseToggleCriteria.setBackground(criteriaPanelColor);

            JCheckBox hideDisabledCriteria = new JCheckBox("Hide Disabled");
            hideDisabledCriteria.setBackground(criteriaPanelColor);

            JPanel criteriaSettingsRow = new JPanel();
            criteriaSettingsRow.setBackground(criteriaPanelColor);
            criteriaSettingsRow.setLayout(new BoxLayout(criteriaSettingsRow, BoxLayout.X_AXIS));
            criteriaSettingsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, preciseToggleCriteria.getPreferredSize().height));

            // layout
            criteriaSettingsRow.add(Box.createHorizontalGlue());
            criteriaSettingsRow.add(preciseToggleCriteria);
            criteriaSettingsRow.add(Box.createHorizontalGlue());
            criteriaSettingsRow.add(hideDisabledCriteria);
            criteriaSettingsRow.add(Box.createHorizontalGlue());

        // Sorting Row for everything sorting-related
            String[] criteriaSortModes = {
                "Alphabetical",
                "By Weight",
                "By Scores",
                "By Variance",
                "By Creation",
                "Disabled Last"
            };

            JComboBox<String> criteriaSortDropdown = new JComboBox<>(criteriaSortModes);

            JCheckBox criteriaReverseCheck = new JCheckBox("Reverse");
            criteriaReverseCheck.setBackground(criteriaPanelColor);

            JButton criteriaSortButton = new JButton("Sort");

            JPanel criteriaSortRow = new JPanel();
            criteriaSortRow.setLayout(new BoxLayout(criteriaSortRow, BoxLayout.X_AXIS));
            criteriaSortRow.setBackground(criteriaPanelColor);

            // layout
            criteriaSortRow.add(Box.createHorizontalGlue());
            criteriaSortRow.add(criteriaSortDropdown);
            criteriaSortRow.add(Box.createHorizontalStrut(9));
            criteriaSortRow.add(criteriaReverseCheck);
            criteriaSortRow.add(Box.createHorizontalStrut(9));
            criteriaSortRow.add(criteriaSortButton);
            criteriaSortRow.add(Box.createHorizontalGlue());

        // Criteria Header
            JPanel criteriaHeader = new JPanel();
            criteriaHeader.setLayout(new BoxLayout(criteriaHeader, BoxLayout.Y_AXIS));
            criteriaHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            criteriaHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            criteriaHeader.setBackground(criteriaPanelColor);

            // layout
            criteriaHeader.add(Box.createVerticalStrut(10));
            criteriaHeader.add(criteriaSortRow);
            criteriaHeader.add(Box.createVerticalStrut(10));
            criteriaHeader.add(criteriaSettingsRow);
            criteriaHeader.add(Box.createVerticalStrut(10));
            criteriaHeader.add(addCriterionRow);
            criteriaHeader.add(Box.createVerticalStrut(10));

            // add header to the panel
            criteriaPanel.add(criteriaHeader);

            preciseToggleCriteria.addActionListener(e -> {
                boolean precise = preciseToggleCriteria.isSelected();
                Main.updatePrecisionCriteria(precise);
            });

            criteriaSortButton.addActionListener(e -> {
                String mode = (String)criteriaSortDropdown.getSelectedItem();
                boolean reverse = criteriaReverseCheck.isSelected();

                switch (mode) {
                    case "Alphabetical" -> Sort.sortCriteriaAlphabetically(reverse, criteriaPanel);
                    case "By Weight" -> Sort.sortCriteriaByWeight(reverse, criteriaPanel);
                    case "By Scores" -> Sort.sortCriteriaByAverageScore(reverse, criteriaPanel);
                    case "By Variance" -> Sort.sortCriteriaByScoreVariance(reverse, criteriaPanel);
                    case "By Creation" -> Sort.sortCriteriaByCreationIndex(reverse, criteriaPanel);
                    case "Disabled Last" -> Sort.sortCriteriaDisabledLast(reverse, criteriaPanel);
                }

                Main.unsavedChanges = true;
                Main.projectDirty = true;
                Metadata.updateWindowTitle(Main.frame);
            });

            hideDisabledCriteria.addActionListener(e -> {
                boolean hide = hideDisabledCriteria.isSelected();
                Main.updateHideDisabledCriteria(hide);
            });

            addCriterionButton.addActionListener(e -> {
                Create.createCriterion(null, Main.frame);
            });

            JScrollPane criteriaScroll = new JScrollPane(criteriaPanel);
            criteriaScroll.getVerticalScrollBar().setUnitIncrement(16);
            criteriaScroll.getHorizontalScrollBar().setUnitIncrement(16);

            Main.criteriaPanel = criteriaPanel;
            Main.preciseToggleCriteria = preciseToggleCriteria;
            Main.hideDisabledCriteria = hideDisabledCriteria;
            Main.criteriaScroll = criteriaScroll;
        //end
    }
}