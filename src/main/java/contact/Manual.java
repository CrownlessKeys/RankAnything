package contact;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import app.Main;

final class Manual {
    private static final ImageIcon copyIcon = new ImageIcon(
        "src/main/resources/iconmonstr-copy-lined.png"
    );

    static void manualBugReport(String project, String settings, String system) {
        // TODO: modal info popup to inform user what is about to happen
        showTextFields(project, settings, system);
    }

    // shows the text fields for the user to copy and paste
    private static void showTextFields(String project, String settings, String system) {

        // Main panel
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Explanation
            JLabel explanation = new JLabel("<html>"
                + "Please copy and paste the text blocks below into their matching fields on the Google Form. In most<br>"
                + "cases, only \"Project JSON\" will need to be added manually, but the other sections are included here<br>"
                + "in case they too were excluded.</html>"
            );
            explanation.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(explanation);
            panel.add(Box.createVerticalStrut(10));

        // Project field
            JPanel projectRow = new JPanel();
            projectRow.setLayout(new BoxLayout(projectRow, BoxLayout.X_AXIS));
            projectRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea projectArea = new JTextArea(project, 10, 40);
            projectArea.setLineWrap(true);
            projectArea.setWrapStyleWord(true);
            projectArea.setEditable(false);

            JScrollPane projectScroll = new JScrollPane(projectArea);

            JButton copyProject = new JButton(copyIcon);
            copyProject.setMaximumSize(new Dimension (
                copyProject.getPreferredSize().height, 
                copyProject.getPreferredSize().height
            ));
            copyProject.setPreferredSize(new Dimension (
                copyProject.getPreferredSize().height, 
                copyProject.getPreferredSize().height
            ));
            copyProject.setToolTipText("Copy \"Project JSON\" field");
            copyProject.addActionListener(e -> {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(projectArea.getText()), null);
            });

            projectRow.add(new JLabel("Project JSON:"));
            projectRow.add(Box.createHorizontalStrut(15));
            projectRow.add(projectScroll);
            projectRow.add(Box.createHorizontalStrut(5));
            projectRow.add(copyProject);

            panel.add(projectRow);
            panel.add(Box.createVerticalStrut(10));

        // Settings field
            JPanel settingsRow = new JPanel();
            settingsRow.setLayout(new BoxLayout(settingsRow, BoxLayout.X_AXIS));
            settingsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea settingsArea = new JTextArea(settings, 10, 40);
            settingsArea.setLineWrap(true);
            settingsArea.setWrapStyleWord(true);
            settingsArea.setEditable(false);

            JScrollPane settingsScroll = new JScrollPane(settingsArea);

            JButton copySettings = new JButton(copyIcon);
            copySettings.setMaximumSize(new Dimension (
                copySettings.getPreferredSize().height, 
                copySettings.getPreferredSize().height
            ));
            copySettings.setPreferredSize(new Dimension (
                copySettings.getPreferredSize().height, 
                copySettings.getPreferredSize().height
            ));
            copySettings.setToolTipText("Copy \"User Settings JSON\" field");
            copySettings.addActionListener(e -> {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(settingsArea.getText()), null);
            });

            settingsRow.add(new JLabel("User Settings JSON:"));
            settingsRow.add(Box.createHorizontalStrut(15));
            settingsRow.add(settingsScroll);
            settingsRow.add(Box.createHorizontalStrut(5));
            settingsRow.add(copySettings);

            panel.add(settingsRow);
            panel.add(Box.createVerticalStrut(10));

        // System field
            JPanel systemRow = new JPanel();
            systemRow.setLayout(new BoxLayout(systemRow, BoxLayout.X_AXIS));
            systemRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea systemArea = new JTextArea(system, 10, 40);
            systemArea.setLineWrap(true);
            systemArea.setWrapStyleWord(true);
            systemArea.setEditable(false);

            JScrollPane systemScroll = new JScrollPane(systemArea);

            JButton copySystem = new JButton(copyIcon);
            copySystem.setMaximumSize(new Dimension (
                copySystem.getPreferredSize().height, 
                copySystem.getPreferredSize().height
            ));
            copySystem.setPreferredSize(new Dimension (
                copySystem.getPreferredSize().height, 
                copySystem.getPreferredSize().height
            ));
            copySystem.setToolTipText("Copy \"System information\" field");
            copySystem.addActionListener(e -> {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(systemArea.getText()), null);
            });

            systemRow.add(new JLabel("System information:"));
            systemRow.add(Box.createHorizontalStrut(15));
            systemRow.add(systemScroll);
            systemRow.add(Box.createHorizontalStrut(5));
            systemRow.add(copySystem);

            panel.add(systemRow);
            panel.add(Box.createVerticalStrut(10));

            // Show dialog (modeless so the webpage can open immediately)
            JDialog dialog = new JDialog(
                Main.frame, 
                "Copy and paste fields for Bug Report Google Form", 
                false // false for modeless
            );
            dialog.setContentPane(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(Main.frame);
            dialog.setVisible(true);
    }
}