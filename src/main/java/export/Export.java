package export;

import app.Main;
import app.Settings;

public final class Export {
    
    public static void export() {
        Dialog.show(
            Main.frame, 
            Main.projectExport == null // should be null if project has not yet been exported 
                    ? Settings.defaultExport // if project has not yet been exported, use universal last-used preferences
                    : Main.projectExport // if it has, use preferences that project last used
        );
    }
    
    public static void print() {
        Print.dialog(
            Main.frame, 
            Main.projectExport == null // should be null if project has not yet been exported 
                    ? Settings.defaultExport // if project has not yet been exported, use universal last-used preferences
                    : Main.projectExport // if it has, use preferences that project last used
        );
    }
}