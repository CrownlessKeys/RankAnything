package metadata;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONObject;

import app.Main;

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
    }

    static void versionMismatchWarnings(JFrame frame) {

        // App version mismatch
        if (!Main.projectAppVersion.equals(Main.APP_VERSION)) {
            JOptionPane.showMessageDialog(frame,
                "This project was saved using Rank Anything version "
                + Main.projectAppVersion + ".\n"
                + "You are currently using version " + Main.APP_VERSION + ".",
                "Version Mismatch",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        // Missing or corrupted file format
        if (Main.projectFileFormatVersion <= 0) {
            JOptionPane.showMessageDialog(frame, 
                """
                    This project does not contain valid file format information.
                    If you save this project, it will be converted to the current file format.
                """,
                "Missing File Format",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        // Old file format
        if (Main.projectFileFormatVersion < Main.FILE_FORMAT_VERSION
            && Main.projectFileFormatVersion > 0
        ) {
            JOptionPane.showMessageDialog(frame, 
                """
                    This project was saved with an older file format.
                    If you save this project, it will be converted to the current file format.
                """,
                "Old File Format",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        // Newer file format
        if (Main.projectFileFormatVersion > Main.FILE_FORMAT_VERSION) {
            JOptionPane.showMessageDialog(frame, 
                """
                    This project was saved with a newer file format than this version of Rank Anything supports.
                    Some features may not load correctly.
                    If you save this project, it will be converted to the current file format.
                """,
                "Newer File Format",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}