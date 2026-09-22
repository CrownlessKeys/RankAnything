package metadata;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONObject;

import app.Main;
import app.Settings;

final class SaveLoadMeta {
    static void loadFromJSON(JSONObject root) {
        Main.projectName = root.optString("projectName", "Untitled");
        Main.projectNotes = root.optString("notes", "");
        Main.projectCreated = root.optString("created", null);
        Main.fileCreated = root.optString("fileCreated", null);
        Main.projectModified = root.optString("modified", null);
        Main.projectAppVersion = root.optString("appVersion", "Unknown");
        Main.projectFileFormatVersion = root.optInt("fileFormatVersion", 0);
        Main.isOriginal = root.optBoolean("isOriginal", false);

        // export preferences
        Main.ranking = root.optBoolean("ranking", Settings.ranking);
        Main.itemInfo = root.optBoolean("itemInfo", Settings.itemInfo);
        Main.critInfo = root.optBoolean("critInfo", Settings.critInfo);
        Main.metadata = root.optBoolean("metadata", Settings.metadata);
        Main.notes = root.optBoolean("notes", Settings.notes);
    }

    static void saveToJSON(JSONObject root, boolean isAuto) {
        if (!isAuto) {
            if (Main.projectCreated == null) Main.projectCreated = Metadata.now();
            if (Main.fileCreated == null) Main.fileCreated = Metadata.now();
            Main.projectModified = Metadata.now();
        }

        root.put("projectName", Main.projectName);
        root.put("notes", Main.projectNotes);
        root.put("created", Main.projectCreated);
        root.put("fileCreated", Main.fileCreated);
        root.put("modified", Main.projectModified);
        root.put("appVersion", Main.APP_VERSION);
        root.put("fileFormatVersion", Main.FILE_FORMAT_VERSION);
        root.put("isOriginal", Main.isOriginal);

        // export preferences TODO: DO NOT SAVE DEFAULT VALUES!! Try to only save if the preferences have been altered
        root.put("ranking", Main.ranking);
        root.put("itemInfo", Main.itemInfo);
        root.put("critInfo", Main.critInfo);
        root.put("metadata", Main.metadata);
        root.put("notes", Main.notes);
    }

    static void versionMismatchWarningsLoad(JFrame frame) {

        // App version mismatch
        if (!Main.projectAppVersion.equals(Main.APP_VERSION)) {
            JOptionPane.showMessageDialog(frame,
                """
                This project was saved using Rank Anything 
                version %s.
                
                You are currently using version %s.
                """.formatted(Main.projectAppVersion, Main.APP_VERSION),
                "Version mismatch",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        // Missing or corrupted file format
        if (Main.projectFileFormatVersion <= 0) {
            JOptionPane.showMessageDialog(frame, 
                """
                    This project does not contain valid file 
                    format information.

                    If you save this project, it will be converted 
                    to the current file format.
                """,
                "Missing file format",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        // Old file format
        if (Main.projectFileFormatVersion < Main.FILE_FORMAT_VERSION
            && Main.projectFileFormatVersion > 0
        ) {
            JOptionPane.showMessageDialog(frame, 
                """
                    This project was saved with an older file format
                    than this version of Rank Anything supports. 
                    
                    If you save this project, it will be converted 
                    to the current file format.
                """,
                "Old file format",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        // Newer file format
        if (Main.projectFileFormatVersion > Main.FILE_FORMAT_VERSION) {
            JOptionPane.showMessageDialog(frame, 
                """
                    This project was saved with a newer file format 
                    than this version of Rank Anything supports. 
                    Some features may not load correctly.

                    If you save this project, it will be converted 
                    to the current file format.
                """,
                "Newer file format",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    static boolean versionMismatchWarningsSave(JFrame frame) {

        // Missing or corrupted file format
        if (Main.projectFileFormatVersion <= 0) {
            int choice = JOptionPane.showConfirmDialog(frame, 
                """
                    This project does not contain valid file format information. If you 
                    save this project, it will be converted to the current file format.

                    Because the original format cannot be determined, it is unknown exactly 
                    what structural changes will occur. This process could permanently 
                    overwrite or misalign some of your existing project settings.

                    In short, it is recommended that you know what you are doing.

                    Do you want to proceed with saving?
                """,
                "Missing file format",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) return false;
        }

        // Old file format
        if (Main.projectFileFormatVersion < Main.FILE_FORMAT_VERSION
            && Main.projectFileFormatVersion > 0
        ) {
            int choice = JOptionPane.showConfirmDialog(frame, 
                """
                    This project was saved with an older file format. If you 
                    save this project, it will be converted to the current 
                    file format.

                    Do you want to proceed with saving?
                """,
                "File format upgrade",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) return false;
        }

        // Newer file format
        if (Main.projectFileFormatVersion > Main.FILE_FORMAT_VERSION) {
            int choice = JOptionPane.showConfirmDialog(frame, 
                """
                    CRITICAL WARNING: This project belongs to a newer 
                    version of Rank Anything than you are currently running. 

                    Saving now will overwrite the file, permanently 
                    stripping out features and data not supported by 
                    this older app version.

                    Are you absolutely sure you want to overwrite this file?
                """,
                "CRITICAL: Potential data loss",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) return false;
        }

        return true;
    }
}