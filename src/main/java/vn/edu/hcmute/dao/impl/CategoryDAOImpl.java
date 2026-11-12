package vn.edu.hcmute.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.config.JpaConfig;
import vn.edu.hcmute.dao.CategoryDAO;
import vn.edu.hcmute.model.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public boolean insert(Category category) throws SQLException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = JpaConfig.getEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.persist(category);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            rollback(tx);
            throw new SQLException("Failed to insert category", e);
        } finally {
            close(em);
        }
    }

    @Override
    public boolean update(Category category) throws SQLException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = JpaConfig.getEntityManager();
            tx = em.getTransaction();
            tx.begin();
            if (em.find(Category.class, category.getId()) == null) {
                tx.rollback();
                return false;
            }
            em.merge(category);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            rollback(tx);
            throw new SQLException("Failed to update category", e);
        } finally {
            close(em);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = JpaConfig.getEntityManager();
            tx = em.getTransaction();
            tx.begin();
            Category category = em.find(Category.class, id);
            if (category == null) {
                tx.rollback();
                return false;
            }
            em.remove(category);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            rollback(tx);
            throw new SQLException("Failed to delete category", e);
        } finally {
            close(em);
        }
    }

    @Override
    public Category getById(int id) throws SQLException {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            return em.find(Category.class, id);
        } catch (RuntimeException e) {
            throw new SQLException("Failed to find category", e);
        }
    }

    @Override
    public Category getByName(String name) throws SQLException {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM Category c WHERE c.name = :name", Category.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
        } catch (RuntimeException e) {
            throw new SQLException("Failed to find category by name", e);
        }
    }

    @Override
    public List<Category> getAll() throws SQLException {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            return em.createNamedQuery("Category.findAll", Category.class)
                .getResultList();
        } catch (RuntimeException e) {
            throw new SQLException("Failed to list categories", e);
        }
    }

    @Override
    public List<Category> search(String keyword) throws SQLException {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM Category c WHERE c.name LIKE :keyword ORDER BY c.id",
                Category.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
        } catch (RuntimeException e) {
            throw new SQLException("Failed to search categories", e);
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
