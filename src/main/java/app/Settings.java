package app;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.json.JSONObject;

public final class Settings {

    // miscellaneous settings
    public static boolean autoRenameFiles = true;

    // autosave settings
    public static int autosaveIntervalSeconds = 5 * 60;
    public static int autosaveMaxPerProject = 10;
    public static boolean autosaveUnlimitedPerProject = false;
    public static int autosaveMaxTotal = 100;
    public static boolean autosaveUnlimitedTotal = true;
    public static boolean autosaveOnClose = true;
    public static boolean autosavePeriodically = true;
    
    // "Don't show again" settings
    public static boolean showRenameWarning = true;
    public static boolean showAutosaveDeleteWarning = true;

    public static void load() {
        try {
            File file = Main.SETTINGS_FILE;
            if (!file.exists()) {
                save(); // create default settings
                return;
            }

            String json = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(json);

            autoRenameFiles = root.optBoolean("autoRenameFiles", true);
            
            autosaveIntervalSeconds = root.optInt("autosaveIntervalSeconds", 5 * 60);
            autosaveMaxPerProject = root.optInt("autosaveMaxPerProject", 10);
            autosaveUnlimitedPerProject = root.optBoolean("autosaveUnlimitedPerProject", false);
            autosaveMaxTotal = root.optInt("autosaveMaxTotal", 100);
            autosaveUnlimitedTotal = root.optBoolean("autosaveUnlimitedTotal", true);
            autosaveOnClose = root.optBoolean("autosaveOnClose", true);
            autosavePeriodically = root.optBoolean("autosavePeriodically", true);
            
            showRenameWarning = root.optBoolean("showRenameWarning", true);
            showAutosaveDeleteWarning = root.optBoolean("showAutosaveDeleteWarning", true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void save() {
        try {
            JSONObject root = new JSONObject();

            root.put("autoRenameFiles", autoRenameFiles);
            
            root.put("autosaveIntervalSeconds", autosaveIntervalSeconds);
            root.put("autosaveMaxPerProject", autosaveMaxPerProject);
            root.put("autosaveUnlimitedPerProject", autosaveUnlimitedPerProject);
            root.put("autosaveMaxTotal", autosaveMaxTotal);
            root.put("autosaveUnlimitedTotal", autosaveUnlimitedTotal);
            root.put("autosaveOnClose", autosaveOnClose);
            root.put("autosavePeriodically", autosavePeriodically);
            
            root.put("showRenameWarning", showRenameWarning);
            root.put("showAutosaveDeleteWarning", showAutosaveDeleteWarning);

            File file = Main.SETTINGS_FILE;
            file.getParentFile().mkdirs(); // ensure directory exists

            Files.writeString(file.toPath(), root.toString(4), StandardCharsets.UTF_8);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}