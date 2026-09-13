package autosave;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import app.Settings;

public final class AutoIndicator {

    private static final JLabel label = new JLabel("", SwingConstants.RIGHT);

    // Last autosave timestamp
    private static LocalDateTime lastAutosaveTime = null;

    // Timer that updates "X minutes ago"
    private static final Timer updateTimer;

    static {
        label.setForeground(new Color(130, 130, 130)); // subtle gray
        label.setText(""); // blank until autosave is enabled

        updateTimer = new Timer(60_000, e -> updateText()); // every minute
        updateTimer.setRepeats(true);
        updateTimer.start();
    }

    public static JLabel getLabel() {
        return label;
    }

    // called when a project loads
    public static void reset() {
        lastAutosaveTime = null;
        updateText();
    }

    // called when an autosave is successful
    public static void markAutosaved() {
        lastAutosaveTime = LocalDateTime.now();
        updateText();
    }

    public static void updateText() {

        // blank when autosave periodically is disabled
        if (!Settings.autosavePeriodically) {
            label.setText("");
            return;
        }

        // no autosaves yet
        if (lastAutosaveTime == null) {
            label.setText("Not yet autosaved");
            return;
        }

        // calculate time difference
        Duration d = Duration.between(lastAutosaveTime, LocalDateTime.now());
        long minutes = d.toMinutes();

        if (minutes <= 0) {
            label.setText("Last autosaved just now");
        } else if (minutes == 1) {
            label.setText("Last autosaved 1 minute ago");
        } else {
            label.setText("Last autosaved " + minutes + " minutes ago");
        }
    }
}