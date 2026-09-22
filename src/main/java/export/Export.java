package export;

import app.Main;

public final class Export {

    static class ExportPreferences {

        public boolean ranking;
        public boolean itemInfo;
        public boolean critInfo;
        public boolean metadata;
        public boolean notes;

        public String fileType;
        public String fileName;
        public String folder;

        // if including .rank snapshot
        public boolean rankProject;
        public String rankFileName;
        public String rankFolder;

        public boolean confirmed;

        // Empty constructor (default)
        public ExportPreferences() {
        }

        // Full constructor
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
    }
    
    public static void export() {
        Dialog.show(
            Main.frame, 
            new ExportPreferences(
                Main.ranking,
                Main.itemInfo,
                Main.critInfo,
                Main.metadata,
                Main.notes,
                "HTML", // temp
                "temp", // temp
                "folder", // temp
                true,
                "rank.rank",
                "rank folder",
                false
            )
        );
    }
}