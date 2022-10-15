package com.outcast.rpgcore.event;

import jakarta.persistence.EntityManagerFactory;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RepositoryInitializedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private EntityManagerFactory entityManagerFactory;

    public RepositoryInitializedEvent(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static  HandlerList getHandlerList() {
        return handlers;
    }

}
