package com.outcast.rpgcore;

import com.outcast.rpgcore.combat.CombatLog;
import com.outcast.rpgcore.command.CommandService;
import com.outcast.rpgcore.command.commands.CommandCore;
import com.outcast.rpgcore.db.DatabaseService;
import com.outcast.rpgcore.listener.RPGCoreListener;
import net.milkbowl.vault.economy.Economy;
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

        // Register Command Manager
        try {
            CommandService.createCommand(this, "core", "Command prefix for RPGCore.", "/core", CommandCore.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Check if vault dependency exists and register Economy
        setupEconomy();

        // Initialize Configuration settings
        try {
            coreConfig = new CoreConfig();
            coreConfig.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load Database settings
        DatabaseService connection = new DatabaseService();
        connection.init();

        // Register Events (Listeners)
        getServer().getPluginManager().registerEvents((Listener)new RPGCoreListener(), (Plugin)this);

        // Register Menu Manager

        // Initialize combat logging data
        this.combatLog = new CombatLog();
        combatLog.init();
    }

    @Override
    public void onDisable() {
        info("RPGCore v%s is being disabled.", getDescription().getVersion());
    }

}