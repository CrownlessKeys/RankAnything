package app;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import saveload.SaveLoad;

public final class RecentProjects {
    
    public static List<String> recentProjects = new ArrayList<>();

    public static void load() {
        try {
            File file = Main.RECENT_PROJECTS_FILE;
            if (!file.exists()) {
                save(); // create default settings
                return;
            }

            String json = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(json);

            JSONArray arr = root.optJSONArray("recentProjects");
            if (arr != null) {
                recentProjects.clear();
                for (int i = 0; i < arr.length(); i++) {
                    recentProjects.add(arr.getString(i));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void save() {
        try {
            JSONObject root = new JSONObject();
            root.put("recentProjects", recentProjects);

            File file = Main.RECENT_PROJECTS_FILE;
            file.getParentFile().mkdirs(); // ensure directory exists

            Files.writeString(file.toPath(), root.toString(4), StandardCharsets.UTF_8);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addRecentProject(String path) {

        // Remove if already exists
        recentProjects.remove(path);

        // Add to top
        recentProjects.add(0, path);

        // Remove missing files
        recentProjects.removeIf(p -> !new File(p).exists());

        // Limit to 10
        while (recentProjects.size() > 10) {
            recentProjects.remove(recentProjects.size() - 1);
        }
    }

    public static void updateRecentProjectsMenu(JFrame frame) {
        JMenu recentProjectsMenu = MenuBar.recentProjectsMenu;
        
        recentProjectsMenu.removeAll();

        if (recentProjects.isEmpty()) {
            JMenuItem none = new JMenuItem("No recent projects");
            none.setEnabled(false);
            none.setToolTipText(
                "No project files have been opened recently. Try creating or opening a project to get started!"
            );
            recentProjectsMenu.add(none);
            return;
        }

        for (String path : recentProjects) {
            File f = new File(path);

            String displayName = shortenFileName(f.getName());
            JMenuItem item = new JMenuItem(displayName);
            item.setToolTipText(f.getAbsolutePath());
            
            item.addActionListener(e -> {
                if (!f.exists()) {
                    JOptionPane.showMessageDialog(
                        frame,
                        """
                            The project file could not be found. 
                            It may have been moved, renamed, or deleted.
                        """,
                        "File Missing",
                        JOptionPane.ERROR_MESSAGE
                    );
                    recentProjects.remove(path);
                    save();
                    updateRecentProjectsMenu(frame);
                    return;
                }

                SaveLoad.loadFromFile(f, frame, false);
            });

            recentProjectsMenu.add(item);
        }

        recentProjectsMenu.addSeparator();

        JMenuItem clear = new JMenuItem("Clear Recent Projects");
        clear.addActionListener(e -> {
            recentProjects.clear();
            save();
            updateRecentProjectsMenu(frame);
        });

        recentProjectsMenu.add(clear);

        recentProjectsMenu.revalidate();
        recentProjectsMenu.repaint();
    }

    private static String shortenFileName(String name) {
        int maxStart = 20;   // characters to keep at the start
        int maxEnd = 4;      // characters to keep at the end ("rank")

        if (name.length() <= maxStart + maxEnd + 3) {
            return name; // no need to shorten
        }

        String start = name.substring(0, maxStart).trim();
        String end = name.substring(name.length() - maxEnd).trim();

        return start + "..." + end;
    }
}