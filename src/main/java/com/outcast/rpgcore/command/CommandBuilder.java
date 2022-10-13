package com.outcast.rpgcore.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandBuilder {

    private List<String> aliases = new ArrayList<String>();
    private List<String> arguments = new ArrayList<>();
    private String description;
    private String name;
    private String permission;
    private String syntax;

    public CommandBuilder() {}

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public void addAlias(String aliases) {
        this.aliases.add(aliases);
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public void addArguments(String argument) {
        this.arguments.add(argument);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public abstract boolean execute(CommandSender sender, String[] args) throws Exception;

}
