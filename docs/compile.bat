@echo off
cd /d c:\Users\danie\Documents\Projects\RankAnything
for /r src\main\java %%f in (*.java) do (
    set "files=!files! %%f"
)
javac -cp "lib\json-20260719.jar" -d out %files%
pause
