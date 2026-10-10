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
import contact.Contact;
import export.Export;
import metadata.Metadata;
import saveload.SaveLoad;
import tasks.Browse;

public final class MenuBar extends JMenuBar {
    
    private static JCheckBoxMenuItem autosaveToggle;
    private static JCheckBoxMenuItem preciseSlidersToggle;
    private static JCheckBoxMenuItem autoRenameFilesToggle;
    public static JMenu recentProjectsMenu;

    public MenuBar(JFrame frame) {

        // FILE MENU
            JMenu fileMenu = new JMenu("File");
            add(fileMenu);

            JMenuItem newProjectItem = new JMenuItem("New project");
            JMenuItem openItem = new JMenuItem("Open...");
            JMenuItem saveItem = new JMenuItem("Save");
            JMenuItem saveAsItem = new JMenuItem("Save as...");
            JMenuItem exportItem = new JMenuItem("Export...");
            JMenuItem printItem = new JMenuItem("Print...");
            recentProjectsMenu = new JMenu("Recent projects");
            JMenuItem unsavedProjectsItem = new JMenuItem("Unsaved projects...");
            JMenuItem openFileLocationItem = new JMenuItem("Open file location");
            JMenuItem exitItem = new JMenuItem("Exit");
            
            JMenu autosavesMenu = new JMenu("Autosaves");
            JMenuItem projectAutosavesItem = new JMenuItem("Current project...");
            JMenuItem allAutosavesItem = new JMenuItem("All autosaves...");
            JMenuItem unsavedAutoItem = new JMenuItem("Unsaved projects...");
            autosavesMenu.add(projectAutosavesItem);
            autosavesMenu.add(allAutosavesItem);
            autosavesMenu.add(unsavedAutoItem);

            newProjectItem.setToolTipText("Create a new, empty project");
            openItem.setToolTipText("Open a previously saved project");
            saveItem.setToolTipText("Save the current project");
            saveAsItem.setToolTipText("Save the current project to a new file");
            exportItem.setToolTipText("Export the results of your project to a viewable file");
            recentProjectsMenu.setToolTipText("Open a recently opened project");
            unsavedProjectsItem.setToolTipText("Recover a project that was never manually saved to a file");
            openFileLocationItem.setToolTipText("Open the location of the current project file");
            exitItem.setToolTipText("Exit the application");
            autosavesMenu.setToolTipText("View backup save files of your projects");
            projectAutosavesItem.setToolTipText("View backup save files of the current project");
            allAutosavesItem.setToolTipText("View all backup save files of your projects");
            unsavedAutoItem.setToolTipText("View backup save files of projects that were never manually saved to a file");

            // layout
            fileMenu.add(newProjectItem);
            fileMenu.add(openItem);
            fileMenu.add(recentProjectsMenu);
            fileMenu.addSeparator();
            fileMenu.add(saveItem);
            fileMenu.add(saveAsItem);
            fileMenu.add(autosavesMenu);
            fileMenu.add(unsavedProjectsItem);
            fileMenu.addSeparator();
            fileMenu.add(exportItem);
            fileMenu.add(printItem);
            fileMenu.addSeparator();
            fileMenu.add(openFileLocationItem);
            fileMenu.addSeparator();
            fileMenu.add(exitItem);

            newProjectItem.setAccelerator(KeyStroke.getKeyStroke("control N"));
            openItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
            saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
            printItem.setAccelerator(KeyStroke.getKeyStroke("control P"));
            saveAsItem.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
            projectAutosavesItem.setAccelerator(KeyStroke.getKeyStroke("control A"));
            exitItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));

            RecentProjects.updateRecentProjectsMenu(frame);

        // PROJECT MENU
            JMenu projectMenu = new JMenu("Project");
            add(projectMenu);

            JMenuItem projectInfoItem = new JMenuItem("Project info...");
            JMenuItem editNotesItem = new JMenuItem("Edit notes...");
            JMenuItem renameProjectItem = new JMenuItem("Rename project...");

            projectInfoItem.setToolTipText("View information about the current project");
            editNotesItem.setToolTipText("Edit notes for the current project");
            renameProjectItem.setToolTipText("Rename the current project");

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

            autosaveToggle = new JCheckBoxMenuItem("Enable autosave");
            autosaveToggle.setSelected(Settings.autosavePeriodically || Settings.autosaveOnClose);
            autosaveToggle.setToolTipText(
                "Enable or disable the automatic creation of backup save files of your project periodically and when closing the project with unsaved changes."
            );
            settingsMenu.add(autosaveToggle);

            JMenuItem autosaveSettingsItem = new JMenuItem("Autosave settings...");
            settingsMenu.add(autosaveSettingsItem);
            settingsMenu.addSeparator();

            preciseSlidersToggle = new JCheckBoxMenuItem("Default to precise sliders");
            preciseSlidersToggle.setSelected(Settings.preciseSlidersCriteria || Settings.preciseSlidersItems);
            preciseSlidersToggle.setToolTipText(
                "Choose whether projects use precise sliders by default."
            );
            settingsMenu.add(preciseSlidersToggle);

            JMenuItem defaultProjectSettingsItem = new JMenuItem("Default project settings...");
            settingsMenu.add(defaultProjectSettingsItem);
            settingsMenu.addSeparator();

            autoRenameFilesToggle = new JCheckBoxMenuItem("Automatically rename files");
            autoRenameFilesToggle.setSelected(Settings.autoRenameFiles);
            autoRenameFilesToggle.setToolTipText(
                "Enable or disable automatic renaming of your current project file when saving."
            );
            settingsMenu.add(autoRenameFilesToggle);
            settingsMenu.addSeparator();

            JMenuItem restoreDefaultsItem = new JMenuItem("Restore default settings");
            settingsMenu.add(restoreDefaultsItem);

        // HELP MENU
            JMenu helpMenu = new JMenu("Help");
            add(helpMenu);

            JMenuItem musicItem = new JMenuItem("Background music ↗");
            musicItem.setToolTipText("Listen to some beautiful piano playing by yours truly while you rank");
            helpMenu.add(musicItem);

            JMenuItem gitItem = new JMenuItem("View source code ↗");
            gitItem.setToolTipText("View this application's source code repository on GitHub in your web browser");
            helpMenu.add(gitItem);
            helpMenu.addSeparator();

            JMenuItem bugItem = new JMenuItem("Report a bug...");
            bugItem.setToolTipText("Tell the developer about a problem or error in the application so he can work on it");
            helpMenu.add(bugItem);

            JMenu contactMenu = new JMenu("Contact developer");
            JMenuItem bugcontactItem = new JMenuItem("Report a bug...");
            bugcontactItem.setToolTipText("Tell the developer about a problem or error in the application so he can work on it");
            contactMenu.add(bugcontactItem);
            JMenuItem featureItem = new JMenuItem("Request a feature...");
            featureItem.setToolTipText("Let me know about any ideas you have to improve Rank Anything!");
            contactMenu.add(featureItem);
            JMenuItem generalItem = new JMenuItem("Other...");
            generalItem.setToolTipText("Send an email to the developer");
            contactMenu.add(generalItem);
            helpMenu.add(contactMenu);
            helpMenu.addSeparator();

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
        contactMenu.setMnemonic('C');

        // LISTENERS
        // File
            saveAsItem.addActionListener(e -> SaveLoad.saveAs(frame));
            openItem.addActionListener(e -> SaveLoad.load(frame));
            saveItem.addActionListener(e -> SaveLoad.save(frame));
            exportItem.addActionListener(e -> Export.export());
            printItem.addActionListener(e -> Export.print());
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

        // Project
            projectInfoItem.addActionListener(e -> Metadata.showProjectInfoDialog(frame));
            editNotesItem.addActionListener(e -> Metadata.showNotesEditor(frame));
            renameProjectItem.addActionListener(e -> Metadata.showProjectNameEditor(frame));

        // Settings
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

            preciseSlidersToggle.addActionListener(e -> {
                boolean enabled = preciseSlidersToggle.isSelected();
                Settings.preciseSlidersCriteria = enabled;
                Settings.preciseSlidersItems = enabled;
                Settings.save();
            });
            defaultProjectSettingsItem.addActionListener(e -> DefaultSettings.show(frame));

            autoRenameFilesToggle.addActionListener(e -> {
                Settings.autoRenameFiles = autoRenameFilesToggle.isSelected();
                Settings.save();
            });
            
            restoreDefaultsItem.addActionListener(e -> Settings.restoreDefaults(frame));

        // Help
            musicItem.addActionListener(e -> Browse.openWebpage(frame, "https://YouTube.com/@CrownlessKeys"));
            gitItem.addActionListener(e -> Browse.openWebpage(frame, "https://github.com/CrownlessKeys/RankAnything"));

            bugItem.addActionListener(e -> Contact.reportBug());
            bugcontactItem.addActionListener(e -> Contact.reportBug());
            featureItem.addActionListener(e -> Contact.requestFeature());
            generalItem.addActionListener(e -> Contact.generalContact());
        // end
    }

    public static void updateSettingsMenu() {
        autoRenameFilesToggle.setSelected(Settings.autoRenameFiles);
        autosaveToggle.setSelected(Settings.autosavePeriodically || Settings.autosaveOnClose);
        preciseSlidersToggle.setSelected(Settings.preciseSlidersCriteria || Settings.preciseSlidersItems);
    }
}