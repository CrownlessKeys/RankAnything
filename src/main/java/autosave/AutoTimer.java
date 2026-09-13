package autosave;

import javax.swing.JFrame;
import javax.swing.Timer;

import app.Settings;

public final class AutoTimer {

    private static Timer timer;

    public static void start(JFrame frame) {
        if (!Settings.autosavePeriodically) return;
        int intervalMs = Settings.autosaveIntervalSeconds * 1000;
        timer = new Timer(intervalMs, e -> {
            if (Settings.autosavePeriodically) Autosave.autosave(frame);
        });
        timer.start();
    }

    public static void restart(JFrame frame) {
        if (timer != null) timer.stop();
        start(frame);
    }

    public static void stop() {
        if (timer != null) timer.stop();
    }
}