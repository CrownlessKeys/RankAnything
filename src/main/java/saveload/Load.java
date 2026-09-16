package saveload;

import java.awt.FileDialog;
import java.io.File;
import java.nio.file.Files;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import app.Main;
import app.RecentProjects;
import app.Settings;
import autosave.AutoIndicator;
import autosave.Autosave;
import criteria.CriteriaPanel;
import items.ItemsPanel;
import metadata.Metadata;
import metadata.ProjectID;
import ranking.RankingPanel.RankingMode;
import tasks.Create;
import tasks.Notify;

final class Load {
    // does nothing except let the user select a file for loadFromFile() to use
    static void load(JFrame frame) {        
        FileDialog dialog = new FileDialog(frame, "Open Project", FileDialog.LOAD);
        dialog.setVisible(true);

        if (dialog.getFile() != null) {
            File file = new File(dialog.getDirectory(), dialog.getFile());
            loadFromFile(file, frame, false);
        }
    }

    static void newProject(JFrame frame) {
        if (Settings.autosaveOnClose) Autosave.autosave(frame);

        try {
            // Create a temporary empty project file
            File temp = File.createTempFile("empty_project", ".rank");
            temp.deleteOnExit();

            // create temporary empty project JSON
            final String CREATION_TIMESTAMP = Metadata.now();
            JSONObject empty = new JSONObject();
            empty.put("appVersion", Main.APP_VERSION);
            empty.put("fileFormatVersion", Main.FILE_FORMAT_VERSION);
            empty.put("created", CREATION_TIMESTAMP);
            empty.put("fileCreated", "Project Not Saved");
            empty.put("modified", CREATION_TIMESTAMP);

            Files.writeString(temp.toPath(), empty.toString(4));
            loadFromFile(temp, frame, true); // effectively auto, for all intents and purposes
            Main.unsavedChanges = false; // except for this intent and purpose, so we fix it
            Metadata.updateWindowTitle(frame);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                "Failed to create new project:\n" + ex.getMessage(),
                "New Project Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    static void loadFromFile(File file, JFrame frame, boolean isAuto) {
        if (Settings.autosaveOnClose) Autosave.autosave(frame);
        if (!Metadata.checkExtension(frame, file)) return;

        try {
            String json = new String(Files.readAllBytes(file.toPath()));
            JSONObject root = new JSONObject(json);

            Metadata.loadFromJSON(root);
            Metadata.versionMismatchWarnings(frame);

            // Criteria and Items
            JSONArray criteriaArray = root.optJSONArray("criteria", new JSONArray());
            JSONArray itemsArray = root.optJSONArray("items", new JSONArray());

            Main.criteriaCreationCounter = root.optInt("criteriaCreationCounter", criteriaArray.length());
            Main.itemsCreationCounter = root.optInt("itemsCreationCounter", itemsArray.length());

            // Clear existing UI
            Main.criteriaList.clear();
            Main.itemsList.clear();
            Clear.clearCriteriaPanel();
            Clear.clearItemsPanel();
            Main.rankingPanel.clearAll();

            // Load criteria
            for (int i = 0; i < criteriaArray.length(); i++) {
                JSONObject obj = criteriaArray.getJSONObject(i);
                CriteriaPanel cp = Create.createCriterion(obj, frame);
                cp.refreshExtraInfo();
            }

            // Load items
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject obj = itemsArray.getJSONObject(i);
                ItemsPanel ip = Create.createItem(obj, frame);

                JSONObject scores = obj.getJSONObject("scores");
                for (CriteriaPanel cp : Main.criteriaList) {
                    double score = scores.getDouble(String.valueOf(cp.creationIndex));
                    ip.setScoreForCriterion(cp, score);
                }

                ip.refreshCriteria();
                ip.refreshExtraInfo();
            }

            Main.updatePrecisionCriteria(root.optBoolean("isCriteriaPrecise", Settings.preciseSlidersCriteria));
            Main.updatePrecisionItems(root.optBoolean("isItemsPrecise", Settings.preciseSlidersItems));

            Main.updateHideDisabledCriteria(root.optBoolean("areDisabledCriteriaHidden", Settings.hideDisabledCriteria));
            Main.updateHideDisabledItems(root.optBoolean("areDisabledItemsHidden", Settings.hideDisabledItems));

            String modeString = root.optString("rankingMode", Settings.rankingMode.name());
            RankingMode mode;
            try {
                mode = RankingMode.valueOf(modeString);
            } catch (IllegalArgumentException ex) {
                // fallback for invalid ranking mode
                mode = RankingMode.WEIGHTED_ENABLED;
            }
            Main.rankingPanel.rankingMode = mode;
            Main.rankingPanel.applyModeToToggles(mode);

            Main.rankingPanel.updateAllScores();

            String newId = ProjectID.ensureExists(file);
            boolean isDifferentProject = !Main.projectId.equals(newId);
            Main.projectId = newId;

            Main.criteriaPanel.revalidate();
            Main.criteriaPanel.repaint();
            Main.itemsPanel.revalidate();
            Main.itemsPanel.repaint();
            Main.rankingPanel.revalidate();
            Main.rankingPanel.repaint();

            if (!isAuto) {
                SaveLoad.currentProjectFile = file;
                RecentProjects.addRecentProject(file.getAbsolutePath());
                RecentProjects.save();
                RecentProjects.updateRecentProjectsMenu(frame);
                Autosave.checkForNewerAutosave(frame);
            } else if (isDifferentProject) {
                SaveLoad.currentProjectFile = null;
            }

            Main.unsavedChanges = isAuto;
            Main.projectDirty = false;
            AutoIndicator.reset();
            Metadata.updateWindowTitle(frame);
            Notify.showToast(frame, "Project loaded");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                "Failed to load project:\n" + ex.getMessage(),
                "Load Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}