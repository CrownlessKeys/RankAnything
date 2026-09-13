package saveload;

import java.io.File;

import javax.swing.JFrame;

public final class SaveLoad {

    static File currentProjectFile = null;

    public static void save(JFrame frame) {
        Save.save(frame);
    }

    public static void saveAs(JFrame frame) {
        SaveAs.save(frame);
    }

    public static void load(JFrame frame) {
        Load.load(frame);
    }

    public static void newProject(JFrame frame) {
        Load.newProject(frame);
    }

    public static void loadFromFile(File file, JFrame frame, boolean isAuto) {
        Load.loadFromFile(file, frame, isAuto);
    }

    public static void saveToFile(File file, JFrame frame, boolean isAuto) {
        SaveToFile.save(file, frame, isAuto);
    }

    public static String getCurrentFilePath() {
        if (currentProjectFile != null) {
            return currentProjectFile.getAbsolutePath();
        } else {
            return "Project Not Saved";
        }
    }
}