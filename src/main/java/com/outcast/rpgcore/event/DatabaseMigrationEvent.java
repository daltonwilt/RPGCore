package com.outcast.rpgcore.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class DatabaseMigrationEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private List<String> pluginIds = new ArrayList<>();

    public DatabaseMigrationEvent() {}

    public void registerForMigration(String pluginId) {
        this.pluginIds.add(pluginId);
    }

    public List<String> getPluginIds() {
        return pluginIds;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static  HandlerList getHandlerList() {
        return handlers;
    }

}
