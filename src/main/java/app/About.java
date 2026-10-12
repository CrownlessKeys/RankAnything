package app;

import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

final class About {

    // Prevent instantiation
    private About() {}

    static void showAbout(JFrame parent) {

        String html = """
<html>
<body style='font-family:sans-serif;'>
Rank Anything<br>
Version %s<br><br>

Created by Daniel Murad<br><br>

<i>Rank Anything</i> is a mathematical ranking tool designed to compare and<br>
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

    static void showGuide(JFrame parent) {

        String html = """
<html>
<body style='font-family:sans-serif; padding:8px;'>

<h2><i>Rank Anything</i>: User Guide</h2>

<b>Welcome to <i>Rank Anything</i>!</b><br>
This guide explains how to use the app, how scoring works, and how to get the most out of your rankings.<br><br>

For questions or clarification regarding any specific action or button, most have an informational tooltip that will appear when you hover your mouse over them. If any go unexplained, please let me know through Help → Contact developer → Request a feature...<br><br>


<h3>What <i>Rank Anything</i> Does</h3>
<i>Rank Anything</i> is a mathematical ranking tool that helps you compare and evaluate options of any kind using weighted criteria.<br><br>

In decision-making, it helps reveal what you truly want. Everything you input is completely subjective, so all the app will do is help you gather your own personal preferences and priorities so the clear choice for you can come to light. You choose what matters, how much it matters, and how each item performs.<br><br>

In objective scoring, it provides a consistent, mathematical framework for analyzing performance, measuring quality, evaluating data, and anything else you can think of that involves weighted scoring.<br><br>

Use it for:<br>
• choosing colleges<br>
• comparing players<br>
• evaluating job offers<br>
• scoring items in games<br>
• planning courses<br>
• judging chess openings<br>
• organizing ideas<br>
• or Ranking Anything else you care about (see what I did there?). The possibilities are limitless!<br><br>

<h3>Core Concepts</h3>
<b>Items:</b> the things you want to compare.<br>
<b>Criteria:</b> the qualities to compare them by.<br>
<b>Weights:</b> importance values for each criterion.<br>
<b>Scores:</b> percent of perfection for each item under each criterion.<br><br>

<h3>How to Use <i>Rank Anything</i></h3>

<b>1. Create your criteria</b><br>
Add each factor you want to consider and assign an importance weight out of ten.<br><br>

<b>2. Add your items</b><br>
Add everything you want to compare.<br><br>

<b>3. Score each item</b><br>
Score each item on each criterion from 0% to 100% based on how close it comes to your ideal in that cateogory.<br><br>

<b>4. View your ranking</b><br>
<i>Rank Anything</i> combines your weights and scores mathematically to produce a final ranking.<br><br>
You can switch between modes at the top of the Ranking Panel to view alternate rankings, such as unweighted, considering disabled criteria, etc.<br><br>

<b>5. Adjust and refine</b><br>
Change weights, scores, criteria, or items at any time. Rankings update instantly.<br><br>

<h3>Tips for Better Rankings</h3>
• Try not to leave things static or only change a little at a time. Little adjustments are valuable, but one of the beauties of this program is the ability to make dramatic changes and see how your items resort themselves. Click the checkbox at the top of each criterion to see what happens if your top criteria were not considered. Boost the weighting of your lowest criteria and see what happens. Disable weighting entirely by clicking the checkbox at the top of the Ranking Panel. Try everything! This can help put your priorities into perspective.<br>
• Save your project using File → Save. Sleep on it. Come back to it later. Maybe you reconsider your priorities. Maybe you remember a criterion you had neglected. This becomes more valuable the more significant the decision or ranking is.<br>
• Collapse items you aren't actively editing by clicking the arrow on each. This makes the list much more readable.<br>
• Hide disabled items and criteria* using the checkbox at the top of each panel when not using them. This makes each list much more readable.<br>
• Use precise sliders* when going based on vibes. Turn them off to make things less susceptible to a mouse slip or minor change in whims. Type directly into the number box when analyzing objective data.<br>
• When finished, save your results as a readable file using File → Export...<br>
• Let me know if you have any suggestions or if you encounter any bugs through Help → Contact developer.<br>
• Trust me, go to Help → Background music ↗. You won't regret it.<br><br>

*Note: you can decide whether new projects use precise sliders by default in Settings. Same goes for hiding disabled items or criteria.<br><br>

<h3>Saving and Loading</h3>
Save your project at any time and load it later. Autosave protects your work and allows restoring older versions.<br><br>

<h3>Bug Reports</h3>
You may optionally submit basic diagnostic information when reporting bugs through the Help menu, such as operating system, settings, or project files.<br>
No personal data is automatically collected.<br><br>

<h3>Thank You!</h3>
<i>Rank Anything</i> is in active development. Your feedback through Help → Contact developer helps improve the app.<br><br>

</body>
</html>
""";

        // Use JEditorPane for scrollable HTML
        JEditorPane pane = new JEditorPane("text/html", html);
        pane.setEditable(false);
        pane.setCaretPosition(0); // start at the top

        JScrollPane scroll = new JScrollPane(pane);
        scroll.setPreferredSize(new Dimension(600, 500));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JOptionPane.showMessageDialog(
            parent,
            scroll,
            "User Guide",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}