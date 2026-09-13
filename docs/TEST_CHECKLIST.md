# RankAnything Comprehensive Refactoring Test Checklist

## Pre-Test Status
✅ **Compilation**: All source files compile with no errors (only minor style warnings)
✅ **Application Launch**: GUI starts successfully

## Test Instructions
This checklist verifies all interactions work correctly after splitting CriteriaPanel, ItemsPanel, and RankingPanel into multiple helper files. Use the GUI to test each item below.

---

## CRITERIA PANEL TESTS

### Basic Criteria Operations
- [ ] **Add Criterion**: Click "Add Criterion" button → Name appears in criteria list
- [ ] **Criterion Name Display**: Name is visible and correct
- [ ] **Criterion Enabled Toggle**: Click checkbox next to criterion → enabled/disabled state changes
- [ ] **Weight Adjustment**: Use slider to adjust weight (0-10) → value updates smoothly
- [ ] **Weight Label**: Label displays correct weight value
- [ ] **Delete Criterion**: Click delete button → criterion removed from list
- [ ] **Multiple Criteria**: Add 3+ criteria → all display correctly with no conflicts

### Criterion Extra Info (More... button)
- [ ] **Toggle Extra Info**: Click "More..." on a criterion → extra info panel appears/disappears
- [ ] **Average Score Display**: Extra info shows "Average Score: XX%" with correct calculation
- [ ] **Score Variance Display**: Extra info shows "Score Variance: XX%" with correct calculation
- [ ] **Multiple Criteria Extra Info**: Toggle extra info on different criteria → correct data for each
- [ ] **Extra Info Updates**: Modify item scores → criterion extra info updates automatically

### Criterion Drag & Reorder
- [ ] **Drag to Reorder**: Drag a criterion up/down in the list → reorders correctly
- [ ] **Drag Multiple**: Drag different criteria around → all maintain correct positions
- [ ] **Drag Handle Visual**: Drag handle (≡ icon) is visible and functional

### Criterion Precision
- [ ] **Precision Toggle**: Use precision checkbox → scores display with/without decimal places
- [ ] **Precision Updates**: Precision change affects all displays immediately

---

## ITEMS PANEL TESTS

