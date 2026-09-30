package export;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import app.Main;
import export.CritInfo.CritExtraInfo;
import export.CritInfo.CritInfoResult;
import export.ItemInfo.CriterionScore;
import export.ItemInfo.ItemExtraInfo;
import export.ItemInfo.ItemInfoResult;
import export.Metadata.MetadataResult;
import export.Ranking.RankingResult;
import ranking.RankingPanel;
import ranking.RankingPanel.RankingMode;

final class PDF {
    private static final int SMALL_GAP = 2;
    private static final int DEFAULT_GAP = 4;
    private static final int HEADER_GAP = 6;
    private static final int LARGE_GAP = 8;

    static boolean exportPDF(File file, ExportPreferences prefs) {
        Document doc = new Document(PageSize.LETTER, 36, 36, 36, 36);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            PdfWriter.getInstance(doc, fos);
            doc.open();

            // Fonts
            Font titleFont   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            Font headerFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font normalFont  = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font boldFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font grayFont = FontFactory.getFont(
                FontFactory.HELVETICA, 12, Font.ITALIC, new Color(100, 100, 100)
            );
            Font preciseFont = FontFactory.getFont(
                FontFactory.HELVETICA, 10, Font.NORMAL, new Color(100, 100, 100)
            );

            // Title
            Paragraph title = new Paragraph(Main.projectName, titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(20);
            doc.add(title);

            // Ranking
            if (prefs.ranking) {
                RankingMode mode = Main.rankingPanel.rankingMode;
                createRankingPage(doc, mode, headerFont, normalFont, preciseFont, grayFont);
            }

            // Item Info
            if (prefs.itemInfo) {
                Paragraph itemHeader = new Paragraph("Items", headerFont);
                itemHeader.setAlignment(Paragraph.ALIGN_CENTER);
                doc.add(itemHeader);
                doc.add(new Paragraph("\n", normalFont));

                List<ItemInfoResult> items = ItemInfo.createItemInfo(prefs.includeDisabledItems);

                for (ItemInfoResult i : items) {
                    Paragraph header = new Paragraph(i.name, headerFont);
                    header.setSpacingAfter(HEADER_GAP);
                    doc.add(header);

                    // Enabled
                    if (!i.enabled) {
                        Paragraph enabled = new Paragraph(
                            "Disabled", grayFont
                        );
                        enabled.setIndentationLeft(20);
                        enabled.setSpacingAfter(DEFAULT_GAP);
                        doc.add(enabled);
                    }

                    // scores
                    Paragraph scoresHeader = new Paragraph("Scores:", boldFont);
                    scoresHeader.setIndentationLeft(10);
                    scoresHeader.setSpacingAfter(HEADER_GAP);
                    doc.add(scoresHeader);

                    for (CriterionScore s : i.criterionScores) {
                        Paragraph critLine = new Paragraph(
                            s.criterionName + ": " + trim(s.score) + "%"
                            + (s.enabled ? "" : " (disabled)"), 
                            s.enabled ? normalFont : grayFont
                        );
                        critLine.setIndentationLeft(20);
                        critLine.setSpacingAfter(SMALL_GAP);
                        doc.add(critLine);
                    }

                    // Extra info
                    if (prefs.includeExtraInfoItems) {

                        Paragraph itemExtraInfoHeader = new Paragraph("Info:", boldFont);
                        itemExtraInfoHeader.setIndentationLeft(10);
                        itemExtraInfoHeader.setSpacingAfter(HEADER_GAP);
                        doc.add(itemExtraInfoHeader);

                        ItemExtraInfo e = i.extra;

                        Paragraph weighted = new Paragraph(
                            "Total weighted score: " + trim(e.weightedScore) + "%", normalFont
                        );
                        weighted.setIndentationLeft(20);
                        weighted.setSpacingAfter(0);
                        doc.add(weighted);

                        Paragraph rankLine = new Paragraph(
                            "Rank: #" + e.rank + " of " + e.rankOf
                            + (i.enabled ? "" : " (if enabled)"), 
                            normalFont
                        );
                        rankLine.setIndentationLeft(20);
                        rankLine.setSpacingAfter(0);
                        doc.add(rankLine);

                        Paragraph variance = new Paragraph(
                            "Score variance: " + trim(e.variance), normalFont
                        );
                        variance.setIndentationLeft(20);
                        variance.setSpacingAfter(0);
                        doc.add(variance);

                        Paragraph creation = new Paragraph(
                            "Creation index: #" + e.creationIndex + " of " + Main.itemsCreationCounter, 
                            normalFont
                        );
                        creation.setIndentationLeft(20);
                        creation.setSpacingAfter(LARGE_GAP);
                        doc.add(creation);

                        Paragraph unweighted = new Paragraph(
                            "Unweighted average score: " + trim(e.unweightedAverage) + "%", normalFont
                        );
                        unweighted.setIndentationLeft(20);
                        unweighted.setSpacingAfter(0);
                        doc.add(unweighted);

                        Paragraph UnweightedRankLine = new Paragraph(
                            "Unweighted rank: #" + e.unweightedRank + " of " + e.rankOf
                            + (i.enabled ? "" : " (if enabled)"), 
                            normalFont
                        );
                        UnweightedRankLine.setIndentationLeft(20);
                        UnweightedRankLine.setSpacingAfter(LARGE_GAP);
                        doc.add(UnweightedRankLine);

                        Paragraph allCritHeader = new Paragraph(
                            "If all criteria were enabled:", grayFont
                        );
                        allCritHeader.setIndentationLeft(20);
                        allCritHeader.setSpacingAfter(0);
                        doc.add(allCritHeader);

                        Paragraph weightedAllCriteria = new Paragraph(
                            "Total weighted score: " +
                            trim(e.weightedAllCriteria) + "%", normalFont
                        );
                        weightedAllCriteria.setIndentationLeft(20);
                        weightedAllCriteria.setSpacingAfter(0);
                        doc.add(weightedAllCriteria);

                        Paragraph rankAllCriteria = new Paragraph(
                            "Rank: #" + e.rankAllCriteria + " of " + e.rankOf
                            + (i.enabled ? "" : " (if enabled)"),
                            normalFont
                        );
                        rankAllCriteria.setIndentationLeft(20);
                        rankAllCriteria.setSpacingAfter(0);
                        doc.add(rankAllCriteria);

                        Paragraph unweightedAllCriteria = new Paragraph(
                            "Unweighted average score: " +
                            trim(e.unweightedAllCriteria) + "%", normalFont
                        );
                        unweightedAllCriteria.setIndentationLeft(20);
                        unweightedAllCriteria.setSpacingAfter(0);
                        doc.add(unweightedAllCriteria);

                        Paragraph unweightedRankAllCriteria = new Paragraph(
                            "Unweighted rank: #" + e.unweightedRankAllCriteria + " of " + e.rankOf
                            + (i.enabled ? "" : " (if enabled)"),
                            normalFont
                        );
                        unweightedRankAllCriteria.setIndentationLeft(20);
                        unweightedRankAllCriteria.setSpacingAfter(LARGE_GAP);
                        doc.add(unweightedRankAllCriteria);

                        Paragraph allItemHeader = new Paragraph(
                            "If all items were enabled:", grayFont
                        );
                        allItemHeader.setIndentationLeft(20);
                        allItemHeader.setSpacingAfter(0);
                        doc.add(allItemHeader);

                        Paragraph rankAllItems = new Paragraph(
                            "Rank: #" + e.rankAllItems + " of " + Main.itemsList.size(), 
                            normalFont
                        );
                        rankAllItems.setIndentationLeft(20);
                        rankAllItems.setSpacingAfter(0);
                        doc.add(rankAllItems);

                        Paragraph unweightedRankAllItems = new Paragraph(
                            "Unweighted rank: #" + e.unweightedRankAllItems 
                            + " of " + Main.itemsList.size(), 
                            normalFont
                        );
                        unweightedRankAllItems.setIndentationLeft(20);
                        unweightedRankAllItems.setSpacingAfter(LARGE_GAP);
                        doc.add(unweightedRankAllItems);

                        Paragraph allItemAllCritHeader = new Paragraph(
                            "If all items AND criteria were enabled:", grayFont
                        );
                        allItemAllCritHeader.setIndentationLeft(20);
                        allItemAllCritHeader.setSpacingAfter(0);
                        doc.add(allItemAllCritHeader);

                        Paragraph rankAllCriteriaAllItems = new Paragraph(
                            "Rank: #" + e.rankAllCriteriaAllItems 
                            + " of " + Main.itemsList.size(),
                            normalFont
                        );
                        rankAllCriteriaAllItems.setIndentationLeft(20);
                        rankAllCriteriaAllItems.setSpacingAfter(0);
                        doc.add(rankAllCriteriaAllItems);

                        Paragraph unweightedRankAllCriteriaAllItems = new Paragraph(
                            "Unweighted rank: #" + e.unweightedRankAllCriteriaAllItems 
                            + " of " + Main.itemsList.size(),
                            normalFont
                        );
                        unweightedRankAllCriteriaAllItems.setIndentationLeft(20);
                        unweightedRankAllCriteriaAllItems.setSpacingAfter(0);
                        doc.add(unweightedRankAllCriteriaAllItems);
                    }

                    doc.add(new Paragraph("\n", normalFont));
                }

                doc.newPage();
            }

            // Criteria Info
            if (prefs.critInfo) {
                Paragraph critHeader = new Paragraph("Criteria", headerFont);
                critHeader.setAlignment(Paragraph.ALIGN_CENTER);
                doc.add(critHeader);
                doc.add(new Paragraph("\n", normalFont));

                List<CritInfoResult> criteria = CritInfo.createCritInfo(prefs.includeDisabledCriteria);

                for (CritInfoResult c : criteria) {
                    Paragraph header = new Paragraph(c.name, headerFont);
                    header.setSpacingAfter(HEADER_GAP);
                    doc.add(header);

                    if (!c.enabled) {
                        Paragraph enabled = new Paragraph(
                            "Disabled", grayFont
                        );
                        enabled.setIndentationLeft(20);
                        enabled.setSpacingAfter(SMALL_GAP);
                        doc.add(enabled);
                    }

                    Paragraph weight = new Paragraph(
                        "Weight: " + trim(c.weight) + " / 10", normalFont
                    );
                    weight.setIndentationLeft(20);
                    weight.setSpacingAfter(SMALL_GAP);
                    doc.add(weight);

                    // extra info
                    if (prefs.includeExtraInfoCriteria) {

                        CritExtraInfo e = c.extra;

                        Paragraph avg = new Paragraph(
                            "Average score: " + trim(e.averageScore) + "%", normalFont
                        );
                        avg.setIndentationLeft(20);
                        avg.setSpacingAfter(SMALL_GAP);
                        doc.add(avg);

                        Paragraph variance = new Paragraph(
                            "Score variance: " + trim(e.variance), normalFont
                        );
                        variance.setIndentationLeft(20);
                        variance.setSpacingAfter(SMALL_GAP);
                        doc.add(variance);

                        Paragraph creation = new Paragraph(
                            "Creation index: #" + e.creationIndex 
                            + " of " + Main.criteriaCreationCounter, 
                            normalFont
                        );
                        creation.setIndentationLeft(20);
                        creation.setSpacingAfter(LARGE_GAP);
                        doc.add(creation);
                    }

                    doc.add(new Paragraph("\n", normalFont));
                }

                doc.newPage();
            }

            // Metadata
            if (prefs.metadata) {
                MetadataResult m = Metadata.createMetadata();

                Paragraph metaHeader = new Paragraph("Project Information", headerFont);
                metaHeader.setAlignment(Paragraph.ALIGN_CENTER);
                doc.add(metaHeader);
                doc.add(new Paragraph("\n", normalFont));

                addMetaLine(doc, normalFont, "Project name: " + m.projectName, 0);
                addMetaLine(doc, grayFont, "Project ID: " + m.projectId, LARGE_GAP);

                addMetaLine(doc, normalFont, "File location:", 0);
                addMetaLine(doc, normalFont, "    " + m.fileLocation, 0);
                if (m.isOriginal) addMetaLine(doc, grayFont, "This file is the original copy of the project.", LARGE_GAP);

                addMetaLine(doc, normalFont, 
                    "Project created: " + metadata.Metadata.formatTimestamp(m.projectCreated), 
                0);
                addMetaLine(doc, normalFont, 
                    "File created: " + metadata.Metadata.formatTimestamp(m.fileCreated), 
                0);
                addMetaLine(doc, normalFont, 
                    "Last modified: " + metadata.Metadata.formatTimestamp(m.projectModified), 
                0);
                addMetaLine(doc, normalFont, 
                    "Project exported: " + metadata.Metadata.formatTimestamp(metadata.Metadata.now()), 
                LARGE_GAP);

                addMetaLine(doc, normalFont, "Project exported using app version: " + m.appVersion, 0);
                addMetaLine(doc, normalFont, "Project last saved using app version: " + m.projectAppVersion, 0);
                addMetaLine(doc, normalFont, "File format version used by this project: " + m.projectFileFormatVersion, LARGE_GAP);

                addMetaLine(doc, normalFont, "Criteria count: " + m.criteriaCount, 0);
                addMetaLine(doc, normalFont, "Criteria created: " + m.criteriaCreated, 0);
                addMetaLine(doc, normalFont, "Items count: " + m.itemsCount, 0);
                addMetaLine(doc, normalFont, "Items created: " + m.itemsCreated, 0);

                doc.newPage();
            }

            // Notes
            if (prefs.notes) {
                Paragraph notesHeader = new Paragraph("Notes", headerFont);
                notesHeader.setAlignment(Paragraph.ALIGN_CENTER);
                doc.add(notesHeader);
                doc.add(new Paragraph("\n", normalFont));

                Paragraph notes = new Paragraph(Main.projectNotes, normalFont);
                notes.setSpacingAfter(12);
                doc.add(notes);

                doc.newPage(); // doesn't actually create a new page if nothing below is selected apparently
            }

            // alternative rankings
            if (prefs.includeWeightedEnabled) {
                createRankingPage(doc, RankingMode.WEIGHTED_ENABLED, headerFont, normalFont, preciseFont, grayFont);
            }
            if (prefs.includeWeightedAllCriteria) {
                createRankingPage(doc, RankingMode.WEIGHTED_ALL_CRITERIA, headerFont, normalFont, preciseFont, grayFont);
            }
            if (prefs.includeWeightedAllItems) {
                createRankingPage(doc, RankingMode.WEIGHTED_ALL_ITEMS, headerFont, normalFont, preciseFont, grayFont);
            }
            if (prefs.includeWeightedAllCriteriaAllItems) {
                createRankingPage(doc, RankingMode.WEIGHTED_ALL_CRITERIA_ALL_ITEMS, headerFont, normalFont, preciseFont, grayFont);
            }
            if (prefs.includeUnweightedEnabled) {
                createRankingPage(doc, RankingMode.UNWEIGHTED_ENABLED, headerFont, normalFont, preciseFont, grayFont);
            }
            if (prefs.includeUnweightedAllCriteria) {
                createRankingPage(doc, RankingMode.UNWEIGHTED_ALL_CRITERIA, headerFont, normalFont, preciseFont, grayFont);
            }
            if (prefs.includeUnweightedAllItems) {
                createRankingPage(doc, RankingMode.UNWEIGHTED_ALL_ITEMS, headerFont, normalFont, preciseFont, grayFont);
            }
            if (prefs.includeUnweightedAllCriteriaAllItems) {
                createRankingPage(doc, RankingMode.UNWEIGHTED_ALL_CRITERIA_ALL_ITEMS, headerFont, normalFont, preciseFont, grayFont);
            }

            doc.close();
            return true;

        } catch (IOException | DocumentException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static String trim(BigDecimal bd) {
        if (bd == null) return "";
        return bd.setScale(15, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    private static String round0(BigDecimal bd) {
        if (bd == null) return "";
        return bd.setScale(0, RoundingMode.HALF_UP).toPlainString();
    }

    private static String precise(BigDecimal bd) {
        if (bd == null) return "";
        return bd.setScale(30, RoundingMode.HALF_UP).toPlainString();
    }

    private static void addMetaLine(Document doc, Font font, String text, int gap)
        throws DocumentException {
        Paragraph p = new Paragraph(text, font);
        p.setSpacingAfter(gap);
        doc.add(p);
    }

    private static void createRankingPage(
        Document doc, 
        RankingMode mode, 
        Font headerFont, 
        Font normalFont, 
        Font preciseFont, 
        Font grayFont
    ) {
        RankingPanel panel = Main.rankingPanel;

        Paragraph rankingHeader = new Paragraph("Ranking", headerFont);
        rankingHeader.setAlignment(Paragraph.ALIGN_CENTER);
        doc.add(rankingHeader);

        // mode
        Paragraph rankingModeLine = new Paragraph(
            (panel.isWeighted(mode) ? "Weighted" : "Unweighted")
            + (panel.isAllCriteria(mode) ? ", considering disabled criteria" : "")
            + (panel.isAllItems(mode) ? ", considering disabled items" : ""),
            grayFont
        );
        rankingModeLine.setAlignment(Paragraph.ALIGN_CENTER);
        doc.add(rankingModeLine);

        doc.add(new Paragraph("\n", normalFont));

        List<RankingResult> rankingList = Ranking.createRanking(mode); 

        int rank = 1;
        for (RankingResult r : rankingList) {
            String rounded = round0(r.score);
            String precise = precise(r.score);

            Paragraph mainLine = new Paragraph(
                rank + ". " + r.itemName + " — " + rounded + "%", headerFont
            );
            mainLine.setSpacingAfter(SMALL_GAP);

            Paragraph preciseLine = new Paragraph(
                "(" + precise + "%)", preciseFont
            );
            preciseLine.setSpacingAfter(LARGE_GAP);

            doc.add(mainLine);
            doc.add(preciseLine);

            rank++;
        }

        doc.newPage();
    }
}