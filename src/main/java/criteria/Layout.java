package criteria;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import app.Main;

final class Layout {
    private Layout() {
    }

    static void build(CriteriaPanel panel) {
        final boolean[] userIsTyping = { false };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(panel.activated);
        panel.setOpaque(true);

        JLabel label = new JLabel("Weight:");

        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));
        leftColumn.setOpaque(false);
        leftColumn.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel dragHandle = new JLabel("⋮⋮");
        dragHandle.setFont(new Font("SansSerif", Font.BOLD, 18));
        dragHandle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        dragHandle.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftColumn.add(Box.createVerticalGlue());
        leftColumn.add(dragHandle);
        leftColumn.add(Box.createVerticalGlue());

        Drag.install(panel, dragHandle);

        JPanel mainRow = new JPanel();
        mainRow.setLayout(new BoxLayout(mainRow, BoxLayout.X_AXIS));
        mainRow.setOpaque(false);
        mainRow.add(leftColumn);
        mainRow.add(Box.createHorizontalStrut(10));

        panel.headerRow = new JPanel();
        panel.headerRow.setLayout(new BoxLayout(panel.headerRow, BoxLayout.X_AXIS));
        panel.headerRow.setBackground(panel.activated);

        panel.nameField = new JTextField("", 12);

        panel.nameField.addActionListener(e -> {
            if (panel.onCriteriaChanged != null) panel.onCriteriaChanged.run();
        });

        panel.nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (panel.onCriteriaChanged != null) panel.onCriteriaChanged.run();
                panel.refreshExtraInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (panel.onCriteriaChanged != null) panel.onCriteriaChanged.run();
                panel.refreshExtraInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (panel.onCriteriaChanged != null) panel.onCriteriaChanged.run();
                panel.refreshExtraInfo();
            }
        });

        ImageIcon trashIcon = new ImageIcon("src/main/resources/iconmonstr-trash-can-filled.png");
        JButton deleteButton = new JButton(trashIcon);
        deleteButton.setPreferredSize(new Dimension(24, 24));
        deleteButton.setMaximumSize(new Dimension(24, 24));
        deleteButton.setMinimumSize(new Dimension(24, 24));
        deleteButton.setBorder(BorderFactory.createEmptyBorder());
        deleteButton.setContentAreaFilled(false);
        deleteButton.setFocusPainted(false);

        panel.enabledCheck = new JCheckBox();
        panel.enabledCheck.setSelected(true);
        panel.enabledCheck.setBackground(panel.activated);

        panel.headerRow.add(panel.enabledCheck);
        panel.headerRow.add(Box.createHorizontalStrut(8));
        panel.headerRow.add(panel.nameField);
        panel.headerRow.add(Box.createHorizontalGlue());
        panel.headerRow.add(deleteButton);

        panel.headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.headerRow.getPreferredSize().height));
        panel.headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.nameField.getPreferredSize().height));
        panel.nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        deleteButton.addActionListener(e -> {
            JPanel wrapper = (JPanel) panel.getParent();
            JPanel column = (JPanel) wrapper.getParent();

            final int frames = 8;
            final int delay = 8;
            final int fullOffset = wrapper.getHeight();
            final Timer[] timerRef = new Timer[1];

            timerRef[0] = new Timer(delay, new ActionListener() {
                int step = 0;

                @Override
                public void actionPerformed(ActionEvent evt) {
                    step++;
                    int offset = (int)(fullOffset * (step / (float)frames));
                    wrapper.setBorder(BorderFactory.createEmptyBorder(-offset, 0, 0, 0));
                    wrapper.revalidate();
                    wrapper.repaint();

                    if (step >= frames) {
                        timerRef[0].stop();
                        wrapper.removeAll();
                        column.remove(wrapper);

                        if (panel.onDelete != null) panel.onDelete.run();
                        if (panel.onStructureChanged != null) panel.onStructureChanged.run();

                        column.revalidate();
                        column.repaint();
                    }
                }
            });

            timerRef[0].start();
        });

        panel.weightRow = new JPanel();
        panel.weightRow.setLayout(new BoxLayout(panel.weightRow, BoxLayout.X_AXIS));
        panel.weightRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.weightRow.getPreferredSize().height));
        panel.weightRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.weightRow.setBackground(panel.activated);

        panel.weightSlider.setMajorTickSpacing(Main.isCriteriaPrecise() ? 10 : 2);
        panel.weightSlider.setPaintTicks(true);
        panel.weightSlider.setPaintLabels(true);
        panel.weightSlider.setMinimumSize(new Dimension(160, panel.weightSlider.getPreferredSize().height));
        panel.weightSlider.setPreferredSize(new Dimension(160, panel.weightSlider.getPreferredSize().height));
        panel.weightSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.weightSlider.getPreferredSize().height));
        panel.weightSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.weightSlider.setBackground(panel.activated);

        java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
        for (int i = 0; i <= 10; i++) {
            labelTable.put(i * (Main.isCriteriaPrecise() ? 10 : 2), new JLabel(String.valueOf(i)));
        }
        panel.weightSlider.setLabelTable(labelTable);

        Dimension taller = new Dimension(75, 28);
        panel.valueField.setPreferredSize(taller);
        panel.valueField.setMinimumSize(taller);
        panel.valueField.setMaximumSize(taller);

        panel.weightSlider.addChangeListener(e -> {
            if (!userIsTyping[0]) {
                double val = panel.weightSlider.getValue() / (Main.isCriteriaPrecise() ? 10.0 : 2.0);
                panel.valueField.setText(String.valueOf(val));
            }

            if (panel.onCriteriaChanged != null) panel.onCriteriaChanged.run();
            panel.refreshExtraInfo();
        });

        panel.valueField.addActionListener(e -> {
            try {
                userIsTyping[0] = true;

                double typed = Double.parseDouble(panel.valueField.getText());
                if (typed >= 0.0 && typed <= 10.0) {
                    int sliderVal = (int)Math.round(typed * (Main.isCriteriaPrecise() ? 10 : 2));
                    panel.weightSlider.setValue(sliderVal);
                }

                if (panel.onCriteriaChanged != null) panel.onCriteriaChanged.run();

            } catch (NumberFormatException ignored) {
            } finally {
                userIsTyping[0] = false;
            }

            panel.refreshExtraInfo();
        });

        panel.weightRow.add(panel.weightSlider);
        panel.weightRow.add(Box.createHorizontalStrut(10));
        panel.weightRow.add(panel.valueField);

        panel.weightRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.weightRow.getPreferredSize().height));
        panel.weightRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.extraInfoToggleLabel = new JLabel("More...");
        panel.extraInfoToggleLabel.setForeground(Color.GRAY);
        panel.extraInfoToggleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.extraInfoToggleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panel.extraInfoPanel = new JPanel();
        panel.extraInfoPanel.setLayout(new BoxLayout(panel.extraInfoPanel, BoxLayout.Y_AXIS));
        panel.extraInfoPanel.setOpaque(false);
        panel.extraInfoPanel.setVisible(false);
        panel.extraInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel extraInfoToggleRow = new JPanel();
        extraInfoToggleRow.setLayout(new BoxLayout(extraInfoToggleRow, BoxLayout.X_AXIS));
        extraInfoToggleRow.setOpaque(false);
        extraInfoToggleRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        extraInfoToggleRow.add(Box.createHorizontalGlue());
        extraInfoToggleRow.add(panel.extraInfoToggleLabel);

        panel.infoName = new JLabel("Name: " + panel.getCriterionName());
        panel.infoName.setForeground(Color.DARK_GRAY);
        panel.extraInfoPanel.add(panel.infoName);

        panel.infoWeight = new JLabel("Weight: " + panel.getWeight());
        panel.infoWeight.setForeground(Color.DARK_GRAY);
        panel.extraInfoPanel.add(panel.infoWeight);

        panel.infoScore = new JLabel("Average Score: " + panel.computeAverageScore(Main.itemsList));
        panel.infoScore.setForeground(Color.DARK_GRAY);
        panel.extraInfoPanel.add(panel.infoScore);

        panel.infoVariance = new JLabel("Score Variance: " + panel.computeScoreVariance(Main.itemsList));
        panel.infoVariance.setForeground(Color.DARK_GRAY);
        panel.extraInfoPanel.add(panel.infoVariance);

        panel.infoCreation = new JLabel("Creation Index: " + panel.creationIndex);
        panel.infoCreation.setForeground(Color.DARK_GRAY);
        panel.extraInfoPanel.add(panel.infoCreation);

        ExtraInfo.initialize(panel);

        JPanel contentColumn = new JPanel();
        contentColumn.setLayout(new BoxLayout(contentColumn, BoxLayout.Y_AXIS));
        contentColumn.setOpaque(false);

        contentColumn.add(panel.headerRow);
        contentColumn.add(label);
        contentColumn.add(panel.weightRow);
        contentColumn.add(panel.extraInfoPanel);
        contentColumn.add(extraInfoToggleRow);

        mainRow.add(contentColumn);
        panel.add(mainRow);

        panel.enabledCheck.addActionListener(e -> panel.updateBackground());
    }
}