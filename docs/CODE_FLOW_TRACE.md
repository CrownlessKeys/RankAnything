# Code Flow Trace - How the Refactored Code Works

## Application Startup Flow

```
main()
  ├─ Create Main frame
  ├─ Create CriteriaPanel (constructor: minimal)
  │   └─ (initialize not yet called)
  ├─ Create ItemsPanel (constructor: minimal)
  │   └─ (initialize not yet called)
  ├─ Create RankingPanel (constructor: minimal)
  │   └─ (initialize not yet called)
  │
  ├─ Add panels to frame
  │
  └─ Call rankingPanel.initialize()
      └─ RankingLayout.build(rankingPanel)
          ├─ Create Save/Load buttons
          └─ Add to BorderLayout.PAGE_START
```

**Critical**: `initialize()` must be called AFTER construction to ensure object is fully initialized before helpers access it.

---

## User Creates Criterion Flow

```
User clicks "Add Criterion"
  ├─ Create.createCriterion()
  │   ├─ CriteriaPanel cp = new CriteriaPanel(...)  // constructor minimal
  │   ├─ cp.initialize()                             // NOW UI is built
  │   │   └─ CriteriaPanelLayout.build(cp)
  │   │       ├─ Create JSlider, JCheckBox, JTextField
  │   │       ├─ Install drag listener (Drag.install)
  │   │       └─ Register event listeners
  │   └─ Add cp to GUI
  │
  └─ Display appears on screen
      ├─ User can drag the criterion
      ├─ User can adjust weight slider
      ├─ User can enable/disable checkbox
      ├─ User can click "More..." for extra info
      └─ User can delete the criterion
```

**Verification Point**: New criterion appears with all interactive features (drag, slider, checkbox, delete, extra info).

---

## User Adjusts Criterion Weight Flow

```
User drags weight slider
  ├─ JSlider fires ChangeListener
  ├─ Event handler updates:
  │   ├─ criterionPanel.weightSlider value
  │   └─ criterionPanel.valueField text
  │
  └─ Item score displays updated (Items depend on criteria)
      ├─ All ItemsPanel instances see the weight change
      └─ Ranking Panel auto-recalculates all scores
          ├─ RankingCalculate.computeScores(rankingPanel)
          ├─ Sort scores descending
          ├─ Call RankingRows.updateRow() for each item
          └─ Ranks update on screen
```

**Verification Point**: Changing criterion weight immediately updates ranking.

---

## User Views Criterion Extra Info Flow

```
User clicks "More..." on criterion
  ├─ ExtraInfo.toggle(criterionPanel)
  │   ├─ criterionPanel.extraInfoVisible = !extraInfoVisible
  │   └─ Panel.setVisible(extraInfoVisible)
  │
  └─ User clicks to show extra info
      ├─ ExtraInfo.refresh(criterionPanel)
      │   ├─ Calculate.computeAverageScore(itemsList)
      │   │   └─ Average of all items' scores for this criterion
      │   │       ├─ Skip disabled items
      │   │       ├─ Skip disabled criteria
      │   │       └─ Return average as BigDecimal
      │   │
      │   ├─ Calculate.computeScoreVariance(itemsList)
      │   │   └─ Variance of all items' scores for this criterion
      │   │
      │   └─ Update display labels with calculated values
      │
      └─ Extra info visible with:
          ├─ Average Score: XX%
          └─ Score Variance: XX%
```

**Verification Point**: Extra info shows correct average and variance calculations.

---

## User Creates Item Flow

```
User clicks "Add Item"
  ├─ Create.createItem()
  │   ├─ ItemsPanel ip = new ItemsPanel(...)  // constructor minimal
  │   ├─ ip.initialize()                       // NOW UI is built
  │   │   └─ Layout.build(ip)
  │   │       ├─ Create name field, enable checkbox
  │   │       ├─ For each criterion, create score slider + text field
  │   │       │   └─ Scores.buildInitialScores(ip)
  │   │       ├─ Install drag listener (Drag.install)
  │   │       ├─ Create "More..." button (ExtraInfo.initialize)
  │   │       └─ Register all event listeners
  │   ├─ rankingPanel.addItemRow(ip)          // Add to ranking
  │   │   ├─ Create display row
  │   │   ├─ Store in rowMap
  │   │   └─ RankingRows.refreshOrder()       // Reorder ranking
  │   │
  │   └─ Add ip to GUI
  │
  └─ Display appears on screen with:
      ├─ Name field
      ├─ Enable checkbox
      ├─ N score sliders (one per criterion)
      ├─ Delete button
      ├─ Drag handle
      └─ "More..." button (extra info)
```

