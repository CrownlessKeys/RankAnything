package contact;

import java.awt.Component;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import app.Main;

final class Dialog {
    static void showBugReportDialog() {

        // Create dialog
        JDialog dialog = new JDialog(Main.frame, "Report a bug", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Helper to left-align rows
        Consumer<JComponent> left = c -> c.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel title = new JLabel(
            "<html>Please describe the issue.<br>All fields are optional, but including more information helps me solve the problem.</html>"
        );
        left.accept(title);
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));

        // Description
        panel.add(new JLabel("Bug description:"));
        JTextArea descriptionArea = new JTextArea(4, 40);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        left.accept(descriptionScroll);
        panel.add(descriptionScroll);
        panel.add(Box.createVerticalStrut(10));

        // Steps
        panel.add(new JLabel("Steps to reproduce:"));
        JTextArea stepsArea = new JTextArea(4, 40);
        stepsArea.setLineWrap(true);
        stepsArea.setWrapStyleWord(true);
        JScrollPane stepsScroll = new JScrollPane(stepsArea);
        left.accept(stepsScroll);
        panel.add(stepsScroll);
        panel.add(Box.createVerticalStrut(10));

        // Expected
        panel.add(new JLabel("Expected behavior:"));
        JTextArea expectedArea = new JTextArea(3, 40);
        expectedArea.setLineWrap(true);
        expectedArea.setWrapStyleWord(true);
        JScrollPane expectedScroll = new JScrollPane(expectedArea);
        left.accept(expectedScroll);
        panel.add(expectedScroll);
        panel.add(Box.createVerticalStrut(10));

        // Actual
        panel.add(new JLabel("Actual behavior:"));
        JTextArea actualArea = new JTextArea(3, 40);
        actualArea.setLineWrap(true);
        actualArea.setWrapStyleWord(true);
        JScrollPane actualScroll = new JScrollPane(actualArea);
        left.accept(actualScroll);
        panel.add(actualScroll);
        panel.add(Box.createVerticalStrut(10));

        // Checkboxes
        JCheckBox includeSystemInfo = new JCheckBox("Include system information");
        includeSystemInfo.setSelected(true); // default
        JCheckBox requestReply = new JCheckBox("I would like a follow-up response");
        requestReply.setSelected(true); // default
        left.accept(includeSystemInfo);
        left.accept(requestReply);
        panel.add(includeSystemInfo);
        panel.add(requestReply);
        panel.add(Box.createVerticalStrut(15));

        // Buttons
        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        left.accept(buttonRow);

        JButton openMail = new JButton("Open email app");
        JButton manual = new JButton("Report manually");
        JButton cancel = new JButton("Cancel");

        buttonRow.add(openMail);
        buttonRow.add(Box.createHorizontalStrut(10));
        buttonRow.add(manual);
        buttonRow.add(Box.createHorizontalStrut(10));
        buttonRow.add(cancel);

        panel.add(buttonRow);

        // Button actions
        openMail.addActionListener(e -> {
            Mail.bugReport(
                descriptionArea.getText().trim(),
                stepsArea.getText().trim(),
                expectedArea.getText().trim(),
                actualArea.getText().trim(),
                includeSystemInfo.isSelected(),
                requestReply.isSelected(),
                false // manual = false
            );
            dialog.dispose();
        });

        manual.addActionListener(e -> {
            Mail.bugReport(
                descriptionArea.getText().trim(),
                stepsArea.getText().trim(),
                expectedArea.getText().trim(),
                actualArea.getText().trim(),
                includeSystemInfo.isSelected(),
                requestReply.isSelected(),
                true // manual = true
            );
            dialog.dispose();
        });

        cancel.addActionListener(e -> dialog.dispose());

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(Main.frame);
        dialog.setVisible(true);
    }

    static void showContactDialog() {

        JDialog dialog = new JDialog(Main.frame, "Contact developer", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        JButton openMail = new JButton("Open email app");
        JButton manual = new JButton("Email manually");
        JButton cancel = new JButton("Cancel");

        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        row.add(openMail);
        row.add(Box.createHorizontalStrut(10));
        row.add(manual);
        row.add(Box.createHorizontalStrut(10));
        row.add(cancel);

        panel.add(row);

        // Actions
        openMail.addActionListener(e -> {
            Mail.contactGeneral();
            dialog.dispose();
        });

        manual.addActionListener(e -> {
            Manual.manualContact();
            dialog.dispose();
        });

        cancel.addActionListener(e -> dialog.dispose());

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(Main.frame);
        dialog.setVisible(true);
    }

    static void showFeatureDialog() {

        JDialog dialog = new JDialog(Main.frame, "Request a feature", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        JButton openMail = new JButton("Open email app");
        JButton manual = new JButton("Email manually");
        JButton cancel = new JButton("Cancel");

        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        row.add(openMail);
        row.add(Box.createHorizontalStrut(10));
        row.add(manual);
        row.add(Box.createHorizontalStrut(10));
        row.add(cancel);

        panel.add(row);

        // Actions
        openMail.addActionListener(e -> {
            Mail.contactFeatureRequest();
            dialog.dispose();
        });

        manual.addActionListener(e -> {
            Manual.manualFeatureRequest();
            dialog.dispose();
        });

        cancel.addActionListener(e -> dialog.dispose());

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(Main.frame);
        dialog.setVisible(true);
    }
}