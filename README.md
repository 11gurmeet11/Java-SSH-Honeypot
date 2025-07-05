# 🛡️ Java SSH Honeypot

A real-time SSH honeypot written in Java that logs unauthorized SSH access attempts, performs GeoIP lookup, sends email alerts, and blocks malicious IPs via Windows Firewall. Lightweight and suitable for educational & defensive cybersecurity research.
---
# ⚠️ Disclaimer

This project is intended only for educational and research use.
Do not use this tool without proper authorization in production or third-party environments.
InternetAddress.parse("receiver@example.com");

---

## 🚀 Features

- 🎯 Listens on custom SSH port (default `2222`)
- 🌍 GeoIP lookup via `ip-api.com`
- 📧 Real-time email alerts via Gmail SMTP
- 🔒 Automatic IP blocking via Windows Defender Firewall
- 📝 Persistent logging to local file

---

## 🔧 Prerequisites

- Java 8 or higher (JDK)
- Gmail account with [App Password](https://myaccount.google.com/apppasswords)
- Windows OS (for firewall blocking)
- Internet connection (for GeoIP & email)

---
### ✏️ Edit Configuration
---

In src/SSHHoneypot.java:

java
```bash

final String username = "youremail@gmail.com";
final String password = "your-app-password"; // NOT your regular Gmail password


