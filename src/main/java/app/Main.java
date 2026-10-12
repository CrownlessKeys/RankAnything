package app;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import app.initialize.CritInit;
import app.initialize.ItemInit;
import app.initialize.LayoutInit;
import app.initialize.RankInit;
import autosave.AutoIndicator;
import autosave.AutoTimer;
import criteria.CriteriaPanel;
import export.ExportPreferences;
import items.ItemsPanel;
import javafx.application.Platform;
import metadata.Metadata;
import metadata.ProjectID;
import ranking.RankingPanel;
import tasks.Count;
import tasks.Update;

public final class Main {
    public static final List<ItemsPanel> itemsList = new ArrayList<>();
    public static final List<CriteriaPanel> criteriaList = new ArrayList<>();

    public static RankingPanel rankingPanel;
    public static JPanel criteriaPanel;
    public static JPanel itemsPanel;
    public static JScrollPane rankingScroll;
    public static JScrollPane criteriaScroll;
    public static JScrollPane itemsScroll;

    public static JPanel criteriaRow;
    public static JPanel itemsRow;

    public static JCheckBox preciseToggleCriteria;
    public static JCheckBox preciseToggleItems;
    public static JCheckBox hideDisabledCriteria;
    public static JCheckBox hideDisabledItems;
    
    public static int criteriaCreationCounter = 0;
    public static int itemsCreationCounter = 0;

    public static boolean projectDirty = false; // for autosave
    public static boolean unsavedChanges = false; // for manual save

    public static JFrame frame;

    // metadata
    private static final String STARTUP_TIMESTAMP = Metadata.now();
    public static String projectName = "Untitled";
    public static String projectNotes = "";
    public static String projectCreated = STARTUP_TIMESTAMP;
    public static String fileCreated = "Project not saved";
    public static String projectModified = STARTUP_TIMESTAMP;
    public static final String APP_VERSION = "1.0.0-beta.1";
    public static String projectAppVersion = APP_VERSION;
    public static final int FILE_FORMAT_VERSION = 1;
    public static int projectFileFormatVersion = FILE_FORMAT_VERSION;
    public static boolean isOriginal = false;
    public static String projectId = ProjectID.generate();

    // export preferences for current project (default to null until the current project is exported)
    public static ExportPreferences projectExport = null;

    // directories
    public static final File APP_DATA_DIR = getAppDataDir();
    public static final File SETTINGS_FILE = new File(APP_DATA_DIR, "Settings.json");
    public static final File RECENT_PROJECTS_FILE = new File(APP_DATA_DIR, "RecentProjects.json");
    public static final File AUTOSAVE_DIR = new File(APP_DATA_DIR, "Autosaves");

    private static File getAppDataDir() {
        String home = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return new File(home + "/AppData/Roaming/RankAnything/");
        } else if (os.contains("mac")) {
            return new File(home + "/Library/Application Support/RankAnything/");
        } else {
            // literally any other OS
            return new File(home + "/.config/RankAnything/");
        }
    }

    public static void main(String[] args) {
        Platform.startup(() -> {}); // Start JavaFX
        Settings.load();
        RecentProjects.load();
        SwingUtilities.invokeLater(() -> {
            RankInit.build();
            CritInit.build();
            ItemInit.build();
            LayoutInit.build();
            AutoTimer.start(frame);
            AutoIndicator.updateText();
        });
    }

    public static void updatePrecisionCriteria(boolean precise) {
        Update.updatePrecisionCriteria(precise);
    }

    public static void updatePrecisionItems(boolean precise) {
        Update.updatePrecisionItems(precise);
    }

    public static boolean isCriteriaPrecise() {
        return preciseToggleCriteria.isSelected();
    }

    public static boolean isItemsPrecise() {
        return preciseToggleItems.isSelected();
    }

    public static void updateHideDisabledCriteria(boolean hide) {
        Update.updateHideDisabledCriteria(hide);
    }

    public static void updateHideDisabledItems(boolean hide) {
        Update.updateHideDisabledItems(hide);
    }

    public static boolean areDisabledCriteriaHidden() {
        return hideDisabledCriteria.isSelected();
    }

    public static boolean areDisabledItemsHidden() {
        return hideDisabledItems.isSelected();
    }

    public static int countEnabledItems() {
        return Count.countEnabledItems();
    }
    
    public static int countEnabledCriteria() {
        return Count.countEnabledCriteria();
    }
}