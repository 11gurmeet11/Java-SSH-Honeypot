import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;

public class SSHHoneypot {
    private static final int SSH_PORT = 2222;

    public static void main(String[] args) {
        System.out.println("[INFO] SSH Honeypot started on port " + SSH_PORT);

        try (ServerSocket serverSocket = new ServerSocket(SSH_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                String clientIP = socket.getInetAddress().getHostAddress();
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                OutputStream out = socket.getOutputStream();
                out.write("SSH-2.0-OpenSSH_8.2p1 Ubuntu-4ubuntu0.3\r\n".getBytes());
                out.flush();

                String country = getCountry(clientIP);
                logAttempt(clientIP, timestamp, country);
                sendEmailAlert(clientIP, timestamp, country);
                blockAttacker(clientIP);

                socket.close();
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Server failed: " + e.getMessage());
        }
    }

    private static String getCountry(String ip) {
        try {
            URL url = new URL("http://ip-api.com/line/" + ip + "?fields=country");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            return in.readLine();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private static void logAttempt(String ip, String time, String country) {
        String log = "[" + time + "] SSH attempt from " + ip + " (" + country + ")";
        System.out.println(log);
        try {
            File logDir = new File("../logs");
            if (!logDir.exists()) logDir.mkdirs();
            try (PrintWriter out = new PrintWriter(new FileWriter("../logs/ssh_honeypot_log.txt", true))) {
                out.println(log);
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Could not log attempt: " + e.getMessage());
        }
    }

    public static void sendEmailAlert(String ip, String time, String country) {
        final String username = "youremail@example.com"; // ✅ Replace with your email
        final String password = "your-app-password";     // ✅ Replace with app-specific password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(username, password); // ✅ correct type
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("receiver@example.com") // ✅ Replace with recipient
            );
            message.setSubject("🚨 SSH Honeypot Alert from " + ip);
            message.setText("SSH login attempt detected!\n\n"
                    + "IP: " + ip + "\n"
                    + "Country: " + country + "\n"
                    + "Time: " + time);

            Transport.send(message);
            System.out.println("[EMAIL] Alert sent.");
        } catch (MessagingException e) {
            System.err.println("[EMAIL ERROR] " + e.getMessage());
        }
    }

    public static void blockAttacker(String ip) {
        try {
            String cmd = "powershell.exe -Command \"New-NetFirewallRule -DisplayName 'Block_" + ip +
                         "' -Direction Inbound -RemoteAddress " + ip + " -Action Block\"";
            Runtime.getRuntime().exec(cmd);
            System.out.println("[DEFENSE] Blocked IP using Windows Firewall: " + ip);
        } catch (IOException e) {
            System.err.println("[DEFENSE ERROR] " + e.getMessage());
        }
    }
}
