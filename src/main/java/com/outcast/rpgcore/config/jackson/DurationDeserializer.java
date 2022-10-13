package com.outcast.rpgcore.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.outcast.rpgcore.RPGCore;
import com.outcast.rpgcore.util.math.TimeUtil;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DurationDeserializer extends StdDeserializer<Duration> {

    private static final Duration DEFAULT_DURATION = Duration.of(30, ChronoUnit.SECONDS);

    public DurationDeserializer() {
        super(Duration.class);
    }

    @Override
    public Duration deserialize(JsonParser jsonparser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ObjectCodec codec = jsonparser.getCodec();
        JsonNode root = codec.readTree(jsonparser);
        String name = root.asText();

        try {
            return TimeUtil.stringToDuration(name);
        } catch (IllegalArgumentException e) {
            RPGCore.warn("Couldn't parse a duration type. Using '%s'.", DEFAULT_DURATION);
        }

        return DEFAULT_DURATION;
    }

}
