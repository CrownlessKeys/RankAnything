mvn clean package -P installer-layout

Remove-Item -Recurse -Force target/jpackage-input -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path target/jpackage-input

Copy-Item target/rankanything-1.0.0-beta.1.jar target/jpackage-input/
Copy-Item target/dependencies/*.jar target/jpackage-input/

jpackage --type dmg `
    --input target/jpackage-input `
    --main-jar rankanything-1.0.0-beta.1.jar `
    --main-class app.Main `
    --name "Rank Anything" `
    --app-version "1.0.0-beta.1" `
    --vendor "Crownless Keys" `
    --module-path target/javafx-modules `
    --add-modules javafx.controls,javafx.graphics,java.desktop,java.logging `
    --icon appIcon.icns `
    --file-associations fileassoc.properties `
    --dest target/installer

Rename-Item `
    -Path "target/installer/Rank Anything-1.0.0-beta.1.dmg" `
    -NewName "Rank-Anything-Installer-1.0.0-beta.1.dmg"