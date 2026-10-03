package export;

import java.awt.FlowLayout;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import app.Main;
import app.Settings;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import tasks.Notify;

final class Dialog {

    private static final int INDENT_WIDTH = 1;

    static void show(JFrame frame, ExportPreferences defaults) {

        ExportPreferences opts = new ExportPreferences();

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // File type dropdown
        JPanel fileTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fileTypePanel.add(new JLabel("File type:"));

        String[] fileTypes = {
            "PDF", // "DOCX", "CSV", "PNG", "HTML", "MD", "TXT",
            "RANK (project file)", "JSON (project file)"
        };
        JComboBox<String> fileTypeBox = new JComboBox<>(fileTypes);
        fileTypeBox.setSelectedItem(defaults.fileType);
        fileTypePanel.add(fileTypeBox);

        panel.add(fileTypePanel);

        // File name input
        JPanel fileNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fileNamePanel.add(new JLabel("Export file name:"));
        JTextField fileNameField = new JTextField(20);
        fileNameField.setText(defaults.fileName);
        fileNamePanel.add(fileNameField);
        panel.add(fileNamePanel);

        // Location chooser
        JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        locationPanel.add(new JLabel("Export location:"));
        JTextField locationField = new JTextField(20);
        locationField.setText(defaults.folder);
        JButton chooseLocationBtn = new JButton("Browse...");
        locationPanel.add(locationField);
        locationPanel.add(chooseLocationBtn);
        panel.add(locationPanel);

        chooseLocationBtn.addActionListener(e -> {
            Platform.runLater(() -> {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Choose export folder");

                // Use whatever is currently in the text field
                String current = locationField.getText().trim();
                if (!current.isEmpty()) {
                    File dir = new File(current);
                    if (dir.exists() && dir.isDirectory()) {
                        chooser.setInitialDirectory(dir);
                    }
                }

                File folder = chooser.showDialog(null);
                if (folder != null) {
                    SwingUtilities.invokeLater(() ->
                        locationField.setText(folder.getAbsolutePath())
                    );
                }
            });
        });

        // Section checkboxes
        JLabel sectionLabel = new JLabel("Select what to include in the export:");
        panel.add(sectionLabel);

        JCheckBox rankingBox = new JCheckBox("Ranking");
        rankingBox.setSelected(defaults.ranking);

        JCheckBox itemsBox = new JCheckBox("Item information");
        itemsBox.setSelected(defaults.itemInfo);

        JPanel includeDisabledItemsPanel = new JPanel();
        JCheckBox includeDisabledItemsBox = new JCheckBox("Include disabled items");
        includeDisabledItemsBox.setSelected(defaults.includeDisabledItems);
        includeDisabledItemsPanel.add(Box.createHorizontalStrut(INDENT_WIDTH));
        includeDisabledItemsPanel.add(includeDisabledItemsBox);

        JPanel includeExtraInfoItemsPanel = new JPanel();
        JCheckBox includeExtraInfoItemsBox = new JCheckBox("Include extra item info");
        includeExtraInfoItemsBox.setSelected(defaults.includeExtraInfoItems);
        includeExtraInfoItemsPanel.add(Box.createHorizontalStrut(INDENT_WIDTH));
        includeExtraInfoItemsPanel.add(includeExtraInfoItemsBox);

        JCheckBox criteriaBox = new JCheckBox("Criteria information");
        criteriaBox.setSelected(defaults.critInfo);

        JPanel includeDisabledCriteriaPanel = new JPanel();
        JCheckBox includeDisabledCriteriaBox = new JCheckBox("Include disabled criteria");
        includeDisabledCriteriaBox.setSelected(defaults.includeDisabledCriteria);
        includeDisabledCriteriaPanel.add(Box.createHorizontalStrut(INDENT_WIDTH));
        includeDisabledCriteriaPanel.add(includeDisabledCriteriaBox);

        JPanel includeExtraInfoCriteriaPanel = new JPanel();
        JCheckBox includeExtraInfoCriteriaBox = new JCheckBox("Include extra criteria info");
        includeExtraInfoCriteriaBox.setSelected(defaults.includeExtraInfoCriteria);
        includeExtraInfoCriteriaPanel.add(Box.createHorizontalStrut(INDENT_WIDTH));
        includeExtraInfoCriteriaPanel.add(includeExtraInfoCriteriaBox);

        JCheckBox metadataBox = new JCheckBox("Project information");
        metadataBox.setSelected(defaults.metadata);

        JCheckBox notesBox = new JCheckBox("Notes");
        notesBox.setSelected(defaults.notes);

        panel.add(rankingBox);
        panel.add(itemsBox);
        panel.add(includeExtraInfoItemsPanel);
        panel.add(includeDisabledItemsPanel);
        panel.add(criteriaBox);
        panel.add(includeExtraInfoCriteriaPanel);
        panel.add(includeDisabledCriteriaPanel);
        panel.add(metadataBox);
        panel.add(notesBox);

        // Include .rank snapshot checkbox
        JCheckBox includeRankFileBox = new JCheckBox("Include project snapshot (.rank)");
        includeRankFileBox.setSelected(defaults.rankProject);
        includeRankFileBox.setToolTipText("Include a .rank project file alongside the primary export.");
        panel.add(includeRankFileBox);

        // Snapshot name input
        JPanel snapshotNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel snapshotNameLabel = new JLabel("Snapshot file name:");
        snapshotNamePanel.add(snapshotNameLabel);
        JTextField snapshotNameField = new JTextField(20);
        snapshotNameField.setText(defaults.rankFileName);
        snapshotNamePanel.add(snapshotNameField);
        panel.add(snapshotNamePanel);

        // Snapshot folder name input
        String snapshotFolderTooltip = "Name the folder housing both the primary export and the .rank snapshot.";
        JPanel snapshotFolderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel folderNameLabel = new JLabel("Folder name:");
        folderNameLabel.setToolTipText(snapshotFolderTooltip);
        snapshotFolderPanel.add(folderNameLabel);
        JTextField snapshotFolderNameField = new JTextField(20);
        snapshotFolderNameField.setText(defaults.rankFolder);
        snapshotFolderNameField.setToolTipText(snapshotFolderTooltip);
        snapshotFolderPanel.add(snapshotFolderNameField);
        panel.add(snapshotFolderPanel);

        // alternative ranking modes
            panel.add(Box.createVerticalStrut(10));
            JLabel altRankHeader = new JLabel("Include alternative rankings:");
            panel.add(altRankHeader);

            JCheckBox includeWeightedEnabledBox =
                new JCheckBox("Weighted");
            includeWeightedEnabledBox.setSelected(defaults.includeWeightedEnabled);
            panel.add(includeWeightedEnabledBox);

            JCheckBox includeWeightedAllCriteriaBox =
                new JCheckBox("Weighted, considering disabled criteria");
            includeWeightedAllCriteriaBox.setSelected(defaults.includeWeightedAllCriteria);
            panel.add(includeWeightedAllCriteriaBox);

            JCheckBox includeWeightedAllItemsBox =
                new JCheckBox("Weighted, considering disabled items");
            includeWeightedAllItemsBox.setSelected(defaults.includeWeightedAllItems);
            panel.add(includeWeightedAllItemsBox);

            JCheckBox includeWeightedAllCriteriaAllItemsBox =
                new JCheckBox("Weighted, considering disabled criteria & items");
            includeWeightedAllCriteriaAllItemsBox.setSelected(defaults.includeWeightedAllCriteriaAllItems);
            panel.add(includeWeightedAllCriteriaAllItemsBox);

            JCheckBox includeUnweightedEnabledBox =
                new JCheckBox("Unweighted");
            includeUnweightedEnabledBox.setSelected(defaults.includeUnweightedEnabled);
            panel.add(includeUnweightedEnabledBox);

            JCheckBox includeUnweightedAllCriteriaBox =
                new JCheckBox("Unweighted, considering disabled criteria");
            includeUnweightedAllCriteriaBox.setSelected(defaults.includeUnweightedAllCriteria);
            panel.add(includeUnweightedAllCriteriaBox);

            JCheckBox includeUnweightedAllItemsBox =
                new JCheckBox("Unweighted, considering disabled items");
            includeUnweightedAllItemsBox.setSelected(defaults.includeUnweightedAllItems);
            panel.add(includeUnweightedAllItemsBox);

            JCheckBox includeUnweightedAllCriteriaAllItemsBox =
                new JCheckBox("Unweighted, considering disabled criteria & items");
            includeUnweightedAllCriteriaAllItemsBox.setSelected(defaults.includeUnweightedAllCriteriaAllItems);
            panel.add(includeUnweightedAllCriteriaAllItemsBox);
        // end

        // All checkboxes that should be disabled for project-file exports
        List<JCheckBox> exportOptionBoxes = List.of(
            rankingBox,
            itemsBox,
            includeDisabledItemsBox,
            includeExtraInfoItemsBox,
            criteriaBox,
            includeDisabledCriteriaBox,
            includeExtraInfoCriteriaBox,
            metadataBox,
            notesBox,
            includeWeightedEnabledBox,
            includeWeightedAllCriteriaBox,
            includeWeightedAllItemsBox,
            includeWeightedAllCriteriaAllItemsBox,
            includeUnweightedEnabledBox,
            includeUnweightedAllCriteriaBox,
            includeUnweightedAllItemsBox,
            includeUnweightedAllCriteriaAllItemsBox
        );

        // enable/disable states

        includeRankFileBox.addActionListener(e -> {
            updateEnabled(
                fileTypeBox,
                includeRankFileBox,
                snapshotNamePanel,
                snapshotFolderPanel,
                snapshotNameField,
                snapshotFolderNameField,
                folderNameLabel,
                snapshotNameLabel,
                exportOptionBoxes,
                sectionLabel,
                altRankHeader,
                itemsBox,
                includeExtraInfoItemsBox,
                includeDisabledItemsBox,
                criteriaBox,
                includeExtraInfoCriteriaBox,
                includeDisabledCriteriaBox
            );
        });

        fileTypeBox.addActionListener(e -> {
            updateEnabled(
                fileTypeBox,
                includeRankFileBox,
                snapshotNamePanel,
                snapshotFolderPanel,
                snapshotNameField,
                snapshotFolderNameField,
                folderNameLabel,
                snapshotNameLabel,
                exportOptionBoxes,
                sectionLabel,
                altRankHeader,
                itemsBox,
                includeExtraInfoItemsBox,
                includeDisabledItemsBox,
                criteriaBox,
                includeExtraInfoCriteriaBox,
                includeDisabledCriteriaBox
            );
        });

        itemsBox.addActionListener(e -> {
            boolean i = itemsBox.isSelected();
            includeExtraInfoItemsBox.setEnabled(i);
            includeDisabledItemsBox.setEnabled(i);
        });

        criteriaBox.addActionListener(e -> {
            boolean c = criteriaBox.isSelected();
            includeExtraInfoCriteriaBox.setEnabled(c);
            includeDisabledCriteriaBox.setEnabled(c);
        });
        
        updateEnabled(
            fileTypeBox,
            includeRankFileBox,
            snapshotNamePanel,
            snapshotFolderPanel,
            snapshotNameField,
            snapshotFolderNameField,
            folderNameLabel,
            snapshotNameLabel,
            exportOptionBoxes,
            sectionLabel,
            altRankHeader,
            itemsBox,
            includeExtraInfoItemsBox,
            includeDisabledItemsBox,
            criteriaBox,
            includeExtraInfoCriteriaBox,
            includeDisabledCriteriaBox
        );

        // Dialog
        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Export project",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            opts.fileType = (String) fileTypeBox.getSelectedItem();
            opts.fileName = fileNameField.getText().trim();
            opts.folder = locationField.getText().trim();

            opts.ranking = rankingBox.isSelected();
            opts.itemInfo = itemsBox.isSelected();
            opts.includeDisabledItems = includeDisabledItemsBox.isSelected();
            opts.includeExtraInfoItems = includeExtraInfoItemsBox.isSelected();

            opts.critInfo = criteriaBox.isSelected();
            opts.includeDisabledCriteria = includeDisabledCriteriaBox.isSelected();
            opts.includeExtraInfoCriteria = includeExtraInfoCriteriaBox.isSelected();

            opts.metadata = metadataBox.isSelected();
            opts.notes = notesBox.isSelected();

            boolean proj = fileTypeBox.getSelectedItem().toString().contains("(project file)");
            opts.rankProject = includeRankFileBox.isSelected() && !proj;
            opts.rankFileName = snapshotNameField.getText().trim();
            opts.rankFolder = snapshotFolderNameField.getText().trim();

            opts.includeWeightedEnabled = includeWeightedEnabledBox.isSelected();
            opts.includeWeightedAllCriteria = includeWeightedAllCriteriaBox.isSelected();
            opts.includeWeightedAllItems = includeWeightedAllItemsBox.isSelected();
            opts.includeWeightedAllCriteriaAllItems = includeWeightedAllCriteriaAllItemsBox.isSelected();
            opts.includeUnweightedEnabled = includeUnweightedEnabledBox.isSelected();
            opts.includeUnweightedAllCriteria = includeUnweightedAllCriteriaBox.isSelected();
            opts.includeUnweightedAllItems = includeUnweightedAllItemsBox.isSelected();
            opts.includeUnweightedAllCriteriaAllItems = includeUnweightedAllCriteriaAllItemsBox.isSelected();

            // Store last-used export preferences
            Main.projectExport = new ExportPreferences(opts); // for the project
            Settings.defaultExport = new ExportPreferences(opts); // and globally

            // keep project-specifics to defaults in Settings.json
            Settings.defaultExport.fileName = "";
            Settings.defaultExport.rankFileName = "";
            Settings.defaultExport.rankFolder = "";

            Settings.save();

            Notify.showToast(frame, Write.export(frame, opts)
                ? "Project exported successfully"
                : "Export failed"
            );
        }
    }

    private static void updateEnabled(
        JComboBox<String> fileTypeBox,
        JCheckBox includeRankFileBox,
        JPanel snapshotNamePanel,
        JPanel snapshotFolderPanel,
        JTextField snapshotNameField,
        JTextField snapshotFolderNameField,
        JLabel folderNameLabel,
        JLabel snapshotNameLabel,
        List<JCheckBox> exportOptionBoxes,
        JLabel sectionLabel,
        JLabel altRankHeader,
        JCheckBox itemsBox,
        JCheckBox includeExtraInfoItemsBox,
        JCheckBox includeDisabledItemsBox,
        JCheckBox criteriaBox,
        JCheckBox includeExtraInfoCriteriaBox,
        JCheckBox includeDisabledCriteriaBox
    ) {
        boolean proj = fileTypeBox.getSelectedItem().toString().contains("(project file)");

        // 1. PROJECT FILE MODE
            if (proj) {
                // Disable ALL export options
                for (JCheckBox box : exportOptionBoxes) {
                    box.setEnabled(false);
                }

                // Disable headers
                sectionLabel.setEnabled(false);
                altRankHeader.setEnabled(false);

                // Disable snapshot checkbox + fields
                includeRankFileBox.setEnabled(false);
                snapshotNamePanel.setEnabled(false);
                snapshotFolderPanel.setEnabled(false);
                snapshotNameField.setEnabled(false);
                snapshotFolderNameField.setEnabled(false);
                folderNameLabel.setEnabled(false);
                snapshotNameLabel.setEnabled(false);

                return; // IMPORTANT: stop here
            }

        // 2. NORMAL EXPORT MODE
            // Enable all export options
            for (JCheckBox box : exportOptionBoxes) {
                box.setEnabled(true);
            }

            // Enable headers
            sectionLabel.setEnabled(true);
            altRankHeader.setEnabled(true);

        // 3. SNAPSHOT LOGIC
            boolean snapshotEnabled = includeRankFileBox.isSelected();
            includeRankFileBox.setEnabled(true);

            snapshotNamePanel.setEnabled(snapshotEnabled);
            snapshotFolderPanel.setEnabled(snapshotEnabled);
            snapshotNameField.setEnabled(snapshotEnabled);
            snapshotFolderNameField.setEnabled(snapshotEnabled);
            folderNameLabel.setEnabled(snapshotEnabled);
            snapshotNameLabel.setEnabled(snapshotEnabled);

        // 4. ITEM SUB-CHECKBOX LOGIC
            boolean itemsEnabled = itemsBox.isSelected();
            includeExtraInfoItemsBox.setEnabled(itemsEnabled);
            includeDisabledItemsBox.setEnabled(itemsEnabled);

        // 5. CRITERIA SUB-CHECKBOX LOGIC
            boolean criteriaEnabled = criteriaBox.isSelected();
            includeExtraInfoCriteriaBox.setEnabled(criteriaEnabled);
            includeDisabledCriteriaBox.setEnabled(criteriaEnabled);
        // end
    }
}