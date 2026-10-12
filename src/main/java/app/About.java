package app;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

final class About {

    // Prevent instantiation
    private About() {}

    static void show(JFrame parent) {

        String html = """
<html>
<body style='font-family:sans-serif;'>
Rank Anything<br>
Version %s<br><br>

Created by Daniel Murad<br><br>

Rank Anything is a mathematical ranking tool designed to compare and<br>
evaluate options of any kind using weighted criteria. Assign importance<br>
values, score each item, and view a clear, data-driven ranking.<br><br>

This beta version is provided for evaluation and feedback.<br>
No personal data is automatically collected or transmitted.<br><br>

&copy; 2026 Daniel Murad
</body>
</html>
""".formatted(Main.APP_VERSION);

        JLabel label = new JLabel(html);

        JOptionPane.showMessageDialog(
            parent,
            label,
            "About Rank Anything",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}