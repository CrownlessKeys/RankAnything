package saveload;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import app.Main;
import app.RecentProjects;
import app.Settings;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import metadata.Metadata;
import metadata.ProjectID;

final class Save {
    static void save(JFrame frame) {
        if (SaveLoad.currentProjectFile == null) {
            saveAs(frame);
        } else {
            SaveToFile.save(SaveLoad.currentProjectFile, frame, false);

            Main.unsavedChanges = false;
            Main.projectDirty = false;
            Metadata.updateWindowTitle();
        }
    }

    static void saveAs(JFrame frame) {
        Platform.runLater(() -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save project as");

            // Default file name
            chooser.setInitialFileName(Main.projectName + ".rank");

            // Filters
            ExtensionFilter rankFilter = new ExtensionFilter(
                "RANK Project File (*.rank)", "*.rank"
            );

            ExtensionFilter allFilter = new ExtensionFilter(
                "All Files (*.*)", "*.*"
            );

            chooser.getExtensionFilters().addAll(rankFilter, allFilter);
            chooser.setSelectedExtensionFilter(rankFilter); // default to enforcing .rank

            // default to current project folder or last used folder
            if (SaveLoad.currentProjectFile != null && SaveLoad.currentProjectFile.exists()) {
                chooser.setInitialDirectory(SaveLoad.currentProjectFile.getParentFile());
            } else if (Settings.lastOpenedProjectFolder != null) {
                File last = new File(Settings.lastOpenedProjectFolder);
                if (last.exists()) chooser.setInitialDirectory(last);
            }

            File selected = chooser.showSaveDialog(null);
            if (selected != null) {
                File finalFile = selected;
                SwingUtilities.invokeLater(() -> {
                    Main.isOriginal = Main.fileCreated.equals("Project not saved");
                    Main.fileCreated = Metadata.now();

                    // give it a "copy" project ID
                    if (Main.projectId != null) {
                        if (!Main.isOriginal) {
                            Main.projectId = Main.projectId + "-COPY-" + ProjectID.generate();
                        }
                    } else {
                        Main.projectId = ProjectID.generate();
                    }

                    SaveToFile.save(finalFile, frame, false);

                    Main.unsavedChanges = false;
                    Main.projectDirty = false;
                    Metadata.updateWindowTitle();

                    RecentProjects.addRecentProject(finalFile.getAbsolutePath());
                    RecentProjects.save();
                    RecentProjects.updateRecentProjectsMenu(frame);
                });
            }
        });
    }
}