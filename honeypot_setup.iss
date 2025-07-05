; honeypot_setup.iss — Inno Setup script for Java SSH Honeypot

[Setup]
AppName=Java SSH Honeypot
AppVersion=1.0
DefaultDirName={pf}\JavaSSH_Honeypot
DefaultGroupName=JavaSSH Honeypot
OutputBaseFilename=honeypot_setup_installer
Compression=lzma
SolidCompression=yes
SilentInstall=true

[Files]
Source: "..\build\honeypot.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\scripts\install_service.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "nssm.exe"; DestDir: "{app}"; Flags: ignoreversion

[Run]
Filename: "{app}\install_service.bat"; Flags: runhidden
