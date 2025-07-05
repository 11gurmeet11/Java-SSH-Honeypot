@echo off
REM === Install Java SSH Honeypot as a Windows Service using NSSM ===

set SERVICE_NAME=JavaSSH_Honeypot
set NSSM_PATH=%~dp0..\installer\nssm.exe
set EXE_PATH=%~dp0..\build\honeypot.exe

echo.
echo [*] Installing %SERVICE_NAME% as a Windows service...

"%NSSM_PATH%" install %SERVICE_NAME% "%EXE_PATH%"
"%NSSM_PATH%" set %SERVICE_NAME% DisplayName "Java SSH Honeypot"
"%NSSM_PATH%" set %SERVICE_NAME% Start SERVICE_AUTO_START

echo [*] Starting service...
"%NSSM_PATH%" start %SERVICE_NAME%

echo [✔] %SERVICE_NAME% is now running in the background.
pause
