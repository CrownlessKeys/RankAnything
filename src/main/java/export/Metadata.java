package export;

import app.Main;
import saveload.SaveLoad;

final class Metadata {

    static final class MetadataResult {
        public final String projectName;
        public final String projectId;

        public final String fileLocation;
        public final boolean isOriginal;

        public final String projectCreated;
        public final String fileCreated;
        public final String projectModified;

        public final String appVersion;
        public final String projectAppVersion;
        public final int fileFormatVersion;
        public final int projectFileFormatVersion;

        public final int criteriaCount;
        public final int criteriaCreated;
        public final int itemsCount;
        public final int itemsCreated;

        public MetadataResult(
            String projectName,
            String projectId,
            String fileLocation,
            boolean isOriginal,
            String projectCreated,
            String fileCreated,
            String projectModified,
            String appVersion,
            String projectAppVersion,
            int fileFormatVersion,
            int projectFileFormatVersion,
            int criteriaCount,
            int criteriaCreated,
            int itemsCount,
            int itemsCreated
        ) {
            this.projectName = projectName;
            this.projectId = projectId;

            this.fileLocation = fileLocation;
            this.isOriginal = isOriginal;

            this.projectCreated = projectCreated;
            this.fileCreated = fileCreated;
            this.projectModified = projectModified;

            this.appVersion = appVersion;
            this.projectAppVersion = projectAppVersion;
            this.fileFormatVersion = fileFormatVersion;
            this.projectFileFormatVersion = projectFileFormatVersion;

            this.criteriaCount = criteriaCount;
            this.criteriaCreated = criteriaCreated;
            this.itemsCount = itemsCount;
            this.itemsCreated = itemsCreated;
        }
    }

    static MetadataResult createMetadata() {

        return new MetadataResult(
            Main.projectName,
            Main.projectId,

            SaveLoad.getCurrentFilePath(),
            Main.isOriginal,

            Main.projectCreated,
            Main.fileCreated,
            Main.projectModified,

            Main.APP_VERSION,
            Main.projectAppVersion,
            Main.FILE_FORMAT_VERSION,
            Main.projectFileFormatVersion,

            Main.criteriaList.size(),
            Main.criteriaCreationCounter,
            Main.itemsList.size(),
            Main.itemsCreationCounter
        );
    }
}