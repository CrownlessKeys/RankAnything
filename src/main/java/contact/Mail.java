package contact;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.file.Files;

import javax.swing.JOptionPane;

import app.Main;
import saveload.SaveLoad;

final class Mail {
    // Core method: opens the user's email client
    // returns false if mailing failed so we can work on sending manually
    static boolean openEmail(String subject, String body) {
        try {
            Desktop desktop = Desktop.getDesktop();

            if (!desktop.isSupported(Desktop.Action.MAIL)) {
                return false;
            }

            // Probe: does the system actually have a mail client?
            try {
                desktop.mail(new URI("mailto:"));
            } catch (Exception probeFailure) {
                return false;
            }

            // Build the real mailto link
            String mailto = "mailto:" + Contact.DEV_EMAIL +
                    "?subject=" + encode(subject) +
                    "&body=" + encode(body);

            desktop.mail(new URI(mailto));
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // Utility: safely encode text for mailto links
    private static String encode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            return text; // fallback
        }
    }

    static void bugReport(
        String description, // bug description
        String steps, // steps to reproduce
        String expected, // expected behavior
        String actual, // actual behavior
        boolean includeProject, // user project file 
        boolean includeSettings, // user Settings.json 
        boolean includeSystemInfo, // OS, Java version, and Rank Anything version
        boolean reply, // does the user want a follow-up
        boolean manual // manual email vs open email app
    ) {
        String subject = "Rank Anything: Bug Report";

        String settingsJson = "";
        try {
            settingsJson = Files.readString(Main.SETTINGS_FILE.toPath());
        } catch (IOException e) {}

        String recentProjectsJson = "";
        try {
            recentProjectsJson = Files.readString(Main.RECENT_PROJECTS_FILE.toPath());
        } catch (IOException e) {}

        String projectJson = SaveLoad.buildProjectJSON().toString();

        String body = """
            Bug description:
            %s

            Steps to reproduce:
            %s

            Expected behavior:
            %s

            Actual behavior:
            %s

        """.formatted(description, steps, expected, actual) + (includeSystemInfo ? """
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
        ) : "") + (
            reply ? "I would like a follow-up response." : "I would not like a follow-up response."
        ) + (includeSettings ? """

            
            Settings.json:
        %s

            RecentProjects.json:
        %s

        """.formatted(
            settingsJson,
            recentProjectsJson
        ) : "") + (includeProject ? """
            Project JSON:
        %s
        """.formatted(projectJson) : "");

        if (!manual && body.length() > 1800) {
            emailTooLong();
            LegacyManual.manualBugReport(subject, body);
            return;
        }

        if (manual) {
            LegacyManual.manualBugReport(subject, body);
        } else if (!Mail.openEmail(subject, body)) {
            emailFailed();
            LegacyManual.manualBugReport(subject, body);
        }
    }

    static void contactFeatureRequest() {

        String subject = "Rank Anything: Feature Request";
        String body = "";

        if (!Mail.openEmail(subject, body)) {
            emailFailed();
            LegacyManual.manualFeatureRequest();
        }
    }

    static void contactGeneral() {

        String subject = "";
        String body = "";

        if (!Mail.openEmail(subject, body)) {
            emailFailed();
            LegacyManual.manualContact();
        }
    }

    private static void emailFailed() {
        JOptionPane.showMessageDialog(
            Main.frame,
            """
                Rank Anything could not open your email app.

                This usually happens when:
                • No default mail app is set
                • Only webmail (Gmail/Outlook.com) is used
                • Windows is set to open mailto links in a browser

                You can still email the developer manually.
                Please select "OK" to proceed with this.
            """,
            "Unable to Open Email",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private static void emailTooLong() {
        JOptionPane.showMessageDialog(
            Main.frame,
            """
                The email is too long to be automatically 
                opened in your email app. This is likely 
                because of the size of your current project.

                You can still email the developer manually.
                Please select "OK" to proceed with this.
            """,
            "Unable to Open Email",
            JOptionPane.ERROR_MESSAGE
        );
    }
}