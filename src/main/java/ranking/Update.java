package ranking;

import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

import ranking.RankingPanel.ItemScore;

final class Update {

    // Full rebuild only used when absolutely necessary
    static void refreshRanking(RankingPanel panel) {
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

        scores.sort(Comparator.comparing((ItemScore s) -> s.score).reversed());

        // Create missing rows
        for (ItemScore s : scores) {
            if (!panel.rowMap.containsKey(s.item)) {
                JPanel row = Rows.createRow();
                panel.rowMap.put(s.item, row);
            }
        }

        // Remove rows for deleted items
        panel.rowMap.keySet().removeIf(item -> !panel.itemsList.contains(item));

        // Update text inside each row
        int rank = 1;
        for (ItemScore s : scores) {
            Rows.updateRow(panel.rowMap.get(s.item), s, rank);
            rank++;
        }

        Rows.refreshOrder(panel);
    }

    // Called when criteria change (weight, enabled, added, removed)
    static void updateAllScores(RankingPanel panel) {
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

        scores.sort(Comparator.comparing((ItemScore s) -> s.score).reversed());

        int rank = 1;
        for (ItemScore s : scores) {
            JPanel row = panel.rowMap.get(s.item);
            if (row != null) {
                Rows.updateRow(row, s, rank);
            }
            rank++;
        }

        Rows.refreshOrder(panel);
    }
}