package items;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.RoundingMode;

import app.Main;

final class ExtraInfo {
    private ExtraInfo() {
    }

    static void initialize(ItemsPanel item) {
        item.extraInfoToggleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggle(item);
            }
        });
    }

    static void toggle(ItemsPanel item) {
        item.extraInfoVisible = !item.extraInfoVisible;
        item.extraInfoPanel.setVisible(item.extraInfoVisible);
        item.extraInfoToggleLabel.setText(item.extraInfoVisible ? "Hide..." : "More...");
        item.revalidate();
        item.repaint();
    }

    static void refresh(ItemsPanel item) {
        int itemCount = item.isEnabledItem() ? Main.countEnabledItems() : Main.countEnabledItems() + 1;

        item.infoName.setText("Name: " + item.getItemName());
        item.infoWeighted.setText("Total Weighted Score: " +
            item.rankingPanel.computeScore(item).score.setScale(5, RoundingMode.HALF_UP)
            + "%"
        );
        item.infoRank.setText("Rank: #" + item.rankingPanel.getRankOf(item)
            + " of " + itemCount
            + (item.isEnabledItem() ? "" : " (if enabled)")
        );
        item.infoVariance.setText("Score Variance: " +
            item.computeScoreVariance().setScale(5, RoundingMode.HALF_UP)
        );
        item.infoCreation.setText("Creation Index: #" + item.creationIndex + " of " + Main.itemsCreationCounter);
        item.infoUnweighted.setText("Unweighted Average Score: " +
            item.getUnweightedAverageScore().setScale(5, RoundingMode.HALF_UP)
            + "%"
        );
        item.infoUnweightedRank.setText(
            "Unweighted Rank: #" + item.rankingPanel.getUnweightedRankOf(item)
            + " of " + itemCount
            + (item.isEnabledItem() ? "" : " (if enabled)")
        );
        item.infoRankAllItems.setText(
            "Rank: #" + item.rankingPanel.getRankAllItems(item) + " of " + Main.itemsList.size()
        );
        item.infoUnweightedRankAllItems.setText(
            "Unweighted Rank: #" + 
            item.rankingPanel.getUnweightedRankAllItems(item)
            + " of " + Main.itemsList.size()
        );
        item.infoWeightedAllCriteria.setText("Total Weighted Score: " +
            item.rankingPanel.computeScoreAllCriteria(item).score.setScale(5, RoundingMode.HALF_UP)
            + "%"
        );
        item.infoRankAllCriteria.setText("Rank: #" + item.rankingPanel.getRankAllCriteria(item)
            + " of " + itemCount
            + (item.isEnabledItem() ? "" : " (if enabled)")
        );
        item.infoUnweightedAllCriteria.setText("Unweighted Average Score: " +
            item.getUnweightedAverageScoreAllCriteria().setScale(5, RoundingMode.HALF_UP)
            + "%"
        );
        item.infoUnweightedRankAllCriteria.setText(
            "Unweighted Rank: #" + item.rankingPanel.getUnweightedRankAllCriteria(item)
            + " of " + itemCount
            + (item.isEnabledItem() ? "" : " (if enabled)")
        );
        item.infoRankAllCriteriaAllItems.setText(
            "Rank: #" + item.rankingPanel.getRankAllCriteriaAllItems(item)
            + " of " + Main.itemsList.size()
        );
        item.infoUnweightedRankAllCriteriaAllItems.setText(
            "Unweighted Rank: #" + 
            item.rankingPanel.getUnweightedRankAllCriteriaAllItems(item)
            + " of " + Main.itemsList.size()
        );

        item.revalidate();
        item.repaint();
    }
}