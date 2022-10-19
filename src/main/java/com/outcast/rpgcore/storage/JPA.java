package com.outcast.rpgcore.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.cfg.Environment;

import java.util.HashMap;
import java.util.Map;

public final class JPA {

    public static final String DRIVER_CLASS_KEY = Environment.CONNECTION_PREFIX + ".driver_class";

    public static final String URL_KEY = Environment.CONNECTION_PREFIX + ".url";

    public static final String USERNAME_KEY = Environment.CONNECTION_PREFIX + ".username";

    public static final String PASSWORD_KEY = Environment.CONNECTION_PREFIX + ".password";

    @JsonProperty("config")
    public Map<String,String> CONFIG = new HashMap<>();
    {
        CONFIG.put(DRIVER_CLASS_KEY, "com.mysql.jdbc.Driver");
        CONFIG.put(URL_KEY, "jdbc:mysql://54.39.221.101:3306/mc168429");
        CONFIG.put(USERNAME_KEY, "mc168429");
        CONFIG.put(PASSWORD_KEY, "298270d782");

        CONFIG.put(Environment.POOL_SIZE, "50");
        CONFIG.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        CONFIG.put(Environment.SHOW_SQL, "true");
        CONFIG.put(Environment.HBM2DDL_AUTO, "update");
    }

}
