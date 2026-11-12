package vn.edu.hcmute.config;

import jakarta.persistence.EntityManager;

public class TestJpaConnection {

    public static void main(String[] args) {
        try (EntityManager em = JpaConfig.getEntityManager()) {
            Long categoryCount = em.createQuery(
                "SELECT COUNT(c) FROM Category c", Long.class
            ).getSingleResult();
            System.out.println("JPA / SQL SERVER CONNECTION OK");
            System.out.println("Number of categories: " + categoryCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaConfig.close();
        }
    }
}
