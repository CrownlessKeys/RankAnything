package autosave;

import javax.swing.JFrame;

import app.Main;

public final class Autosave {
    
    public static void autosave(JFrame frame) {
        if (Main.projectDirty) Auto.save(frame);
    }
    
    public static void checkForNewerAutosave(JFrame frame) {
        Auto.checkForNewerAutosave(frame);
    }

    public static void showAutosaveSettings(JFrame frame) {
        AutoSettings.show(frame);
    }
}