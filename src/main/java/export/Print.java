package export;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import app.Main;
import app.Settings;

final class Print {

    private static final int INDENT_WIDTH = 1;

    static void dialog(JFrame frame, ExportPreferences defaults) {

        ExportPreferences opts = new ExportPreferences();

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Section checkboxes
        JLabel sectionLabel = new JLabel("Select what to include in the print:");
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

        boolean ii = itemsBox.isSelected();
        includeExtraInfoItemsBox.setEnabled(ii);
        includeDisabledItemsBox.setEnabled(ii);
        boolean cc = criteriaBox.isSelected();
        includeExtraInfoCriteriaBox.setEnabled(cc);
        includeDisabledCriteriaBox.setEnabled(cc);

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

        // Dialog
        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Export project",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            opts.fileType = defaults.fileType;
            opts.fileName = defaults.fileName;
            opts.folder = defaults.folder;

            opts.ranking = rankingBox.isSelected();
            opts.itemInfo = itemsBox.isSelected();
            opts.includeDisabledItems = includeDisabledItemsBox.isSelected();
            opts.includeExtraInfoItems = includeExtraInfoItemsBox.isSelected();

            opts.critInfo = criteriaBox.isSelected();
            opts.includeDisabledCriteria = includeDisabledCriteriaBox.isSelected();
            opts.includeExtraInfoCriteria = includeExtraInfoCriteriaBox.isSelected();

            opts.metadata = metadataBox.isSelected();
            opts.notes = notesBox.isSelected();

            opts.rankProject = defaults.rankProject;
            opts.rankFileName = defaults.rankFileName;
            opts.rankFolder = defaults.rankFolder;

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

            print(frame, opts);
        }
    }

    private static void print(JFrame frame, ExportPreferences prefs) {
        try {
            // Create temporary PDF
            File tempPDF = File.createTempFile("print_", ".pdf");
            tempPDF.deleteOnExit();

            boolean ok = PDF.exportPDF(tempPDF, prefs);
            if (!ok) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Could not generate PDF for printing.",
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Check if printing is supported
            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.PRINT)) {
                JOptionPane.showMessageDialog(frame, """
                        Printing is not supported on this system.
                        Opening the PDF instead.
                    """,
                    "Print Error",
                    JOptionPane.WARNING_MESSAGE
                );
                Desktop.getDesktop().open(tempPDF);
                return;
            }

            // Try printing
            try {
                Desktop.getDesktop().print(tempPDF);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, """
                        Printing was unsuccessful.
                        Opening the PDF instead.
                    """,
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE
                );
                Desktop.getDesktop().open(tempPDF);
            }

        } catch (HeadlessException | IOException | SecurityException ex) {
            JOptionPane.showMessageDialog(
                frame,
                "Printing failed:\n" + ex.getMessage(),
                "Print Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}