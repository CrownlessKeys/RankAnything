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

    private static final class Defaults {

        // miscellaneous settings
        static final boolean autoRenameFiles = true;

        // autosave settings
        static final int autosaveIntervalSeconds = 5 * 60;
        static final int autosaveMaxPerProject = 10;
        static final boolean autosaveUnlimitedPerProject = false;
        static final int autosaveMaxTotal = 100;
        static final boolean autosaveUnlimitedTotal = true;
        static final boolean autosaveOnClose = true;
        static final boolean autosavePeriodically = true;

        // default project settings
        static final boolean preciseSlidersCriteria = false;
        static final boolean preciseSlidersItems = false;
        static final boolean hideDisabledCriteria = false;
        static final boolean hideDisabledItems = false;
        static final RankingMode rankingMode = RankingMode.WEIGHTED_ENABLED;

        // last opened folder for load dialog
        static final String lastOpenedProjectFolder = null;
        
        // "Don't show again" settings
        static final boolean showRenameWarning = true;
        static final boolean showAutosaveDeleteWarning = true;
    }

    // miscellaneous settings
    public static boolean autoRenameFiles = Defaults.autoRenameFiles;

    // autosave settings
    public static int autosaveIntervalSeconds = Defaults.autosaveIntervalSeconds;
    public static int autosaveMaxPerProject = Defaults.autosaveMaxPerProject;
    public static boolean autosaveUnlimitedPerProject = Defaults.autosaveUnlimitedPerProject;
    public static int autosaveMaxTotal = Defaults.autosaveMaxTotal;
    public static boolean autosaveUnlimitedTotal = Defaults.autosaveUnlimitedTotal;
    public static boolean autosaveOnClose = Defaults.autosaveOnClose;
    public static boolean autosavePeriodically = Defaults.autosavePeriodically;

    // default project settings
    public static boolean preciseSlidersCriteria = Defaults.preciseSlidersCriteria;
    public static boolean preciseSlidersItems = Defaults.preciseSlidersItems;
    public static boolean hideDisabledCriteria = Defaults.hideDisabledCriteria;
    public static boolean hideDisabledItems = Defaults.hideDisabledItems;
    public static RankingMode rankingMode = Defaults.rankingMode;

    // universal default export settings
    public static ExportPreferences defaultExport = new ExportPreferences(); // initialize to defaults

    // last opened folder for load dialog
    public static String lastOpenedProjectFolder = Defaults.lastOpenedProjectFolder;

    // "Don't show again" settings
    public static boolean showRenameWarning = Defaults.showRenameWarning;
    public static boolean showAutosaveDeleteWarning = Defaults.showAutosaveDeleteWarning;

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

            autoRenameFiles = root.optBoolean("autoRenameFiles", Defaults.autoRenameFiles);
            
            autosaveIntervalSeconds = root.optInt("autosaveIntervalSeconds", Defaults.autosaveIntervalSeconds);
            autosaveMaxPerProject = root.optInt("autosaveMaxPerProject", Defaults.autosaveMaxPerProject);
            autosaveUnlimitedPerProject = root.optBoolean("autosaveUnlimitedPerProject", Defaults.autosaveUnlimitedPerProject);
            autosaveMaxTotal = root.optInt("autosaveMaxTotal", Defaults.autosaveMaxTotal);
            autosaveUnlimitedTotal = root.optBoolean("autosaveUnlimitedTotal", Defaults.autosaveUnlimitedTotal);
            autosaveOnClose = root.optBoolean("autosaveOnClose", Defaults.autosaveOnClose);
            autosavePeriodically = root.optBoolean("autosavePeriodically", Defaults.autosavePeriodically);

            preciseSlidersCriteria = root.optBoolean("preciseSlidersCriteria", Defaults.preciseSlidersCriteria);
            preciseSlidersItems = root.optBoolean("preciseSlidersItems", Defaults.preciseSlidersItems);
            hideDisabledCriteria = root.optBoolean("hideDisabledCriteria", Defaults.hideDisabledCriteria);
            hideDisabledItems = root.optBoolean("hideDisabledItems", Defaults.hideDisabledItems);

            String rankingModeStr = root.optString("rankingMode", Defaults.rankingMode.name());
            try {
                rankingMode = RankingMode.valueOf(rankingModeStr);
            } catch (IllegalArgumentException e) {
                rankingMode = Defaults.rankingMode;
            }

            // universal most recent export preferences
            if (root.has("defaultExport") && !root.isNull("defaultExport")) {
                defaultExport = ExportPreferences.fromJSON(root.getJSONObject("defaultExport"));
            } else {
                defaultExport = new ExportPreferences(); // set defaults
            }

            lastOpenedProjectFolder = root.optString("lastOpenedProjectFolder", Defaults.lastOpenedProjectFolder);

            showRenameWarning = root.optBoolean("showRenameWarning", Defaults.showRenameWarning);
            showAutosaveDeleteWarning = root.optBoolean("showAutosaveDeleteWarning", Defaults.showAutosaveDeleteWarning);

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

            root.put("defaultExport", defaultExport != null ? defaultExport.toJSON() : new ExportPreferences().toJSON());

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