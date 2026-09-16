package autosave;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import app.Main;
import app.MenuBar;
import app.Settings;
import autosave.AutoBrowser.AutosaveRendererMode;

final class AutoSettings {

    static void show(JFrame frame) {

        JCheckBox enableAutosave = new JCheckBox(
            "Enable Autosave",
            Settings.autosaveOnClose || Settings.autosavePeriodically
        );
        enableAutosave.setToolTipText(
            "Enable or disable the automatic creation of backup save files of your project. This will affect both types of autosaves below."
        );

        // Convert stored seconds into minutes + seconds
        int storedMinutes = Settings.autosaveIntervalSeconds / 60;
        int storedSeconds = Settings.autosaveIntervalSeconds % 60;

        String intervalTooltip = "Set the interval at which backup save files of your project are automatically created.";

        JLabel intervalLabel = new JLabel("Autosave Interval:");
        intervalLabel.setToolTipText(intervalTooltip);

        // Minutes spinner
        JSpinner minutesSpinner = new JSpinner(
            new SpinnerNumberModel(storedMinutes, 0, 120, 1)
        );
        JLabel minutesLabel = new JLabel("minutes");
        minutesLabel.setToolTipText(intervalTooltip);
        minutesSpinner.setToolTipText(intervalTooltip);

        // Seconds spinner
        JSpinner secondsSpinner = new JSpinner(
            new SpinnerNumberModel(storedSeconds, 0, 59, 1)
        );
        JLabel secondsLabel = new JLabel("seconds");
        secondsLabel.setToolTipText(intervalTooltip);
        secondsSpinner.setToolTipText(intervalTooltip);

        // Max autosaves per project
        JSpinner maxPerProjectSpinner = new JSpinner(
            new SpinnerNumberModel(
                Settings.autosaveMaxPerProject,     // default
                1,
                500,
                1
            )
        );
        JCheckBox maxPerProjectUnlimited = new JCheckBox("Unlimited");
        maxPerProjectUnlimited.setSelected(Settings.autosaveUnlimitedPerProject);
        String maxPerProjectTooltip = "Set the maximum number of backup save files to keep for each project. The oldest autosaves will be deleted when this limit is exceeded.";
        JLabel maxPerProjectLabel = new JLabel("Max Autosaves Per Project:");
        maxPerProjectSpinner.setToolTipText(maxPerProjectTooltip);
        maxPerProjectUnlimited.setToolTipText(maxPerProjectTooltip);
        maxPerProjectLabel.setToolTipText(maxPerProjectTooltip);

        // Max autosaves total
        JSpinner maxTotalSpinner = new JSpinner(
            new SpinnerNumberModel(
                Settings.autosaveMaxTotal,    // default
                1,
                5000,
                1
            )
        );
        JCheckBox maxTotalUnlimited = new JCheckBox("Unlimited");
        maxTotalUnlimited.setSelected(Settings.autosaveUnlimitedTotal);
        String maxTotalTooltip = "Set the maximum number of backup save files to keep in total. The oldest autosaves will be deleted when this limit is exceeded.";
        JLabel maxTotalLabel = new JLabel("Max Autosaves Total:");
        maxTotalSpinner.setToolTipText(maxTotalTooltip);
        maxTotalUnlimited.setToolTipText(maxTotalTooltip);
        maxTotalLabel.setToolTipText(maxTotalTooltip);

        JCheckBox autosaveOnClose = new JCheckBox(
            "Autosave on Project Close",
            Settings.autosaveOnClose
        );
        autosaveOnClose.setToolTipText(
            "Enable or disable the automatic creation of backup save files of your project when closing it with unsaved changes."
        );

        JCheckBox autosavePeriodically = new JCheckBox(
            "Autosave Periodically",
            Settings.autosavePeriodically
        );
        autosavePeriodically.setToolTipText(
            "Enable or disable the automatic creation of backup save files of your project periodically (see below)."
        );

        JButton viewAutosaves = new JButton("View All Autosaves");
        viewAutosaves.addActionListener(e -> AutoBrowser.show(
            frame, 
            null, 
            AutosaveRendererMode.ALL_AUTOSAVES
        ));
        viewAutosaves.setToolTipText("View an interactive list of all autosave files for all projects.");

        JButton openFolder = new JButton("Open Autosave Folder");
        openFolder.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(Main.AUTOSAVE_DIR);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        openFolder.setToolTipText("Open the folder where all autosave files are stored.");

        // layout
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6, 6, 6, 6);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;

            int row = 0;

            // Enable autosave
            c.gridx = 0; c.gridy = row++;
            panel.add(enableAutosave, c);

