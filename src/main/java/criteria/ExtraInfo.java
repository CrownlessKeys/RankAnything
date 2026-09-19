package criteria;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.RoundingMode;

import javax.swing.Box;

import app.Main;

final class ExtraInfo {
    private ExtraInfo() {
    }

    static void initialize(CriteriaPanel criterion) {
        criterion.extraInfoPanel.add(Box.createVerticalStrut(10));
        criterion.extraInfoPanel.add(criterion.infoName);
        criterion.extraInfoPanel.add(criterion.infoWeight);
        criterion.extraInfoPanel.add(criterion.infoScore);
        criterion.extraInfoPanel.add(criterion.infoVariance);
        criterion.extraInfoPanel.add(criterion.infoCreation);

        criterion.extraInfoToggleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggle(criterion);
            }
        });
    }

    static void toggle(CriteriaPanel criterion) {
        criterion.extraInfoVisible = !criterion.extraInfoVisible;
        criterion.extraInfoPanel.setVisible(criterion.extraInfoVisible);
        criterion.extraInfoToggleLabel.setText(criterion.extraInfoVisible ? "Hide..." : "More...");
        criterion.revalidate();
        criterion.repaint();
    }

    static void refresh(CriteriaPanel criterion) {
        criterion.infoName.setText("Name: " + criterion.getCriterionName());
        criterion.infoWeight.setText("Weight: " + criterion.getWeight() + " / 10");
        criterion.infoScore.setText(
            "Average score: " +
            criterion.computeAverageScore(Main.itemsList).setScale(5, RoundingMode.HALF_UP)
            + "%"
        );
        criterion.infoVariance.setText(
            "Score variance: " +
            criterion.computeScoreVariance(Main.itemsList).setScale(5, RoundingMode.HALF_UP)
        );
        criterion.infoCreation.setText(
            "Creation index: #" + criterion.creationIndex + " of " + Main.criteriaCreationCounter
        );

        criterion.revalidate();
        criterion.repaint();
    }
}