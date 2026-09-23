package export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

import app.Main;
import metadata.Metadata;
import metadata.ProjectID;
import saveload.SaveLoad;

// writes files
final class Write {

    public static boolean export(JFrame frame, ExportPreferences prefs) {

        File exportFolder;

        if (prefs.rankProject) {
            // Export into: prefs.folder / prefs.rankFolder
            exportFolder = new File(prefs.folder, prefs.rankFolder);
        } else {
            // Export directly into prefs.folder
            exportFolder = new File(prefs.folder);
        }

        // Ensure folder exists
        if (!exportFolder.exists()) {
            exportFolder.mkdirs();
        }

        // Build full export path
        File exportFile = new File(exportFolder, prefs.fileName + "." + prefs.fileType.toLowerCase());

        boolean ok;

        switch (prefs.fileType.toUpperCase()) {
            case "PDF" -> ok = exportPDF(exportFile, prefs);
            case "HTML" -> ok = exportHTML(exportFile, prefs);
            case "TXT" -> ok = exportTXT(exportFile, prefs);
            case "PNG" -> ok = exportPNG(exportFile, prefs);
            default -> {
                System.err.println("Unknown export type: " + prefs.fileType);
                return false;
            }
        }

        // If export succeeded, handle snapshot if requested
        if (ok && prefs.rankProject) {
            exportSnapshot(exportFolder, prefs, frame);
        }

        return ok;
    }

    // TODO: placeholder
    private static boolean exportPDF(File file, ExportPreferences prefs) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("PDF Export\n");
            fw.write("Ranking: " + prefs.ranking + "\n");
            fw.write("Item Info: " + prefs.itemInfo + "\n");
            fw.write("Criteria Info: " + prefs.critInfo + "\n");
            fw.write("Metadata: " + prefs.metadata + "\n");
            fw.write("Notes: " + prefs.notes + "\n");
            fw.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // TODO: placeholder
    private static boolean exportHTML(File file, ExportPreferences prefs) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("<html><head><title>Export</title></head><body>\n");
            fw.write("<h1>Export</h1>\n");

            if (prefs.ranking) fw.write("<h2>Ranking</h2><p>...</p>\n");
            if (prefs.itemInfo) fw.write("<h2>Item Information</h2><p>...</p>\n");
            if (prefs.critInfo) fw.write("<h2>Criteria Information</h2><p>...</p>\n");
            if (prefs.metadata) fw.write("<h2>Metadata</h2><p>...</p>\n");
            if (prefs.notes) fw.write("<h2>Notes</h2><p>" + Main.projectNotes + "</p>\n");

            fw.write("</body></html>");
            fw.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // TODO: placeholder
    private static boolean exportTXT(File file, ExportPreferences prefs) {
        try {
            FileWriter fw = new FileWriter(file);

            fw.write("Export\n");
            fw.write("====================\n\n");

            if (prefs.ranking) fw.write("Ranking:\n...\n\n");
            if (prefs.itemInfo) fw.write("Item Information:\n...\n\n");
            if (prefs.critInfo) fw.write("Criteria Information:\n...\n\n");
            if (prefs.metadata) fw.write("Metadata:\n...\n\n");
            if (prefs.notes) fw.write("Notes:\n" + Main.projectNotes + "\n\n");

            fw.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // TODO: placeholder
    private static boolean exportPNG(File file, ExportPreferences prefs) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("PNG Export Placeholder\n");
            fw.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static void exportSnapshot(File exportFolder, ExportPreferences prefs, JFrame frame) {

        String name = prefs.rankFileName;
        if (!name.toLowerCase().endsWith(".rank")) {
            name += ".rank";
        }
        
        File snapshotFile = new File(exportFolder, name);

        // store current values before temporarily adjusting them for the save
        final String CURRENT_FILE_CREATED = Main.fileCreated;
        final String CURRENT_PROJECT_MODIFIED = Main.projectModified;
        final String CURRENT_PROJECT_ID = Main.projectId;

        final String TIMESTAMP = Metadata.now();
        Main.fileCreated = TIMESTAMP;
        Main.projectModified = TIMESTAMP;
        Main.projectId = Main.projectId + "-SNAPSHOT-" + ProjectID.generate();

        SaveLoad.saveToFile(snapshotFile, frame, true); // effectively auto

        // restore values
        Main.fileCreated = CURRENT_FILE_CREATED;
        Main.projectModified = CURRENT_PROJECT_MODIFIED;
        Main.projectId = CURRENT_PROJECT_ID;
    }
}