package metadata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import app.Main;
import saveload.SaveLoad;

final class ProjectInfo {
    static void showProjectInfoDialog(JFrame frame) {
        String created = Tasks.formatTimestamp(Main.projectCreated);
        String fileCreated = Tasks.formatTimestamp(Main.fileCreated);
        String modified = Tasks.formatTimestamp(Main.projectModified);

        int criteriaCount = Main.criteriaList.size();
        int itemsCount = Main.itemsList.size();

        String fileFileFormatVersionString = 
            (Main.projectFileFormatVersion > 0) 
            ? String.valueOf(Main.projectFileFormatVersion) 
            : "Unknown"
        ;

        // PANEL
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(500, 500));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // project name label
        JLabel projectNameLabel = new JLabel("Project name: ");
        projectNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(projectNameLabel);

        // editable name field
        JPanel nameWrapper = new JPanel();
        nameWrapper.setLayout(new BoxLayout(nameWrapper, BoxLayout.X_AXIS));
        nameWrapper.setOpaque(false); // Keeps it the background gray color
        nameWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameWrapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        JTextField nameField = new JTextField(Main.projectName);
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        nameWrapper.add(nameField);
        panel.add(nameWrapper);
        JLabel projectIdLabel = new JLabel("Project ID: " + Main.projectId);
        projectIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        projectIdLabel.setForeground(Color.GRAY);
        projectIdLabel.setToolTipText("(Click to copy to clipboard) " + Main.projectId);
        projectIdLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(projectIdLabel);
        panel.add(Box.createVerticalStrut(10));

        // file information
        JLabel fileLocationHeaderLabel = new JLabel("File location:");
        fileLocationHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(fileLocationHeaderLabel);
        JLabel fileLocationLabel = new JLabel("    " + SaveLoad.getCurrentFilePath());
        fileLocationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fileLocationLabel.setToolTipText("(Click to open folder) " + SaveLoad.getCurrentFilePath());
        fileLocationLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(fileLocationLabel);
        JLabel originalLabel = new JLabel("This file is the original copy of the project.");
        originalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (Main.isOriginal) panel.add(originalLabel);
        panel.add(Box.createVerticalStrut(10));

        // copy project ID
        projectIdLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    new StringSelection(Main.projectId),
                    null
                );
            }
        });

        // open file location
        fileLocationLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Tasks.openFileLocation(frame);
            }
        });

        // timestamps
        JLabel createdLabel = new JLabel("Project created: " + created);
        createdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(createdLabel);
        JLabel fileCreatedLabel = new JLabel("File created: " + fileCreated);
        fileCreatedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(fileCreatedLabel);
        JLabel modifiedLabel = new JLabel("Last modified: " + modified);
        modifiedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(modifiedLabel);
        panel.add(Box.createVerticalStrut(10));

        // version info
        JLabel appVersionLabel = new JLabel("Current app version: " + Main.APP_VERSION);
        appVersionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(appVersionLabel);
        JLabel fileAppVersionLabel = new JLabel("Project last saved using app version: " + Main.projectAppVersion);
        fileAppVersionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(fileAppVersionLabel);
        JLabel fileFormatVersionLabel = new JLabel("Current file format version: " + Main.FILE_FORMAT_VERSION);
        fileFormatVersionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(fileFormatVersionLabel);
        JLabel fileFileFormatVersionLabel = new JLabel("File format version used by this project: " + fileFileFormatVersionString);
        fileFileFormatVersionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(fileFileFormatVersionLabel);
        panel.add(Box.createVerticalStrut(10));

        // counts
        JLabel criteriaCountLabel = new JLabel("Criteria count: " + criteriaCount);
        criteriaCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(criteriaCountLabel);
        JLabel criteriaCreatedLabel = new JLabel("Criteria created: " + Main.criteriaCreationCounter);
        criteriaCreatedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(criteriaCreatedLabel);
        JLabel itemsCountLabel = new JLabel("Items count: " + itemsCount);
        itemsCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(itemsCountLabel);
        JLabel itemsCreatedLabel = new JLabel("Items created: " + Main.itemsCreationCounter);
        itemsCreatedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(itemsCreatedLabel);
        panel.add(Box.createVerticalStrut(10));

        // notes label
        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(notesLabel);

        // editable notes area
        JTextArea notesArea = new JTextArea(Main.projectNotes, 6, 40);
        notesArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        notesScroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 10, 0, 10), 
            notesScroll.getBorder()
        ));
        panel.add(notesScroll);
        panel.add(Box.createVerticalStrut(10));

        // SCROLL WRAPPER
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(500, 500));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        int result = JOptionPane.showConfirmDialog(
            frame,
            scroll,
            "Project Info",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            boolean nameChanged = !nameField.getText().equals(Main.projectName);
            boolean notesChanged = !notesArea.getText().equals(Main.projectNotes);

            if (nameChanged || notesChanged) {
                Main.projectName = nameField.getText();
                Main.projectNotes = notesArea.getText();

                Main.projectDirty = true;
                Main.unsavedChanges = true;
                Metadata.updateWindowTitle();
            }
        }
    }

    static void showNotesEditor(JFrame frame) {
        JTextArea area = new JTextArea(Main.projectNotes, 20, 50);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        
        // Move cursor to the end
        area.setCaretPosition(area.getText().length());
        
        // Request focus the moment the component is added to the dialog window
        area.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                area.requestFocusInWindow();
            }
            @Override public void ancestorRemoved(AncestorEvent event) {}
            @Override public void ancestorMoved(AncestorEvent event) {}
        });
        
        JScrollPane scroll = new JScrollPane(area);

        int result = JOptionPane.showConfirmDialog(
            frame,
            scroll,
            "Edit Project Notes",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (!area.getText().equals(Main.projectNotes)) {
                Main.projectNotes = area.getText();
                Main.projectDirty = true;
                Main.unsavedChanges = true;
                Metadata.updateWindowTitle();
            }
        }
    }

    static void showProjectNameEditor(JFrame frame) {
        JTextField field = new JTextField(Main.projectName, 30);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Select all text automatically
        field.selectAll();
        
        // Request focus and re-select text the moment it appears on screen
        field.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                field.requestFocusInWindow();
                field.selectAll(); // Re-called because focus loss can clear selection
            }
            @Override public void ancestorRemoved(AncestorEvent event) {}
            @Override public void ancestorMoved(AncestorEvent event) {}
        });

        int result = JOptionPane.showConfirmDialog(
            frame,
            field,
            "Edit Project Name",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (!field.getText().equals(Main.projectName)) {
                Main.projectName = field.getText();
                Main.projectDirty = true;
                Main.unsavedChanges = true;
                Metadata.updateWindowTitle();
            }
        }
    }
}