package export;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import app.Main;
import criteria.CriteriaPanel;

final class CritInfo {

    static final class CritInfoResult {
        public final String name;
        public final boolean enabled;
        public final BigDecimal weight;
        public final CritExtraInfo extra;

        public CritInfoResult(
            String name,
            boolean enabled,
            BigDecimal weight,
            CritExtraInfo extra
        ) {
            this.name = name;
            this.enabled = enabled;
            this.weight = weight;
            this.extra = extra;
        }
    }

    static final class CritExtraInfo {
        public final String name;
        public final BigDecimal weight;
        public final BigDecimal averageScore;
        public final BigDecimal variance;
        public final int creationIndex;

        public CritExtraInfo(
            String name,
            BigDecimal weight,
            BigDecimal averageScore,
            BigDecimal variance,
            int creationIndex
        ) {
            this.name = name;
            this.weight = weight;
            this.averageScore = averageScore;
            this.variance = variance;
            this.creationIndex = creationIndex;
        }
    }

    static List<CritInfoResult> createCritInfo(boolean includeDisabledCriteria) {

        List<CritInfoResult> results = new ArrayList<>();

        for (CriteriaPanel c : Main.criteriaList) {

            // Skip disabled criteria unless user wants them
            if (!includeDisabledCriteria && !c.isEnabledCriterion()) {
                continue;
            }

            // Build extra info block
            CritExtraInfo extra = new CritExtraInfo(
                c.getCriterionName(),
                new BigDecimal(c.getWeight()),
                c.computeAverageScore(Main.itemsList),
                c.computeScoreVariance(Main.itemsList),
                c.creationIndex
            );

            // Build main result object
            results.add(new CritInfoResult(
                c.getCriterionName(),
                c.isEnabledCriterion(),
                new BigDecimal (c.getWeight()),
                extra
            ));
        }

        return results;
    }
}