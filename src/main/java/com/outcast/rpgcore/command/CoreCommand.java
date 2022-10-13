package com.outcast.rpgcore.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CoreCommand extends Command {

    private final List<CommandBuilder> commands;

    public CoreCommand(String name, String description, String usageMessage, List<String> aliases, List<CommandBuilder> commands) {
        super(name, description, usageMessage, aliases);

        this.commands = commands;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if(args.length > 0) {
            // /<command> <args>
            for (CommandBuilder cb : commands) {
                if (args[0].equalsIgnoreCase(cb.getName()) || cb.getAliases().contains(args[0])) {
                    if (!sender.hasPermission(cb.getPermission())) {
                        sender.sendMessage(
                                ChatColor.translateAlternateColorCodes(
                                        '&',
                                        "&8[&6Command&8] &7You don't have permission to use this command."
                                ));
                        return false;
                    }
                    try {
                        cb.execute(sender, args);
                    } catch (Exception e) {
                        sender.sendMessage(
                                ChatColor.translateAlternateColorCodes(
                                        '&',
                                        "&8[&6Command&8] &cIncorrect usage. &6( &2" + cb.getSyntax() + "&6 )"
                                ));
                    }
                    return true;
                } else {
                    // This command hasn't been built and/or registered.
                    sender.sendMessage(
                            ChatColor.translateAlternateColorCodes(
                                    '&',
                                    "&8[&6Command&8] &7Unknown command."
                            ));
                    return false;
                }
            }
        } else {
            // main command list view of sub commands
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            "&8-----------------------------------------------------"
                    ));
            for(CommandBuilder cb : commands) {
                sender.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                                '&',
                                "&6" + cb.getSyntax()
                        ) +
                                ChatColor.translateAlternateColorCodes(
                                        '&',
                                        " &7- "
                                ) +
                                ChatColor.translateAlternateColorCodes(
                                        '&',
                                        "&7" + cb.getDescription()
                                )
                );
            }
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            "&8-----------------------------------------------------"
                    ));
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args)
            throws IllegalArgumentException {
        if(args.length == 1) {
            // <command> <subcommands> <args>
            List<String> commandArguments = new ArrayList<>();

            // Does the command autocomplete
            for(CommandBuilder cb : commands) {
                commandArguments.add(cb.getName());
            }

            return commandArguments;
        } else if(args.length == 2) {
            for(CommandBuilder cb : commands) {
                if(args[0].equalsIgnoreCase(cb.getName()) || cb.getAliases().contains(args[0])){
                    if(cb.getArguments() != null)
                        return cb.getArguments();

                    return Collections.emptyList();
                }
            }
        }
        return Collections.emptyList();
    }

}