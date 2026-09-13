package ranking;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import criteria.CriteriaPanel;
import items.ItemsPanel;

final class Calculate {
    private Calculate() {
    }

    static RankingPanel.ItemScore computeScore(RankingPanel panel, ItemsPanel item) {
        BigDecimal totalWeighted = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (CriteriaPanel cp : panel.criteriaList) {
            if (!cp.isEnabledCriterion()) continue;

            double w = cp.getWeight();
            if (w <= 0.0) continue;

            double raw = item.getScoreForCriterion(cp);
            BigDecimal weight = BigDecimal.valueOf(w);
            BigDecimal scoreFraction = BigDecimal.valueOf(raw)
                    .divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);

            totalWeighted = totalWeighted.add(scoreFraction.multiply(weight, MathContext.DECIMAL128));
            totalWeight = totalWeight.add(weight);
        }

        BigDecimal finalScore = totalWeight.compareTo(BigDecimal.ZERO) > 0
                ? totalWeighted.divide(totalWeight, MathContext.DECIMAL128)
                    .multiply(BigDecimal.valueOf(100), MathContext.DECIMAL128)
                : BigDecimal.ZERO;

        return new RankingPanel.ItemScore(item, finalScore);
    }

    static RankingPanel.ItemScore computeScoreAllCriteria(RankingPanel panel, ItemsPanel item) {
        BigDecimal totalWeighted = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (CriteriaPanel cp : panel.criteriaList) {

            // IGNORE cp.isEnabledCriterion()
            double w = cp.getWeight();
            if (w <= 0.0) continue;

            double raw = item.getScoreForCriterion(cp);
            BigDecimal weight = BigDecimal.valueOf(w);
            BigDecimal scoreFraction = BigDecimal.valueOf(raw)
                    .divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);

            totalWeighted = totalWeighted.add(scoreFraction.multiply(weight, MathContext.DECIMAL128));
            totalWeight = totalWeight.add(weight);
        }

        BigDecimal finalScore = totalWeight.compareTo(BigDecimal.ZERO) > 0
                ? totalWeighted.divide(totalWeight, MathContext.DECIMAL128)
                    .multiply(BigDecimal.valueOf(100), MathContext.DECIMAL128)
                : BigDecimal.ZERO;

        return new RankingPanel.ItemScore(item, finalScore);
    }

    static List<RankingPanel.ItemScore> computeScores(RankingPanel panel) {
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel item : panel.itemsList) {
            if (item.isEnabledItem()) {
                scores.add(computeScore(panel, item));
            }
        }
        return scores;
    }

    static List<RankingPanel.ItemScore> computeScoresAllCriteria(RankingPanel panel) {
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel item : panel.itemsList) {
            if (item.isEnabledItem()) {
                scores.add(computeScoreAllCriteria(panel, item));
            }
        }
        return scores;
    }

    static List<RankingPanel.ItemScore> computeScoresAllItems(RankingPanel panel) {
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel item : panel.itemsList) {
            scores.add(computeScore(panel, item)); // normal weighted score
        }
        return scores;
    }

    static List<RankingPanel.ItemScore> computeScoresAllCriteriaAllItems(RankingPanel panel) {
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel item : panel.itemsList) {
            scores.add(computeScoreAllCriteria(panel, item));
        }
        return scores;
    }

    static List<RankingPanel.ItemScore> computeUnweightedScores(RankingPanel panel) {
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel item : panel.itemsList) {
            if (item.isEnabledItem()) {
                scores.add(new RankingPanel.ItemScore(item, item.getUnweightedAverageScore()));
            }
        }
        return scores;
    }

    static List<RankingPanel.ItemScore> computeUnweightedScoresAllItems(RankingPanel panel) {
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel item : panel.itemsList) {
            scores.add(new RankingPanel.ItemScore(item, item.getUnweightedAverageScore()));
        }
        return scores;
    }

    static List<RankingPanel.ItemScore> computeUnweightedScoresAllCriteria(RankingPanel panel) {
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel item : panel.itemsList) {
            if (item.isEnabledItem()) {
                scores.add(new RankingPanel.ItemScore(item, item.getUnweightedAverageScoreAllCriteria()));
            }
        }
        return scores;
    }

    static List<RankingPanel.ItemScore> computeUnweightedScoresAllCriteriaAllItems(RankingPanel panel) {
        List<RankingPanel.ItemScore> scores = new ArrayList<>();
        for (ItemsPanel item : panel.itemsList) {
            scores.add(new RankingPanel.ItemScore(item, item.getUnweightedAverageScoreAllCriteria()));
        }
        return scores;
    }
}