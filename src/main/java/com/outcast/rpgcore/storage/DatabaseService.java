package com.outcast.rpgcore.storage;

import com.outcast.rpgcore.RPGCore;
import com.outcast.rpgcore.storage.event.RepositoryConfigurationEvent;

import org.bukkit.Bukkit;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManagerFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DatabaseService implements AutoCloseable {

    private final JPA jpa;

    private EntityManagerFactory entityManagerFactory;

    public DatabaseService(JPA jpa) {
        this.jpa = jpa;
        createEntityManagerFactory();
        RPGCore.info("Enabled database...");
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    private void createEntityManagerFactory() {
        MetadataSources metadataSources = new MetadataSources(configureServiceRegistry(jpa));

        addClasses(metadataSources);

        entityManagerFactory = (EntityManagerFactory) metadataSources.buildMetadata()
                .getSessionFactoryBuilder()
                .build();
    }

    private ServiceRegistry configureServiceRegistry(JPA jpa) {
        return new StandardServiceRegistryBuilder()
                .applySettings(getProperties(jpa))
                .build();
    }

    private Properties getProperties(JPA jpa) {
        Properties properties = new Properties();
        jpa.CONFIG.forEach(properties::setProperty);
        return properties;
    }

    private void addClasses(MetadataSources metadataSources) {
        List<Class<?>> classes = new LinkedList<>();

        RepositoryConfigurationEvent event = new RepositoryConfigurationEvent(classes);
        Bukkit.getPluginManager().callEvent(event);

        classes.forEach(metadataSources::addAnnotatedClass);
    }

    @Override
    public void close() {
        if (entityManagerFactory != null ) {
            entityManagerFactory.close();
        }
    }

}
