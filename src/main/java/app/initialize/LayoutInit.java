package app.initialize;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import app.Main;
import app.MenuBar;
import app.Settings;
import autosave.AutoTimer;
import autosave.Autosave;

public final class LayoutInit {
    public static void build() {

        JFrame frame = new JFrame("Rank Anything — " + Main.projectName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        MenuBar menu = new MenuBar(frame);
        frame.setJMenuBar(menu);

        // First split: Criteria | Items
        JSplitPane splitLeft = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            Main.criteriaScroll,
            Main.itemsScroll
        );
        splitLeft.setResizeWeight(0.33);
        splitLeft.setDividerSize(8);

        // Second split: (Criteria + Items) | Ranking
        JSplitPane splitRight = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            splitLeft,
            Main.rankingScroll
        );
        splitRight.setResizeWeight(0.66);
        splitRight.setDividerSize(8);

        frame.add(splitRight);
        frame.setVisible(true);

        // Force equal thirds, but leave some extra width for Items
        splitLeft.setDividerLocation(frame.getWidth() / 3);
        splitRight.setDividerLocation(2 * frame.getWidth() / 3 + 10);

        splitRight.setContinuousLayout(true);
        splitLeft.setContinuousLayout(true);

        // autosave on close
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AutoTimer.stop();
                if (Settings.autosaveOnClose) Autosave.autosave(frame);
                System.exit(0);
            }
        });

        Main.frame = frame;
    }
}