**Verification Point**: New item appears with sliders for every criterion, integrates into ranking.

---

## User Adjusts Item Score Flow

```
User drags item score slider
  ├─ JSlider fires ChangeListener
  ├─ Event handler updates:
  │   ├─ itemPanel.scoreMap.put(criterion, newScore)
  │   └─ Scores helper syncs text field to slider
  │
  ├─ Callback: onScoreChanged.run()
  │   └─ rankingPanel.updateItemScore(itemPanel)
  │       ├─ Calculate.computeScore(rankingPanel, itemPanel)
  │       │   ├─ Skip if item disabled
  │       │   ├─ For each enabled criterion:
  │       │   │   ├─ Get criterion weight
  │       │   │   ├─ Get item score for criterion
  │       │   │   ├─ Multiply: score/100 × weight
  │       │   │   ├─ Add to total weighted
  │       │   │   ├─ Add weight to total weight
  │       │   ├─ Return totalWeighted / totalWeight × 100
  │       │   └─ Result: ItemScore object
  │       │
  │       ├─ Get ranking row for this item (rowMap)
  │       ├─ RankingRows.updateRow(row, score, rank)
  │       │   ├─ Update main text: "N. ItemName — XX%"
  │       │   └─ Update precise text: "(XX.XXXXX%)"
  │       │
  │       └─ RankingRows.refreshOrder(rankingPanel)
  │           ├─ Recalculate all item scores
  │           ├─ Sort scores descending
  │           ├─ Reorder rows in contentPanel
  │           └─ Update all rank numbers
  │
  └─ Ranking display updated on screen
```

**Verification Point**: Score change immediately updates ranking and positions.

---

## User Views Item Extra Info Flow

```
User clicks "More..." on item
  ├─ ExtraInfo.toggle(itemPanel)
  │   ├─ itemPanel.extraInfoVisible = !extraInfoVisible
  │   └─ Panel.setVisible(extraInfoVisible)
  │
  └─ User clicks to show extra info
      ├─ ExtraInfo.refresh(itemPanel)
      │   ├─ Get item name:
      │   │   └─ itemPanel.getItemName()
      │   │
      │   ├─ Get item rank:
      │   │   └─ rankingPanel.getRankOf(itemPanel)  // PUBLIC API
      │   │       └─ Calculate.getRankOf(rankingPanel, itemPanel)
      │   │           ├─ Get all item scores
      │   │           ├─ Sort descending
      │   │           └─ Find position of this item
      │   │
      │   ├─ Get item's weighted score:
      │   │   └─ rankingPanel.computeScore(itemPanel)  // PUBLIC API
      │   │       └─ Calculate.computeScore(rankingPanel, itemPanel)
      │   │           └─ (see "Adjust Item Score" flow above)
      │   │
      │   ├─ Get unweighted average:
      │   │   └─ itemPanel.getUnweightedAverageScore()
      │   │       └─ Calculate.getUnweightedAverageScore(itemPanel)
      │   │           ├─ For each enabled criterion:
      │   │           │   └─ Get item score
      │   │           ├─ Average all scores
      │   │           └─ Return average
      │   │
      │   ├─ Get score variance:
      │   │   └─ itemPanel.computeScoreVariance()
      │   │       └─ Calculate.computeScoreVariance(itemPanel)
      │   │           ├─ Get all criterion scores for item
      │   │           ├─ Calculate variance
      │   │           └─ Return variance
      │   │
      │   ├─ Get creation index:
      │   │   └─ itemPanel.creationIndex
      │   │
      │   └─ Update display labels
      │
      └─ Extra info visible with:
          ├─ Name: (item name)
          ├─ Rank: #N
          ├─ Total Weighted Score: XX.XXXXX%
          ├─ Unweighted Average Score: XX%
          ├─ Score Variance: XX%
          └─ Creation Index: #N
```

**Verification Point**: Extra info shows correct calculations, updates when scores/ranking changes.

---

## User Drags Item to Reorder Flow

