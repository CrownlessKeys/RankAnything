package tasks;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public final class Browse {
    
    private static final ImageIcon copyIcon = new ImageIcon(
        Browse.class.getResource("/iconmonstr-copy-lined.png")
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
            showErrorDialog(parentComponent, "Your system does not support opening external web links automatically.", urlString);
            return;
        }

        try {
            // 2. Perform the link actions safely
            URI uri = new URI(urlString);
            Desktop.getDesktop().browse(uri);
            
        } catch (URISyntaxException e) {
            // Catches malformed string formatting structures before attempting launch
            LOGGER.log(Level.SEVERE, "Failed to parse malformed string as a valid URI reference: " + urlString, e);
            showErrorDialog(parentComponent, "The web address format is malformed or invalid.", urlString);
            
        } catch (IOException e) {
            // Catches system/OS restrictions, missing browser drivers, or denied permissions
            LOGGER.log(Level.SEVERE, "Operating system failed to initiate a browser instance for URI: " + urlString, e);
            showErrorDialog(parentComponent, "Could not open your default web browser application.", urlString);
            
        } catch (SecurityException e) {
            // Catches restrictions where app sandbox or security managers block external processes
            LOGGER.log(Level.SEVERE, "Security manager blocked browser execution attempt.", e);
            showErrorDialog(parentComponent, "Permission denied by system security layout.", urlString);
        }
    }

    /**
     * Centralized GUI alert utility to alert the user of failure and provide the option to copy and open the link manually.
     */
    private static void showErrorDialog(Component parent, String message, String urlString) {
        // Run on the Event Dispatch Thread (EDT) to ensure thread-safe UI rendering
        SwingUtilities.invokeLater(() -> {

            // Main container panel with a vertical box layout
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            // 1. Error message label
            JLabel messageLabel = new JLabel(
                "<html>Could not open webpage.<br>" + message 
                + "<br><br>You can still copy and paste the link to open it manually.</html>"
            );
            messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(messageLabel);
            mainPanel.add(Box.createVerticalStrut(10));

            // 2. Container for the URL field and the copy button
            JPanel linkPanel = new JPanel();
            linkPanel.setLayout(new BoxLayout(linkPanel, BoxLayout.X_AXIS));
            linkPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // 3. Selectable, non-editable text field for the URL
            JTextField urlField = new JTextField(urlString);
            urlField.setEditable(false);
            urlField.setCaretPosition(0); // Scroll back to the beginning of the link text
            linkPanel.add(urlField);

            // Add slight horizontal spacing between the field and the button
            linkPanel.add(Box.createHorizontalStrut(5));

            // 4. Copy button using the pre-loaded copyIcon
            JButton copyButton = new JButton(copyIcon);
            copyButton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            copyButton.setToolTipText("Copy link to clipboard");
            
            // Copy action listener
            copyButton.addActionListener(e -> {
                StringSelection stringSelection = new StringSelection(urlString);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                Notify.showToast(mainPanel, "Copied!");
            });
            
            linkPanel.add(copyButton);
            mainPanel.add(linkPanel);

            // 5. Present the fully constructed component panel inside a modal standard dialog
            JOptionPane.showMessageDialog(
                parent, 
                mainPanel, 
                "Could not open webpage", 
                JOptionPane.ERROR_MESSAGE
            );
        });
    }
}