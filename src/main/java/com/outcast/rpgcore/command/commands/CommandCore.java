package com.outcast.rpgcore.command.commands;

import com.outcast.rpgcore.command.CommandBuilder;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public final class CommandCore extends CommandBuilder {

    public CommandCore() {
        super();
        this.addAlias("core");
        this.setDescription("Testing command service in RPGCore.");
        this.setName("core");
        this.setPermission("rpgcore.core");
        this.setSyntax("/core <args>");
        this.setArguments(new ArrayList<>(Arrays.asList("core1", "core2")));
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        try {
            sender.sendMessage("Testing command service!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
