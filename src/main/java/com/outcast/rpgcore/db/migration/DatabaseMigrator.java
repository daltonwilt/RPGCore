package com.outcast.rpgcore.db.migration;

import com.outcast.rpgcore.RPGCore;
import com.outcast.rpgcore.db.JPA;
import com.outcast.rpgcore.event.DatabaseMigrationEvent;
import org.bukkit.Bukkit;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

public class DatabaseMigrator {

    private JPA jpa;

    public DatabaseMigrator(JPA jpa) {
        this.jpa = jpa;
    }

    public void migrate() {
        RPGCore.info("Migrating database...");

        String vendor = jpa.HIBERNATE.get(JPA.URL_KEY).split(":")[1];

        // call database migration event
        DatabaseMigrationEvent event = new DatabaseMigrationEvent();
        Bukkit.getPluginManager().callEvent(event);

        event.getPluginIds().forEach(id -> {
            String location = String.format("classpath:db/migration/%s/%s", id, vendor);
            RPGCore.info("Migrating " + location);

            FluentConfiguration fc = new FluentConfiguration()
                    .dataSource(
                            jpa.HIBERNATE.get(JPA.URL_KEY),
                            jpa.HIBERNATE.get(JPA.USERNAME_KEY),
                            jpa.HIBERNATE.get(JPA.PASSWORD_KEY)
                    )
                    .schemas(id)
                    .table("flyway_schema_history_" + id)
                    .locations(location);

            new Flyway(fc).migrate();
        });

        RPGCore.info("Migrating finished...");
    }
}
