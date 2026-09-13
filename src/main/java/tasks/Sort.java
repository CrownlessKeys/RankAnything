package tasks;

import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JPanel;

import app.Main;
import criteria.CriteriaPanel;
import items.ItemsPanel;
import ranking.RankingPanel;

public final class Sort {
    
    private static void reorderCriteriaWrappers(JPanel criteriaPanel) {

        java.util.List<JPanel> wrappers = new java.util.ArrayList<>();

        for (Component c : criteriaPanel.getComponents()) {
            if (c instanceof JPanel wrapper &&
                wrapper.getComponentCount() > 0 &&
                wrapper.getComponent(0) instanceof CriteriaPanel) {
                wrappers.add(wrapper);
            }
        }

        for (JPanel w : wrappers) criteriaPanel.remove(w);

        for (CriteriaPanel cp : Main.criteriaList) {
            for (JPanel w : wrappers) {
                if (w.getComponent(0) == cp) {
                    criteriaPanel.add(w);
                    break;
                }
            }
        }

        criteriaPanel.revalidate();
        criteriaPanel.repaint();

        for (ItemsPanel item : Main.itemsList) {
            item.refreshCriteria();
        }
    }

    private static void reorderItemWrappers(JPanel itemsPanel) {

        java.util.List<JPanel> wrappers = new java.util.ArrayList<>();

        for (Component c : itemsPanel.getComponents()) {
            if (c instanceof JPanel wrapper &&
                wrapper.getComponentCount() > 0 &&
                wrapper.getComponent(0) instanceof ItemsPanel) {
                wrappers.add(wrapper);
            }
        }

        for (JPanel w : wrappers) itemsPanel.remove(w);

        for (ItemsPanel ip : Main.itemsList) {
            for (JPanel w : wrappers) {
                if (w.getComponent(0) == ip) {
                    itemsPanel.add(w);
                    break;
                }
            }
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    // Criteria Sorting
        public static void sortCriteriaAlphabetically(boolean reverse, JPanel criteriaPanel) {

            Main.criteriaList.sort((a, b) -> {
                int cmp = a.getCriterionName().compareToIgnoreCase(b.getCriterionName());
                return reverse ? -cmp : cmp;
            });

            reorderCriteriaWrappers(criteriaPanel);
        }

        public static void sortCriteriaByWeight(boolean reverse, JPanel criteriaPanel) {

            Main.criteriaList.sort((a, b) -> {
                int cmp = Double.compare(a.getWeight(), b.getWeight());
                return reverse ? cmp : -cmp;
            });

            reorderCriteriaWrappers(criteriaPanel);
        }

        public static void sortCriteriaByAverageScore(boolean reverse, JPanel criteriaPanel) {

            Main.criteriaList.sort((a, b) -> {
                BigDecimal avgA = a.computeAverageScore(Main.itemsList);
                BigDecimal avgB = b.computeAverageScore(Main.itemsList);
                int cmp = avgA.compareTo(avgB);
                return reverse ? cmp : -cmp;
            });

            reorderCriteriaWrappers(criteriaPanel);
        }

        public static void sortCriteriaByScoreVariance(boolean reverse, JPanel criteriaPanel) {

            Main.criteriaList.sort((a, b) -> {
                BigDecimal varA = a.computeScoreVariance(Main.itemsList);
                BigDecimal varB = b.computeScoreVariance(Main.itemsList);
                int cmp = varA.compareTo(varB);
                return reverse ? cmp : -cmp;
            });

            reorderCriteriaWrappers(criteriaPanel);
        }

        public static void sortCriteriaByCreationIndex(boolean reverse, JPanel criteriaPanel) {

            Main.criteriaList.sort((a, b) -> {
                int cmp = Integer.compare(a.creationIndex, b.creationIndex);
                return reverse ? -cmp : cmp;
            });

            reorderCriteriaWrappers(criteriaPanel);
        }

        public static void sortCriteriaDisabledLast(boolean reverse, JPanel criteriaPanel) {

            Main.criteriaList.sort((a, b) -> {
                boolean ea = a.isEnabledCriterion();
                boolean eb = b.isEnabledCriterion();

                // If both enabled or both disabled → keep current order
                if (ea == eb) return 0;

                // Normal: enabled first, disabled last
                // Reverse: disabled first, enabled last
                return reverse
                    ? (ea ? 1 : -1)   // reverse: enabled goes last
                    : (ea ? -1 : 1)  // normal: enabled goes first
                ;
            });

            reorderCriteriaWrappers(criteriaPanel);
        }

    // Items Sorting
        public static void sortItemsAlphabetically(boolean reverse, JPanel itemsPanel) {

            Main.itemsList.sort((a, b) -> {
                int cmp = a.getItemName().compareToIgnoreCase(b.getItemName());
                return reverse ? -cmp : cmp;
            });

            reorderItemWrappers(itemsPanel);
        }

        public static void sortItemsByRanking(boolean reverse, JPanel itemsPanel, RankingPanel rankingPanel) {

            Main.itemsList.sort((a, b) -> {
                int cmp = Integer.compare(
                    rankingPanel.getRankForSorting(a),
                    rankingPanel.getRankForSorting(b)
                );
                return reverse ? -cmp : cmp;
            });

            reorderItemWrappers(itemsPanel);
        }

        public static void sortItemsByHighestScore(boolean reverse, JPanel itemsPanel) {

            Main.itemsList.sort((a, b) -> {
                int cmp = Double.compare(a.getHighestCriterionScore(), b.getHighestCriterionScore());
                return reverse ? cmp : -cmp;
            });

            reorderItemWrappers(itemsPanel);
        }

        public static void sortItemsByLowestScore(boolean reverse, JPanel itemsPanel) {

            Main.itemsList.sort((a, b) -> {
                int cmp = Double.compare(a.getLowestCriterionScore(), b.getLowestCriterionScore());
                return reverse ? cmp : -cmp;
            });

            reorderItemWrappers(itemsPanel);
        }

        public static void sortItemsByScoreVariance(boolean reverse, JPanel itemsPanel) {

            Main.itemsList.sort((a, b) -> {
                BigDecimal varA = a.computeScoreVariance();
                BigDecimal varB = b.computeScoreVariance();
                int cmp = varA.compareTo(varB);
                return reverse ? cmp : -cmp;
            });

            reorderItemWrappers(itemsPanel);
        }

        public static void sortItemsByCreationIndex(boolean reverse, JPanel itemsPanel) {

            Main.itemsList.sort((a, b) -> {
                int cmp = Integer.compare(a.creationIndex, b.creationIndex);
                return reverse ? -cmp : cmp;
            });

            reorderItemWrappers(itemsPanel);
        }

        public static void sortItemsDisabledLast(boolean reverse, JPanel itemsPanel) {

            Main.itemsList.sort((a, b) -> {
                boolean ea = a.isEnabledItem();
                boolean eb = b.isEnabledItem();

                // If both enabled or both disabled → keep current order
                if (ea == eb) return 0;

                // Normal: enabled first, disabled last
                // Reverse: disabled first, enabled last
                return reverse
                    ? (ea ? 1 : -1)
                    : (ea ? -1 : 1)
                ;
            });

            reorderItemWrappers(itemsPanel);
        }
    // end
}