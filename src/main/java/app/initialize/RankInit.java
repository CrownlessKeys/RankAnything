package app.initialize;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import app.Main;
import ranking.RankingPanel;

public final class RankInit {
    public static void build() {
        
        RankingPanel rankingPanel = new RankingPanel(Main.itemsList, Main.criteriaList);
        rankingPanel.initialize();
        rankingPanel.setBorder(BorderFactory.createTitledBorder("Ranking"));
        rankingPanel.setBackground(new Color(195, 185, 210));

        JScrollPane rankingScroll = new JScrollPane(rankingPanel);
        rankingScroll.getVerticalScrollBar().setUnitIncrement(16);
        rankingScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rankingScroll.setBorder(BorderFactory.createEmptyBorder());

        Main.rankingPanel = rankingPanel;
        Main.rankingScroll = rankingScroll;
    }
}