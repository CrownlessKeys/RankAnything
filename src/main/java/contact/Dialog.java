package contact;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.Main;
import saveload.SaveLoad;
import tasks.Browse;

public final class Dialog {

    private static final String BUG_GOOGLE_FORM_URL =
            "https://docs.google.com/forms/d/e/1FAIpQLSdLaiEjGqzWIU1LatuD9p38J1OW1XYiNI_WaQI4KTFxhEbZlg/viewform?usp=pp_url";

    static void showBugReportDialog() {

        JDialog dialog = new JDialog(Main.frame, "Report a bug", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        Consumer<JComponent> left = c -> c.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel title = new JLabel(
            "<html>Please select what information you would like to include<br>"
            +     "in the report. Including more information helps me solve<br>"
            +     "the problem. Thank you!<br><br>"
            +     "Press <b>OK</b> to open the Google Form.</html>"
        );
        left.accept(title);
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));

        // Checkboxes
        JCheckBox includeProject = new JCheckBox("Include current project information");
        includeProject.setSelected(true);

        JCheckBox includeSettings = new JCheckBox("<html>Include <i>Rank Anything</i> settings</html>");
        includeSettings.setSelected(true);

        JCheckBox includeSystem = new JCheckBox("Include basic system information");
        includeSystem.setSelected(true);

        left.accept(includeProject);
        left.accept(includeSettings);
        left.accept(includeSystem);

        panel.add(includeProject);
        panel.add(includeSettings);
        panel.add(includeSystem);
        panel.add(Box.createVerticalStrut(15));

        // Buttons
        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        left.accept(buttonRow);

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(ok);
        buttonRow.add(Box.createHorizontalStrut(10));
        buttonRow.add(cancel);
        buttonRow.add(Box.createHorizontalGlue());

        panel.add(buttonRow);
        panel.add(Box.createVerticalStrut(10));

        // Legacy link
        JLabel legacy = new JLabel("<html><i>Send by email instead</i></html>");
        legacy.setForeground(new Color(120, 120, 120));
        legacy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        left.accept(legacy);

        legacy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
                LegacyDialog.showBugReportDialog();
            }
        });

        panel.add(legacy);

        // OK button → open Google Form with encoded parameters
        ok.addActionListener(e -> {
            try {
                String notIncluded = URLEncoder.encode("Not included", StandardCharsets.UTF_8);

                String project = URLEncoder.encode(SaveLoad.buildProjectJSON().toString(4), StandardCharsets.UTF_8);
                
                String rawSettings = "";
                try {
                    rawSettings = Files.readString(Main.SETTINGS_FILE.toPath());
                } catch (IOException ex) {}

                String rawRecentProjects = "";
                try {
                    rawRecentProjects = Files.readString(Main.RECENT_PROJECTS_FILE.toPath());
                } catch (IOException ex) {}

                String rawCombinedSettings = rawSettings + "\n\n" + rawRecentProjects;
                String settings = URLEncoder.encode(rawCombinedSettings, StandardCharsets.UTF_8);

                String rawSystem = """
                    App version: %s
                    File format version: %s
                    Java version: %s
                    OS: %s %s
                """.formatted(
                    Main.APP_VERSION, 
                    Main.FILE_FORMAT_VERSION,
                    System.getProperty("java.version"), 
                    System.getProperty("os.name"), 
                    System.getProperty("os.version")
                );
                String system = URLEncoder.encode(rawSystem, StandardCharsets.UTF_8);

                String url = BUG_GOOGLE_FORM_URL
                        + "&entry.593604431=" + (includeProject.isSelected() ? project : notIncluded)
                        + "&entry.783400744=" + (includeSettings.isSelected() ? settings : notIncluded)
                        + "&entry.1751666920=" + (includeSystem.isSelected() ? system : notIncluded);
                
                if (url.length() > 8000) {
                    url = BUG_GOOGLE_FORM_URL
                            + "&entry.783400744=" + (includeSettings.isSelected() ? settings : notIncluded)
                            + "&entry.1751666920=" + (includeSystem.isSelected() ? system : notIncluded);
                    // TODO: Copy and paste JSON manually
                }
                if (url.length() > 8000) {
                    url = BUG_GOOGLE_FORM_URL
                            + "&entry.1751666920=" + (includeSystem.isSelected() ? system : notIncluded);
                    // TODO: Copy and paste JSON manually
                }
                if (url.length() > 8000) {
                    url = BUG_GOOGLE_FORM_URL;
                    // TODO: Copy and paste JSON manually
                }

                Browse.openWebpage(Main.frame, url);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            dialog.dispose();
        });

        cancel.addActionListener(e -> dialog.dispose());

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(Main.frame);
        dialog.setVisible(true);
    }
}