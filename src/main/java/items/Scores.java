package items;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;

import app.Main;
import criteria.CriteriaPanel;

final class Scores {
    private Scores() {
    }

    static void buildInitialScores(ItemsPanel panel) {
        panel.scoresColumn = new JPanel();
        panel.scoresColumn.setLayout(new BoxLayout(panel.scoresColumn, BoxLayout.Y_AXIS));
        panel.scoresColumn.setOpaque(false);

        for (CriteriaPanel cp : panel.criteriaList) {
            if (cp.getParent() == null) continue;
            if (!cp.isEnabledCriterion()) continue;
            addRow(panel, cp);
        }
    }

    static void refreshCriteria(ItemsPanel panel) {
        if (panel.scoresColumn == null) return;

        panel.scoresColumn.removeAll();

        for (CriteriaPanel cp : panel.criteriaList) {
            if (cp.getParent() == null) continue;
            ensureSlider(panel, cp);
            ensureValueField(panel, cp);

            JSlider slider = panel.scoreMap.get(cp);
            JTextField valueField = panel.valueFieldMap.get(cp);
            if (slider == null || valueField == null) {
                continue;
            }

            final JSlider finalSlider = slider;
            slider.addChangeListener(e -> {
                if (panel.typingScore) return;

                JTextField vf = panel.valueFieldMap.get(cp);
                if (vf != null) vf.setText((finalSlider.getValue() * (Main.isItemsPrecise() ? 1 : 5)) + "%");

                if (panel.debounceTimer != null && panel.debounceTimer.isRunning()) {
                    panel.debounceTimer.restart();
                } else {
                    panel.debounceTimer = new Timer(30, evt -> {
                        panel.debounceTimer.stop();
                        if (panel.onScoresChanged != null) panel.onScoresChanged.run();
                    });
                    panel.debounceTimer.setRepeats(false);
                    panel.debounceTimer.start();
                }
            });

            final JTextField finalValueField = valueField;
            valueField.addActionListener(e -> {
                panel.typingScore = true;

                try {
                    String raw = finalValueField.getText().trim();
                    if (raw.endsWith("%")) raw = raw.substring(0, raw.length() - 1);

                    double typed = Double.parseDouble(raw);
                    int sliderVal = (int)Math.round(typed);

                    finalSlider.setValue(sliderVal);
                    finalValueField.setText(typed + "%");
                } catch (Exception ignored) {
                    finalValueField.setText((finalSlider.getValue() * (Main.isItemsPrecise() ? 1 : 5)) + "%");
                }

                panel.typingScore = false;

                if (panel.onScoresChanged != null) panel.onScoresChanged.run();
            });

            slider.setBackground(panel.enabledCheck.isSelected() ? panel.activated : panel.deactivated);
            slider.setMinimumSize(new Dimension(155, slider.getPreferredSize().height));
            slider.setPreferredSize(new Dimension(155, slider.getPreferredSize().height));
            slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, slider.getPreferredSize().height));

            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
            row.setOpaque(false);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel critLabel = new JLabel(cp.getCriterionName());
            critLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel sliderRow = new JPanel();
            sliderRow.setLayout(new BoxLayout(sliderRow, BoxLayout.X_AXIS));
            sliderRow.setOpaque(false);
            sliderRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            boolean enabled = cp.isEnabledCriterion();
            if (!enabled) continue;

            sliderRow.add(slider);
            sliderRow.add(Box.createHorizontalStrut(10));
            sliderRow.add(valueField);

            row.add(critLabel);
            row.add(sliderRow);

