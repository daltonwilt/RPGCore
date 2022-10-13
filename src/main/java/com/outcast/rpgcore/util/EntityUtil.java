package com.outcast.rpgcore.util;

import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.EntityDamageSourceIndirect;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class EntityUtil {

    public static Optional<Entity> getEntity(UUID id) {
        return getEntity(id);
    }

    public static Optional<Player> playerAttackedEntity(EntityDamageSource source) {
        Entity root = getRootEntity(source);
        return root instanceof Player ? Optional.of((Player) root) : Optional.empty();
    }

    public static Entity getRootEntity(EntityDamageSource source) {
        if(source instanceof EntityDamageSourceIndirect) {
            EntityDamageSourceIndirect indirect = (EntityDamageSourceIndirect) source;
            return indirect.getProximateDamageSource();
        }
        return source.m();
    }

}
