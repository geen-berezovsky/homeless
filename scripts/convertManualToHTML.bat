@echo off
cd %~dp0
echo %CD%
pandoc -f docx -t html -s -S --toc ..\src\main\resources\manual.docx > manual.html