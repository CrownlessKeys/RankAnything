package criteria;

import java.math.BigDecimal;
import java.math.MathContext;

import items.ItemsPanel;

final class Calculate {
    private Calculate() {
    }

    static BigDecimal averageScore(CriteriaPanel panel, java.util.List<ItemsPanel> itemsList) {
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;

        for (ItemsPanel item : itemsList) {
            if (!item.isEnabledItem()) {
                continue;
            }

            BigDecimal itemScore = BigDecimal.valueOf(item.getScoreForCriterion(panel));
            sum = sum.add(itemScore);
            count++;
        }

        if (count == 0) {
            return BigDecimal.ZERO;
        }

        return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
    }

    static BigDecimal scoreVariance(CriteriaPanel panel, java.util.List<ItemsPanel> itemsList) {
        BigDecimal avg = averageScore(panel, itemsList);
        BigDecimal sumSq = BigDecimal.ZERO;
        int count = 0;

        for (ItemsPanel item : itemsList) {
            if (!item.isEnabledItem()) {
                continue;
            }

            BigDecimal score = BigDecimal.valueOf(item.getScoreForCriterion(panel));
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