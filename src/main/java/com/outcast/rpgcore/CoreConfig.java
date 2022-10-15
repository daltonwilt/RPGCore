package com.outcast.rpgcore;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.outcast.rpgcore.config.ConfigService;
import com.outcast.rpgcore.config.jackson.DurationDeserializer;
import com.outcast.rpgcore.config.jackson.DurationSerializer;
import com.outcast.rpgcore.db.JPA;


import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class CoreConfig extends ConfigService {

    @JsonProperty("combat-limit")
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    public Duration COMBAT_LIMIT = Duration.of(5, ChronoUnit.SECONDS);

    @JsonProperty("db-enabled")
    public boolean DB_ENABLED = true;

    @JsonProperty("jpa")
    public JPA JPA_CONFIG = new JPA();

    public CoreConfig() throws IOException {
        super("config/rpgcore", "config.json", FileType.JSON);
    }

}
