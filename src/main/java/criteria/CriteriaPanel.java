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
    JCheckBox enabledCheck;
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
        valueField = new JTextField("5.0", 4);
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
            return new BigDecimal(valueField.getText().trim()).doubleValue();
        } catch (Exception e) {
            return weightSlider.getValue() / (Main.isCriteriaPrecise() ? 10 : 2); // fallback
        }
    }

    Runnable onStructureChanged;

    public void setOnStructureChanged(Runnable r) {
        this.onStructureChanged = r;
    }

    public void updatePrecision(boolean precise) {
        Tasks.updatePrecision(precise, this);
    }

    public void setEnabledCriterion(boolean enabled) {
        enabledCheck.setSelected(enabled);
        updateBackground();
    }

    public int getCreationIndex() {
        return creationIndex;
    }

    public void rename(String name) {
        nameField.setText(name);
    }

    public final BigDecimal computeAverageScore(List<ItemsPanel> itemsList) {
        return Calculate.averageScore(this, itemsList);
    }

    public final BigDecimal computeScoreVariance(List<ItemsPanel> itemsList) {
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
        Tasks.updateBackground(this);
    }
}