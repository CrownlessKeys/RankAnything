package app;

import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import autosave.AutoBrowser;
import autosave.AutoIndicator;
import autosave.AutoTimer;
import autosave.Autosave;
import metadata.Metadata;
import saveload.SaveLoad;

public final class MenuBar extends JMenuBar {
    
    private static JCheckBoxMenuItem autosaveToggle;
    private static JCheckBoxMenuItem autoRenameFilesToggle;
    public static JMenu recentProjectsMenu;

    public MenuBar(JFrame frame) {

        // FILE MENU
            JMenu fileMenu = new JMenu("File");
            add(fileMenu);

            JMenuItem newProjectItem = new JMenuItem("New Project");
            JMenuItem openItem = new JMenuItem("Open...");
            JMenuItem saveItem = new JMenuItem("Save");
            JMenuItem saveAsItem = new JMenuItem("Save As...");
            recentProjectsMenu = new JMenu("Recent Projects");
            JMenuItem unsavedProjectsItem = new JMenuItem("Unsaved Projects");
            JMenuItem openFileLocationItem = new JMenuItem("Open File Location");
            JMenuItem exitItem = new JMenuItem("Exit");
            
            JMenu autosavesMenu = new JMenu("Autosaves...");
            JMenuItem projectAutosavesItem = new JMenuItem("Current Project");
            JMenuItem allAutosavesItem = new JMenuItem("All Autosaves");
            JMenuItem unsavedAutoItem = new JMenuItem("Unsaved Projects");
            autosavesMenu.add(projectAutosavesItem);
            autosavesMenu.add(allAutosavesItem);
            autosavesMenu.add(unsavedAutoItem);

            fileMenu.add(newProjectItem);
            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            fileMenu.add(saveAsItem);
            fileMenu.add(autosavesMenu);
            fileMenu.add(openFileLocationItem);
            fileMenu.add(recentProjectsMenu);
            fileMenu.add(unsavedProjectsItem);
            fileMenu.addSeparator();
            fileMenu.add(exitItem);

            newProjectItem.setAccelerator(KeyStroke.getKeyStroke("control N"));
            openItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
            saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
            saveAsItem.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
            projectAutosavesItem.setAccelerator(KeyStroke.getKeyStroke("control A"));
            exitItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));

            RecentProjects.updateRecentProjectsMenu(frame);

        // PROJECT MENU
            JMenu projectMenu = new JMenu("Project");
            add(projectMenu);

            JMenuItem projectInfoItem = new JMenuItem("Project Info");
            JMenuItem editNotesItem = new JMenuItem("Edit Notes");
            JMenuItem renameProjectItem = new JMenuItem("Rename Project");

            projectMenu.add(projectInfoItem);
            projectMenu.addSeparator();
            projectMenu.add(renameProjectItem);
            projectMenu.add(editNotesItem);
            
            projectInfoItem.setAccelerator(KeyStroke.getKeyStroke("control I"));
            editNotesItem.setAccelerator(KeyStroke.getKeyStroke("control E"));
            renameProjectItem.setAccelerator(KeyStroke.getKeyStroke("control R"));

        // SETTINGS MENU
            JMenu settingsMenu = new JMenu("Settings");
            add(settingsMenu);

            autosaveToggle = new JCheckBoxMenuItem("Enable Autosave");
            autosaveToggle.setSelected(Settings.autosavePeriodically || Settings.autosaveOnClose);
            autosaveToggle.setToolTipText(
                "Enable or disable the automatic creation of backup save files of your project periodically and when closing the project with unsaved changes."
            );
            settingsMenu.add(autosaveToggle);

            JMenuItem autosaveSettingsItem = new JMenuItem("Autosave Settings...");
            settingsMenu.add(autosaveSettingsItem);
            settingsMenu.addSeparator();

            autoRenameFilesToggle = new JCheckBoxMenuItem("Automatically Rename Files");
            autoRenameFilesToggle.setSelected(Settings.autoRenameFiles);
            autoRenameFilesToggle.setToolTipText(
                "Enable or disable automatic renaming of your current project file when saving."
            );
            settingsMenu.add(autoRenameFilesToggle);
            settingsMenu.addSeparator();

            JMenuItem restoreDefaultsItem = new JMenuItem("Restore Default Settings");
            settingsMenu.add(restoreDefaultsItem);

        // HELP MENU
            JMenu helpMenu = new JMenu("Help");
            add(helpMenu);

            JMenuItem aboutItem = new JMenuItem("About");
            helpMenu.add(aboutItem);
        // end

        // autosave indicator
        add(Box.createHorizontalGlue()); // push to right
        add(AutoIndicator.getLabel());
        add(Box.createHorizontalStrut(10));

        // mnemonics
        fileMenu.setMnemonic('F');
        projectMenu.setMnemonic('P');
        settingsMenu.setMnemonic('S');
        helpMenu.setMnemonic('H');
        autosavesMenu.setMnemonic('A');
        recentProjectsMenu.setMnemonic('R');

        // LISTENERS
        saveAsItem.addActionListener(e -> SaveLoad.saveAs(frame));
        openItem.addActionListener(e -> SaveLoad.load(frame));
        saveItem.addActionListener(e -> SaveLoad.save(frame));
        openFileLocationItem.addActionListener(e -> Metadata.openFileLocation(frame));
        newProjectItem.addActionListener(e -> SaveLoad.newProject(frame));
        projectAutosavesItem.addActionListener(e -> AutoBrowser.show(
            frame, Main.projectId, AutoBrowser.AutosaveRendererMode.CURRENT_PROJECT
        ));
        allAutosavesItem.addActionListener(e -> AutoBrowser.show(
            frame, Main.projectId, AutoBrowser.AutosaveRendererMode.ALL_AUTOSAVES
        ));
        unsavedAutoItem.addActionListener(e -> AutoBrowser.show(
            frame, Main.projectId, AutoBrowser.AutosaveRendererMode.UNSAVED_PROJECTS
        ));
        unsavedProjectsItem.addActionListener(e -> AutoBrowser.show(
            frame, Main.projectId, AutoBrowser.AutosaveRendererMode.UNSAVED_PROJECTS
        ));
        exitItem.addActionListener(e -> 
            frame.dispatchEvent(new WindowEvent(
                frame, WindowEvent.WINDOW_CLOSING
            ))
        );

        projectInfoItem.addActionListener(e -> Metadata.showProjectInfoDialog(frame));
        editNotesItem.addActionListener(e -> Metadata.showNotesEditor(frame));
        renameProjectItem.addActionListener(e -> Metadata.showProjectNameEditor(frame));

        autosaveToggle.addActionListener(e -> {
            boolean enabled = autosaveToggle.isSelected();
            Settings.autosavePeriodically = enabled;
            Settings.autosaveOnClose = enabled;
            Settings.save();

            if (Settings.autosavePeriodically) {
                AutoTimer.start(Main.frame);
            } else {
                AutoTimer.stop();
            }
        });
        autosaveSettingsItem.addActionListener(e -> Autosave.showAutosaveSettings(frame));
        autoRenameFilesToggle.addActionListener(e -> {
            Settings.autoRenameFiles = autoRenameFilesToggle.isSelected();
            Settings.save();
        });
        restoreDefaultsItem.addActionListener(e -> Settings.restoreDefaults(frame));
    }

    public static void updateSettingsMenu() {
        autoRenameFilesToggle.setSelected(Settings.autoRenameFiles);
        autosaveToggle.setSelected(Settings.autosavePeriodically || Settings.autosaveOnClose);
    }
}