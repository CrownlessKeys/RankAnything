package metadata;

import java.awt.Desktop;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import app.Main;
import app.Settings;
import saveload.SaveLoad;
import tasks.Notify;

final class Tasks {
    static void updateWindowTitle(JFrame frame) {
        String title = Main.baseWindowTitle;

        if (Main.projectName != null) {
            title += " — " + Main.projectName;
        } else {
            title += " — Untitled";
        }

        if (Main.unsavedChanges) title += "*";
        
        frame.setTitle(title);
    }

    static String formatTimestamp(String ts) {
        if (ts == null) return "Unknown";

        try {
            LocalDateTime dt = LocalDateTime.parse(ts);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM d, yyyy — h:mm:ss a");
            return dt.format(fmt);
        } catch (Exception ex) {
            return ts; // fallback
        }
    }

    static void openFileLocation(JFrame frame) {
        String path = SaveLoad.getCurrentFilePath();
        if (path != null && !path.equals("Project not saved")) {
            try {
                File file = new File(path);
                Desktop.getDesktop().open(file.getParentFile());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Notify.showToast(frame, "Project not saved");
        }
    }

    static boolean checkExtension(JFrame frame, File file) {
        if (!file.getName().toLowerCase().endsWith(".rank")) {
            int choice = JOptionPane.showConfirmDialog(
                frame, 
                """
                    This file does not use the .rank extension. 
                    It may not be a valid project file and could 
                    cause errors when loading.

                    Attempt to load it anyway?
                """,
                "Non-Rank File",
                JOptionPane.YES_NO_OPTION
            );
            
            return choice == JOptionPane.YES_OPTION;
        } else {
            return true;
        }
    }

    static void checkFileName(JFrame frame) {
        String desiredName = Main.projectName + ".rank";
        String currentName = SaveLoad.currentProjectFile.getName();
        boolean needsRename = !currentName.equals(desiredName);

        if (needsRename && Settings.autoRenameFiles) {
            if (Settings.showRenameWarning) {
                JCheckBox dontShowAgain = new JCheckBox("Don't show again");
                JPanel message = new JPanel();
                message.setLayout(new BoxLayout(message, BoxLayout.Y_AXIS));

                message.add(new JLabel(
                    "<html>"
                    + "Your project file name does not match the project name.<br><br>"
                    + "Current file name: <b>" + currentName + "</b><br>"
                    + "Expected file name: <b>" + desiredName + "</b><br><br>"
                    + "Rename project?<br><br>"
                    + "You can change this behavior in Settings."
                    + "</html>"
                ));

                message.add(Box.createVerticalStrut(10));
                message.add(dontShowAgain);

                int result = JOptionPane.showConfirmDialog(
                    frame, 
                    message,
                    "Auto-rename project file",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (dontShowAgain.isSelected()) {
                    Settings.showRenameWarning = false;
                    Settings.save();
                }

                if (result == JOptionPane.YES_OPTION) {
                    File newFile = new File(SaveLoad.currentProjectFile.getParentFile(), desiredName);
                    if (SaveLoad.currentProjectFile.renameTo(newFile)) {
                        SaveLoad.currentProjectFile = newFile;
                    }
                }
                
            } else {
                // Silent auto-rename
                File newFile = new File(SaveLoad.currentProjectFile.getParentFile(), desiredName);
                if (SaveLoad.currentProjectFile.renameTo(newFile)) {
                    SaveLoad.currentProjectFile = newFile;
                }
            }
        }
    }
}