package contact;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import app.Main;

final class LegacyManual {
    private static final ImageIcon copyIcon = new ImageIcon(
        LegacyManual.class.getResource("/iconmonstr-copy-lined.png")
    );

    static void manualBugReport(String subject, String body) {
        String DEV_EMAIL = Contact.DEV_EMAIL;

        // Main panel
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Explanation
            JLabel explanation = new JLabel(
                "Please send the following message to the email address below."
            );
            explanation.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(explanation);
            panel.add(Box.createVerticalStrut(10));

        // Email field
            JPanel emailRow = new JPanel();
            emailRow.setLayout(new BoxLayout(emailRow, BoxLayout.X_AXIS));
            emailRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextField emailField = new JTextField(DEV_EMAIL);
            emailField.setEditable(false);

            JButton copyEmail = new JButton(copyIcon);
            copyEmail.setMaximumSize(new Dimension (
                copyEmail.getPreferredSize().height, 
                copyEmail.getPreferredSize().height
            ));
            copyEmail.setPreferredSize(new Dimension (
                copyEmail.getPreferredSize().height, 
                copyEmail.getPreferredSize().height
            ));
            copyEmail.setToolTipText("Copy email address");
            copyEmail.addActionListener(e -> {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    new StringSelection(DEV_EMAIL), 
                    null
                );
            });

            emailRow.add(new JLabel("Email: "));
            emailRow.add(Box.createHorizontalStrut(15));
            emailRow.add(emailField);
            emailRow.add(Box.createHorizontalStrut(5));
            emailRow.add(copyEmail);

            panel.add(emailRow);
            panel.add(Box.createVerticalStrut(10));

        // Subject field
            JPanel subjectRow = new JPanel();
            subjectRow.setLayout(new BoxLayout(subjectRow, BoxLayout.X_AXIS));
            subjectRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextField subjectField = new JTextField(subject);

