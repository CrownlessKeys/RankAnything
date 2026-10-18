mvn clean package -P installer-layout

Remove-Item -Recurse -Force target/jpackage-input -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path target/jpackage-input

Copy-Item target/rankanything-1.0.0-beta.1.jar target/jpackage-input/
Copy-Item target/dependencies/*.jar target/jpackage-input/

jpackage --type msi `
    --input target/jpackage-input `
    --main-jar rankanything-1.0.0-beta.1.jar `
    --main-class app.Main `
    --name "Rank Anything" `
    --app-version "0.9.1" `
    --win-upgrade-uuid "3E50C518-34A0-43A0-9D42-C39DAB5B083A" `
    --vendor "Crownless Keys" `
    --module-path target/javafx-modules `
    --add-modules javafx.controls,javafx.graphics,java.desktop,java.logging `
    --win-dir-chooser `
    --win-shortcut `
    --win-menu `
    --win-shortcut-prompt `
    --icon appIcon.ico `
    --file-associations fileassoc.properties `
    --dest target/installer

Rename-Item `
    -Path "target/installer/Rank Anything-0.9.1.msi" `
    -NewName "Rank-Anything-Installer-1.0.0-beta.1.msi"