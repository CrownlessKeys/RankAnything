package items;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import app.Main;
import criteria.CriteriaPanel;

final class Update {
    static void updateBackground(ItemsPanel item) {
        
        boolean on = item.enabledCheck.isSelected();
        Color bg = on ? item.activated : item.deactivated;

        // Bubble + rows
        item.setBackground(bg);
        item.headerRow.setBackground(bg);
        item.scoresColumn.setBackground(bg);

        // Child components
        item.enabledCheck.setBackground(bg);

        // Update backgrounds of score rows (but NOT number boxes)
        for (Component comp : item.scoresColumn.getComponents()) {
            if (comp instanceof JPanel row) {
                row.setBackground(bg);

                for (Component inner : row.getComponents()) {
                    // critLabel or sliderRow
                    inner.setBackground(bg);

                    if (inner instanceof JPanel sliderRow) {
                        sliderRow.setBackground(bg);

                        for (Component sliderComp : sliderRow.getComponents()) {

                            // Skip number box :)
                            if (sliderComp instanceof JTextField) {
                                continue;
                            }

                            // Update slider background
                            if (sliderComp != null) {
                                sliderComp.setBackground(bg);
                            }
                        }
                    }
                }
            }
        }

        item.repaint();

        if (item.onScoresChanged != null) item.onScoresChanged.run();
        if (item.onEnabledChanged != null) item.onEnabledChanged.run();
    }

    static void updatePrecision(boolean precise, ItemsPanel item) {

        for (CriteriaPanel cp : item.criteriaList) {

            JSlider slider = item.scoreMap.get(cp);
            JTextField valueField = item.valueFieldMap.get(cp);

            // get current value by checking valueField
            double value;
            try {
                String raw = valueField.getText().trim();
                if (raw.endsWith("%")) raw = raw.substring(0, raw.length() - 1);
                value = Double.parseDouble(raw);
            } catch (Exception ex) {
                value = slider.getValue() * (precise ? 1 : 5); // fallback
            }

            // Change slider range
            if (precise) {
                slider.setMaximum(100);   // 1% increments
                slider.setMajorTickSpacing(20);

                // Custom labels: 0–100 instead of 0–100... hey wait a minute... if it aint broke
                java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
                for (int i = 0; i <= 5; i++) {
                    labelTable.put(i * 20, new JLabel(String.valueOf(i * 20)));
                }
                slider.setLabelTable(labelTable);

            } else {
                slider.setMaximum(20);    // 5% increments
                slider.setMajorTickSpacing(4);

                // Custom labels: 0–100 instead of 0-20
                java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
                for (int i = 0; i <= 5; i++) {
                    labelTable.put(i * 4, new JLabel(String.valueOf(i * 20)));
                }
                slider.setLabelTable(labelTable);

            }

            // Convert value to slider's new scale
            int newSliderValue = precise
                    ? (int)Math.round(value)         // 1% increments
                    : (int)Math.round(value / 5);    // 5% increments

            slider.setValue(newSliderValue);

            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.revalidate();
            slider.repaint();

            // Make sure valueField stays accurate
            valueField.setText(String.valueOf(value) + "%");
        }
    }

    static void setScoreForCriterion(CriteriaPanel cp, double score, ItemsPanel item) {
        // Ensure the slider exists
        JSlider slider = item.scoreMap.get(cp);
        if (slider == null) {
            slider = new JSlider(0, Main.isItemsPrecise() ? 100 : 20, 0);
            item.scoreMap.put(cp, slider);
        }

        // Ensure the valueField exists
        JTextField valueField = item.valueFieldMap.get(cp);
        if (valueField == null) {
            valueField = new JTextField("0%", 4);
            item.valueFieldMap.put(cp, valueField);
        }

        // Convert score to slider scale
        int sliderVal = Main.isItemsPrecise()
                ? (int)Math.round(score)        // 1% increments
                : (int)Math.round(score / 5);   // 5% increments

        // Update slider
        slider.setValue(sliderVal);

        // Update text field
        valueField.setText(score + "%");
    }

    static double getScoreForCriterion(CriteriaPanel cp, ItemsPanel item) {
        JTextField valueField = item.valueFieldMap.get(cp);
        if (valueField == null) return 0;

        String raw = valueField.getText().trim();
        if (raw.endsWith("%")) raw = raw.substring(0, raw.length() - 1);

        try {
            return Double.parseDouble(raw);
        } catch (Exception e) {
            JSlider slider = item.scoreMap.get(cp);
            return slider != null ? (slider.getValue() * (Main.isItemsPrecise() ? 1 : 5)) : 0;
        }
    }
}
