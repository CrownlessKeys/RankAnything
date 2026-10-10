package contact;

public final class Contact {

    static final String DEV_EMAIL = "crownlesskeys@gmail.com";

    public static void reportBug() {
        Dialog.showBugReportDialog();
    }

    public static void requestFeature() {
        Dialog.showFeatureRequestDialog();
    }

    public static void generalContact() {
        LegacyDialog.showContactDialog();
    }
}