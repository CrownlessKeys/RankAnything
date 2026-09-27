package export;

import org.json.JSONObject;

public final class ExportPreferences {

    // whether to include these fields in the export
    public boolean ranking;
    public boolean itemInfo; public boolean includeDisabledItems;
    public boolean critInfo; public boolean includeDisabledCriteria;
    public boolean metadata;
    public boolean notes;

    public String fileType; // file type for export
    public String fileName; // name of export file
    public String folder; // file location to store the export

    public boolean rankProject; // whether to include .rank project snapshot
    public String rankFileName; // what to name .rank project snapshot
    public String rankFolder; // what to name the folder where the snapshot and export are stored

    public ExportPreferences() {
    }

    public ExportPreferences(
        boolean ranking,
        boolean itemInfo, boolean includeDisabledItems,
        boolean critInfo, boolean includeDisabledCriteria,
        boolean metadata,
        boolean notes,
        String fileType,
        String fileName,
        String folder,
        boolean rankProject,
        String rankFileName,
        String rankFolder
    ) {
        this.ranking = ranking;
        this.itemInfo = itemInfo; this.includeDisabledItems = includeDisabledItems;
        this.critInfo = critInfo; this.includeDisabledCriteria = includeDisabledCriteria;
        this.metadata = metadata;
        this.notes = notes;

        this.fileType = fileType;
        this.fileName = fileName;
        this.folder = folder;

        this.rankProject = rankProject;
        this.rankFileName = rankFileName;
        this.rankFolder = rankFolder;
    }

    // mitosis constructor
    public ExportPreferences(
        ExportPreferences e
    ) {
        this.ranking = e.ranking;
        this.itemInfo = e.itemInfo; this.includeDisabledItems = e.includeDisabledItems;
        this.critInfo = e.critInfo; this.includeDisabledCriteria = e.includeDisabledCriteria;
        this.metadata = e.metadata;
        this.notes = e.notes;

        this.fileType = e.fileType;
        this.fileName = e.fileName;
        this.folder = e.folder;

        this.rankProject = e.rankProject;
        this.rankFileName = e.rankFileName;
        this.rankFolder = e.rankFolder;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        obj.put("ranking", ranking);
        obj.put("itemInfo", itemInfo); obj.put("includeDisabledItems", includeDisabledItems);
        obj.put("critInfo", critInfo); obj.put("includeDisabledCriteria", includeDisabledCriteria);
        obj.put("metadata", metadata);
        obj.put("notes", notes);

        obj.put("fileType", fileType);
        obj.put("fileName", fileName);
        obj.put("folder", folder);

        obj.put("rankProject", rankProject);
        obj.put("rankFileName", rankFileName);
        obj.put("rankFolder", rankFolder);

        return obj;
    }

    public static ExportPreferences fromJSON(JSONObject obj) {
        ExportPreferences p = new ExportPreferences();

        p.ranking = obj.optBoolean("ranking", true);
        p.itemInfo = obj.optBoolean("itemInfo", true); p.includeDisabledItems = obj.optBoolean("includeDisabledItems", true);
        p.critInfo = obj.optBoolean("critInfo", true); p.includeDisabledCriteria = obj.optBoolean("includeDisabledCriteria", true);
        p.metadata = obj.optBoolean("metadata", true);
        p.notes = obj.optBoolean("notes", true);

        p.fileType = obj.optString("fileType", "PDF");
        p.fileName = obj.optString("fileName", "");
        p.folder = obj.optString("folder", "");

        p.rankProject = obj.optBoolean("rankProject", false);
        p.rankFileName = obj.optString("rankFileName", "");
        p.rankFolder = obj.optString("rankFolder", "");

        return p;
    }
}