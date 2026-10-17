package app;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONObject;

import autosave.AutoIndicator;
import export.ExportPreferences;
import ranking.RankingPanel.RankingMode;

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

    // default project settings
    public static boolean preciseSlidersCriteria = false;
    public static boolean preciseSlidersItems = false;
    public static boolean hideDisabledCriteria = false;
    public static boolean hideDisabledItems = false;
    public static RankingMode rankingMode = RankingMode.WEIGHTED_ENABLED;

    // universal default export settings
    public static ExportPreferences defaultExport = new ExportPreferences();

    // last opened folder for load dialog
    public static String lastOpenedProjectFolder = null;
    
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
            
            // handle completely empty or whitespace-only files gracefully
            if (json.trim().isEmpty()) {
                json = "{}";
            }
            
            JSONObject root = new JSONObject(json);

            autoRenameFiles = root.optBoolean("autoRenameFiles", autoRenameFiles);
            
            autosaveIntervalSeconds = root.optInt("autosaveIntervalSeconds", autosaveIntervalSeconds);
            autosaveMaxPerProject = root.optInt("autosaveMaxPerProject", autosaveMaxPerProject);
            autosaveUnlimitedPerProject = root.optBoolean("autosaveUnlimitedPerProject", autosaveUnlimitedPerProject);
            autosaveMaxTotal = root.optInt("autosaveMaxTotal", autosaveMaxTotal);
            autosaveUnlimitedTotal = root.optBoolean("autosaveUnlimitedTotal", autosaveUnlimitedTotal);
            autosaveOnClose = root.optBoolean("autosaveOnClose", autosaveOnClose);
            autosavePeriodically = root.optBoolean("autosavePeriodically", autosavePeriodically);

            preciseSlidersCriteria = root.optBoolean("preciseSlidersCriteria", preciseSlidersCriteria);
            preciseSlidersItems = root.optBoolean("preciseSlidersItems", preciseSlidersItems);
            hideDisabledCriteria = root.optBoolean("hideDisabledCriteria", hideDisabledCriteria);
            hideDisabledItems = root.optBoolean("hideDisabledItems", hideDisabledItems);
            
            String rankingModeStr = root.optString("rankingMode", rankingMode.name()); 
            try {
                rankingMode = RankingMode.valueOf(rankingModeStr);
            } catch (IllegalArgumentException e) {
                // keep default
            }

            // universal most recent export preferences
            if (root.has("defaultExport") && !root.isNull("defaultExport")) {
                defaultExport = ExportPreferences.fromJSON(root.getJSONObject("defaultExport"));
            }

            lastOpenedProjectFolder = root.optString("lastOpenedProjectFolder", lastOpenedProjectFolder);

            showRenameWarning = root.optBoolean("showRenameWarning", showRenameWarning);
            showAutosaveDeleteWarning = root.optBoolean("showAutosaveDeleteWarning", showAutosaveDeleteWarning);

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

            root.put("preciseSlidersCriteria", preciseSlidersCriteria);
            root.put("preciseSlidersItems", preciseSlidersItems);
            root.put("hideDisabledCriteria", hideDisabledCriteria);
            root.put("hideDisabledItems", hideDisabledItems);
            root.put("rankingMode", rankingMode.name());

            root.put("defaultExport", defaultExport != null ? defaultExport.toJSON() : JSONObject.NULL);

            root.put("lastOpenedProjectFolder", lastOpenedProjectFolder != null ? lastOpenedProjectFolder : JSONObject.NULL);

            root.put("showRenameWarning", showRenameWarning);
            root.put("showAutosaveDeleteWarning", showAutosaveDeleteWarning);

            File file = Main.SETTINGS_FILE;
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs(); // ensure directory exists
            }

            Files.writeString(file.toPath(), root.toString(4), StandardCharsets.UTF_8);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void restoreDefaults(JFrame frame) {
        
        int result = JOptionPane.showConfirmDialog(
            frame,
            "<html>This will reset ALL settings to<br>their default values. Are you<br>sure you want to continue?</html>",
            "Restore default settings",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return; // user cancelled
        }

        wipeSettingsFile();
        load();
        MenuBar.updateSettingsMenu();
        AutoIndicator.updateText();
    }

    private static void wipeSettingsFile() {
        try {
            // Delete existing file if it exists
            if (Main.SETTINGS_FILE.exists()) Main.SETTINGS_FILE.delete();

            // Ensure the app data directory exists
            if (!Main.APP_DATA_DIR.exists()) Main.APP_DATA_DIR.mkdirs();

            // Write an empty JSON object
            Files.writeString(
                Main.SETTINGS_FILE.toPath(),
                "{}",
                StandardCharsets.UTF_8
            );

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}