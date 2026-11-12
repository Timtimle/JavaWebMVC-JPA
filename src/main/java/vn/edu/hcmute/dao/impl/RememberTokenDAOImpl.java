package vn.edu.hcmute.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.config.JpaConfig;
import vn.edu.hcmute.dao.RememberTokenDAO;
import vn.edu.hcmute.model.RememberToken;
import vn.edu.hcmute.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class RememberTokenDAOImpl implements RememberTokenDAO {

    @Override
    public boolean save(int userId, String tokenHash) throws SQLException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = JpaConfig.getEntityManager();
            tx = em.getTransaction();
            tx.begin();
            RememberToken token = new RememberToken();
            token.setUser(em.getReference(User.class, userId));
            token.setTokenHash(tokenHash);
            token.setExpiresAt(LocalDateTime.now().plusDays(30));
            em.persist(token);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            rollback(tx);
            throw new SQLException("Failed to save remember-me token", e);
        } finally {
            close(em);
        }
    }

    @Override
    public String findUsernameByTokenHash(String tokenHash) throws SQLException {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            return em.createQuery(
                "SELECT t.user.username FROM RememberToken t " +
                "WHERE t.tokenHash = :hash AND t.expiresAt > :now", String.class)
                .setParameter("hash", tokenHash)
                .setParameter("now", LocalDateTime.now())
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
        } catch (RuntimeException e) {
            throw new SQLException("Failed to read remember-me token", e);
        }
    }

    @Override
    public void deleteByTokenHash(String tokenHash) throws SQLException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = JpaConfig.getEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.createQuery("DELETE FROM RememberToken t WHERE t.tokenHash = :hash")
                .setParameter("hash", tokenHash)
                .executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            rollback(tx);
            throw new SQLException("Failed to revoke remember-me token", e);
        } finally {
            close(em);
        }
    }

    private static void rollback(EntityTransaction tx) {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
    }

    private static void close(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
