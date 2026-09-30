package export;

import org.json.JSONObject;

public final class ExportPreferences {

    // whether to include these fields in the export
    public boolean ranking;
    public boolean itemInfo; 
        public boolean includeDisabledItems; 
        public boolean includeExtraInfoItems;
    public boolean critInfo; 
        public boolean includeDisabledCriteria; 
        public boolean includeExtraInfoCriteria;
    public boolean metadata;
    public boolean notes;

    public String fileType; // file type for export
    public String fileName; // name of export file
    public String folder; // file location to store the export

    public boolean rankProject; // whether to include .rank project snapshot
    public String rankFileName; // what to name .rank project snapshot
    public String rankFolder; // what to name the folder where the snapshot and export are stored

    // alternative rankings
    public boolean includeWeightedEnabled;
    public boolean includeWeightedAllCriteria;
    public boolean includeWeightedAllItems;
    public boolean includeWeightedAllCriteriaAllItems;
    public boolean includeUnweightedEnabled;
    public boolean includeUnweightedAllCriteria;
    public boolean includeUnweightedAllItems;
    public boolean includeUnweightedAllCriteriaAllItems;

    public ExportPreferences() {
    }

    public ExportPreferences(
        boolean ranking,
        boolean itemInfo, 
            boolean includeDisabledItems, 
            boolean includeExtraInfoItems,
        boolean critInfo, 
            boolean includeDisabledCriteria, 
            boolean includeExtraInfoCriteria,
        boolean metadata,
        boolean notes,
        String fileType,
        String fileName,
        String folder,
        boolean rankProject,
        String rankFileName,
        String rankFolder,
        boolean includeWeightedEnabled,
        boolean includeWeightedAllCriteria,
        boolean includeWeightedAllItems,
        boolean includeWeightedAllCriteriaAllItems,
        boolean includeUnweightedEnabled,
        boolean includeUnweightedAllCriteria,
        boolean includeUnweightedAllItems,
        boolean includeUnweightedAllCriteriaAllItems
    ) {
        this.ranking = ranking;
        this.itemInfo = itemInfo; 
            this.includeDisabledItems = includeDisabledItems; 
            this.includeExtraInfoItems = includeExtraInfoItems;
        this.critInfo = critInfo; 
            this.includeDisabledCriteria = includeDisabledCriteria; 
            this.includeExtraInfoCriteria = includeExtraInfoCriteria;
        this.metadata = metadata;
        this.notes = notes;

        this.fileType = fileType;
        this.fileName = fileName;
        this.folder = folder;

        this.rankProject = rankProject;
        this.rankFileName = rankFileName;
        this.rankFolder = rankFolder;

        this.includeWeightedEnabled = includeWeightedEnabled;
        this.includeWeightedAllCriteria = includeWeightedAllCriteria;
        this.includeWeightedAllItems = includeWeightedAllItems;
        this.includeWeightedAllCriteriaAllItems = includeWeightedAllCriteriaAllItems;
        this.includeUnweightedEnabled = includeUnweightedEnabled;
        this.includeUnweightedAllCriteria = includeUnweightedAllCriteria;
        this.includeUnweightedAllItems = includeUnweightedAllItems;
        this.includeUnweightedAllCriteriaAllItems = includeUnweightedAllCriteriaAllItems;
    }

