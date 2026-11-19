package vn.edu.hcmute.service.impl;

import jakarta.mail.MessagingException;
import jakarta.persistence.*;
import vn.edu.hcmute.config.JpaConfig;
import vn.edu.hcmute.model.PendingEmailChange;
import vn.edu.hcmute.model.User;
import vn.edu.hcmute.util.FormValidation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

public class ProfileService {
    private static final SecureRandom RNG = new SecureRandom();
    private final MailService mail = new MailService();

    public User find(int id) {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            return em.find(User.class, id);
        }
    }

    public User update(int id, String fullName, String phone, String avatar) {
        EntityTransaction tx = null;
        try (EntityManager em = JpaConfig.getEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            User user = em.find(User.class, id);
            if (user == null) throw new IllegalArgumentException("Account not found.");
            user.setFullName(fullName);
            user.setPhone(phone);
            if (avatar != null) user.setAvatar(avatar);
            tx.commit();
            return user;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public String pendingEmail(int userId) {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            PendingEmailChange change = em.find(PendingEmailChange.class, userId);
            return change == null ? null : change.getNewEmail();
        }
    }

    public void requestEmailChange(int userId, String email) throws MessagingException {
        if (!FormValidation.email(email)) throw new IllegalArgumentException("Invalid email address.");
        if (!mail.isConfigured()) throw new MessagingException("SMTP is not configured.");
        email = email.trim().toLowerCase();
        String code = String.format("%06d", RNG.nextInt(1_000_000));
        EntityTransaction tx = null;
        try (EntityManager em = JpaConfig.getEntityManager()) {
            tx = em.getTransaction(); tx.begin();
            User user = em.find(User.class, userId);
            if (user == null) throw new IllegalArgumentException("Account not found.");
            if (email.equalsIgnoreCase(user.getEmail())) {
                throw new IllegalArgumentException("The new email must be different.");
            }
            if (isEmailTaken(em, email, userId)) {
                throw new IllegalArgumentException("Email address is already in use.");
            }
            PendingEmailChange pending = em.find(PendingEmailChange.class, userId, LockModeType.PESSIMISTIC_WRITE);
            LocalDateTime now = LocalDateTime.now();
            if (pending == null) {
                pending = new PendingEmailChange(); pending.setUserId(userId);
            } else if (pending.getCreatedAt() != null && pending.getCreatedAt().plusSeconds(60).isAfter(now)) {
                throw new IllegalStateException("Please wait 60 seconds before requesting another code.");
            }
            pending.setNewEmail(email);
            pending.setCodeHash(hash(userId, email, code));
            pending.setCreatedAt(now);
            pending.setExpiresAt(now.plusMinutes(10));
            pending.setAttempts(0);
            if (!em.contains(pending)) em.persist(pending);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw ex;
        }
        mail.sendOtp(email, "CHANGE_EMAIL", code);
    }

    public boolean confirmEmailChange(int userId, String code) {
        if (code == null || !code.matches("[0-9]{6}")) return false;
        EntityTransaction tx = null;
        try (EntityManager em = JpaConfig.getEntityManager()) {
            tx = em.getTransaction(); tx.begin();
            PendingEmailChange pending = em.find(PendingEmailChange.class, userId, LockModeType.PESSIMISTIC_WRITE);
            if (pending == null) { tx.commit(); return false; }
            if (pending.getExpiresAt().isBefore(LocalDateTime.now()) || pending.getAttempts() >= 5) {
                em.remove(pending); tx.commit(); return false;
            }
            String digest = hash(userId, pending.getNewEmail(), code);
            if (!MessageDigest.isEqual(digest.getBytes(StandardCharsets.US_ASCII),
                    pending.getCodeHash().getBytes(StandardCharsets.US_ASCII))) {
                pending.setAttempts(pending.getAttempts() + 1); tx.commit(); return false;
            }
            if (isEmailTaken(em, pending.getNewEmail(), userId)) {
                em.remove(pending); tx.commit(); return false;
            }
            User user = em.find(User.class, userId);
            if (user == null) { tx.rollback(); return false; }
            user.setEmail(pending.getNewEmail());
            em.remove(pending);
            tx.commit();
            return true;
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw ex;
        }
    }

    private static boolean isEmailTaken(EntityManager em, String email, int currentUser) {
        List<Integer> ids = em.createQuery(
                "SELECT u.id FROM User u WHERE LOWER(u.email)=:email AND u.id<>:id", Integer.class)
                .setParameter("email", email.toLowerCase()).setParameter("id", currentUser)
                .setMaxResults(1).getResultList();
        return !ids.isEmpty();
    }
    private static String hash(int id, String email, String code) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            String secret = System.getenv("JWM_OTP_SECRET");
            if (secret == null || secret.isBlank()) throw new IllegalStateException("OTP secret is missing.");
            return HexFormat.of().formatHex(sha.digest(
                    (secret + ":EMAIL:" + id + ":" + email + ":" + code).getBytes(StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
