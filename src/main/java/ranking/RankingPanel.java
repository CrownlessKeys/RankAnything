package ranking;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JViewport;

import app.Settings;
import criteria.CriteriaPanel;
import items.ItemsPanel;

public final class RankingPanel extends JPanel {

    final List<ItemsPanel> itemsList;
    final List<CriteriaPanel> criteriaList;

    final JPanel contentPanel;
    final Color bg = new Color(195, 185, 210);

    final Map<ItemsPanel, JPanel> rowMap = new HashMap<>(); // Persistent rows for each item

    public enum RankingMode {
        WEIGHTED_ENABLED,
        WEIGHTED_ALL_CRITERIA,
        WEIGHTED_ALL_ITEMS,
        WEIGHTED_ALL_CRITERIA_ALL_ITEMS,
        UNWEIGHTED_ENABLED,
        UNWEIGHTED_ALL_ITEMS,
        UNWEIGHTED_ALL_CRITERIA,
        UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS
    }

    public RankingMode rankingMode = Settings.rankingMode; // default from settings
    public JCheckBox weightedToggle;
    public JCheckBox allCriteriaToggle;
    public JCheckBox allItemsToggle;

    public RankingPanel(List<ItemsPanel> itemsList, List<CriteriaPanel> criteriaList) {
        this.itemsList = itemsList;
        this.criteriaList = criteriaList;

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(bg);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void initialize() {
        Layout.build(this);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();

        Container parent = getParent();
        if (parent instanceof JViewport viewport) {
            int maxWidth = viewport.getWidth();
            if (maxWidth > 0 && d.width > maxWidth) {
                d.width = maxWidth;
            }
        }

        return d;
    }

    // Called when an item is added
    public void addItemRow(ItemsPanel item) {
        Rows.addItemRow(item, this);
    }

    // Called when an item is deleted
    public void removeItemRow(ItemsPanel item) {
        Rows.removeItemRow(item, this);
    }

    // Called when an item name changes
    public void updateItemName(ItemsPanel item) {
        ItemScore score = Calculate.computeScore(this, item);
        JPanel row = rowMap.get(item);
        if (row != null) {
            Rows.updateRow(row, score, Rank.getRankOf(this, item));
        }
    }

    // Called when an item score changes
    public void updateItemScore(ItemsPanel item) {
        ItemScore score = Calculate.computeScore(this, item);
        JPanel row = rowMap.get(item);
        if (row != null) {
            Rows.updateRow(row, score, Rank.getRankOf(this, item));
        }
    }

    // Called when an item might need to move
    public void updateItemRank(ItemsPanel item) {
        Rows.refreshOrder(this);
    }

    // Called when criteria change (weight, enabled, added, removed)
    public void updateAllScores() {
        Update.updateAllScores(this);
    }

    // Public scoring methods (delegates to Calculate)
    public ItemScore computeScore(ItemsPanel item) {
        return Calculate.computeScore(this, item);
    }

    public ItemScore computeScoreAllCriteria(ItemsPanel item) {
        return Calculate.computeScoreAllCriteria(this, item);
    }

    public int getRankOf(ItemsPanel item) {
        return Rank.getRankOf(this, item);
    }

    public int getRankAllItems(ItemsPanel item) {
        return Rank.getRankAllItems(this, item);
    }

    public int getUnweightedRankOf(ItemsPanel item) {
        return Rank.getUnweightedRankOf(this, item);
    }

    public int getUnweightedRankAllItems(ItemsPanel item) {
        return Rank.getUnweightedRankAllItems(this, item);
    }

    public int getRankAllCriteria(ItemsPanel item) {
        return Rank.getRankAllCriteria(this, item);
    }

    public int getRankAllCriteriaAllItems(ItemsPanel item) {
        return Rank.getRankAllCriteriaAllItems(this, item);
    }

    public int getUnweightedRankAllCriteria(ItemsPanel item) {
        return Rank.getUnweightedRankAllCriteria(this, item);
    }

    public int getUnweightedRankAllCriteriaAllItems(ItemsPanel item) {
        return Rank.getUnweightedRankAllCriteriaAllItems(this, item);
    }

    public int getRankForSorting(ItemsPanel item) {
        return Rank.getRankForSorting(this, item);
    }

    // Full rebuild only used when absolutely necessary
    public void refreshRanking() {
        Update.refreshRanking(this);
    }

    // Internal score class
    public static final class ItemScore {
        public final ItemsPanel item;
        public final BigDecimal score;

        ItemScore(ItemsPanel item, BigDecimal score) {
            this.item = item;
            this.score = score;
        }
    }

    public void clearAll() {
        rowMap.clear();
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void applyModeToToggles(RankingMode mode) {
        Mode.applyModeToToggles(
            mode, 
            weightedToggle, 
            allCriteriaToggle,
            allItemsToggle
        );
    }

    public RankingMode computeMode(boolean weighted, boolean includeDisabledCriteria, boolean includeDisabledItems) {
        return Mode.computeMode(weighted, includeDisabledCriteria, includeDisabledItems);
    }

    public boolean isWeighted(RankingMode mode) {
        return Mode.isWeighted(mode);
    }

    public boolean isAllCriteria(RankingMode mode) {
        return Mode.isAllCriteria(mode);
    }

    public boolean isAllItems(RankingMode mode) {
        return Mode.isAllItems(mode);
    }

    // "compute scores" methods for exports
        public List<ItemScore> computeScores() {
            return Calculate.computeScores(this);
        }
        public List<ItemScore> computeScoresAllCriteria() {
            return Calculate.computeScoresAllCriteria(this);
        }
        public List<ItemScore> computeScoresAllItems() {
            return Calculate.computeScoresAllItems(this);
        }
        public List<ItemScore> computeScoresAllCriteriaAllItems() {
            return Calculate.computeScoresAllCriteriaAllItems(this);
        }
        public List<ItemScore> computeUnweightedScores() {
            return Calculate.computeUnweightedScores(this);
        }
        public List<ItemScore> computeUnweightedScoresAllItems() {
            return Calculate.computeUnweightedScoresAllItems(this);
        }
        public List<ItemScore> computeUnweightedScoresAllCriteria() {
            return Calculate.computeUnweightedScoresAllCriteria(this);
        }
        public List<ItemScore> computeUnweightedScoresAllCriteriaAllItems() {
            return Calculate.computeUnweightedScoresAllCriteriaAllItems(this);
        }
    // end
}