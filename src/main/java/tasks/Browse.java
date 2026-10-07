package tasks;

import java.awt.Component;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public final class Browse {
    
    private static final ImageIcon copyIcon = new ImageIcon(
        "src/main/resources/iconmonstr-copy-lined.png"
    );

    // Initialize an official logger for this class
    private static final Logger LOGGER = Logger.getLogger(Browse.class.getName());

    /**
     * Opens a URL string in the default web browser safely.
     * @param parentComponent The parent JFrame/Component for error dialog positioning.
     * @param urlString The absolute URL target to browse.
     */
    public static void openWebpage(Component parentComponent, String urlString) {
        // 1. Pre-flight check: Verify system desktop features are accessible
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            LOGGER.log(Level.WARNING, "System desktop environment or browser action is unsupported.");
            showErrorDialog(parentComponent, "Your system does not support opening external web links automatically.");
            return;
        }

        try {
            // 2. Perform the link actions safely
            URI uri = new URI(urlString);
            Desktop.getDesktop().browse(uri);
            
        } catch (URISyntaxException e) {
            // Catches malformed string formatting structures before attempting launch
            LOGGER.log(Level.SEVERE, "Failed to parse malformed string as a valid URI reference: " + urlString, e);
            showErrorDialog(parentComponent, "The web address format is malformed or invalid.");
            
        } catch (IOException e) {
            // Catches system/OS restrictions, missing browser drivers, or denied permissions
            LOGGER.log(Level.SEVERE, "Operating system failed to initiate a browser instance for URI: " + urlString, e);
            showErrorDialog(parentComponent, "Could not open your default web browser application.");
            
        } catch (SecurityException e) {
            // Catches restrictions where app sandbox or security managers block external processes
            LOGGER.log(Level.SEVERE, "Security manager blocked browser execution attempt.", e);
            showErrorDialog(parentComponent, "Permission denied by system security layout.");
        }
    }

    /**
     * Centralized GUI alert utility to alert the user of failure.
     */
    private static void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message + "\n\nPlease try manually opening your web browser.",
            "Link execution failed",
            JOptionPane.WARNING_MESSAGE
        );
    }
}