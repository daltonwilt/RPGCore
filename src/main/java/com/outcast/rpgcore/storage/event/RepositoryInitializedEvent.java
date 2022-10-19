package com.outcast.rpgcore.storage.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.persistence.EntityManagerFactory;

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
