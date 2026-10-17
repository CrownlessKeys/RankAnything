package criteria;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

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
    private Layout() {}

    static void build(CriteriaPanel cp) {
        final boolean[] userIsTyping = { false };

        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        cp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        cp.setBackground(cp.activated);
        cp.setOpaque(true);

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

        Drag.install(cp, dragHandle);

        JPanel mainRow = new JPanel();
        mainRow.setLayout(new BoxLayout(mainRow, BoxLayout.X_AXIS));
        mainRow.setOpaque(false);
        mainRow.add(leftColumn);
        mainRow.add(Box.createHorizontalStrut(10));

        cp.headerRow = new JPanel();
        cp.headerRow.setLayout(new BoxLayout(cp.headerRow, BoxLayout.X_AXIS));
        cp.headerRow.setBackground(cp.activated);

        cp.nameField = new JTextField("", 12);

        cp.nameField.addActionListener(e -> {
            if (cp.onCriteriaChanged != null) cp.onCriteriaChanged.run();
        });

        cp.nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (cp.onCriteriaChanged != null) cp.onCriteriaChanged.run();
                cp.refreshExtraInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (cp.onCriteriaChanged != null) cp.onCriteriaChanged.run();
                cp.refreshExtraInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (cp.onCriteriaChanged != null) cp.onCriteriaChanged.run();
                cp.refreshExtraInfo();
            }
        });

        ImageIcon trashIcon = new ImageIcon(
            Layout.class.getResource("/iconmonstr-trash-can-filled.png")
        );

        JButton deleteButton = new JButton(trashIcon);
        deleteButton.setPreferredSize(new Dimension(24, 24));
        deleteButton.setMaximumSize(new Dimension(24, 24));
        deleteButton.setMinimumSize(new Dimension(24, 24));
        deleteButton.setBorder(BorderFactory.createEmptyBorder());
        deleteButton.setContentAreaFilled(false);
        deleteButton.setFocusPainted(false);

        cp.enabledCheck = new JCheckBox();
        cp.enabledCheck.setSelected(true);
        cp.enabledCheck.setBackground(cp.activated);

        cp.headerRow.add(cp.enabledCheck);
        cp.headerRow.add(Box.createHorizontalStrut(8));
        cp.headerRow.add(cp.nameField);
        cp.headerRow.add(Box.createHorizontalGlue());
        cp.headerRow.add(deleteButton);

        cp.headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, cp.headerRow.getPreferredSize().height));
        cp.headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        cp.nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, cp.nameField.getPreferredSize().height));
        cp.nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        deleteButton.addActionListener(e -> {
            JPanel wrapper = (JPanel) cp.getParent();
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

                        if (cp.onDelete != null) cp.onDelete.run();
                        if (cp.onStructureChanged != null) cp.onStructureChanged.run();

                        column.revalidate();
                        column.repaint();
                    }
                }
            });

            timerRef[0].start();
        });

        cp.weightRow = new JPanel();
        cp.weightRow.setLayout(new BoxLayout(cp.weightRow, BoxLayout.X_AXIS));
        cp.weightRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, cp.weightRow.getPreferredSize().height));
        cp.weightRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        cp.weightRow.setBackground(cp.activated);

        cp.weightSlider.setMajorTickSpacing(Main.isCriteriaPrecise() ? 10 : 2);
        cp.weightSlider.setPaintTicks(true);
        cp.weightSlider.setPaintLabels(true);
        cp.weightSlider.setMinimumSize(new Dimension(160, cp.weightSlider.getPreferredSize().height));
        cp.weightSlider.setPreferredSize(new Dimension(160, cp.weightSlider.getPreferredSize().height));
        cp.weightSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, cp.weightSlider.getPreferredSize().height));
        cp.weightSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        cp.weightSlider.setBackground(cp.activated);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 0; i <= 10; i++) {
            labelTable.put(i * (Main.isCriteriaPrecise() ? 10 : 2), new JLabel(String.valueOf(i)));
        }
        cp.weightSlider.setLabelTable(labelTable);

        Dimension taller = new Dimension(75, 28);
        cp.valueField.setPreferredSize(taller);
        cp.valueField.setMinimumSize(taller);
        cp.valueField.setMaximumSize(taller);

        cp.weightSlider.addChangeListener(e -> {
            if (!userIsTyping[0]) {
                double val = cp.weightSlider.getValue() / (Main.isCriteriaPrecise() ? 10.0 : 2.0);
                cp.valueField.setText(String.valueOf(val));
            }

            if (cp.onCriteriaChanged != null) cp.onCriteriaChanged.run();
            cp.refreshExtraInfo();
        });

        cp.valueField.addActionListener(e -> {
            try {
                userIsTyping[0] = true;

                double typed = Double.parseDouble(cp.valueField.getText());
                if (typed >= 0.0 && typed <= 10.0) {
                    int sliderVal = (int)Math.round(typed * (Main.isCriteriaPrecise() ? 10 : 2));
                    cp.weightSlider.setValue(sliderVal);
                }

                if (cp.onCriteriaChanged != null) cp.onCriteriaChanged.run();

            } catch (NumberFormatException ignored) {
            } finally {
                userIsTyping[0] = false;
            }

            cp.refreshExtraInfo();
        });

        cp.weightRow.add(cp.weightSlider);
        cp.weightRow.add(Box.createHorizontalStrut(10));
        cp.weightRow.add(cp.valueField);

        cp.weightRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, cp.weightRow.getPreferredSize().height));
        cp.weightRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        cp.setAlignmentX(Component.LEFT_ALIGNMENT);

        cp.extraInfoToggleLabel = new JLabel("More...");
        cp.extraInfoToggleLabel.setForeground(Color.GRAY);
        cp.extraInfoToggleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cp.extraInfoToggleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        cp.extraInfoPanel = new JPanel();
        cp.extraInfoPanel.setLayout(new BoxLayout(cp.extraInfoPanel, BoxLayout.Y_AXIS));
        cp.extraInfoPanel.setOpaque(false);
        cp.extraInfoPanel.setVisible(false);
        cp.extraInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel extraInfoToggleRow = new JPanel();
        extraInfoToggleRow.setLayout(new BoxLayout(extraInfoToggleRow, BoxLayout.X_AXIS));
        extraInfoToggleRow.setOpaque(false);
        extraInfoToggleRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        extraInfoToggleRow.add(Box.createHorizontalGlue());
        extraInfoToggleRow.add(cp.extraInfoToggleLabel);

        cp.infoName = new JLabel("Name: " + cp.getCriterionName());
        cp.infoName.setForeground(Color.DARK_GRAY);
        cp.extraInfoPanel.add(cp.infoName);

        cp.infoWeight = new JLabel("Weight: " + cp.getWeight());
        cp.infoWeight.setForeground(Color.DARK_GRAY);
        cp.extraInfoPanel.add(cp.infoWeight);

        cp.infoScore = new JLabel("Average Score: " + cp.computeAverageScore(Main.itemsList));
        cp.infoScore.setForeground(Color.DARK_GRAY);
        cp.extraInfoPanel.add(cp.infoScore);

        cp.infoVariance = new JLabel("Score Variance: " + cp.computeScoreVariance(Main.itemsList));
        cp.infoVariance.setForeground(Color.DARK_GRAY);
        cp.extraInfoPanel.add(cp.infoVariance);

        cp.infoCreation = new JLabel("Creation Index: " + cp.creationIndex);
        cp.infoCreation.setForeground(Color.DARK_GRAY);
        cp.extraInfoPanel.add(cp.infoCreation);

        ExtraInfo.initialize(cp);

        JPanel contentColumn = new JPanel();
        contentColumn.setLayout(new BoxLayout(contentColumn, BoxLayout.Y_AXIS));
        contentColumn.setOpaque(false);

        contentColumn.add(cp.headerRow);
        contentColumn.add(label);
        contentColumn.add(cp.weightRow);
        contentColumn.add(cp.extraInfoPanel);
        contentColumn.add(extraInfoToggleRow);

        mainRow.add(contentColumn);
        cp.add(mainRow);

        cp.enabledCheck.addActionListener(e -> cp.updateBackground());
    }
}