```
User drags item down in list
  ├─ Drag.install() registered mouse listener on drag handle
  ├─ MouseEvent: MOUSE_PRESSED on drag handle
  │   └─ Drag.java starts drag (stores initial position)
  │
  ├─ MouseEvent: MOUSE_DRAGGED
  │   └─ Visual feedback while dragging
  │
  ├─ MouseEvent: MOUSE_RELEASED
  │   ├─ Determine new position based on drop location
  │   ├─ Reorder itemsList in Main
  │   ├─ UI panel removed from container
  │   ├─ All itemPanel references stay intact
  │   │   ├─ All scores preserved
  │   │   ├─ All data preserved
  │   │   └─ scoreMap unchanged
  │   │
  │   ├─ ItemPanel re-added to container at new index
  │   ├─ container.revalidate() / repaint()
  │   │
  │   └─ rankingPanel.updateItemRank(itemPanel) called
  │       └─ RankingRows.refreshOrder(rankingPanel)
  │           └─ Reorder ranking rows to match scores
  │
  └─ Visual: Item moved to new position in list, ranking unchanged
```

**Verification Point**: Drag reorders items, all scores and data preserved, ranking unaffected.

---

## User Sorts Criteria Flow

```
User clicks "Sort Alphabetically" button
  ├─ Sort.sortCriteriaAlphabetically(reverse, criteriaPanel)
  │   ├─ Main.criteriaList.sort((a, b) -> a.getName().compareTo(b.getName()))
  │   │   └─ Criteria reordered in-memory
  │   │
  │   ├─ Sort.reorderCriteriaWrappers(criteriaPanel)
  │   │   ├─ Collect all criterion UI wrappers
  │   │   ├─ Remove all from container
  │   │   ├─ Re-add in order of sorted criteriaList
  │   │   ├─ container.revalidate() / repaint()
  │   │   │
  │   │   └─ For each ItemsPanel:
  │   │       └─ itemPanel.refreshCriteria()
  │   │           ├─ Scores helper removes old score rows
  │   │           ├─ Scores.buildInitialScores() creates new rows
  │   │           │   └─ For each criterion in sorted order:
  │   │           │       └─ Create slider + text field
  │   │           │
  │   │           └─ Item UI updated with sorted criteria
  │   │
  │   └─ rankingPanel.updateAllScores()
  │       ├─ Calculate.computeScores(rankingPanel)
  │       │   └─ Recalculate all weighted averages with new weight order
  │       │
  │       ├─ Sort scores descending
  │       ├─ For each item, update rank
  │       │   └─ RankingRows.updateRow(row, score, newRank)
  │       │
  │       └─ RankingRows.refreshOrder() reorder ranking rows
  │
  └─ Display: Criteria reordered, all score fields updated, ranking recalculated
```

**Verification Point**: Sorting criteria reorders UI, updates all item score fields, recalculates ranking.

---

## User Sorts Items Flow

```
User clicks "Sort by Ranking" button
  ├─ Sort.sortItemsByRanking(reverse, itemsPanel, rankingPanel)
  │   ├─ Main.itemsList.sort((a, b) -> rankingPanel.getRankOf(a) - rankingPanel.getRankOf(b))
  │   │   ├─ For each pair: Calculate.getRankOf(rankingPanel, item)
  │   │   │   ├─ Calculate.computeScores(rankingPanel)
  │   │   │   ├─ Sort scores
  │   │   │   └─ Find rank
  │   │   └─ Items reordered in-memory by rank
  │   │
  │   └─ Sort.reorderItemWrappers(itemsPanel)
  │       ├─ Collect all item UI wrappers
  │       ├─ Remove all from container
  │       ├─ Re-add in order of sorted itemsList
  │       └─ container.revalidate() / repaint()
  │
  └─ Display: Items reordered by current ranking
```

**Verification Point**: Sorting items reorders them, data unchanged, visually sorted.

---

## User Saves Project Flow

```
User clicks "Save" button
  ├─ SaveLoad.saveProject()
  │   ├─ Create JSONArray of criteria
  │   │   ├─ For each CriteriaPanel:
  │   │   │   ├─ Get name: getCriterionName()
  │   │   │   ├─ Get weight: getWeight()
  │   │   │   ├─ Get enabled: isEnabledCriterion()
  │   │   │   └─ Add to JSON
  │   │   │
  │   ├─ Create JSONArray of items
  │   │   ├─ For each ItemsPanel:
  │   │   │   ├─ Get name: getItemName()
  │   │   │   ├─ Get enabled: isEnabledItem()
  │   │   │   ├─ Get scores: getScoreForCriterion(criterion)
  │   │   │   │   └─ Retrieve from scoreMap
  │   │   │   └─ Add to JSON
  │   │   │
  │   └─ Write JSON to file (data.json)
  │
  └─ File saved with all current state
```

