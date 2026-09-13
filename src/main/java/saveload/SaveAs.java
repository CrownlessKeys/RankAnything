package saveload;

import java.awt.FileDialog;
import java.io.File;

import javax.swing.JFrame;

import app.Main;
import metadata.Metadata;
import metadata.ProjectID;

final class SaveAs {
    static void save(JFrame frame) {
        FileDialog dialog = new FileDialog(frame, "Save Project As", FileDialog.SAVE);
        dialog.setFile(Main.projectName + ".rank");
        dialog.setVisible(true);

        if (dialog.getFile() != null) {
            String fileName = dialog.getFile();
            if (!fileName.toLowerCase().endsWith(".rank")) fileName += ".rank";
            
            File file = new File(dialog.getDirectory(), fileName);

            Main.isOriginal = Main.fileCreated.equals("Project Not Saved");
            Main.fileCreated = Metadata.now();
            if (!Main.fileCreated.equals("Project Not Saved")) Main.projectId = ProjectID.generate();

            SaveToFile.save(file, frame, false);

            Main.unsavedChanges = false;
            Main.projectDirty = false;
            Metadata.updateWindowTitle(frame);
        }
    }
}