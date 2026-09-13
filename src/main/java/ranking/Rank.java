package ranking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import items.ItemsPanel;

final class Rank {

    static int getRankOf(RankingPanel panel, ItemsPanel item) {

        // 1. Compute scores for enabled items
        List<RankingPanel.ItemScore> scores = Calculate.computeScores(panel);

        // 2. Compute score for THIS item even if disabled
        RankingPanel.ItemScore thisScore = Calculate.computeScore(panel, item);

        // 3. Add it to the list temporarily
        scores.add(thisScore);

        // 4. Sort normally
        scores.sort(Comparator.comparing((RankingPanel.ItemScore s) -> s.score).reversed());

        // 5. Find its rank
        int rank = 1;
        for (RankingPanel.ItemScore s : scores) {
            if (s.item == item) return rank;
            rank++;
        }

        return rank;
    }

    static int getRankAllItems(RankingPanel panel, ItemsPanel item) {

        // 1. Compute scores for ALL items (ignore enabled/disabled)
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel ip : panel.itemsList) {
            scores.add(Calculate.computeScore(panel, ip));
        }

        // 2. Sort descending
        scores.sort(Comparator.comparing((RankingPanel.ItemScore s) -> s.score).reversed());

        // 3. Find rank
        int rank = 1;
        for (RankingPanel.ItemScore s : scores) {
            if (s.item == item) return rank;
            rank++;
        }

        return rank;
    }

    static int getUnweightedRankOf(RankingPanel panel, ItemsPanel item) {

        // 1. Compute scores for enabled items only
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel ip : panel.itemsList) {
            if (ip.isEnabledItem()) {
                BigDecimal score = ip.getUnweightedAverageScore();
                scores.add(new RankingPanel.ItemScore(ip, score));
            }
        }

        // 2. Compute score for THIS item even if disabled
        BigDecimal thisScore = item.getUnweightedAverageScore();
        scores.add(new RankingPanel.ItemScore(item, thisScore));

        // 3. Sort descending
        scores.sort(Comparator.comparing((RankingPanel.ItemScore s) -> s.score).reversed());

        // 4. Find rank
        int rank = 1;
        for (RankingPanel.ItemScore s : scores) {
            if (s.item == item) return rank;
            rank++;
        }

        return rank;
    }

    static int getUnweightedRankAllItems(RankingPanel panel, ItemsPanel item) {

        // Compute scores for ALL items
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel ip : panel.itemsList) {
            BigDecimal score = ip.getUnweightedAverageScore();
            scores.add(new RankingPanel.ItemScore(ip, score));
        }

        // Sort descending
        scores.sort(Comparator.comparing((RankingPanel.ItemScore s) -> s.score).reversed());

        // Find rank
        int rank = 1;
        for (RankingPanel.ItemScore s : scores) {
            if (s.item == item) return rank;
            rank++;
        }

        return rank;
    }

    static int getRankAllCriteria(RankingPanel panel, ItemsPanel item) {

        // Enabled items only
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel ip : panel.itemsList) {
            if (ip.isEnabledItem()) {
                scores.add(Calculate.computeScoreAllCriteria(panel, ip));
            }
        }

        // Add this item even if disabled
        scores.add(Calculate.computeScoreAllCriteria(panel, item));

        // Sort
        scores.sort(Comparator.comparing((RankingPanel.ItemScore s) -> s.score).reversed());

        // Find rank
        int rank = 1;
        for (RankingPanel.ItemScore s : scores) {
            if (s.item == item) return rank;
            rank++;
        }

        return rank;
    }

    static int getRankAllCriteriaAllItems(RankingPanel panel, ItemsPanel item) {

        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel ip : panel.itemsList) {
            scores.add(Calculate.computeScoreAllCriteria(panel, ip));
        }

        scores.sort(Comparator.comparing((RankingPanel.ItemScore s) -> s.score).reversed());

        int rank = 1;
        for (RankingPanel.ItemScore s : scores) {
            if (s.item == item) return rank;
            rank++;
        }

        return rank;
    }

    static int getUnweightedRankAllCriteria(RankingPanel panel, ItemsPanel item) {

        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel ip : panel.itemsList) {
            if (ip.isEnabledItem()) {
                scores.add(new RankingPanel.ItemScore(ip, ip.getUnweightedAverageScore()));
            }
        }

        scores.add(new RankingPanel.ItemScore(item, item.getUnweightedAverageScoreAllCriteria()));

        scores.sort(Comparator.comparing((RankingPanel.ItemScore s) -> s.score).reversed());

        int rank = 1;
        for (RankingPanel.ItemScore s : scores) {
            if (s.item == item) return rank;
            rank++;
        }

        return rank;
    }

    static int getUnweightedRankAllCriteriaAllItems(RankingPanel panel, ItemsPanel item) {

        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel ip : panel.itemsList) {
            scores.add(new RankingPanel.ItemScore(ip, ip.getUnweightedAverageScoreAllCriteria()));
        }

        scores.sort(Comparator.comparing((RankingPanel.ItemScore s) -> s.score).reversed());

        int rank = 1;
        for (RankingPanel.ItemScore s : scores) {
            if (s.item == item) return rank;
            rank++;
        }

        return rank;
    }

    static int getRankForSorting(RankingPanel panel, ItemsPanel item) {
        return switch (panel.rankingMode) {
            case WEIGHTED_ENABLED ->
                getRankAllItems(panel, item); // always ALL items

            case WEIGHTED_ALL_CRITERIA ->
                getRankAllCriteriaAllItems(panel, item);

            case WEIGHTED_ALL_ITEMS ->
                getRankAllItems(panel, item);

            case WEIGHTED_ALL_CRITERIA_ALL_ITEMS ->
                getRankAllCriteriaAllItems(panel, item);

            case UNWEIGHTED_ENABLED ->
                getUnweightedRankAllItems(panel, item);

            case UNWEIGHTED_ALL_ITEMS ->
                getUnweightedRankAllItems(panel, item);

            case UNWEIGHTED_ALL_CRITERIA ->
                getUnweightedRankAllCriteriaAllItems(panel, item);

            case UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS ->
                getUnweightedRankAllCriteriaAllItems(panel, item);
        };
    }
}