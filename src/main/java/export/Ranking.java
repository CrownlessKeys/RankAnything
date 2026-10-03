package export;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.Main;
import ranking.RankingPanel;
import ranking.RankingPanel.ItemScore;
import ranking.RankingPanel.RankingMode;

final class Ranking {
    
    static final class RankingResult {
        public final int rank;
        public final String itemName;
        public final BigDecimal score;

        public RankingResult(
            int rank, 
            String itemName,
            BigDecimal score
        ) {
            this.rank = rank;
            this.itemName = itemName;
            this.score = score;
        }
    }

    static List<RankingResult> createRanking(RankingMode mode) {

        RankingPanel panel = Main.rankingPanel;
        List<ItemScore> scores;

        // Compute scores based on the mode passed in
        switch (mode) {
            case WEIGHTED_ENABLED ->
                scores = panel.computeScores();

            case WEIGHTED_ALL_CRITERIA ->
                scores = panel.computeScoresAllCriteria();

            case WEIGHTED_ALL_ITEMS ->
                scores = panel.computeScoresAllItems();

            case WEIGHTED_ALL_CRITERIA_ALL_ITEMS ->
                scores = panel.computeScoresAllCriteriaAllItems();

            case UNWEIGHTED_ENABLED ->
                scores = panel.computeUnweightedScores();

            case UNWEIGHTED_ALL_ITEMS ->
                scores = panel.computeUnweightedScoresAllItems();

            case UNWEIGHTED_ALL_CRITERIA ->
                scores = panel.computeUnweightedScoresAllCriteria();

            case UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS ->
                scores = panel.computeUnweightedScoresAllCriteriaAllItems();

            default -> throw new IllegalStateException("Unknown ranking mode: " + mode);
        }

        // Sort descending by score (same as ranking.Rows.refreshOrder())
        scores.sort(Comparator.comparing((ItemScore s) -> s.score).reversed());

        // Build export-friendly results
        List<RankingResult> results = new ArrayList<>();
        int rank = 1;

        for (ItemScore s : scores) {

            results.add(new RankingResult(
                rank,
                s.item.getItemName(),
                s.score
            ));

            rank++;
        }

        return results;
    }
}