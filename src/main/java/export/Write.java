package export;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import app.Main;
import metadata.Metadata;
import metadata.ProjectID;
import saveload.SaveLoad;

// handles file writing
final class Write {

    // manages exports
    // returns whether export was successful
    static boolean export(JFrame frame, ExportPreferences prefs) {

        File exportFolder;

        if (prefs.rankProject) {
            // Export into: prefs.folder / prefs.rankFolder
            String folder = prefs.folder.replaceAll("[<>:\"/\\\\|?*]", "").trim();
            if (folder.isEmpty()) folder = "export";
            String rankFolder = prefs.rankFolder.replaceAll("[<>:\"/\\\\|?*]", "").trim();
            if (rankFolder.isEmpty()) rankFolder = "export";

            exportFolder = new File(folder, rankFolder);
        } else {
            // Export directly into prefs.folder
            exportFolder = new File(prefs.folder);
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