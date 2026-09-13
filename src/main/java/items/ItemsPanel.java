package items;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import criteria.CriteriaPanel;
import ranking.RankingPanel;

@SuppressWarnings("JdkObsolete")
public final class ItemsPanel extends JPanel {

    public int creationIndex;
    public JCheckBox enabledCheck;
    boolean collapsed = false;
    boolean extraInfoVisible = false;
    boolean typingScore = false;

    Color activated = new Color(250, 251, 249);
    Color deactivated = new Color(180, 180, 180);

    JPanel headerRow;
    JPanel collapsible;
    JPanel scoresColumn;
    JPanel extraInfoPanel;
    JLabel extraInfoToggleLabel;

    JTextField nameField;
    Timer debounceTimer;

    List<CriteriaPanel> criteriaList;
    final Map<CriteriaPanel, JSlider> scoreMap = new HashMap<>();
    final Map<CriteriaPanel, JTextField> valueFieldMap = new HashMap<>();

    RankingPanel rankingPanel;

    Point dragStart;
    public JButton collapseButton;

    JLabel infoName;
    JLabel infoWeighted;
    JLabel infoRank;
    JLabel infoVariance;
    JLabel infoCreation;
    JLabel infoUnweighted;
    JLabel infoUnweightedRank;
    JLabel infoRankAllItems;
    JLabel infoUnweightedRankAllItems;
    JLabel infoWeightedAllCriteria;
    JLabel infoRankAllCriteria;
    JLabel infoUnweightedAllCriteria;
    JLabel infoUnweightedRankAllCriteria;
    JLabel infoRankAllCriteriaAllItems;
    JLabel infoUnweightedRankAllCriteriaAllItems;

    @Override
    public Dimension getMaximumSize() {
        Dimension pref = getPreferredSize();
        return new Dimension(Integer.MAX_VALUE, pref.height);
    }

    public ItemsPanel(List<CriteriaPanel> criteriaList) {

        nameField = new JTextField("", 12);

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (onNameChanged != null) onNameChanged.run();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (onNameChanged != null) onNameChanged.run();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                if (onNameChanged != null) onNameChanged.run();
            }
        });

        nameField.addActionListener(e -> {
            if (onNameChanged != null) onNameChanged.run();
        });

        this.criteriaList = criteriaList;
    }

    public void initialize() {
        Layout.build(this);
    }

    public void refreshCriteria() {
        Scores.refreshCriteria(this);
    }

    public double getScoreForCriterion(CriteriaPanel cp) {
        return Update.getScoreForCriterion(cp, this);
    }

    public String getItemName() {
        return nameField.getText();
    }

    public boolean isEnabledItem() {
        return enabledCheck.isSelected();
    }

    Runnable onScoresChanged;

    public void setOnScoresChanged(Runnable r) {
        this.onScoresChanged = r;
    }

    Runnable onDelete;

    public void setOnDelete(Runnable r) {
        this.onDelete = r;
    }    

    private Runnable onNameChanged;

    public void setOnNameChanged(Runnable r) {
        this.onNameChanged = r;
    }

    public void updatePrecision(boolean precise) {
        Update.updatePrecision(precise, this);
    }

    public void rename(String name) {
        nameField.setText(name);
    }

    public BigDecimal getUnweightedAverageScore() {
        return Calculate.unweightedAverageScore(this);
    }

    public BigDecimal getUnweightedAverageScoreAllCriteria() {
        return Calculate.unweightedAverageScoreAllCriteria(this);
    }

    public double getHighestCriterionScore() {
        return Calculate.highestCriterionScore(this);
    }

    public double getLowestCriterionScore() {
        return Calculate.lowestCriterionScore(this);
    }

    public BigDecimal computeScoreVariance() {
        return Calculate.scoreVariance(this);
    }

    public Runnable onEnabledChanged;

    public void setOnEnabledChanged(Runnable r) {
        this.onEnabledChanged = r;
    }

    public void refreshExtraInfo() {
        ExtraInfo.refresh(this);
    }

    public void setRankingPanel(RankingPanel rp) {
        this.rankingPanel = rp;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean c) {
        collapsed = c;
        if (collapsible != null) collapsible.setVisible(!collapsed);
        if (collapseButton != null) collapseButton.setText(collapsed ? "▲" : "▼");
    }

    public void setScoreForCriterion(CriteriaPanel cp, double score) {
        Update.setScoreForCriterion(cp, score, this);
    }

    public void updateBackground() {
        Update.updateBackground(this);
    }
}