### Basic Item Operations
- [ ] **Add Item**: Click "Add Item" button → name field appears in items list
- [ ] **Item Name Entry**: Type name in text field → updates correctly
- [ ] **Item Enabled Toggle**: Click checkbox next to item → enabled/disabled state changes
- [ ] **Delete Item**: Click delete button → item removed from list
- [ ] **Multiple Items**: Add 3+ items → all display correctly with no conflicts
- [ ] **Creation Index**: Each item has correct sequential creation index (#1, #2, #3, etc.)

### Item Score Sliders
- [ ] **Score Slider Creation**: For each criterion, a slider appears for each item
- [ ] **Slider Adjustment**: Drag slider → score updates (0-100)
- [ ] **Score Text Field**: Type number in score field → slider updates to match
- [ ] **Score Range Validation**: Try entering >100 or <0 → values constrained correctly
- [ ] **Multiple Items Multiple Criteria**: Create 3 items and 3 criteria → all 9 sliders work independently

### Item Drag & Reorder
- [ ] **Drag Items**: Drag item up/down in list → reorders correctly
- [ ] **Drag Persistence**: After drag, item maintains all scores and data
- [ ] **Drag Handle**: Drag handle (≡ icon) is visible and functional

### Item Extra Info (More... button)
- [ ] **Toggle Extra Info**: Click "More..." on an item → extra info panel appears/disappears
- [ ] **Name Display**: Extra info shows correct item name
- [ ] **Rank Display**: Extra info shows current rank (e.g., "Rank: #1")
- [ ] **Weighted Score Display**: Extra info shows "Total Weighted Score: XX.XXXXX%"
- [ ] **Unweighted Average Display**: Extra info shows "Unweighted Average Score: XX%"
- [ ] **Variance Display**: Extra info shows "Score Variance: XX%"
- [ ] **Creation Index Display**: Extra info shows creation index
- [ ] **Extra Info Auto-Update**: Modify scores or criteria → all extra info values update automatically
- [ ] **Multiple Items Extra Info**: Toggle extra info on different items → correct data for each

### Item Collapse
- [ ] **Collapse Item**: Click collapse button on an item → item collapses (only header visible)
- [ ] **Expand Item**: Click expand button → item expands (shows scores again)
- [ ] **Multiple Collapsed**: Collapse multiple items → each can be independently expanded

---

## RANKING PANEL TESTS

### Ranking Display
- [ ] **Ranking List**: Items displayed in ranking order (highest score first)
- [ ] **Rank Numbers**: Ranks shown correctly (#1, #2, #3, etc.)
- [ ] **Rank Precision**: Scores displayed with correct precision (rounded vs. decimal)
- [ ] **Ranking Order**: Disable top-ranked item → ranking automatically re-orders
- [ ] **Score Display**: Rank shows item name and score percentage

### Ranking Automatic Updates
- [ ] **Score Change Updates Rank**: Change an item's score → ranking re-orders automatically
- [ ] **Criteria Weight Change Updates Rank**: Change criterion weight → all ranks update
- [ ] **Enable/Disable Item Updates Rank**: Toggle item enabled → removed from/re-added to ranking
- [ ] **Enable/Disable Criterion Updates Rank**: Toggle criterion enabled → ranking recalculates
- [ ] **Add Criterion Updates Rank**: Add new criterion → ranking recalculates with new weights
- [ ] **Delete Criterion Updates Rank**: Delete criterion → ranking recalculates without its weight
- [ ] **Add Item Updates Rank**: Add new item with scores → inserts into correct ranking position
- [ ] **Delete Item Updates Rank**: Delete item → removed from ranking display

### Save/Load Functionality
- [ ] **Save Project**: Click "Save" button → no errors, file created/updated
- [ ] **Load Project**: Click "Load" button → data restored correctly
- [ ] **Save/Load Roundtrip**: Save, then load → all criteria, items, scores identical
- [ ] **Load With Changes**: Modify data, load saved file → reverts to saved state
- [ ] **Multiple Saves**: Save multiple times → each overwrites previous

---

## SORTING TESTS

### Criteria Sorting
- [ ] **Sort Alphabetically (A-Z)**: Click sort button → criteria sorted by name ascending
- [ ] **Sort Alphabetically (Z-A)**: Click sort button again → criteria sorted by name descending
- [ ] **Sort by Weight**: Criteria sorted by weight value
- [ ] **Sort by Average Score**: Criteria sorted by average score calculation
- [ ] **Sort by Variance**: Criteria sorted by score variance
- [ ] **Sort by Creation Index**: Criteria sorted by creation order
- [ ] **Sort Disabled Last**: Disabled criteria move to bottom
- [ ] **Sort Persistence**: After sort, all data (scores, weights) maintained

### Items Sorting
- [ ] **Sort Alphabetically (A-Z)**: Items sorted by name ascending
- [ ] **Sort Alphabetically (Z-A)**: Items sorted by name descending
- [ ] **Sort by Ranking**: Items sorted by current ranking
- [ ] **Sort by Unweighted Average**: Items sorted by average score
- [ ] **Sort by Highest Score**: Items sorted by their highest single score
- [ ] **Sort by Lowest Score**: Items sorted by their lowest single score
- [ ] **Sort by Variance**: Items sorted by score variance
- [ ] **Sort by Creation Index**: Items sorted by creation order
- [ ] **Sort Disabled Last**: Disabled items move to bottom
- [ ] **Sort Persistence**: After sort, all data maintained

---

## COMPLEX INTERACTION TESTS

### Multi-Criteria Multi-Item Workflow
- [ ] **Create 4 criteria** with different weights
- [ ] **Create 4 items** with different score patterns
- [ ] **Verify Ranking**: Ranking correctly calculates weighted averages
- [ ] **Modify Weights**: Change criterion weights → ranking updates
- [ ] **Reorder Criteria**: Drag criteria → weights correctly applied to reordered criteria
- [ ] **Reorder Items**: Drag items → ranking preserves scores for each item
- [ ] **Enable/Disable**: Toggle various criteria/items → ranking and extra info update

### Extra Info Accuracy
- [ ] **Criterion Average**: Add 3 items with scores 50, 60, 70 for criterion → extra info shows avg 60%
- [ ] **Criterion Variance**: Same scores → variance calculated correctly
- [ ] **Item Weighted Score**: Multiple criteria with weights → item total matches ranking
- [ ] **Item Unweighted**: Unweighted average = (50+60+70)/3 / numberOfCriteria calculation
- [ ] **Precision Affects Extra Info**: Toggle precision → extra info displays update

### Cross-Component Updates
- [ ] **Criteria Change Affects Items**: Add criterion → new score row appears for all items
- [ ] **Criteria Change Affects Ranking**: Modify criterion weight → ranking updates
- [ ] **Item Name Change**: Change item name → updates in ranking display
- [ ] **Item Score Change**: Modify score → updates in ranking and all displays
- [ ] **Item Enable/Disable**: Toggles affect ranking and extra info
- [ ] **Criterion Enable/Disable**: Toggles affect all weighted score calculations

---

## EDGE CASES

- [ ] **Empty State**: Start fresh → no crashes, "Add Criterion" and "Add Item" buttons work
- [ ] **Single Criterion**: Create only 1 criterion → works correctly
- [ ] **Single Item**: Create only 1 item → item ranked #1
- [ ] **Disable All Criteria**: All criteria unchecked → items show 0% score
- [ ] **Disable All Items**: All items unchecked → ranking empty
- [ ] **All Same Score**: All items have 50% for all criteria → ranking order stable
- [ ] **Extreme Weights**: Set criterion weight to 0.1 → calculations work
- [ ] **Extreme Weights**: Set criterion weight to 10.0 → calculations work
- [ ] **Long Names**: Use 100+ character names → display handles without truncation issues
- [ ] **Special Characters**: Use names with emoji, symbols → handle correctly

---

## SUMMARY CHECKLIST

- [ ] **No Application Crashes**: Tested all interactions without crashes
- [ ] **All Data Consistent**: Scores, ranks, extra info all match correctly
- [ ] **Performance**: Application responds smoothly to all interactions
- [ ] **Visual Layout**: All components visible and properly aligned
- [ ] **Calculation Accuracy**: All mathematical calculations verified correct
- [ ] **Drag & Drop**: All drag operations smooth and functional
- [ ] **Save/Load**: Data persists correctly across sessions

---

## Notes for Verification

**The refactoring split:**
- CriteriaPanel → CriteriaPanelLayout, Drag, ExtraInfo, Calculate helpers
- ItemsPanel → Layout, Drag, ExtraInfo, Calculate, Scores helpers  
- RankingPanel → Layout, Calculate, Rows helpers

**If any test fails**, the issue is likely in:
- The corresponding helper class (check imports and method calls)
- The coordinator class (check delegation to helpers)
- The Create.java factory (check that `.initialize()` is called after construction)
- The Main.java constructor (check that `rankingPanel.initialize()` is called)

---

**✅ All tests passing = Refactoring successful!**
**❌ Test failure = Check the helper class responsible for that functionality**
