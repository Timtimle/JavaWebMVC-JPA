package vn.edu.hcmute.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JpaConfig {

    private static volatile EntityManagerFactory factory;

    private JpaConfig() {
    }

    // Reuse one factory and create a separate EntityManager per DAO operation.
    public static EntityManager getEntityManager() {
        EntityManagerFactory current = factory;
        if (current == null || !current.isOpen()) {
            synchronized (JpaConfig.class) {
                if (factory == null || !factory.isOpen()) {
                    factory = Persistence.createEntityManagerFactory("javawebmvc-jpa");
                }
                current = factory;
            }
        }
        return current.createEntityManager();
    }

    public static synchronized void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
