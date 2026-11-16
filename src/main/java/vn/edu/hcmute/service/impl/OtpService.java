package vn.edu.hcmute.service.impl;

import jakarta.mail.MessagingException;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmute.config.JpaConfig;
import vn.edu.hcmute.model.EmailOtp;
import vn.edu.hcmute.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

public class OtpService {
    public static final String ACTIVATE = "ACTIVATE";
    public static final String RESET = "RESET";
    private static final SecureRandom RNG = new SecureRandom();
    private final MailService mail = new MailService();

    public void send(String email, String purpose) throws MessagingException {
        if (!ACTIVATE.equals(purpose) && !RESET.equals(purpose)) {
            throw new IllegalArgumentException("Invalid OTP purpose");
        }
        if (email == null || email.isBlank()) return;
        if (!mail.isConfigured()) {
            throw new MessagingException("SMTP is not configured. See README.md");
        }

        String code = String.format("%06d", RNG.nextInt(1_000_000));
        String destination = null;

        try (EntityManager em = JpaConfig.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                List<User> users = em.createQuery(
                                "SELECT u FROM User u WHERE LOWER(u.email) = :email", User.class)
                        .setParameter("email", email.trim().toLowerCase())
                        .setMaxResults(1).getResultList();
                if (users.isEmpty()) {
                    tx.commit();
                    return;
                }
                User user = users.get(0);
                if ((ACTIVATE.equals(purpose) && user.isActive()) ||
                        (RESET.equals(purpose) && !user.isActive())) {
                    tx.commit();
                    return;
                }

                List<EmailOtp> old = em.createQuery(
                                "SELECT o FROM EmailOtp o WHERE o.user.id = :uid AND o.purpose = :p",
                                EmailOtp.class)
                        .setParameter("uid", user.getId())
                        .setParameter("p", purpose).getResultList();
                LocalDateTime now = LocalDateTime.now();
                EmailOtp otp;
                if (old.isEmpty()) {
                    otp = new EmailOtp();
                    otp.setUser(user);
                    otp.setPurpose(purpose);
                } else {
                    otp = old.get(0);
                    if (otp.getCreatedAt() != null && otp.getCreatedAt().plusSeconds(60).isAfter(now)) {
                        throw new IllegalStateException(
                                "Please wait 60 seconds before requesting another code.");
                    }
                }

                // Set all NOT NULL fields before persist(). IDENTITY may INSERT immediately.
                otp.setCodeHash(hash(user.getId(), purpose, code));
                otp.setCreatedAt(now);
                otp.setExpiresAt(now.plusMinutes(10));
                otp.setAttempts(0);
                if (old.isEmpty()) em.persist(otp);

                tx.commit();
                destination = user.getEmail();
            } catch (RuntimeException ex) {
                rollbackSafely(em, tx, ex);
                throw ex;
            }
        }

        // Send after committing and closing the persistence context.
        if (destination != null) mail.sendOtp(destination, purpose, code);
    }

    // Verify atomically; revoke persistent-login tokens on password reset.
    public boolean verify(String email, String purpose, String code, String newPassword) {
        if (email == null || code == null || !code.matches("[0-9]{6}")) return false;

        try (EntityManager em = JpaConfig.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                List<EmailOtp> matches = em.createQuery(
                                "SELECT o FROM EmailOtp o JOIN FETCH o.user WHERE LOWER(o.user.email) = :email AND o.purpose = :purpose",
                                EmailOtp.class)
                        .setParameter("email", email.trim().toLowerCase())
                        .setParameter("purpose", purpose)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .setMaxResults(1).getResultList();
                if (matches.isEmpty()) {
                    tx.commit();
                    return false;
                }
                EmailOtp otp = matches.get(0);
                if (otp.getExpiresAt().isBefore(LocalDateTime.now()) || otp.getAttempts() >= 5) {
                    em.remove(otp);
                    tx.commit();
                    return false;
                }
                String candidate = hash(otp.getUser().getId(), purpose, code);
                if (!MessageDigest.isEqual(candidate.getBytes(StandardCharsets.US_ASCII),
                        otp.getCodeHash().getBytes(StandardCharsets.US_ASCII))) {
                    otp.setAttempts(otp.getAttempts() + 1);
                    tx.commit();
                    return false;
                }

                User user = otp.getUser();
                if (ACTIVATE.equals(purpose)) {
                    user.setActive(true);
                } else if (RESET.equals(purpose)) {
                    if (newPassword == null || newPassword.length() < 8) {
                        tx.rollback();
                        return false;
                    }
                    user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
                    em.createQuery("DELETE FROM RememberToken t WHERE t.user.id = :id")
                            .setParameter("id", user.getId()).executeUpdate();
                } else {
                    tx.rollback();
                    return false;
                }
                em.remove(otp);
                tx.commit();
                return true;
            } catch (RuntimeException ex) {
                rollbackSafely(em, tx, ex);
                throw ex;
            }
        }
    }

    // Preserve the original database exception if rollback also fails.
    private static void rollbackSafely(EntityManager em, EntityTransaction tx, RuntimeException original) {
        if (!em.isOpen()) return;
        try {
            if (tx != null && tx.isActive()) tx.rollback();
        } catch (RuntimeException rollbackError) {
            original.addSuppressed(rollbackError);
        }
    }

    private String hash(int id, String purpose, String otp) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            String secret = System.getenv().getOrDefault("JWM_OTP_SECRET", "");
            byte[] digest = sha.digest((secret + ":" + id + ":" + purpose + ":" + otp)
                    .getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
