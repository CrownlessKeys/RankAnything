package export;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import app.Main;
import metadata.Metadata;
import metadata.ProjectID;
import saveload.SaveLoad;

// manages file writing
final class Write {

    // manages exports
    // returns whether export was successful
    static boolean export(JFrame frame, ExportPreferences prefs) {

        File exportFolder;
        File baseFolder = new File(prefs.folder);

        if (prefs.rankProject) {
            // sanitize the sub-folder name typing, not the parent path
            String rankFolder = prefs.rankFolder.replaceAll("[<>:\"/\\\\|?*]", "").trim();
            if (rankFolder.isEmpty()) rankFolder = "export";

            // links base folder with the safe sub-folder name
            exportFolder = new File(baseFolder, rankFolder);
        } else {
            // export directly into the selected parent folder
            exportFolder = baseFolder;
        }

        // Ensure folder exists
        if (!exportFolder.exists() && !exportFolder.mkdirs()) {
            JOptionPane.showMessageDialog(frame, 
                "Cannot create folder: " + exportFolder,
                "Export failed",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        // Sanitize file name
        String fileName = prefs.fileName.replaceAll("[<>:\"/\\\\|?*]", "").trim();
        if (fileName.isEmpty()) fileName = "untitled"; // fallback to avoid empty file names

        // create project file and end early if we are creating a project file
        boolean proj = prefs.fileType.contains("(project file)");
        if (proj) {
            exportSnapshot(exportFolder, fileName, prefs.fileType, frame);
            return true;
        }

        // add extension here if not project file
        String fileExtension = "." + prefs.fileType.toLowerCase();
        if (!fileName.toLowerCase().endsWith(fileExtension)) {
            fileName += fileExtension;
        }

        File exportFile = new File(exportFolder, fileName);

        // auto rename if file exists
        File f = exportFile;
        int counter = 1;
        while (f.exists()) {
            f = new File(exportFolder, "(" + counter++ + ")_" + fileName);
        }
        exportFile = f;

        boolean success; // whether export succeeded

        switch (prefs.fileType.toUpperCase()) {
            case "PDF" -> success = PDF.exportPDF(exportFile, prefs);
            default -> {
                System.err.println("Unknown export type: " + prefs.fileType);
                return false;
            }
        }

        // If export succeeded, handle snapshot if requested
        if (success && prefs.rankProject) {
            String rankFileName = prefs.rankFileName.replaceAll("[<>:\"/\\\\|?*]", "").trim();
            if (rankFileName.isEmpty()) rankFileName = "snapshot"; // fallback
            exportSnapshot(exportFolder, rankFileName, prefs.fileType, frame);
        }

        return success;
    }

    private static void exportSnapshot(File exportFolder, String fileName, String fileType, JFrame frame) {

        // add file extension
        boolean JSON = fileType.equals("JSON (project file)");
        String fileExtension = JSON ? ".json" : ".rank";
        if (!fileName.toLowerCase().endsWith(fileExtension)) {
            fileName += fileExtension;
        }
        
        File snapshotFile = new File(exportFolder, fileName);
        
        // auto rename if file exists
        File f = snapshotFile;
        int counter = 1;
        while (f.exists()) {
            f = new File(exportFolder, "(" + counter++ + ")_" + fileName);
        }
        snapshotFile = f;

        // store current values before temporarily adjusting them for the save
        final String CURRENT_FILE_CREATED = Main.fileCreated;
        final String CURRENT_PROJECT_MODIFIED = Main.projectModified;
        final String CURRENT_PROJECT_ID = Main.projectId;

        final String TIMESTAMP = Metadata.now();
        Main.fileCreated = TIMESTAMP;
        Main.projectModified = TIMESTAMP;
        Main.projectId = Main.projectId + "-EXPORT-" + ProjectID.generate();

        SaveLoad.saveToFile(snapshotFile, frame, true); // effectively auto

        // restore values
        Main.fileCreated = CURRENT_FILE_CREATED;
        Main.projectModified = CURRENT_PROJECT_MODIFIED;
        Main.projectId = CURRENT_PROJECT_ID;
    }
}