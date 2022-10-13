package com.outcast.rpgcore.command;

import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandService {

    @SafeVarargs
    public static void createCommand(JavaPlugin plugin, String name, String description,
                                     String usage, List<String> aliases,
                                     Class<? extends CommandBuilder>... subcommands)
            throws NoSuchFieldException, IllegalAccessException {

        List<CommandBuilder> commands = new ArrayList<>();

        Arrays.stream(subcommands).map(subcommand -> {
            try {
                Constructor<? extends CommandBuilder> constructor = subcommand.getConstructor();
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }).forEach(commands::add);

        Field field = plugin.getServer().getClass().getDeclaredField("commandMap");
        field.setAccessible(true);
        CommandMap commandMap = (CommandMap) field.get(plugin.getServer());
        commandMap.register(name, new CoreCommand(name, description, usage, aliases, commands));
    }

    @SafeVarargs
    public static void createCommand(JavaPlugin plugin, String name, String description,
                                     String usage,
                                     Class<? extends CommandBuilder>... subcommands)
            throws NoSuchFieldException, IllegalAccessException {
        createCommand(plugin, name, description, usage, Collections.singletonList(""), subcommands);
    }

}
