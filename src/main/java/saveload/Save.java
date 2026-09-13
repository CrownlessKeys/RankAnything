package saveload;

import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import app.Main;
import app.Settings;
import metadata.Metadata;

final class Save {
    static void save(JFrame frame) {
        if (SaveLoad.currentProjectFile == null) {
            SaveAs.save(frame);
        } else {
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
                        "Auto-Rename Project File",
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

            SaveToFile.save(SaveLoad.currentProjectFile, frame, false);

            Main.unsavedChanges = false;
            Main.projectDirty = false;
            Metadata.updateWindowTitle(frame);
        }
    }
}
