package items;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import app.Main;
import metadata.Metadata;

final class Drag {
    private Drag() {
    }

    static void install(ItemsPanel panel, JLabel dragHandle) {
        dragHandle.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JPanel wrapper = (JPanel) panel.getParent();
                JPanel column = (JPanel) wrapper.getParent();
                panel.dragStart = SwingUtilities.convertPoint(dragHandle, e.getPoint(), column);
            }
        });

        dragHandle.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                JPanel wrapper = (JPanel) panel.getParent();
                JPanel column = (JPanel) wrapper.getParent();

                Point current = SwingUtilities.convertPoint(dragHandle, e.getPoint(), column);
                int dy = current.y - panel.dragStart.y;

                if (Math.abs(dy) < 5) {
                    return;
                }

                int wrapperIndex = column.getComponentZOrder(wrapper);

                if (dy < 0 && wrapperIndex > 0) {
                    Component above = column.getComponent(wrapperIndex - 1);
                    Rectangle r = above.getBounds();
                    int midY = r.y + r.height / 2;

                    if (current.y < midY) {
                        if (wrapperIndex == 1 && dy < 0) {
                            return;
                        }

                        column.remove(wrapper);
                        column.add(wrapper, wrapperIndex - 1);
                        column.revalidate();
                        column.repaint();
                        panel.dragStart = current;

                        Main.unsavedChanges = true;
                        Main.projectDirty = true;
                        Metadata.updateWindowTitle();
                    }
                }

                if (dy > 0 && wrapperIndex < column.getComponentCount() - 1) {
                    Component below = column.getComponent(wrapperIndex + 1);
                    Rectangle r = below.getBounds();
                    int midY = r.y + r.height / 2;

                    if (current.y > midY) {
                        column.remove(wrapper);
                        column.add(wrapper, wrapperIndex + 1);
                        column.revalidate();
                        column.repaint();
                        panel.dragStart = current;

                        Main.unsavedChanges = true;
                        Main.projectDirty = true;
                        Metadata.updateWindowTitle();
                    }
                }
            }
        });
    }
}