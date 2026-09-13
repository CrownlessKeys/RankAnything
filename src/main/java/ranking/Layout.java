package ranking;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import app.Main;
import metadata.Metadata;
import ranking.RankingPanel.RankingMode;

final class Layout {
    private Layout() {
    }

    static void build(RankingPanel panel) {

        JCheckBox unweightedToggle = new JCheckBox("Unweighted");
        JCheckBox allCriteriaToggle = new JCheckBox("Consider disabled criteria");
        JCheckBox allItemsToggle = new JCheckBox("Consider disabled items");

        unweightedToggle.setBackground(panel.bg);
        allCriteriaToggle.setBackground(panel.bg);
        allItemsToggle.setBackground(panel.bg);

        // initial state based on current rankingMode
        Mode.applyModeToToggles(panel.rankingMode, unweightedToggle, allCriteriaToggle, allItemsToggle);

        // when any toggle changes, recompute mode
        ActionListener listener = e -> {
            RankingMode mode = Mode.computeMode(
                unweightedToggle.isSelected(),
                allCriteriaToggle.isSelected(),
                allItemsToggle.isSelected()
            );

            panel.rankingMode = mode;
            panel.updateAllScores();

            Main.unsavedChanges = true;
            Main.projectDirty = true;
            Metadata.updateWindowTitle(Main.frame);
        };

        unweightedToggle.addActionListener(listener);
        allCriteriaToggle.addActionListener(listener);
        allItemsToggle.addActionListener(listener);

        JPanel unweightedRow = new JPanel();
        unweightedRow.setLayout(new BoxLayout(unweightedRow, BoxLayout.X_AXIS));
        unweightedRow.setBackground(panel.bg);

        // layout
        unweightedRow.add(Box.createHorizontalGlue());
        unweightedRow.add(unweightedToggle);
        unweightedRow.add(Box.createHorizontalGlue());

        JPanel allCriteriaRow = new JPanel();
        allCriteriaRow.setLayout(new BoxLayout(allCriteriaRow, BoxLayout.X_AXIS));
        allCriteriaRow.setBackground(panel.bg);

        // layout
        allCriteriaRow.add(Box.createHorizontalGlue());
        allCriteriaRow.add(allCriteriaToggle);
        allCriteriaRow.add(Box.createHorizontalGlue());

        JPanel allItemsRow = new JPanel();
        allItemsRow.setLayout(new BoxLayout(allItemsRow, BoxLayout.X_AXIS));
        allItemsRow.setBackground(panel.bg);

        // layout
        allItemsRow.add(Box.createHorizontalGlue());
        allItemsRow.add(allItemsToggle);
        allItemsRow.add(Box.createHorizontalGlue());

        // store references on panel
        panel.unweightedToggle = unweightedToggle;
        panel.allCriteriaToggle = allCriteriaToggle;
        panel.allItemsToggle = allItemsToggle;

        // header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        // layout
        header.add(Box.createVerticalStrut(12));
        header.add(unweightedRow);
        header.add(Box.createVerticalStrut(12));
        header.add(allCriteriaRow);
        header.add(Box.createVerticalStrut(12));
        header.add(allItemsRow);

        // Create a vertical container for both the header and the ranking content
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setOpaque(false);

        // layout
        topContainer.add(header);
        topContainer.add(Box.createVerticalStrut(10));
        topContainer.add(panel.contentPanel);

        panel.add(topContainer, BorderLayout.PAGE_START);
    }
}