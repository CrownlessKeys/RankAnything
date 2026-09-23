package export;

import org.json.JSONObject;

public final class ExportPreferences {

    public boolean ranking;
    public boolean itemInfo;
    public boolean critInfo;
    public boolean metadata;
    public boolean notes;

    public String fileType;
    public String fileName;
    public String folder;

    public boolean rankProject;
    public String rankFileName;
    public String rankFolder;

    public boolean confirmed;

    public ExportPreferences() {
    }

    public ExportPreferences(
        boolean ranking,
        boolean itemInfo,
        boolean critInfo,
        boolean metadata,
        boolean notes,
        String fileType,
        String fileName,
        String folder,
        boolean rankProject,
        String rankFileName,
        String rankFolder,
        boolean confirmed
    ) {
        this.ranking = ranking;
        this.itemInfo = itemInfo;
        this.critInfo = critInfo;
        this.metadata = metadata;
        this.notes = notes;

        this.fileType = fileType;
        this.fileName = fileName;
        this.folder = folder;

        this.rankProject = rankProject;
        this.rankFileName = rankFileName;
        this.rankFolder = rankFolder;

        this.confirmed = confirmed;
    }

    // mitosis constructor
    public ExportPreferences(
        ExportPreferences e
    ) {
        this.ranking = e.ranking;
        this.itemInfo = e.itemInfo;
        this.critInfo = e.critInfo;
        this.metadata = e.metadata;
        this.notes = e.notes;

        this.fileType = e.fileType;
        this.fileName = e.fileName;
        this.folder = e.folder;

        this.rankProject = e.rankProject;
        this.rankFileName = e.rankFileName;
        this.rankFolder = e.rankFolder;

        this.confirmed = e.confirmed;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        
        obj.put("ranking", ranking);
        obj.put("itemInfo", itemInfo);
        obj.put("critInfo", critInfo);
        obj.put("metadata", metadata);
        obj.put("notes", notes);

        obj.put("fileType", fileType);
        obj.put("fileName", fileName);
        obj.put("folder", folder);

        obj.put("rankProject", rankProject);
        obj.put("rankFileName", rankFileName);
        obj.put("rankFolder", rankFolder);

        obj.put("confirmed", confirmed);

        return obj;
    }

    public static ExportPreferences fromJSON(JSONObject obj) {
        ExportPreferences p = new ExportPreferences();

        p.ranking = obj.optBoolean("ranking", true);
        p.itemInfo = obj.optBoolean("itemInfo", true);
        p.critInfo = obj.optBoolean("critInfo", true);
        p.metadata = obj.optBoolean("metadata", true);
        p.notes = obj.optBoolean("notes", true);

        p.fileType = obj.optString("fileType", "PDF");
        p.fileName = obj.optString("fileName", "");
        p.folder = obj.optString("folder", "");

        p.rankProject = obj.optBoolean("rankProject", false);
        p.rankFileName = obj.optString("rankFileName", "");
        p.rankFolder = obj.optString("rankFolder", "");

        p.confirmed = obj.optBoolean("confirmed", true);

        return p;
    }
}