package criteria;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import app.Main;
import items.ItemsPanel;

@SuppressWarnings("JdkObsolete")
public final class CriteriaPanel extends JPanel {

    public final JTextField valueField;
    public final JSlider weightSlider;
    JTextField nameField;
    public JCheckBox enabledCheck;
    Point dragStart;
    JPanel extraInfoPanel;
    JLabel extraInfoToggleLabel;
    boolean extraInfoVisible = false;
    JPanel headerRow;
    JPanel weightRow;

    JLabel infoName;
    JLabel infoWeight;
    JLabel infoScore;
    JLabel infoVariance;
    JLabel infoCreation;

    final Color activated = new Color(250, 251, 249);
    final Color deactivated = new Color(180, 180, 180);

    List<CriteriaPanel> criteriaList;

    public int creationIndex;

    public void setCriteriaList(List<CriteriaPanel> list) {
        this.criteriaList = list;
    }

    public CriteriaPanel() {
        weightSlider = new JSlider(0, Main.isCriteriaPrecise() ? 100 : 20, Main.isCriteriaPrecise() ? 50 : 10);
        valueField = new JTextField("5", 4);
    }

    public void initialize() {
        Layout.build(this);
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension pref = getPreferredSize();
        return new Dimension(Integer.MAX_VALUE, pref.height);
    }

    public boolean isEnabledCriterion() {
        return enabledCheck.isSelected();
    }

    public final String getCriterionName() {
        return nameField.getText();
    }

    Runnable onCriteriaChanged;

    public void setOnCriteriaChanged(Runnable r) {
        this.onCriteriaChanged = r;
    }

    Runnable onDelete;

    public void setOnDelete(Runnable r) {
        this.onDelete = r;
    }

    public final double getWeight() {
        try {
            return new java.math.BigDecimal(valueField.getText().trim()).doubleValue();
        } catch (Exception e) {
            return weightSlider.getValue() / (Main.isCriteriaPrecise() ? 10 : 2); // fallback
        }
    }

    Runnable onStructureChanged;

    public void setOnStructureChanged(Runnable r) {
        this.onStructureChanged = r;
    }

    public void updatePrecision(boolean precise) {

        // get current value by checking valueField
        double value;
        try {
            String raw = valueField.getText().trim();
            if (raw.endsWith("%")) raw = raw.substring(0, raw.length() - 1);
            value = Double.parseDouble(raw);
        } catch (Exception ex) {
            value = weightSlider.getValue() / (precise ? 10 : 2); // fallback
        }

        // Change slider range
        if (precise) {
            weightSlider.setMaximum(100);   // 0.1 increments
            weightSlider.setMajorTickSpacing(10);

            // Custom labels: 0–10 instead of 0–100
            java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
            for (int i = 0; i <= 10; i++) {
                labelTable.put(i * 10, new JLabel(String.valueOf(i)));
            }
            weightSlider.setLabelTable(labelTable);

        } else {
            weightSlider.setMaximum(20);    // 0.5 increments
            weightSlider.setMajorTickSpacing(2);

            // Custom labels: 0–10 instead of 0-20
            java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
            for (int i = 0; i <= 10; i++) {
                labelTable.put(i * 2, new JLabel(String.valueOf(i)));
            }
            weightSlider.setLabelTable(labelTable);

        }

        // Convert value to slider's new scale
        int newSliderValue = precise
                ? (int)Math.round(value * 10)    // 0.1 increments
                : (int)Math.round(value * 2);    // 0.5 increments

        weightSlider.setValue(newSliderValue);

        weightSlider.setPaintTicks(true);
        weightSlider.setPaintLabels(true);
        weightSlider.revalidate();
        weightSlider.repaint();

        // Make sure valueField stays accurate
        valueField.setText(String.valueOf(value));
    }

    public int getCreationIndex() {
        return creationIndex;
    }

    public void rename(String name) {
        nameField.setText(name);
    }

    public final BigDecimal computeAverageScore(java.util.List<ItemsPanel> itemsList) {
        return Calculate.averageScore(this, itemsList);
    }

    public final BigDecimal computeScoreVariance(java.util.List<ItemsPanel> itemsList) {
        return Calculate.scoreVariance(this, itemsList);
    }

    Runnable onEnabledChanged;

    public void setOnEnabledChanged(Runnable r) {
        this.onEnabledChanged = r;
    }

    public void refreshExtraInfo() {
        ExtraInfo.refresh(this);
    }

    public void updateBackground() {
        boolean on = enabledCheck.isSelected();
        Color bg = on ? activated : deactivated;

        // Bubble + rows
        setBackground(bg);
        headerRow.setBackground(bg);
        weightRow.setBackground(bg);

        // Child components
        enabledCheck.setBackground(bg);
        weightSlider.setBackground(bg);

        repaint();

        if (onStructureChanged != null) onStructureChanged.run();
        if (onEnabledChanged != null) onEnabledChanged.run();
    }
}