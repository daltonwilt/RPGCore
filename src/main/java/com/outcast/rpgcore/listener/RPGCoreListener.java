package com.outcast.rpgcore.listener;

import com.outcast.rpgcore.RPGCore;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RPGCoreListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity) || !(event instanceof EntityDamageByEntityEvent edbe))
            return;

        if((edbe.getEntity() instanceof Player victim) && (edbe.getDamager() instanceof Player attacker))
            RPGCore.getCombatLog().initiateCombat(attacker, victim);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(RPGCore.getCombatLog().isCombat(player))
            player.setHealth(0);
    }

}
