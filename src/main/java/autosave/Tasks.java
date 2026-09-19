package autosave;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONObject;

import app.Settings;
import autosave.AutoBrowser.AutosaveRendererMode;

final class Tasks {
    // Extract timestamp from autosave filename
    static String extractAutosaveTimestamp(String filename) {
        try {
            int start = filename.indexOf("_autosave_") + "_autosave_".length();
            int end = filename.lastIndexOf('.');
            String ts = filename.substring(start, end);
            return ts.replace("_", ":");
        } catch (Exception ex) {
            return null;
        }
    }

    static long parseTimestamp(String ts) {
        try {
            LocalDateTime ldt = LocalDateTime.parse(ts);
            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    // gather autosaves for browser
    static File[] gatherAutosaves(File autosaveDir, AutosaveRendererMode mode, String projectId) {

        File[] autosaves;
        if (mode == AutosaveRendererMode.CURRENT_PROJECT) {
            // Only autosaves for the current project
            autosaves = autosaveDir.listFiles((dir, name) ->
                name.startsWith(projectId + "_autosave_")
            );
        } else {
            // ALL_AUTOSAVES and UNSAVED_PROJECTS both need ALL autosaves
            autosaves = autosaveDir.listFiles((dir, name) ->
                name.contains("_autosave_")
            );
        }
        if (mode == AutosaveRendererMode.UNSAVED_PROJECTS) {
            autosaves = Arrays.stream(autosaves)
                .filter(f -> {
                    try {
                        JSONObject root = new JSONObject(Files.readString(f.toPath()));
                        String fileCreated = root.optString("fileCreated", "");
                        return fileCreated.equals("Project not saved");
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .toArray(File[]::new);
        }
        return autosaves;
    }

    static void deleteAutosave(JList<File> list, JFrame frame, DefaultListModel<File> model) {
        // Get all selected autosaves
        List<File> selectedFiles = list.getSelectedValuesList();
        if (selectedFiles == null || selectedFiles.isEmpty()) return;

        // If user disabled warnings, delete immediately
        if (!Settings.showAutosaveDeleteWarning) {
            for (File f : selectedFiles) {
                f.delete();
                model.removeElement(f);
            }
            return;
        }

        // Build custom dialog with checkbox
        JCheckBox dontShowAgain = new JCheckBox("Don't show again");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (selectedFiles.size() == 1) {
            panel.add(new JLabel(
                "Delete this autosave?"
            ));
        } else {
            panel.add(new JLabel(
                "Delete " + selectedFiles.size() + " autosaves?"
            ));
        }

        panel.add(dontShowAgain);

        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Delete Autosave" + (selectedFiles.size() > 1 ? "s" : ""),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            for (File f : selectedFiles) {
                f.delete();
                model.removeElement(f);
            }

            if (dontShowAgain.isSelected()) {
                Settings.showAutosaveDeleteWarning = false;
                Settings.save();
            }
        }
    }
}