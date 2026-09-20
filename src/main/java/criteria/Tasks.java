package criteria;

import java.awt.Color;
import java.util.Hashtable;

import javax.swing.JLabel;

final class Tasks {
    static void updatePrecision(boolean precise, CriteriaPanel cp) {

        // get current value by checking valueField
        double value;
        try {
            String raw = cp.valueField.getText().trim();
            if (raw.endsWith("%")) raw = raw.substring(0, raw.length() - 1);
            value = Double.parseDouble(raw);
        } catch (Exception ex) {
            value = cp.weightSlider.getValue() / (precise ? 10 : 2); // fallback
        }

        // Change slider range
        if (precise) {
            cp.weightSlider.setMaximum(100);   // 0.1 increments
            cp.weightSlider.setMajorTickSpacing(10);

            // Custom labels: 0–10 instead of 0–100
            Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
            for (int i = 0; i <= 10; i++) {
                labelTable.put(i * 10, new JLabel(String.valueOf(i)));
            }
            cp.weightSlider.setLabelTable(labelTable);

        } else {
            cp.weightSlider.setMaximum(20);    // 0.5 increments
            cp.weightSlider.setMajorTickSpacing(2);

            // Custom labels: 0–10 instead of 0-20
            Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
            for (int i = 0; i <= 10; i++) {
                labelTable.put(i * 2, new JLabel(String.valueOf(i)));
            }
            cp.weightSlider.setLabelTable(labelTable);

        }

        // Convert value to slider's new scale
        int newSliderValue = precise
                ? (int)Math.round(value * 10)    // 0.1 increments
                : (int)Math.round(value * 2);    // 0.5 increments

        cp.weightSlider.setValue(newSliderValue);

        cp.weightSlider.setPaintTicks(true);
        cp.weightSlider.setPaintLabels(true);
        cp.weightSlider.revalidate();
        cp.weightSlider.repaint();

        // Make sure valueField stays accurate
        cp.valueField.setText(String.valueOf(value));
    }

    static void updateBackground(CriteriaPanel cp) {
        boolean on = cp.enabledCheck.isSelected();
        Color bg = on ? cp.activated : cp.deactivated;

        // Bubble + rows
        cp.setBackground(bg);
        cp.headerRow.setBackground(bg);
        cp.weightRow.setBackground(bg);

        // Child components
        cp.enabledCheck.setBackground(bg);
        cp.weightSlider.setBackground(bg);

        cp.repaint();

        if (cp.onStructureChanged != null) cp.onStructureChanged.run();
        if (cp.onEnabledChanged != null) cp.onEnabledChanged.run();
    }
}