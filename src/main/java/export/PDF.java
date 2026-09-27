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
import export.ItemInfo.ItemExtraInfo;
import export.ItemInfo.ItemInfoResult;
import export.Metadata.MetadataResult;
import export.Ranking.RankingResult;

final class PDF {

    static boolean exportPDF(File file, ExportPreferences prefs) {
        Document doc = new Document(PageSize.LETTER, 36, 36, 36, 36);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            PdfWriter.getInstance(doc, fos);
            doc.open();

            // Fonts
            Font titleFont   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font normalFont  = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font preciseFont = FontFactory.getFont(
                FontFactory.HELVETICA, 10, Font.NORMAL, new Color(100, 100, 100)
            );

            // Title page
            Paragraph title = new Paragraph(Main.projectName, titleFont);
            title.setSpacingAfter(20);
            doc.add(title);

            doc.newPage();

            // Ranking
            if (prefs.ranking) {
                doc.add(new Paragraph("Ranking", headerFont));
                doc.add(new Paragraph("\n", normalFont));

                List<RankingResult> rankingList = Ranking.createRanking(Main.rankingPanel.rankingMode); 

                int rank = 1;
                for (RankingResult r : rankingList) {
                    String rounded = round0(r.score);
                    String precise = precise(r.score);

                    Paragraph mainLine = new Paragraph(
                        rank + ". " + r.itemName + " — " + rounded + "%", normalFont
                    );
                    mainLine.setSpacingAfter(2);

                    Paragraph preciseLine = new Paragraph(
                        "(" + precise + "%)", preciseFont
                    );
                    preciseLine.setSpacingAfter(8);

                    doc.add(mainLine);
                    doc.add(preciseLine);

                    rank++;
                }

                doc.newPage();
            }

            // Item Info
            if (prefs.itemInfo) {
                doc.add(new Paragraph("Item Information", headerFont));
                doc.add(new Paragraph("\n", normalFont));

                List<ItemInfoResult> items = ItemInfo.createItemInfo(prefs.includeDisabledItems);

                for (ItemInfoResult i : items) {
                    Paragraph header = new Paragraph("Item: " + i.name, headerFont);
                    header.setSpacingAfter(6);
                    doc.add(header);

                    // Enabled
                    Paragraph enabled = new Paragraph(
                        "Enabled: " + (i.enabled ? "Yes" : "No"), normalFont
                    );
                    enabled.setIndentationLeft(20);
                    enabled.setSpacingAfter(4);
                    doc.add(enabled);

                    // Extra info
                    ItemExtraInfo e = i.extra;

                    Paragraph weighted = new Paragraph(
                        "Total weighted score: " + trim(e.weightedScore) + "%", normalFont
                    );
                    weighted.setIndentationLeft(20);
                    weighted.setSpacingAfter(4);
                    doc.add(weighted);

                    Paragraph rankLine = new Paragraph(
                        "Rank: #" + e.rank + " of " + e.rankOf, normalFont
                    );
                    rankLine.setIndentationLeft(20);
                    rankLine.setSpacingAfter(4);
                    doc.add(rankLine);

                    Paragraph variance = new Paragraph(
                        "Score variance: " + trim(e.variance), normalFont
                    );
                    variance.setIndentationLeft(20);
                    variance.setSpacingAfter(4);
                    doc.add(variance);

                    Paragraph creation = new Paragraph(
                        "Creation index: #" + e.creationIndex, normalFont
                    );
                    creation.setIndentationLeft(20);
                    creation.setSpacingAfter(4);
                    doc.add(creation);

                    Paragraph unweighted = new Paragraph(
                        "Unweighted average score: " + trim(e.unweightedAverage) + "%", normalFont
                    );
                    unweighted.setIndentationLeft(20);
                    unweighted.setSpacingAfter(4);
                    doc.add(unweighted);

                    Paragraph rankAllItems = new Paragraph(
                        "Rank (all items): #" + e.rankAllItems, normalFont
                    );
                    rankAllItems.setIndentationLeft(20);
                    rankAllItems.setSpacingAfter(4);
                    doc.add(rankAllItems);

                    Paragraph unweightedRankAllItems = new Paragraph(
                        "Unweighted rank (all items): #" + e.unweightedRankAllItems, normalFont
                    );
                    unweightedRankAllItems.setIndentationLeft(20);
                    unweightedRankAllItems.setSpacingAfter(4);
                    doc.add(unweightedRankAllItems);

                    Paragraph weightedAllCriteria = new Paragraph(
                        "Total weighted score (all criteria): " +
                        trim(e.weightedAllCriteria) + "%", normalFont
                    );
                    weightedAllCriteria.setIndentationLeft(20);
                    weightedAllCriteria.setSpacingAfter(4);
                    doc.add(weightedAllCriteria);

                    Paragraph rankAllCriteria = new Paragraph(
                        "Rank (all criteria): #" + e.rankAllCriteria, normalFont
                    );
                    rankAllCriteria.setIndentationLeft(20);
                    rankAllCriteria.setSpacingAfter(4);
                    doc.add(rankAllCriteria);

                    Paragraph unweightedAllCriteria = new Paragraph(
                        "Unweighted average score (all criteria): " +
                        trim(e.unweightedAllCriteria) + "%", normalFont
                    );
                    unweightedAllCriteria.setIndentationLeft(20);
                    unweightedAllCriteria.setSpacingAfter(4);
                    doc.add(unweightedAllCriteria);

                    Paragraph unweightedRankAllCriteria = new Paragraph(
                        "Unweighted rank (all criteria): #" + e.unweightedRankAllCriteria,
                        normalFont
                    );
                    unweightedRankAllCriteria.setIndentationLeft(20);
                    unweightedRankAllCriteria.setSpacingAfter(4);
                    doc.add(unweightedRankAllCriteria);

                    Paragraph rankAllCriteriaAllItems = new Paragraph(
                        "Rank (all criteria, all items): #" + e.rankAllCriteriaAllItems,
                        normalFont
                    );
                    rankAllCriteriaAllItems.setIndentationLeft(20);
                    rankAllCriteriaAllItems.setSpacingAfter(4);
                    doc.add(rankAllCriteriaAllItems);

                    Paragraph unweightedRankAllCriteriaAllItems = new Paragraph(
                        "Unweighted rank (all criteria, all items): #" +
                        e.unweightedRankAllCriteriaAllItems,
                        normalFont
                    );
                    unweightedRankAllCriteriaAllItems.setIndentationLeft(20);
                    unweightedRankAllCriteriaAllItems.setSpacingAfter(8);
                    doc.add(unweightedRankAllCriteriaAllItems);

                    doc.add(new Paragraph("\n", normalFont));
                }

                doc.newPage();
            }

            // Criteria Info
            if (prefs.critInfo) {
                doc.add(new Paragraph("Criteria Information", headerFont));
                doc.add(new Paragraph("\n", normalFont));

                List<CritInfoResult> criteria = CritInfo.createCritInfo(prefs.includeDisabledCriteria);

                for (CritInfoResult c : criteria) {
                    Paragraph header = new Paragraph("Criterion: " + c.name, headerFont);
                    header.setSpacingAfter(6);
                    doc.add(header);

                    Paragraph enabled = new Paragraph(
                        "Enabled: " + (c.enabled ? "Yes" : "No"), normalFont
                    );
                    enabled.setIndentationLeft(20);
                    enabled.setSpacingAfter(4);
                    doc.add(enabled);

                    CritExtraInfo e = c.extra;

                    Paragraph weight = new Paragraph(
                        "Weight: " + trim(e.weight) + " / 10", normalFont
                    );
                    weight.setIndentationLeft(20);
                    weight.setSpacingAfter(4);
                    doc.add(weight);

                    Paragraph avg = new Paragraph(
                        "Average score: " + trim(e.averageScore) + "%", normalFont
                    );
                    avg.setIndentationLeft(20);
                    avg.setSpacingAfter(4);
                    doc.add(avg);

                    Paragraph variance = new Paragraph(
                        "Score variance: " + trim(e.variance), normalFont
                    );
                    variance.setIndentationLeft(20);
                    variance.setSpacingAfter(4);
                    doc.add(variance);

                    Paragraph creation = new Paragraph(
                        "Creation index: #" + e.creationIndex, normalFont
                    );
                    creation.setIndentationLeft(20);
                    creation.setSpacingAfter(8);
                    doc.add(creation);

                    doc.add(new Paragraph("\n", normalFont));
                }

                doc.newPage();
            }

            // Metadata
            if (prefs.metadata) {
                MetadataResult m = Metadata.createMetadata();

                doc.add(new Paragraph("Project Metadata", headerFont));
                doc.add(new Paragraph("\n", normalFont));

                addMetaLine(doc, normalFont, "Project Name: " + m.projectName);
                addMetaLine(doc, normalFont, "Project ID: " + m.projectId);
                addMetaLine(doc, normalFont, "File Location: " + m.fileLocation);
                addMetaLine(doc, normalFont, "Original File: " + (m.isOriginal ? "Yes" : "No"));
                addMetaLine(doc, normalFont, "Project Created: " + m.projectCreated);
                addMetaLine(doc, normalFont, "File Created: " + m.fileCreated);
                addMetaLine(doc, normalFont, "Last Modified: " + m.projectModified);
                addMetaLine(doc, normalFont, "Current App Version: " + m.appVersion);
                addMetaLine(doc, normalFont, "Project Saved With Version: " + m.projectAppVersion);
                addMetaLine(doc, normalFont, "Current File Format Version: " + m.fileFormatVersion);
                addMetaLine(doc, normalFont, "Project File Format Version: " + m.projectFileFormatVersion);
                addMetaLine(doc, normalFont, "Criteria Count: " + m.criteriaCount);
                addMetaLine(doc, normalFont, "Criteria Created: " + m.criteriaCreated);
                addMetaLine(doc, normalFont, "Items Count: " + m.itemsCount);
                addMetaLine(doc, normalFont, "Items Created: " + m.itemsCreated);

                doc.newPage();
            }

            // Notes
            if (prefs.notes) {
                doc.add(new Paragraph("Notes", headerFont));
                doc.add(new Paragraph("\n", normalFont));

                Paragraph notes = new Paragraph(Main.projectNotes, normalFont);
                notes.setSpacingAfter(12);
                doc.add(notes);
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
        return bd.stripTrailingZeros().toPlainString();
    }

    private static String round0(BigDecimal bd) {
        if (bd == null) return "";
        return bd.setScale(0, RoundingMode.HALF_UP).toPlainString();
    }

    private static String precise(BigDecimal bd) {
        if (bd == null) return "";
        return bd.setScale(30, RoundingMode.HALF_UP).toPlainString();
    }

    private static void addMetaLine(Document doc, Font font, String text)
        throws DocumentException {
        Paragraph p = new Paragraph(text, font);
        p.setSpacingAfter(4);
        doc.add(p);
    }
}