package app.initialize;

import java.awt.datatransfer.DataFlavor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.TransferHandler;

import app.Main;
import app.MenuBar;
import app.Settings;
import autosave.AutoTimer;
import autosave.Autosave;
import metadata.Metadata;
import saveload.SaveLoad;

public final class LayoutInit {
    public static void build() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        // drag-and-drop file opening
        frame.setTransferHandler(new TransferHandler() {

            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    @SuppressWarnings("unchecked")
                    List<File> files = (List<File>) support.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);

                    if (files.isEmpty()) return false;

                    File file = files.get(0);

                    // load file, same as "Open..."
                    SaveLoad.loadFromFile(file, frame, false);

                    return true;

                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        });

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
        Metadata.updateWindowTitle();
    }
}