            panel.scoresColumn.add(row);
            panel.scoresColumn.add(Box.createVerticalStrut(5));
        }

        panel.scoresColumn.revalidate();
        panel.scoresColumn.repaint();
        panel.refreshExtraInfo();
    }

    private static void addRow(ItemsPanel panel, CriteriaPanel cp) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel critLabel = new JLabel(cp.getCriterionName());
        critLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        critLabel.setText(cp.getCriterionName());

        JPanel sliderRow = new JPanel();
        sliderRow.setLayout(new BoxLayout(sliderRow, BoxLayout.X_AXIS));
        sliderRow.setOpaque(false);
        sliderRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider slider = new JSlider(0, Main.isItemsPrecise() ? 100 : 20, Main.isItemsPrecise() ? 50 : 10);
        slider.setMajorTickSpacing(Main.isItemsPrecise() ? 20 : 4);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(panel.activated);
        slider.setMinimumSize(new Dimension(155, slider.getPreferredSize().height));
        slider.setPreferredSize(new Dimension(155, slider.getPreferredSize().height));
        slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, slider.getPreferredSize().height));
        panel.scoreMap.put(cp, slider);

        if (!Main.isItemsPrecise()) {
            java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
            for (int i = 0; i <= 5; i++) {
                labelTable.put(i * 4, new JLabel(String.valueOf(i * 20)));
            }
            slider.setLabelTable(labelTable);
        }

        JTextField valueField = new JTextField("50%", 4);
        Dimension valueFieldSize = new Dimension(75, 28);
        valueField.setPreferredSize(valueFieldSize);
        valueField.setMinimumSize(valueFieldSize);
        valueField.setMaximumSize(valueFieldSize);
        panel.valueFieldMap.put(cp, valueField);

        slider.addChangeListener(e -> {
            if (panel.typingScore) return;

            valueField.setText((slider.getValue() * (Main.isItemsPrecise() ? 1 : 5)) + "%");

            if (panel.debounceTimer != null && panel.debounceTimer.isRunning()) {
                panel.debounceTimer.restart();
                return;
            }

            panel.debounceTimer = new Timer(30, evt -> {
                panel.debounceTimer.stop();
                if (panel.onScoresChanged != null) panel.onScoresChanged.run();
            });

            panel.debounceTimer.setRepeats(false);
            panel.debounceTimer.start();
        });

        valueField.addActionListener(e -> {
            panel.typingScore = true;

            try {
                String raw = valueField.getText().trim();
                if (raw.endsWith("%")) raw = raw.substring(0, raw.length() - 1);

                double typed = Double.parseDouble(raw);
                int sliderVal = (int)Math.round(typed / (Main.isItemsPrecise() ? 1 : 5));
                slider.setValue(sliderVal);
                valueField.setText(typed + "%");
            } catch (Exception ignored) {
                valueField.setText((slider.getValue() * (Main.isItemsPrecise() ? 1 : 5)) + "%");
            }

            panel.typingScore = false;

            if (panel.onScoresChanged != null) panel.onScoresChanged.run();
        });

        sliderRow.add(slider);
        sliderRow.add(Box.createHorizontalStrut(10));
        sliderRow.add(valueField);

        row.add(critLabel);
        row.add(sliderRow);

        panel.scoresColumn.add(row);
        panel.scoresColumn.add(Box.createVerticalStrut(5));
    }

    private static void ensureSlider(ItemsPanel panel, CriteriaPanel cp) {
        JSlider slider = panel.scoreMap.get(cp);
        if (slider == null) {
            slider = new JSlider(0, Main.isItemsPrecise() ? 100 : 20, Main.isItemsPrecise() ? 50 : 10);
            slider.setMajorTickSpacing(Main.isItemsPrecise() ? 20 : 4);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            panel.scoreMap.put(cp, slider);

            if (!Main.isItemsPrecise()) {
                java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
                for (int i = 0; i <= 5; i++) {
                    labelTable.put(i * 4, new JLabel(String.valueOf(i * 20)));
                }
                slider.setLabelTable(labelTable);
            }
        }
    }

    private static void ensureValueField(ItemsPanel panel, CriteriaPanel cp) {
        JTextField valueField = panel.valueFieldMap.get(cp);
        if (valueField == null) {
            valueField = new JTextField("50%", 4);
            Dimension size = new Dimension(75, 28);
            valueField.setPreferredSize(size);
            valueField.setMinimumSize(size);
            valueField.setMaximumSize(size);
            panel.valueFieldMap.put(cp, valueField);
        }
    }
}