            JButton copySubject = new JButton(copyIcon);
            copySubject.setMaximumSize(new Dimension (
                copySubject.getPreferredSize().height, 
                copySubject.getPreferredSize().height
            ));
            copySubject.setPreferredSize(new Dimension (
                copySubject.getPreferredSize().height, 
                copySubject.getPreferredSize().height
            ));
            copySubject.setToolTipText("Copy subject");
            copySubject.addActionListener(e -> {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(subjectField.getText()), null);
            });

            subjectRow.add(new JLabel("Subject: "));
            subjectRow.add(Box.createHorizontalStrut(15));
            subjectRow.add(subjectField);
            subjectRow.add(Box.createHorizontalStrut(5));
            subjectRow.add(copySubject);

            panel.add(subjectRow);
            panel.add(Box.createVerticalStrut(10));

        // Body field
            JPanel bodyRow = new JPanel();
            bodyRow.setLayout(new BoxLayout(bodyRow, BoxLayout.X_AXIS));
            bodyRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea bodyArea = new JTextArea(body, 10, 40);
            bodyArea.setLineWrap(true);
            bodyArea.setWrapStyleWord(true);

            JScrollPane bodyScroll = new JScrollPane(bodyArea);

            JButton copyBody = new JButton(copyIcon);
            copyBody.setMaximumSize(new Dimension (
                copyBody.getPreferredSize().height, 
                copyBody.getPreferredSize().height
            ));
            copyBody.setPreferredSize(new Dimension (
                copyBody.getPreferredSize().height, 
                copyBody.getPreferredSize().height
            ));
            copyBody.setToolTipText("Copy message body");
            copyBody.addActionListener(e -> {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(bodyArea.getText()), null);
            });

            bodyRow.add(new JLabel("Message: "));
            bodyRow.add(Box.createHorizontalStrut(15));
            bodyRow.add(bodyScroll);
            bodyRow.add(Box.createHorizontalStrut(5));
            bodyRow.add(copyBody);

            panel.add(bodyRow);
            panel.add(Box.createVerticalStrut(10));

        // Show dialog
        JOptionPane.showMessageDialog(
            Main.frame,
            panel,
            "Manual bug report",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    static void manualFeatureRequest() {
        String DEV_EMAIL = Contact.DEV_EMAIL;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel explanation = new JLabel(
            "<html>Please send your message to the email address below.<br>You can also copy the subject line to let me know what the email is about!</html>"
        );
        explanation.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(explanation);
        panel.add(Box.createVerticalStrut(10));

        // Email row
            JPanel emailRow = new JPanel();
            emailRow.setLayout(new BoxLayout(emailRow, BoxLayout.X_AXIS));
            emailRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextField emailField = new JTextField(DEV_EMAIL);
            emailField.setEditable(false);

            JButton copyEmail = new JButton(copyIcon);
            copyEmail.setMaximumSize(new Dimension (
                copyEmail.getPreferredSize().height, 
                copyEmail.getPreferredSize().height
            ));
            copyEmail.setPreferredSize(new Dimension (
                copyEmail.getPreferredSize().height, 
                copyEmail.getPreferredSize().height
            ));
            copyEmail.setToolTipText("Copy email address");
            copyEmail.addActionListener(e -> {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(DEV_EMAIL), null);
            });

            emailRow.add(new JLabel("Email: "));
            emailRow.add(Box.createHorizontalStrut(15));
            emailRow.add(emailField);
            emailRow.add(Box.createHorizontalStrut(5));
            emailRow.add(copyEmail);

            panel.add(emailRow);
            panel.add(Box.createVerticalStrut(10));

        // Subject row
            JPanel subjectRow = new JPanel();
            subjectRow.setLayout(new BoxLayout(subjectRow, BoxLayout.X_AXIS));
            subjectRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            String subjectText = "Rank Anything: Feature Request";
            JTextField subjectField = new JTextField(subjectText);

            JButton copySubject = new JButton(copyIcon);
            copySubject.setMaximumSize(new Dimension (
                copySubject.getPreferredSize().height, 
                copySubject.getPreferredSize().height
            ));
            copySubject.setPreferredSize(new Dimension (
                copySubject.getPreferredSize().height, 
                copySubject.getPreferredSize().height
            ));
            copySubject.setToolTipText("Copy subject line");
            copySubject.addActionListener(e -> {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    new StringSelection(subjectField.getText()), 
                    null
                );
            });

            subjectRow.add(new JLabel("Subject: "));
            subjectRow.add(Box.createHorizontalStrut(15));
            subjectRow.add(subjectField);
            subjectRow.add(Box.createHorizontalStrut(5));
            subjectRow.add(copySubject);

            panel.add(subjectRow);
            panel.add(Box.createVerticalStrut(10));

        JOptionPane.showMessageDialog(
            Main.frame,
            panel,
            "Manual feature request",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    static void manualContact() {
        String DEV_EMAIL = Contact.DEV_EMAIL;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel explanation = new JLabel(
            "Please send your message to the email address below."
        );
        explanation.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(explanation);
        panel.add(Box.createVerticalStrut(10));

        JPanel emailRow = new JPanel();
        emailRow.setLayout(new BoxLayout(emailRow, BoxLayout.X_AXIS));
        emailRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField emailField = new JTextField(DEV_EMAIL);
        emailField.setEditable(false);

        JButton copyEmail = new JButton(copyIcon);
        copyEmail.setMaximumSize(new Dimension (
            copyEmail.getPreferredSize().height, 
            copyEmail.getPreferredSize().height
        ));
        copyEmail.setPreferredSize(new Dimension (
            copyEmail.getPreferredSize().height, 
            copyEmail.getPreferredSize().height
        ));
        copyEmail.setToolTipText("Copy email address");
        copyEmail.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(DEV_EMAIL), null);
        });

        emailRow.add(new JLabel("Email: "));
        emailRow.add(Box.createHorizontalStrut(15));
        emailRow.add(emailField);
        emailRow.add(Box.createHorizontalStrut(5));
        emailRow.add(copyEmail);

        panel.add(emailRow);
        panel.add(Box.createVerticalStrut(10));

        JOptionPane.showMessageDialog(
            Main.frame,
            panel,
            "Manual contact",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}