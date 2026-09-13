package ranking;

import javax.swing.JCheckBox;

import ranking.RankingPanel.RankingMode;

final class Mode {
    static RankingMode computeMode(boolean unweighted, boolean includeDisabledCriteria, boolean includeDisabledItems) {
        if (!unweighted) { // weighted
            if (!includeDisabledCriteria && !includeDisabledItems) return RankingMode.WEIGHTED_ENABLED;
            if ( includeDisabledCriteria && !includeDisabledItems) return RankingMode.WEIGHTED_ALL_CRITERIA;
            if (!includeDisabledCriteria &&  includeDisabledItems) return RankingMode.WEIGHTED_ALL_ITEMS;
            if ( includeDisabledCriteria &&  includeDisabledItems) return RankingMode.WEIGHTED_ALL_CRITERIA_ALL_ITEMS;
        } else { // unweighted
            if (!includeDisabledCriteria && !includeDisabledItems) return RankingMode.UNWEIGHTED_ENABLED;
            if ( includeDisabledCriteria && !includeDisabledItems) return RankingMode.UNWEIGHTED_ALL_CRITERIA;
            if (!includeDisabledCriteria &&  includeDisabledItems) return RankingMode.UNWEIGHTED_ALL_ITEMS;
            if ( includeDisabledCriteria &&  includeDisabledItems) return RankingMode.UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS;
        }
        return RankingMode.WEIGHTED_ENABLED; // fallback
    }

    static void applyModeToToggles(
        RankingMode mode,
        JCheckBox unweightedToggle,
        JCheckBox allCriteriaToggle,
        JCheckBox allItemsToggle
    ) {
        switch (mode) {
            case WEIGHTED_ENABLED -> {
                unweightedToggle.setSelected(false);
                allCriteriaToggle.setSelected(false);
                allItemsToggle.setSelected(false);
            }
            case WEIGHTED_ALL_CRITERIA -> {
                unweightedToggle.setSelected(false);
                allCriteriaToggle.setSelected(true);
                allItemsToggle.setSelected(false);
            }
            case WEIGHTED_ALL_ITEMS -> {
                unweightedToggle.setSelected(false);
                allCriteriaToggle.setSelected(false);
                allItemsToggle.setSelected(true);
            }
            case WEIGHTED_ALL_CRITERIA_ALL_ITEMS -> {
                unweightedToggle.setSelected(false);
                allCriteriaToggle.setSelected(true);
                allItemsToggle.setSelected(true);
            }
            case UNWEIGHTED_ENABLED -> {
                unweightedToggle.setSelected(true);
                allCriteriaToggle.setSelected(false);
                allItemsToggle.setSelected(false);
            }
            case UNWEIGHTED_ALL_CRITERIA -> {
                unweightedToggle.setSelected(true);
                allCriteriaToggle.setSelected(true);
                allItemsToggle.setSelected(false);
            }
            case UNWEIGHTED_ALL_ITEMS -> {
                unweightedToggle.setSelected(true);
                allCriteriaToggle.setSelected(false);
                allItemsToggle.setSelected(true);
            }
            case UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS -> {
                unweightedToggle.setSelected(true);
                allCriteriaToggle.setSelected(true);
                allItemsToggle.setSelected(true);
            }
        }
    }
}