            // autosave types
            c.gridx = 0; c.gridy = row++;
            panel.add(autosaveOnClose, c);

            JPanel autosavePeriodicallyPanel = new JPanel();
            autosavePeriodicallyPanel.add(autosavePeriodically);

            c.gridx = 1;
            panel.add(autosavePeriodicallyPanel, c);

            // Autosave interval row
            c.gridx = 0; c.gridy = row;
            panel.add(intervalLabel, c);

            JPanel intervalPanel = new JPanel();
            intervalPanel.add(minutesSpinner);
            intervalPanel.add(minutesLabel);
            intervalPanel.add(secondsSpinner);
            intervalPanel.add(secondsLabel);

            c.gridx = 1;
            panel.add(intervalPanel, c);
            row++;

            // Max per project
            c.gridx = 0; c.gridy = row;
            panel.add(maxPerProjectLabel, c);

            JPanel maxPerProjectPanel = new JPanel();
            maxPerProjectPanel.add(maxPerProjectUnlimited);
            maxPerProjectPanel.add(maxPerProjectSpinner);

            c.gridx = 1;
            panel.add(maxPerProjectPanel, c);
            row++;

            // Max total
            c.gridx = 0; c.gridy = row;
            panel.add(maxTotalLabel, c);

            JPanel maxTotalPanel = new JPanel();
            maxTotalPanel.add(maxTotalUnlimited);
            maxTotalPanel.add(maxTotalSpinner);
            
            c.gridx = 1;
            panel.add(maxTotalPanel, c);
            row++;

            // Open folder
            c.gridx = 0; c.gridy = row++;
            panel.add(viewAutosaves, c);

            // Open folder
            c.gridx = 1;
            panel.add(openFolder, c);

        // end

        Runnable updateEnabledState = () -> {

            boolean autosaveEnabled = enableAutosave.isSelected();
            boolean periodicEnabled = autosavePeriodically.isSelected();
            boolean perProjectUnlimited = maxPerProjectUnlimited.isSelected();
            boolean totalUnlimited = maxTotalUnlimited.isSelected();

            // Autosave types only matter if autosave is enabled
            autosaveOnClose.setEnabled(autosaveEnabled);
            autosavePeriodically.setEnabled(autosaveEnabled);

            // Interval only matters if periodic autosave is enabled
            minutesSpinner.setEnabled(autosaveEnabled && periodicEnabled);
            secondsSpinner.setEnabled(autosaveEnabled && periodicEnabled);
            minutesLabel.setEnabled(autosaveEnabled && periodicEnabled);
            secondsLabel.setEnabled(autosaveEnabled && periodicEnabled);
            intervalLabel.setEnabled(autosaveEnabled && periodicEnabled);

            // Max per project
            maxPerProjectLabel.setEnabled(autosaveEnabled);
            maxPerProjectUnlimited.setEnabled(autosaveEnabled);
            maxPerProjectSpinner.setEnabled(autosaveEnabled && !perProjectUnlimited);

            // Max total
            maxTotalLabel.setEnabled(autosaveEnabled);
            maxTotalUnlimited.setEnabled(autosaveEnabled);
            maxTotalSpinner.setEnabled(autosaveEnabled && !totalUnlimited);
        };

        updateEnabledState.run();
        enableAutosave.addActionListener(e -> updateEnabledState.run());
        autosavePeriodically.addActionListener(e -> updateEnabledState.run());
        maxPerProjectUnlimited.addActionListener(e -> updateEnabledState.run());
        maxTotalUnlimited.addActionListener(e -> updateEnabledState.run());

        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Autosave Settings",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            int minutes = (int) minutesSpinner.getValue();
            int seconds = (int) secondsSpinner.getValue();
            Settings.autosaveIntervalSeconds = minutes * 60 + seconds;
            if (Settings.autosaveIntervalSeconds < 1) Settings.autosaveIntervalSeconds = 1;

            Settings.autosaveMaxPerProject = (int) maxPerProjectSpinner.getValue();
            Settings.autosaveMaxTotal = (int) maxTotalSpinner.getValue();

            if (enableAutosave.isSelected()) {                
                Settings.autosaveOnClose = autosaveOnClose.isSelected();
                Settings.autosavePeriodically = autosavePeriodically.isSelected();
            } else {
                Settings.autosaveOnClose = false;
                Settings.autosavePeriodically = false;
            }

            if (Settings.autosavePeriodically) {
                AutoTimer.start(Main.frame);
            } else {
                AutoTimer.stop();
            }

            MenuBar.updateSettingsMenu();
            AutoIndicator.updateText();
            Settings.save();
        }
    }
}