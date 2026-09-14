package metadata;

import java.io.File;
import java.time.LocalDateTime;

import javax.swing.JFrame;

import org.json.JSONObject;

public final class Metadata {

    public static void loadFromJSON(JSONObject root) {
        SaveLoadMeta.loadFromJSON(root);
    }

    public static void saveToJSON(JSONObject root, boolean isAuto) {
        SaveLoadMeta.saveToJSON(root, isAuto);
    }

    public static void versionMismatchWarnings(JFrame frame) {
        SaveLoadMeta.versionMismatchWarnings(frame);
    }

    public static boolean checkExtension(JFrame frame, File file) {
        return Tasks.checkExtension(frame, file);
    }

    public static void showProjectInfoDialog(JFrame frame) {
        ProjectInfo.showProjectInfoDialog(frame);
    }

    public static void showNotesEditor(JFrame frame) {
        ProjectInfo.showNotesEditor(frame);
    }

    public static void showProjectNameEditor(JFrame frame) {
        ProjectInfo.showProjectNameEditor(frame);
    }

    public static void updateWindowTitle(JFrame frame) {
        Tasks.updateWindowTitle(frame);
    }

    public static void openFileLocation(JFrame frame) {
        Tasks.openFileLocation(frame);
    }

    public static String now() {
        return LocalDateTime.now().toString();
    }

    public static String formatTimestamp(String ts) {
        return Tasks.formatTimestamp(ts);
    }
}