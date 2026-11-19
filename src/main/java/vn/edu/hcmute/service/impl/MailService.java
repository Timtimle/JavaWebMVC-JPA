package vn.edu.hcmute.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {
    // Configure credentials through environment variables, not source code.
    public boolean isConfigured() {
        return nonblank("JWM_SMTP_HOST") && nonblank("JWM_SMTP_USER") &&
               nonblank("JWM_SMTP_PASSWORD") && nonblank("JWM_SMTP_FROM") &&
               nonblank("JWM_OTP_SECRET");
    }

    private boolean nonblank(String key) {
        String value = System.getenv(key);
        return value != null && !value.isBlank();
    }

    public void sendOtp(String to, String purpose, String code) throws MessagingException {
        if (!isConfigured()) {
            throw new MessagingException("SMTP is not configured. Set JWM_SMTP_* environment variables.");
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", System.getenv("JWM_SMTP_HOST"));
        props.put("mail.smtp.port", System.getenv().getOrDefault("JWM_SMTP_PORT", "587"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(System.getenv("JWM_SMTP_USER"),
                        System.getenv("JWM_SMTP_PASSWORD"));
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(System.getenv("JWM_SMTP_FROM")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, true));
        message.setSubject("JavaWebMVC - " + ("ACTIVATE".equals(purpose) ?
                "Account activation code" :
                "CHANGE_EMAIL".equals(purpose) ? "Email change code" : "Password reset code"), "UTF-8");
        message.setText("Your verification code: " + code +
                "\nThis code expires in 10 minutes. Do not share it.", "UTF-8");
        Transport.send(message);
    }
}