    // mitosis constructor
    public ExportPreferences(
        ExportPreferences e
    ) {
        this.ranking = e.ranking;
        this.itemInfo = e.itemInfo; 
            this.includeDisabledItems = e.includeDisabledItems; 
            this.includeExtraInfoItems = e.includeExtraInfoItems;
        this.critInfo = e.critInfo; 
            this.includeDisabledCriteria = e.includeDisabledCriteria; 
            this.includeExtraInfoCriteria = e.includeExtraInfoCriteria;
        this.metadata = e.metadata;
        this.notes = e.notes;

        this.fileType = e.fileType;
        this.fileName = e.fileName;
        this.folder = e.folder;

        this.rankProject = e.rankProject;
        this.rankFileName = e.rankFileName;
        this.rankFolder = e.rankFolder;

        this.includeWeightedEnabled = e.includeWeightedEnabled;
        this.includeWeightedAllCriteria = e.includeWeightedAllCriteria;
        this.includeWeightedAllItems = e.includeWeightedAllItems;
        this.includeWeightedAllCriteriaAllItems = e.includeWeightedAllCriteriaAllItems;
        this.includeUnweightedEnabled = e.includeUnweightedEnabled;
        this.includeUnweightedAllCriteria = e.includeUnweightedAllCriteria;
        this.includeUnweightedAllItems = e.includeUnweightedAllItems;
        this.includeUnweightedAllCriteriaAllItems = e.includeUnweightedAllCriteriaAllItems;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        obj.put("ranking", ranking);
        obj.put("itemInfo", itemInfo); 
            obj.put("includeDisabledItems", includeDisabledItems); 
            obj.put("includeExtraInfoItems", includeExtraInfoItems);
        obj.put("critInfo", critInfo); 
            obj.put("includeDisabledCriteria", includeDisabledCriteria); 
            obj.put("includeExtraInfoCriteria", includeExtraInfoCriteria);
        obj.put("metadata", metadata);
        obj.put("notes", notes);

        obj.put("fileType", fileType);
        obj.put("fileName", fileName);
        obj.put("folder", folder);

        obj.put("rankProject", rankProject);
        obj.put("rankFileName", rankFileName);
        obj.put("rankFolder", rankFolder);

        obj.put("includeWeightedEnabled", includeWeightedEnabled);
        obj.put("includeWeightedAllCriteria", includeWeightedAllCriteria);
        obj.put("includeWeightedAllItems", includeWeightedAllItems);
        obj.put("includeWeightedAllCriteriaAllItems", includeWeightedAllCriteriaAllItems);
        obj.put("includeUnweightedEnabled", includeUnweightedEnabled);
        obj.put("includeUnweightedAllCriteria", includeUnweightedAllCriteria);
        obj.put("includeUnweightedAllItems", includeUnweightedAllItems);
        obj.put("includeUnweightedAllCriteriaAllItems", includeUnweightedAllCriteriaAllItems);

        return obj;
    }

    public static ExportPreferences fromJSON(JSONObject obj) {
        ExportPreferences p = new ExportPreferences();

        p.ranking = obj.optBoolean("ranking", true);
        p.itemInfo = obj.optBoolean("itemInfo", true); 
            p.includeDisabledItems = obj.optBoolean("includeDisabledItems", false); 
            p.includeExtraInfoItems = obj.optBoolean("includeExtraInfoItems", false);
        p.critInfo = obj.optBoolean("critInfo", true); 
            p.includeDisabledCriteria = obj.optBoolean("includeDisabledCriteria", false); 
            p.includeExtraInfoCriteria = obj.optBoolean("includeExtraInfoCriteria", false);
        p.metadata = obj.optBoolean("metadata", true);
        p.notes = obj.optBoolean("notes", true);

        p.fileType = obj.optString("fileType", "PDF");
        p.fileName = obj.optString("fileName", "");
        p.folder = obj.optString("folder", "");

        p.rankProject = obj.optBoolean("rankProject", false);
        p.rankFileName = obj.optString("rankFileName", "");
        p.rankFolder = obj.optString("rankFolder", "");

        p.includeWeightedEnabled = obj.optBoolean("includeWeightedEnabled", false);
        p.includeWeightedAllCriteria = obj.optBoolean("includeWeightedAllCriteria", false);
        p.includeWeightedAllItems = obj.optBoolean("includeWeightedAllItems", false);
        p.includeWeightedAllCriteriaAllItems = obj.optBoolean("includeWeightedAllCriteriaAllItems", false);
        p.includeUnweightedEnabled = obj.optBoolean("includeUnweightedEnabled", false);
        p.includeUnweightedAllCriteria = obj.optBoolean("includeUnweightedAllCriteria", false);
        p.includeUnweightedAllItems = obj.optBoolean("includeUnweightedAllItems", false);
        p.includeUnweightedAllCriteriaAllItems = obj.optBoolean("includeUnweightedAllCriteriaAllItems", false);

        return p;
    }
}