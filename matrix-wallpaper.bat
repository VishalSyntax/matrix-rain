@echo off
cd /d "%~dp0"
start "" "C:\Program Files\Google\Chrome\Application\chrome.exe" --kiosk --no-default-browser-check --disable-infobars --disable-extensions --app="file:///%CD%/index.html"