package app;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

final class DefaultSettings {

    static void show(JFrame frame) {

        JCheckBox preciseSlidersCriteria = new JCheckBox(
            "Precise Sliders (Criteria)",
            Settings.preciseSlidersCriteria
        );
        preciseSlidersCriteria.setToolTipText(
            "Decide whether criterion weightings default to using precise sliders (increments of 0.1 rather than 0.5)"
        );

        JCheckBox preciseSlidersItems = new JCheckBox(
            "Precise Sliders (Items)",
            Settings.preciseSlidersItems
        );
        preciseSlidersItems.setToolTipText(
            "Decide whether item scores default to using precise sliders (increments of 1% rather than 5%)"
        );

        JCheckBox hideDisabledCriteria = new JCheckBox(
            "Hide disabled criteria",
            Settings.hideDisabledCriteria
        );
        hideDisabledCriteria.setToolTipText(
            "Decide whether disabled crtieria should be hidden by default"
        );

        JCheckBox hideDisabledItems = new JCheckBox(
            "Hide disabled items",
            Settings.hideDisabledItems
        );
        hideDisabledItems.setToolTipText(
            "Decide whether disabled items should be hidden by default"
        );

        JCheckBox weighted = new JCheckBox(
            "Weighted Rankings",
            Main.rankingPanel.isWeighted(Settings.rankingMode)
        );
        weighted.setToolTipText(
            "Decide whether rankings are weighted by default"
        );

        JCheckBox allCriteria = new JCheckBox(
            "Consider disabled criteria",
            Main.rankingPanel.isAllCriteria(Settings.rankingMode)
        );
        allCriteria.setToolTipText(
            "Decide whether rankings consider disabled criteria by default"
        );

        JCheckBox allItems = new JCheckBox(
            "Consider disabled items",
            Main.rankingPanel.isAllItems(Settings.rankingMode)
        );
        allItems.setToolTipText(
            "Decide whether rankings consider disabled items by default"
        );

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(preciseSlidersCriteria);
        panel.add(preciseSlidersItems);
        panel.add(Box.createVerticalStrut(10));
        panel.add(hideDisabledCriteria);
        panel.add(hideDisabledItems);
        panel.add(Box.createVerticalStrut(10));
        panel.add(weighted);
        panel.add(allCriteria);
        panel.add(allItems);

        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Default Project Settings",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            Settings.preciseSlidersCriteria = preciseSlidersCriteria.isSelected();
            Settings.preciseSlidersItems = preciseSlidersItems.isSelected();
            Settings.hideDisabledCriteria = hideDisabledCriteria.isSelected();
            Settings.hideDisabledItems = hideDisabledItems.isSelected();
            Settings.rankingMode = Main.rankingPanel.computeMode(
                weighted.isSelected(),
                allCriteria.isSelected(),
                allItems.isSelected()
            );

            MenuBar.updateSettingsMenu();
            Settings.save();
        }
    }
}