package vn.edu.hcmute.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class JpaLifecycleListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Release Hibernate resources when Tomcat undeploys this WAR.
        JpaConfig.close();
    }
}
