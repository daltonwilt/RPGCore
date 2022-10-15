package com.outcast.rpgcore;

import com.outcast.rpgcore.combat.CombatLog;
import com.outcast.rpgcore.db.DatabaseService;
import com.outcast.rpgcore.event.RepositoryInitializedEvent;
import com.outcast.rpgcore.listener.RPGCoreListener;
import jakarta.persistence.EntityManagerFactory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public final class RPGCore extends JavaPlugin {

    private static RPGCore instance;
    private Level logLevel = Level.INFO;
    private CoreConfig coreConfig;
    private CombatLog combatLog;
    private DatabaseService databaseService;
    private Economy economy;

    //===========================================================================================================
    // Getter methods.
    //===========================================================================================================

    public static RPGCore getInstance() {
        return instance;
    }

    public static CoreConfig getCoreConfig() {
        return getInstance().coreConfig;
    }

    public static CombatLog getCombatLog() {
        return getInstance().combatLog;
    }

    public static DatabaseService getDatabaseService() {
        return getInstance().databaseService;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return getDatabaseService().getEntityManagerFactory();
    }

    public static Economy getEconomy() {
        return getInstance().economy;
    }

    //===========================================================================================================
    // Logging utilities methods.
    //===========================================================================================================

    public static void log(Level level, String message, Object... format) {
        log(instance, level, message, format);
    }

    public static void log(JavaPlugin plugin, Level level, String message, Object... format) {
        if (format.length > 0)
            message = String.format(message, format);
        plugin.getLogger().log(level, message);
    }

    public static void info(JavaPlugin plugin, String message, Object... format) {
        log(plugin, Level.INFO, message, format);
    }

    public static void info(String message, Object... format) {
        info(instance, message, format);
    }

    public static void warn(JavaPlugin plugin, String message, Object... format) {
        log(plugin, Level.WARNING, message, format);
    }

    public static void warn(String message, Object... format) {
        warn(instance, message, format);
    }

    public static void severe(JavaPlugin plugin, String message, Object... format) {
        log(plugin, Level.SEVERE, message, format);
    }

    public static void severe(String message, Object... format) {
        severe(instance, message, format);
    }

    private void printBlank() { info(""); }
    private void printDivider() {
        info("============================================================================================");
    }

    private void setupEconomy() {
        boolean setup = true;

        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            severe("[%s] - Disabled due to no Vault dependency found!", getDescription().getName());
            getServer().getPluginManager().disablePlugin(this);
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) {
            setup = false;
        }

        assert rsp != null;
        economy = rsp.getProvider();
        if(!setup) {
            severe("[%s] - Disabled due to no Vault dependency found!", getDescription().getName());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    //===========================================================================================================
    // onEnable onDisable methods.
    //===========================================================================================================

    @Override
    public void onEnable() {
        // Setting logger level and plugin instance
        instance = this;

        printDivider();
        printBlank();
        info("  RPGCore v%s", getDescription().getVersion());
        info("  You're running on %s.", getServer().getVersion());
        printDivider();

        // Initialize Configuration settings
        try {
            coreConfig = new CoreConfig();
            coreConfig.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load Database settings
        if(coreConfig.DB_ENABLED) {
            databaseService = new DatabaseService(coreConfig.JPA_CONFIG);
        }

        // Initialize combat logging data
        this.combatLog = new CombatLog();
        combatLog.init();

        //post Repository Initialized event
        Bukkit.getPluginManager().callEvent(new RepositoryInitializedEvent(databaseService.getEntityManagerFactory()));

        // Check if vault dependency exists and register Economy
        setupEconomy();

        // Register Events (Listeners)
        getServer().getPluginManager().registerEvents((Listener)new RPGCoreListener(), (Plugin)this);

        // Register Menu Manager
    }

    @Override
    public void onDisable() {
        // close database service
        if(coreConfig.DB_ENABLED) {
            databaseService.close();
        }

        info("RPGCore v%s is being disabled.", getDescription().getVersion());
    }

}