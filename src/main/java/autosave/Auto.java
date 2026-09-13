package autosave;

import java.io.File;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import app.Main;
import app.Settings;
import metadata.Metadata;
import saveload.SaveLoad;

final class Auto {
    static void save(JFrame frame) {
        try {
            String projectId = Main.projectId;

            File autosaveDir = Main.AUTOSAVE_DIR;
            autosaveDir.mkdirs();

            String timestamp = Metadata.now().replace(':', '_');
            File autosaveFile = new File(autosaveDir,
                projectId + "_autosave_" + timestamp + ".rank"
            );

            SaveLoad.saveToFile(autosaveFile, frame, true);

            if (!Settings.autosaveUnlimitedPerProject) {
                // clean up old project autosaves 
                File[] projectAutosaves = autosaveDir.listFiles((dir, name) ->
                    name.startsWith(projectId + "_autosave_")
                );

                if (projectAutosaves != null && projectAutosaves.length > Settings.autosaveMaxPerProject) {
                    // Sort by oldest first
                    Arrays.sort(projectAutosaves, (a, b) -> {
                        String ta = Tasks.extractAutosaveTimestamp(a.getName());
                        String tb = Tasks.extractAutosaveTimestamp(b.getName());

                        long pa = ta != null ? Tasks.parseTimestamp(ta) : 0L;
                        long pb = tb != null ? Tasks.parseTimestamp(tb) : 0L;

                        return Long.compare(pa, pb);
                    });

                    // Delete oldest autosaves
                    for (int i = 0; i < projectAutosaves.length - Settings.autosaveMaxPerProject; i++) {
                        if (!projectAutosaves[i].delete()) {
                            System.err.println("Failed to delete autosave: " + projectAutosaves[i].getName());
                        }
                    }
                }
            }

            if (!Settings.autosaveUnlimitedTotal) {
                // clean up old autosaves (all projects)
                File[] autosaves = autosaveDir.listFiles((dir, name) ->
                    name.contains("_autosave_")
                );

                if (autosaves != null && autosaves.length > Settings.autosaveMaxTotal) {
                    // Sort by oldest first
                    Arrays.sort(autosaves, (a, b) -> {
                        String ta = Tasks.extractAutosaveTimestamp(a.getName());
                        String tb = Tasks.extractAutosaveTimestamp(b.getName());

                        long pa = ta != null ? Tasks.parseTimestamp(ta) : 0L;
                        long pb = tb != null ? Tasks.parseTimestamp(tb) : 0L;

                        return Long.compare(pa, pb);
                    });

                    // Delete oldest autosaves
                    for (int i = 0; i < autosaves.length - Settings.autosaveMaxTotal; i++) {
                        if (!autosaves[i].delete()) {
                            System.err.println("Failed to delete autosave: " + autosaves[i].getName());
                        }
                    }
                }
            }

            Main.projectDirty = false;
            AutoIndicator.markAutosaved();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void checkForNewerAutosave(JFrame frame) {
        try {
            String projectId = Main.projectId;
            String modified = Main.projectModified;

            // Parse project modified timestamp
            long projectModifiedTime = Tasks.parseTimestamp(modified);

            File autosaveDir = Main.AUTOSAVE_DIR;

            File[] autosaves = autosaveDir.listFiles((dir, name) ->
                name.startsWith(projectId + "_autosave_")
            );

            if (autosaves == null || autosaves.length == 0) return;

            // Find newest autosave
            File newest = null;
            long newestTime = 0;
            for (File f : autosaves) {
                String t = Tasks.extractAutosaveTimestamp(f.getName());
                if (t != null && !t.isEmpty()) {
                    long timestamp = Tasks.parseTimestamp(t);
                    if (timestamp > newestTime) {
                        newestTime = timestamp;
                        newest = f;
                    }
                }
            }

            // Compare timestamps
            if (newestTime > projectModifiedTime) {
                showAutosaveRecoveryDialog(frame, newest);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void showAutosaveRecoveryDialog(JFrame frame, File autosaveFile) {
        int choice = JOptionPane.showOptionDialog(
            frame,
            """
                A more recent autosave was found for this project.
                It may contain changes that were not saved.
                
                Would you like to restore the autosave?
            """,
            "Autosave Found",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            new String[] { "Restore Autosave", "Open Main File", "View Autosaves" },
            "Restore Autosave"
        );

        if (choice == 0) {
            // Restore autosave
            SaveLoad.loadFromFile(autosaveFile, frame, true);
        } else if (choice == 2) {
            // Open autosave browser 
            AutoBrowser.show(
                frame, 
                Main.projectId, 
                AutoBrowser.AutosaveRendererMode.CURRENT_PROJECT
            );
        }
    }
}