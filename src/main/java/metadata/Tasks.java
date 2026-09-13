package metadata;

import java.awt.Desktop;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;

import app.Main;
import saveload.SaveLoad;
import tasks.Notify;

final class Tasks {
    static void updateWindowTitle(JFrame frame) {
        String title = Main.baseWindowTitle;

        if (Main.projectName != null) {
            title += " — " + Main.projectName;
        } else {
            title += " — Untitled";
        }

        if (Main.unsavedChanges) title += "*";
        
        frame.setTitle(title);
    }

    static String formatTimestamp(String ts) {
        if (ts == null) return "Unknown";

        try {
            LocalDateTime dt = LocalDateTime.parse(ts);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM d, yyyy — h:mm:ss a");
            return dt.format(fmt);
        } catch (Exception ex) {
            return ts; // fallback
        }
    }

    static void openFileLocation(JFrame frame) {
        String path = SaveLoad.getCurrentFilePath();
        if (path != null && !path.equals("Project Not Saved")) {
            try {
                File file = new File(path);
                Desktop.getDesktop().open(file.getParentFile());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Notify.showToast(frame, "Project Not Saved");
        }
    }
}
