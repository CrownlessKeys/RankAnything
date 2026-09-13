cd c:\Users\danie\Documents\Projects\RankAnything
$files = Get-ChildItem -Path src/main/java -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
javac -cp "lib\json-20260719.jar" -d out $files
