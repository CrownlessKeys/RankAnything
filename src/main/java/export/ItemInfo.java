package export;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import app.Main;
import criteria.CriteriaPanel;
import items.ItemsPanel;
import ranking.RankingPanel;

final class ItemInfo {

    static final class ItemInfoResult {
        public final String name;
        public final boolean enabled;
        public final List<CriterionScore> criterionScores;
        public final ItemExtraInfo extra;

        public ItemInfoResult(
            String name,
            boolean enabled,
            List<CriterionScore> criterionScores,
            ItemExtraInfo extra
        ) {
            this.name = name;
            this.enabled = enabled;
            this.criterionScores = criterionScores;
            this.extra = extra;
        }
    }

    static final class CriterionScore {
        public final String criterionName;
        public final BigDecimal score;
        public final boolean enabled;

        public CriterionScore(String criterionName, BigDecimal score, boolean enabled) {
            this.criterionName = criterionName;
            this.score = score;
            this.enabled = enabled;
        }
    }

    static final class ItemExtraInfo {
        public final String name;
        public final BigDecimal weightedScore;
        public final int rank;
        public final int rankOf;
        public final BigDecimal variance;
        public final int creationIndex;
        public final BigDecimal unweightedAverage;
        public final int unweightedRank;
        public final int rankAllItems;
        public final int unweightedRankAllItems;
        public final BigDecimal weightedAllCriteria;
        public final int rankAllCriteria;
        public final BigDecimal unweightedAllCriteria;
        public final int unweightedRankAllCriteria;
        public final int rankAllCriteriaAllItems;
        public final int unweightedRankAllCriteriaAllItems;

        public ItemExtraInfo(
            String name,
            BigDecimal weightedScore,
            int rank,
            int rankOf,
            BigDecimal variance,
            int creationIndex,
            BigDecimal unweightedAverage,
            int unweightedRank,
            int rankAllItems,
            int unweightedRankAllItems,
            BigDecimal weightedAllCriteria,
            int rankAllCriteria,
            BigDecimal unweightedAllCriteria,
            int unweightedRankAllCriteria,
            int rankAllCriteriaAllItems,
            int unweightedRankAllCriteriaAllItems
        ) {
            this.name = name;
            this.weightedScore = weightedScore;
            this.rank = rank;
            this.rankOf = rankOf;
            this.variance = variance;
            this.creationIndex = creationIndex;
            this.unweightedAverage = unweightedAverage;
            this.unweightedRank = unweightedRank;
            this.rankAllItems = rankAllItems;
            this.unweightedRankAllItems = unweightedRankAllItems;
            this.weightedAllCriteria = weightedAllCriteria;
            this.rankAllCriteria = rankAllCriteria;
            this.unweightedAllCriteria = unweightedAllCriteria;
            this.unweightedRankAllCriteria = unweightedRankAllCriteria;
            this.rankAllCriteriaAllItems = rankAllCriteriaAllItems;
            this.unweightedRankAllCriteriaAllItems = unweightedRankAllCriteriaAllItems;
        }
    }

    static List<ItemInfoResult> createItemInfo(boolean includeDisabledItems) {

        RankingPanel panel = Main.rankingPanel;
        List<ItemInfoResult> results = new ArrayList<>();

        for (ItemsPanel item : Main.itemsList) {

            // Skip disabled items unless user wants them
            if (!includeDisabledItems && !item.isEnabledItem()) {
                continue;
            }

            // Criterion scores
            List<CriterionScore> criterionScores = new ArrayList<>();

            for (CriteriaPanel c : Main.criteriaList) {
                BigDecimal score = new BigDecimal(item.getScoreForCriterion(c));
                criterionScores.add(new CriterionScore(c.getCriterionName(), score, c.isEnabledCriterion()));
            }

            // Extra info section
            int enabledCount = item.isEnabledItem()
                    ? Main.countEnabledItems()
                    : Main.countEnabledItems() + 1;

            ItemExtraInfo extra = new ItemExtraInfo(
                item.getItemName(),
                panel.computeScore(item).score,
                panel.getRankOf(item),
                enabledCount,
                item.computeScoreVariance(),
                item.creationIndex,
                item.getUnweightedAverageScore(),
                panel.getUnweightedRankOf(item),
                panel.getRankAllItems(item),
                panel.getUnweightedRankAllItems(item),
                panel.computeScoreAllCriteria(item).score,
                panel.getRankAllCriteria(item),
                item.getUnweightedAverageScoreAllCriteria(),
                panel.getUnweightedRankAllCriteria(item),
                panel.getRankAllCriteriaAllItems(item),
                panel.getUnweightedRankAllCriteriaAllItems(item)
            );

            // Build final result object
            results.add(new ItemInfoResult(
                item.getItemName(),
                item.isEnabledItem(),
                criterionScores,
                extra
            ));
        }

        return results;
    }
}