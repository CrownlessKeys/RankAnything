package saveload;

import java.io.File;
import java.io.FileWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import app.Main;
import criteria.CriteriaPanel;
import items.ItemsPanel;
import metadata.Metadata;
import tasks.Notify;

final class SaveToFile {
    static void save(File file, JFrame frame, boolean isAuto) {
        try {
            if (!isAuto && !Metadata.versionMismatchWarningsSave(frame)) return;
            if (!isAuto && file == SaveLoad.currentProjectFile) Metadata.checkFileName(frame);

            JSONObject root = buildProjectJSON(isAuto);

            try (FileWriter fw = new FileWriter(file)) {
                fw.write(root.toString(2));
            }

            SaveLoad.currentProjectFile = file;
            
            if (!isAuto) Notify.showToast(frame, "Project saved");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                "Failed to save project:\n" + ex.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    static JSONObject buildProjectJSON(boolean isAuto) {
        JSONObject root = new JSONObject();

        Metadata.saveToJSON(root, isAuto);

        // Criteria and Items
        JSONArray criteriaArray = new JSONArray();
        JSONArray itemsArray = new JSONArray();

        // Save criteria
        for (CriteriaPanel cp : Main.criteriaList) {
            JSONObject obj = new JSONObject();
            obj.put("name", cp.getCriterionName());
            obj.put("weight", cp.getWeight());
            obj.put("enabled", cp.isEnabledCriterion());
            obj.put("creationIndex", cp.creationIndex);
            criteriaArray.put(obj);
        }

        // Save items
        for (ItemsPanel ip : Main.itemsList) {
            JSONObject obj = new JSONObject();
            obj.put("name", ip.getItemName());
            obj.put("enabled", ip.isEnabledItem());
            obj.put("collapsed", ip.isCollapsed());
            obj.put("creationIndex", ip.creationIndex);

            JSONObject scores = new JSONObject();
            for (CriteriaPanel cp : Main.criteriaList) {
                double score = ip.getScoreForCriterion(cp);
                scores.put(String.valueOf(cp.creationIndex), score);
            }
            obj.put("scores", scores);

            itemsArray.put(obj);
        }

        root.put("criteria", criteriaArray);
        root.put("items", itemsArray);

        root.put("criteriaCreationCounter", Main.criteriaCreationCounter);
        root.put("itemsCreationCounter", Main.itemsCreationCounter);

        root.put("isCriteriaPrecise", Main.isCriteriaPrecise());
        root.put("isItemsPrecise", Main.isItemsPrecise());

        root.put("areDisabledCriteriaHidden", Main.areDisabledCriteriaHidden());
        root.put("areDisabledItemsHidden", Main.areDisabledItemsHidden());

        root.put("rankingMode", Main.rankingPanel.rankingMode);

        root.put("projectId", Main.projectId);

        return root;
    }
}