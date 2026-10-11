package ranking;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import items.ItemsPanel;
import ranking.RankingPanel.ItemScore;

final class Rows {
    private Rows() {
    }

    static JPanel createRow() {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JTextArea mainLine = new JTextArea();
        mainLine.setFont(new Font("SansSerif", Font.BOLD, 16));
        mainLine.setLineWrap(true);
        mainLine.setWrapStyleWord(true);
        mainLine.setOpaque(false);
        mainLine.setEditable(false);

        JTextArea preciseLine = new JTextArea();
        preciseLine.setFont(new Font("SansSerif", Font.PLAIN, 12));
        preciseLine.setForeground(new Color(100, 100, 100));
        preciseLine.setLineWrap(true);
        preciseLine.setWrapStyleWord(true);
        preciseLine.setOpaque(false);
        preciseLine.setEditable(false);

        row.add(mainLine);
        row.add(preciseLine);

        return row;
    }

    static void updateRow(JPanel row, RankingPanel.ItemScore s, int rank) {
        JTextArea mainLine = (JTextArea) row.getComponent(0);
        JTextArea preciseLine = (JTextArea) row.getComponent(1);

        BigDecimal rounded = s.score.setScale(0, RoundingMode.HALF_UP);
        BigDecimal precise = s.score.setScale(15, RoundingMode.HALF_UP);

        mainLine.setText(rank + ". " + s.item.getItemName() + " (" + rounded + "%)");
        preciseLine.setText("(" + precise.toPlainString() + "%)");
    }

    static void refreshOrder(RankingPanel panel) {
        List<ItemScore> scores;

        switch (panel.rankingMode) {
            case WEIGHTED_ENABLED ->
                scores = Calculate.computeScores(panel);

            case WEIGHTED_ALL_CRITERIA ->
                scores = Calculate.computeScoresAllCriteria(panel);

            case WEIGHTED_ALL_ITEMS ->
                scores = Calculate.computeScoresAllItems(panel);

            case WEIGHTED_ALL_CRITERIA_ALL_ITEMS ->
                scores = Calculate.computeScoresAllCriteriaAllItems(panel);

            case UNWEIGHTED_ENABLED ->
                scores = Calculate.computeUnweightedScores(panel);

            case UNWEIGHTED_ALL_ITEMS ->
                scores = Calculate.computeUnweightedScoresAllItems(panel);

            case UNWEIGHTED_ALL_CRITERIA ->
                scores = Calculate.computeUnweightedScoresAllCriteria(panel);

            case UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS ->
                scores = Calculate.computeUnweightedScoresAllCriteriaAllItems(panel);

            default -> throw new IllegalStateException("Unknown ranking mode: " + panel.rankingMode);
        }

        scores.sort(Comparator.comparing((RankingPanel.ItemScore s) -> s.score).reversed());

        panel.contentPanel.removeAll();

        int rank = 1;
        for (RankingPanel.ItemScore s : scores) {
            JPanel row = panel.rowMap.get(s.item);
            if (row != null) {
                updateRow(row, s, rank);
                panel.contentPanel.add(row);
            }
            rank++;
        }

        panel.contentPanel.revalidate();
        panel.contentPanel.repaint();
    }

    // Called when an item is added
    static void addItemRow(ItemsPanel item, RankingPanel panel) {
        JPanel row = Rows.createRow();
        panel.rowMap.put(item, row);
        Rows.refreshOrder(panel);
    }

    // Called when an item is deleted
    static void removeItemRow(ItemsPanel item, RankingPanel panel) {
        JPanel row = panel.rowMap.remove(item);
        if (row != null) {
            panel.contentPanel.remove(row);
            Rows.refreshOrder(panel);
        }
    }
}