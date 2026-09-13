package tasks;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;

public final class Notify {
    public static void showToast(JFrame frame, String message) {
        JWindow toast = new JWindow(frame);
        JLabel label = new JLabel(message);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 180));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        toast.add(label);

        toast.pack();

        // Position at bottom-center of the frame
        int x = frame.getX() + (frame.getWidth() - toast.getWidth()) / 2;
        int y = frame.getY() + frame.getHeight() - toast.getHeight() - 50;
        toast.setLocation(x, y);

        toast.setVisible(true);

        // Auto-hide after 1.5 seconds
        new Timer(1500, e -> toast.setVisible(false)).start();
    }
}