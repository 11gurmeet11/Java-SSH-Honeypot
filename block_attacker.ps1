param (
    [string]$ip
)

if (-not $ip) {
    Write-Host "❌ Usage: .\block_attacker.ps1 -ip <IP_ADDRESS>"
    exit
}

Write-Host "[+] Blocking IP: $ip"

# Create a Windows Defender Firewall rule to block the IP
New-NetFirewallRule `
    -DisplayName "Blocked_$ip" `
    -Direction Inbound `
    -RemoteAddress $ip `
    -Action Block `
    -Protocol TCP `
    -Enabled True

Write-Host "[✔] IP blocked successfully."
