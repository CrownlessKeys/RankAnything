package ranking;

import javax.swing.JCheckBox;

import ranking.RankingPanel.RankingMode;

final class Mode {
    static RankingMode computeMode(boolean weighted, boolean includeDisabledCriteria, boolean includeDisabledItems) {
        if (weighted) { 
            if (!includeDisabledCriteria && !includeDisabledItems) return RankingMode.WEIGHTED_ENABLED;
            if ( includeDisabledCriteria && !includeDisabledItems) return RankingMode.WEIGHTED_ALL_CRITERIA;
            if (!includeDisabledCriteria &&  includeDisabledItems) return RankingMode.WEIGHTED_ALL_ITEMS;
            if ( includeDisabledCriteria &&  includeDisabledItems) return RankingMode.WEIGHTED_ALL_CRITERIA_ALL_ITEMS;
        } else { 
            if (!includeDisabledCriteria && !includeDisabledItems) return RankingMode.UNWEIGHTED_ENABLED;
            if ( includeDisabledCriteria && !includeDisabledItems) return RankingMode.UNWEIGHTED_ALL_CRITERIA;
            if (!includeDisabledCriteria &&  includeDisabledItems) return RankingMode.UNWEIGHTED_ALL_ITEMS;
            if ( includeDisabledCriteria &&  includeDisabledItems) return RankingMode.UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS;
        }
        return RankingMode.WEIGHTED_ENABLED; // fallback
    }

    static void applyModeToToggles(
        RankingMode mode,
        JCheckBox weightedToggle,
        JCheckBox allCriteriaToggle,
        JCheckBox allItemsToggle
    ) {
        switch (mode) {
            case WEIGHTED_ENABLED -> {
                weightedToggle.setSelected(true);
                allCriteriaToggle.setSelected(false);
                allItemsToggle.setSelected(false);
            }
            case WEIGHTED_ALL_CRITERIA -> {
                weightedToggle.setSelected(true);
                allCriteriaToggle.setSelected(true);
                allItemsToggle.setSelected(false);
            }
            case WEIGHTED_ALL_ITEMS -> {
                weightedToggle.setSelected(true);
                allCriteriaToggle.setSelected(false);
                allItemsToggle.setSelected(true);
            }
            case WEIGHTED_ALL_CRITERIA_ALL_ITEMS -> {
                weightedToggle.setSelected(true);
                allCriteriaToggle.setSelected(true);
                allItemsToggle.setSelected(true);
            }
            case UNWEIGHTED_ENABLED -> {
                weightedToggle.setSelected(false);
                allCriteriaToggle.setSelected(false);
                allItemsToggle.setSelected(false);
            }
            case UNWEIGHTED_ALL_CRITERIA -> {
                weightedToggle.setSelected(false);
                allCriteriaToggle.setSelected(true);
                allItemsToggle.setSelected(false);
            }
            case UNWEIGHTED_ALL_ITEMS -> {
                weightedToggle.setSelected(false);
                allCriteriaToggle.setSelected(false);
                allItemsToggle.setSelected(true);
            }
            case UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS -> {
                weightedToggle.setSelected(false);
                allCriteriaToggle.setSelected(true);
                allItemsToggle.setSelected(true);
            }
        }
    }

    static boolean isWeighted(RankingMode mode) {
        return switch (mode) {
            case WEIGHTED_ENABLED,
                WEIGHTED_ALL_CRITERIA,
                WEIGHTED_ALL_ITEMS,
                WEIGHTED_ALL_CRITERIA_ALL_ITEMS -> true;
            default -> false;
        };
    }

    static boolean isAllCriteria(RankingMode mode) {
        return switch (mode) {
            case WEIGHTED_ALL_CRITERIA,
                WEIGHTED_ALL_CRITERIA_ALL_ITEMS,
                UNWEIGHTED_ALL_CRITERIA,
                UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS -> true;
            default -> false;
        };
    }

    static boolean isAllItems(RankingMode mode) {
        return switch (mode) {
            case WEIGHTED_ALL_ITEMS,
                WEIGHTED_ALL_CRITERIA_ALL_ITEMS,
                UNWEIGHTED_ALL_ITEMS,
                UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS -> true;
            default -> false;
        };
    }
}