@echo off
echo Setting up Matrix wallpaper for Windows startup...
set "startup=%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup"
copy "%~dp0matrix-wallpaper.bat" "%startup%\matrix-wallpaper.bat"
echo Matrix wallpaper added to startup folder.
echo It will run automatically when Windows starts.
pause