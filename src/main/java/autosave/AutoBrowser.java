package autosave;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import app.Main;
import metadata.Metadata;
import saveload.SaveLoad;

public final class AutoBrowser {

    public enum AutosaveRendererMode {
        CURRENT_PROJECT,
        ALL_AUTOSAVES,
        UNSAVED_PROJECTS
    }

    public static void show(JFrame frame, String projectId, AutosaveRendererMode mode) {
        File autosaveDir = Main.AUTOSAVE_DIR;
        
        File[] autosaves = Tasks.gatherAutosaves(autosaveDir, mode, projectId);
        if (autosaves == null || autosaves.length == 0) {
            JOptionPane.showMessageDialog(frame,
                "No autosaves found" + (
                    mode == AutosaveRendererMode.CURRENT_PROJECT 
                            ? " for this project." 
                            : "."
                ),
                "Autosaves",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // 1. Pre-calculate the maximum lastModified time for each project ID
        Map<String, Long> projectNewestTime = new HashMap<>();
        for (File f : autosaves) {
            String id = f.getName().split("_")[0];
            long modified = f.lastModified();
            projectNewestTime.merge(id, modified, Long::max);
        }

        // 2. Sort groups by their newest file first, then newest to oldest within each group
        Arrays.sort(autosaves, (a, b) -> {
            String idA = a.getName().split("_")[0];
            String idB = b.getName().split("_")[0];
            
            // If they belong to different projects, sort by the project's overall newest autosave
            if (!idA.equals(idB)) {
                long maxA = projectNewestTime.get(idA);
                long maxB = projectNewestTime.get(idB);
                return Long.compare(maxB, maxA); // Newest project group first
            }
            
            // If they belong to the same project, sort newest to oldest
            return Long.compare(b.lastModified(), a.lastModified());
        });

        // UI list model
        DefaultListModel<File> model = new DefaultListModel<>();
        for (File f : autosaves) model.addElement(f);

        JList<File> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setCellRenderer(new AutosaveCellRenderer(mode));

        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(400, 300));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // Buttons
        JButton loadButton = new JButton("Load");
        JButton saveAsButton = new JButton("Save as...");
        JButton deleteButton = new JButton("Delete");
        JButton settingsButton = new JButton("Settings");

        loadButton.setEnabled(false);
        saveAsButton.setEnabled(false);
        deleteButton.setEnabled(false);

        loadButton.addActionListener(e -> {
            File selected = list.getSelectedValue();
            if (selected != null) {
                SaveLoad.loadFromFile(selected, frame, true);
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // double-click
                    File selected = list.getSelectedValue();
                    if (selected != null) {
                        SaveLoad.loadFromFile(selected, frame, true);
                    }
                }
            }
        });

        saveAsButton.addActionListener(e -> {
            File selected = list.getSelectedValue();
            if (selected != null) {
                SaveLoad.loadFromFile(selected, frame, true);
                SaveLoad.saveAs(frame);
            }
        });

        deleteButton.addActionListener(e -> {
            Tasks.deleteAutosave(list, frame, model);
        });

        settingsButton.addActionListener(e -> {
            AutoSettings.show(frame);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(saveAsButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(settingsButton);

        list.addListSelectionListener(e -> {
            int count = list.getSelectedIndices().length;

            loadButton.setEnabled(count == 1);
            saveAsButton.setEnabled(count == 1);
            deleteButton.setEnabled(count >= 1);
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(
            frame,
            panel,
            "Autosaves",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    private static class AutosaveCellRenderer extends DefaultListCellRenderer {
        private final AutosaveRendererMode mode;

        public AutosaveCellRenderer(AutosaveRendererMode mode) {
            this.mode = mode;
        }

        @Override
        public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus
        ) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus
            );
            File file = (File) value;

            // Load JSON from autosave file
            JSONObject root;
            try {
                String json = Files.readString(file.toPath());
                root = new JSONObject(json);
            } catch (Exception ex) {
                label.setText("<html><b>Unreadable autosave</b></html>");
                return label;
            }

            // Extract timestamps
            String created = Metadata.formatTimestamp(root.optString("created", "Unknown"));
            String fileCreated = Metadata.formatTimestamp(root.optString("fileCreated", "Unknown"));
            String autosaved = Metadata.formatTimestamp(Tasks.extractAutosaveTimestamp(file.getName()));
            
            int criteriaCount = 0;
            int itemsCount = 0;
            JSONArray criteria = root.optJSONArray("criteria");
            if (criteria != null) criteriaCount = criteria.length();
            JSONArray items = root.optJSONArray("items");
            if (items != null) itemsCount = items.length();
            long sizeBytes = file.length();
            String projectId = root.optString("projectId", "Unknown ID");

            // Name preview
            String projectName = root.optString("projectName", "Untitled Project").trim();
            String namePreview = "";
            if (!projectName.isEmpty()) {
                int newline = projectName.indexOf('\n');
                namePreview = (newline != -1) ? projectName.substring(0, newline).trim() : projectName;
                if (namePreview.length() > 55) {
                    namePreview = namePreview.substring(0, 55).trim() + "...";
                }
            }

            // Notes preview
            String notes = root.optString("notes", "").trim();
            String notesPreview = "";
            if (!notes.isEmpty()) {
                int newline = notes.indexOf('\n');
                notesPreview = (newline != -1) ? notes.substring(0, newline).trim() : notes;
                if (notesPreview.length() > 60) {
                    notesPreview = notesPreview.substring(0, 60).trim() + "...";
                }
            }

            // Base text layout without internal HTML dividers
            label.setText("<html>" +
                "<b>" + namePreview + "</b><br>" +
                "Saved on " + autosaved + "<br>" +
                (mode == AutosaveRendererMode.CURRENT_PROJECT ? "" : "Project created: " + created + "<br>") +
                (mode == AutosaveRendererMode.ALL_AUTOSAVES ? "File created: " + fileCreated + "<br>" : "") +
                "Criteria: " + criteriaCount + " &nbsp;&nbsp; Items: " + itemsCount + "<br>" +
                "Size: " + sizeBytes + " bytes" +
                (notesPreview.isEmpty() ? "" : "<br><i>" + notesPreview + "</i>") +
                (mode == AutosaveRendererMode.CURRENT_PROJECT ? "" : "<br><font color='gray'>Project ID: " + projectId + "</font>") +
                "</html>"
            );

            // --- GROUPING & DIVIDER LOGIC ---
            // Thin default item padding/divider
            Border innerPadding = BorderFactory.createEmptyBorder(6, 8, 6, 8);
            Border itemDivider = BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.LIGHT_GRAY);
            Border finalBorder = new CompoundBorder(itemDivider, innerPadding);

            // Only show strong project headers if we are looking at multiple projects
            if (mode != AutosaveRendererMode.CURRENT_PROJECT && index > 0) {
                File previousFile = (File) list.getModel().getElementAt(index - 1);
                String currentProjectId = file.getName().split("_")[0];
                String previousProjectId = previousFile.getName().split("_")[0];

                // If the project ID changed, inject a thick separator border above this row
                if (!currentProjectId.equals(previousProjectId)) {
                    Border projectDivider = BorderFactory.createMatteBorder(4, 0, 0, 0, java.awt.Color.DARK_GRAY);
                    finalBorder =  new CompoundBorder(projectDivider, finalBorder);
                }
            }

            label.setBorder(finalBorder);
            return label;
        }
    }
}