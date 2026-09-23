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
    public static boolean autoRenameFiles;

    // autosave settings
    public static int autosaveIntervalSeconds;
    public static int autosaveMaxPerProject;
    public static boolean autosaveUnlimitedPerProject;
    public static int autosaveMaxTotal;
    public static boolean autosaveUnlimitedTotal;
    public static boolean autosaveOnClose;
    public static boolean autosavePeriodically;

    // default project settings
    public static boolean preciseSlidersCriteria;
    public static boolean preciseSlidersItems;
    public static boolean hideDisabledCriteria;
    public static boolean hideDisabledItems;
    public static RankingMode rankingMode;

    // universal default export settings
    public static ExportPreferences defaultExport;
    
    // "Don't show again" settings
    public static boolean showRenameWarning;
    public static boolean showAutosaveDeleteWarning;

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

            preciseSlidersCriteria = root.optBoolean("preciseSlidersCriteria", false);
            preciseSlidersItems = root.optBoolean("preciseSlidersItems", false);
            hideDisabledCriteria = root.optBoolean("hideDisabledCriteria", false);
            hideDisabledItems = root.optBoolean("hideDisabledItems", false);
            String rankingModeStr = root.optString("rankingMode", RankingMode.WEIGHTED_ENABLED.name()); 
            try {
                rankingMode = RankingMode.valueOf(rankingModeStr);
            } catch (IllegalArgumentException e) {
                rankingMode = RankingMode.WEIGHTED_ENABLED; // default if invalid
            }

            // universal most recent export preferences
            if (root.has("defaultExport")) {
                defaultExport = ExportPreferences.fromJSON(root.getJSONObject("defaultExport"));
            } else {
                // sensible defaults
                defaultExport = new ExportPreferences(
                    true,  // ranking
                    true,  // itemInfo
                    true,  // critInfo
                    true,  // metadata
                    true,  // notes
                    "PDF",
                    "",
                    "",
                    false, // rankProject
                    "",
                    "",
                    true   // confirmed
                );
            }

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

            root.put("preciseSlidersCriteria", preciseSlidersCriteria);
            root.put("preciseSlidersItems", preciseSlidersItems);
            root.put("hideDisabledCriteria", hideDisabledCriteria);
            root.put("hideDisabledItems", hideDisabledItems);
            root.put("rankingMode", rankingMode.name());

            root.put("defaultExport", defaultExport.toJSON());

            root.put("showRenameWarning", showRenameWarning);
            root.put("showAutosaveDeleteWarning", showAutosaveDeleteWarning);

            File file = Main.SETTINGS_FILE;
            file.getParentFile().mkdirs(); // ensure directory exists

            Files.writeString(file.toPath(), root.toString(4), StandardCharsets.UTF_8);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void restoreDefaults(JFrame frame) {
        
        Object[] params = {
            "This will reset ALL settings to their default values.",
            "Are you sure you want to continue?",
        };

        int result = JOptionPane.showConfirmDialog(
            frame,
            params,
            "Restore Default Settings",
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