package com.outcast.rpgcore.util;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InventoryUtil {

    public static final List<EquipmentSlot> EQUIPMENT_SLOTS = Arrays.asList(
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET,
            EquipmentSlot.HAND,
            EquipmentSlot.OFF_HAND
    );

    //===========================================================================================================
    // Utility methods for retrieving equipment on an entity.
    //===========================================================================================================

    public static Optional<List<ItemStack>> getEquippedItems(Entity entity) {
        if(!(entity instanceof EntityEquipment))
            return Optional.empty();

        EntityEquipment equipment = (EntityEquipment) entity;

        List<ItemStack> equipped = new ArrayList<>();

        EQUIPMENT_SLOTS.forEach((type) -> equipped.add(equipment.getItem(type)));

        return Optional.of(equipped);
    }

    public static Optional<ItemStack> getMainHand(Entity entity) {
        return getEquipment(entity, EquipmentSlot.HAND);
    }

    public static Optional<ItemStack> getOffHand(Entity entity) {
        return getEquipment(entity, EquipmentSlot.OFF_HAND);
    }

    public static Optional<ItemStack> getHead(Entity entity) {
        return getEquipment(entity, EquipmentSlot.HEAD);
    }

    public static Optional<ItemStack> getChest(Entity entity) {
        return getEquipment(entity, EquipmentSlot.CHEST);
    }

    public static Optional<ItemStack> getLegs(Entity entity) {
        return getEquipment(entity, EquipmentSlot.LEGS);
    }

    public static Optional<ItemStack> getBoots(Entity entity) {
        return getEquipment(entity, EquipmentSlot.FEET);
    }

    public static Optional<ItemStack> getEquipment(Entity entity, EquipmentSlot type) {
        if(!(entity instanceof EntityEquipment))
            return Optional.empty();

        EntityEquipment equipment = (EntityEquipment) entity;
        return Optional.of(equipment.getItem(type));
    }

}