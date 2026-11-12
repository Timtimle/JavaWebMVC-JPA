package vn.edu.hcmute.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.config.JpaConfig;
import vn.edu.hcmute.dao.UserDAO;
import vn.edu.hcmute.model.User;

import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public User getByUsername(String username) throws SQLException {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            return em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
        } catch (RuntimeException e) {
            throw new SQLException("Failed to find user", e);
        }
    }

    @Override
    public boolean existsByUsername(String username) throws SQLException {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            Long count = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
            return count > 0;
        } catch (RuntimeException e) {
            throw new SQLException("Failed to check username", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            Long count = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
            return count > 0;
        } catch (RuntimeException e) {
            throw new SQLException("Failed to check email", e);
        }
    }

    @Override
    public boolean insert(User user) throws SQLException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = JpaConfig.getEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.persist(user);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new SQLException("Failed to register user", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
