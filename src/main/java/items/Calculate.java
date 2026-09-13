package items;

import java.math.BigDecimal;
import java.math.MathContext;

import criteria.CriteriaPanel;

final class Calculate {
    private Calculate() {
    }

    static BigDecimal unweightedAverageScore(ItemsPanel panel) {
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;

        for (CriteriaPanel cp : panel.criteriaList) {
            if (cp.getParent() == null) continue;
            if (!cp.isEnabledCriterion()) continue;

            double criterionScore = panel.getScoreForCriterion(cp);
            sum = sum.add(BigDecimal.valueOf(criterionScore));
            count++;
        }

        if (count == 0) {
            return BigDecimal.ZERO;
        }

        return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
    }

    static BigDecimal unweightedAverageScoreAllCriteria(ItemsPanel panel) {
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;

        for (CriteriaPanel cp : panel.criteriaList) {
            if (cp.getParent() == null) continue;
            // SKIP if (!cp.isEnabledCriterion()) continue;

            double criterionScore = panel.getScoreForCriterion(cp);
            sum = sum.add(BigDecimal.valueOf(criterionScore));
            count++;
        }

        if (count == 0) {
            return BigDecimal.ZERO;
        }

        return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
    }

    static double highestCriterionScore(ItemsPanel panel) {
        double max = Double.NEGATIVE_INFINITY;

        for (CriteriaPanel cp : panel.criteriaList) {
            if (cp.getParent() == null) continue;
            if (!cp.isEnabledCriterion()) continue;

            double score = panel.getScoreForCriterion(cp);
            if (score > max) max = score;
        }

        return max == Double.NEGATIVE_INFINITY ? 0 : max;
    }

    static double lowestCriterionScore(ItemsPanel panel) {
        double min = Double.POSITIVE_INFINITY;

        for (CriteriaPanel cp : panel.criteriaList) {
            if (cp.getParent() == null) continue;
            if (!cp.isEnabledCriterion()) continue;

            double score = panel.getScoreForCriterion(cp);
            if (score < min) min = score;
        }

        return min == Double.POSITIVE_INFINITY ? 0 : min;
    }

    static BigDecimal scoreVariance(ItemsPanel panel) {
        BigDecimal avg = unweightedAverageScore(panel);
        BigDecimal sumSq = BigDecimal.ZERO;
        int count = 0;

        for (CriteriaPanel cp : panel.criteriaList) {
            if (cp.getParent() == null) continue;
            if (!cp.isEnabledCriterion()) continue;

            BigDecimal score = BigDecimal.valueOf(panel.getScoreForCriterion(cp));
            BigDecimal diff = score.subtract(avg);
            BigDecimal diffSq = diff.multiply(diff);
            sumSq = sumSq.add(diffSq);
            count++;
        }

        if (count == 0) {
            return BigDecimal.ZERO;
        }

        return sumSq.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
    }
}
