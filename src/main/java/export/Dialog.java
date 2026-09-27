package export;

import java.awt.FlowLayout;
import java.io.File;

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

final class Dialog {

    static void show(JFrame frame, ExportPreferences defaults) {

        ExportPreferences opts = new ExportPreferences();

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // File type dropdown
        JPanel fileTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fileTypePanel.add(new JLabel("File type:"));

        String[] fileTypes = { "PDF", "DOCX", "CSV", "PNG", "HTML", "MD", "TXT"};
        JComboBox<String> fileTypeBox = new JComboBox<>(fileTypes);
        fileTypeBox.setSelectedItem(defaults.fileType);
        fileTypePanel.add(fileTypeBox);

        panel.add(fileTypePanel);

        // File name input
        JPanel fileNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fileNamePanel.add(new JLabel("Export file name:"));
        JTextField fileNameField = new JTextField(20);
        fileNamePanel.add(fileNameField);
        panel.add(fileNamePanel);

        // Location chooser
        JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        locationPanel.add(new JLabel("Export location:"));
        JTextField locationField = new JTextField(20);
        JButton chooseLocationBtn = new JButton("Choose...");
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
        
        // Checkboxes
        JCheckBox rankingBox = new JCheckBox("Ranking");
        JCheckBox itemsBox = new JCheckBox("Item information");
        JCheckBox criteriaBox = new JCheckBox("Criteria information");
        JCheckBox metadataBox = new JCheckBox("Metadata");
        JCheckBox notesBox = new JCheckBox("Notes");

        panel.add(rankingBox);
        panel.add(itemsBox);
        panel.add(criteriaBox);
        panel.add(metadataBox);
        panel.add(notesBox);

        // Include .rank snapshot checkbox
        JCheckBox includeRankFileBox = new JCheckBox("Include project snapshot (.rank)");
        panel.add(includeRankFileBox);

        // Snapshot name input
        JPanel snapshotNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel snapshotNameLabel = new JLabel("Snapshot file name:");
        snapshotNamePanel.add(snapshotNameLabel);
        JTextField snapshotNameField = new JTextField(20);
        snapshotNamePanel.add(snapshotNameField);
        snapshotNamePanel.setEnabled(false);
        panel.add(snapshotNamePanel);

        // Snapshot folder chooser (JavaFX DirectoryChooser)
        JPanel snapshotFolderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel folderNameLabel = new JLabel("Folder name:");
        snapshotFolderPanel.add(folderNameLabel);
        JTextField snapshotFolderNameField = new JTextField(20);
        snapshotFolderPanel.add(snapshotFolderNameField);
        snapshotFolderPanel.setEnabled(false);
        panel.add(snapshotFolderPanel);

        // Enable/disable snapshot fields
        includeRankFileBox.addActionListener(e -> {
            boolean enabled = includeRankFileBox.isSelected();
            snapshotNamePanel.setEnabled(enabled);
            snapshotFolderPanel.setEnabled(enabled);
            snapshotNameField.setEnabled(enabled);
            snapshotFolderPanel.setEnabled(enabled);
            snapshotFolderNameField.setEnabled(enabled);
            folderNameLabel.setEnabled(enabled);
            snapshotNameLabel.setEnabled(enabled);
        });

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
            opts.ranking = rankingBox.isSelected();
            opts.itemInfo = itemsBox.isSelected();
            opts.critInfo = criteriaBox.isSelected();
            opts.metadata = metadataBox.isSelected();
            opts.notes = notesBox.isSelected();

            opts.fileName = fileNameField.getText().trim();
            opts.folder = locationField.getText().trim();

            opts.rankProject = includeRankFileBox.isSelected();
            opts.rankFileName = snapshotNameField.getText().trim();
            opts.rankFolder = snapshotFolderNameField.getText().trim();

            // Store last-used export preferences
            Main.projectExport = new ExportPreferences(opts); // for the project
            Settings.defaultExport = new ExportPreferences(opts); // and globally

            // keep project-specifics to defaults in Settings.json
            Settings.defaultExport.fileName = "";
            Settings.defaultExport.rankFileName = "";
            Settings.defaultExport.rankFolder = "";

            Settings.save();

            Write.export(frame, opts);
        }
    }
}