**Verification Point**: Save button works, data file created with current state.

---

## User Loads Project Flow

```
User clicks "Load" button
  ├─ SaveLoad.loadProject()
  │   ├─ Read JSON from file
  │   ├─ Clear existing data
  │   │   └─ rankingPanel.clearAll()
  │   │
  │   ├─ For each criterion in JSON:
  │   │   ├─ Create.createCriterion()
  │   │   ├─ Set name: setCriterionName()
  │   │   ├─ Set weight: setWeight()
  │   │   ├─ Set enabled: setEnabledCriterion()
  │   │   └─ Add to Main.criteriaList
  │   │
  │   ├─ For each item in JSON:
  │   │   ├─ Create.createItem()
  │   │   ├─ Set name: setItemName()
  │   │   ├─ Set enabled: setEnabledItem()
  │   │   ├─ For each criterion score:
  │   │   │   └─ setScoreForCriterion(criterion, score)
  │   │   │       └─ scoreMap.put(criterion, score)
  │   │   │
  │   │   └─ Add to Main.itemsList
  │   │
  │   ├─ rankingPanel.updateAllScores()
  │   │   └─ Recalculate all ranks and scores
  │   │
  │   └─ UI completely rebuilt from saved state
  │
  └─ Application state restored to saved point
```

**Verification Point**: Load button works, all data restored correctly, ranking recalculates.

---

## Multiple Calculate Classes Disambiguation

**How Java handles two Calculate.java files in different packages:**

```
itemspanel/Calculate.java
  ├─ package itemspanel;
  ├─ Methods:
  │   ├─ getUnweightedAverageScore(ItemsPanel)
  │   └─ computeScoreVariance(ItemsPanel)
  └─ Accessed as: itemspanel.Calculate.method()

rankingpanel/Calculate.java
  ├─ package rankingpanel;
  ├─ Methods:
  │   ├─ computeScore(RankingPanel, ItemsPanel)
  │   ├─ computeScores(RankingPanel)
  │   └─ getRankOf(RankingPanel, ItemsPanel)
  └─ Accessed as: rankingpanel.Calculate.method()

No naming conflict because they're in different packages!
Each package imports only its own Calculate:
  - itemspanel package imports rankingpanel.RankingPanel (for ranking lookup)
  - rankingpanel package imports itemspanel.ItemsPanel (for scoring)
  - They DON'T import each other's Calculate classes
```

**Verification**: If you see a compiler error about "Calculate" being ambiguous, check imports.

---

## Troubleshooting: If Something Doesn't Work

### Ranking doesn't update after score change
1. Check: ItemsPanel.Layout registers score change listener
2. Check: Listener calls rankingPanel.updateItemScore(itemPanel)
3. Check: RankingPanel.updateItemScore() calls Calculate.computeScore()
4. Check: Result passed to Rows.updateRow()
5. Verify: All steps execute by adding temporary log statements

### Extra info shows wrong values
1. Check: ExtraInfo.refresh() is called at right time
2. Check: Calculate methods return correct values (test with print statements)
3. Check: UI labels updated with returned values
4. Verify: Calculations manually with known test data

### Drag doesn't work
1. Check: Drag.install() called from Layout.build()
2. Check: Mouse listener registered on correct component
3. Check: Drag completion calls refresh/reorder
4. Verify: Drag handle visible and clickable

### Sorting doesn't reorder ranking
1. Check: Sort.java calls rankingPanel.getRankOf() (public API)
2. Check: RankingPanel.getRankOf() delegates to Calculate.getRankOf()
3. Check: calculateRankOf() logic is correct
4. Verify: Sort calculations with simple example (3 items, 2 criteria)

---

## Summary: The Refactored Code Flow

**Original (Monolithic)**:
- All logic in one class
- Hard to maintain, test, understand
- UI construction mixed with logic

**New (Modular)**:
- Thin coordinators delegate to focused helpers
- Each helper does one thing well
- Easy to maintain, test, understand
- Public API stable, internal organization clean

**Key insight**: The flow is identical to the original, just organized better. If all tests pass, the refactoring is successful!
