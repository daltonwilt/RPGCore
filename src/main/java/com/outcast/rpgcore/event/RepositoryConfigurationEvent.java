package com.outcast.rpgcore.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class RepositoryConfigurationEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private List<Class<?>> classes;

    public RepositoryConfigurationEvent(List<Class<?>> classes) {
        this.classes = classes;
    }

    public void registerEntity(Class<?> clazz) {
        classes.add(clazz);
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static  HandlerList getHandlerList() {
        return handlers;
    }

}
