package saveload;

import java.io.File;

import javax.swing.JFrame;

import org.json.JSONObject;

public final class SaveLoad {

    public static File currentProjectFile = null;

    public static void save(JFrame frame) {
        Save.save(frame);
    }

    public static void saveAs(JFrame frame) {
        Save.saveAs(frame);
    }

    public static void load(JFrame frame) {
        Load.load(frame);
    }

    public static void newProject(JFrame frame) {
        Load.newProject(frame);
    }

    public static void reloadProject(JFrame frame) {
        Load.reloadProject(frame);
    }

    public static void loadFromFile(File file, JFrame frame, boolean isAuto) {
        Load.loadFromFile(file, frame, isAuto);
    }

    public static void saveToFile(File file, JFrame frame, boolean isAuto) {
        SaveToFile.save(file, frame, isAuto);
    }

    public static JSONObject buildProjectJSON() {
        return SaveToFile.buildProjectJSON(true); // auto true since this method should only create the JSON object without changing anything
    }

    public static String getCurrentFilePath() {
        if (currentProjectFile != null) {
            return currentProjectFile.getAbsolutePath();
        } else {
            return "Project not saved";
        }
    }
}