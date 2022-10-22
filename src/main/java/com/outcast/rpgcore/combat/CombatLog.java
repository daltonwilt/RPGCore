package com.outcast.rpgcore.combat;

import com.outcast.rpgcore.RPGCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

// Make this to where it houses only one instance of player entry instead of player and victim

public class CombatLog {

    private static class Entry {

        private UUID attacker;

        private UUID victim;

        private Long initiationTime;

        public Entry(UUID attacker, UUID victim, Long initiationTime) {
            this.attacker = attacker;
            this.victim = victim;
            this.initiationTime = initiationTime;
        }

        public UUID getAttacker() {
            return attacker;
        }

        public void setAttacker(UUID attacker) {
            this.attacker = attacker;
        }

        public UUID getVictim() {
            return victim;
        }

        public void setVictim(UUID victim) {
            this.victim = victim;
        }

        public Long getInitiationTime() {
            return initiationTime;
        }

        public void setInitiationTime(Long initiationTime) {
            this.initiationTime = initiationTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Objects.equals(getAttacker(), entry.getAttacker()) &&
                    Objects.equals(getVictim(), entry.getVictim()) &&
                    Objects.equals(getInitiationTime(), entry.getInitiationTime());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getAttacker(), getVictim(), getInitiationTime());
        }
    }

    private Set<Entry> log = new HashSet<>();

    public void init() {
        System.out.println("Combat Time : " + RPGCore.getCoreConfig().COMBAT_LIMIT.toMillis());
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentMillis = System.currentTimeMillis();
                // remove all combat mappings whose duration has expired
                for(Entry e : log) {
                    if((currentMillis - e.getInitiationTime()) >= RPGCore.getCoreConfig().COMBAT_LIMIT.toMillis()) {
                        endCombat(Bukkit.getPlayer(e.getAttacker()), Bukkit.getPlayer(e.getVictim()));
                    }
                }
//                log.removeIf(entry -> (currentMillis - entry.getInitiationTime()) >= RPGCore.getCoreConfig().COMBAT_LIMIT.toMillis());
            }
        }.runTaskTimerAsynchronously(RPGCore.getInstance(), 0, 20L);
    }

    /**
     * If these 2 players are currently engaged in combat, this method
     * will provide the player who initiated the combat by first damaging
     * the other.
     *
     * @param playerA Player A
     * @param playerB Player B
     * @return The attacking player, if any
     */
    public Optional<Player> fetchAttacker(Player playerA, Player playerB) {
        return log.stream()
                .filter(entry ->
                        (entry.getAttacker().equals(playerA.getUniqueId()) && entry.getVictim().equals(playerB.getUniqueId())) ||
                                (entry.getAttacker().equals(playerB.getUniqueId()) && entry.getVictim().equals(playerA.getUniqueId()))
                )
                .map(entry -> {
                    if (entry.getAttacker().equals(playerA.getUniqueId())) {
                        return playerA;
                    } else {
                        return playerB;
                    }
                })
                .findAny();
    }

    /**
     * Inverse method of {@link #fetchAttacker(Player, Player)}
     *
     * @param playerA Player A
     * @param playerB Player B
     * @return The attacking player, if any
     */
    public Optional<Player> fetchVictim(Player playerA, Player playerB) {
        return fetchAttacker(playerA, playerB).map(attacker -> {
            if (attacker.getUniqueId().equals(playerA.getUniqueId())) {
                return playerB;
            } else {
                return playerA;
            }
        });
    }

    /**
     * If the player has an entry grab the latest timestamp
     * of initiated combat, if any
     *
     * @param player player we are checking for entry
     * @return that latest entry time stamp, if any
     */
    public Optional<Long> fetchLatestCombatLogTimestamp(Player player) {
        return log.stream()
                .filter(entry -> entry.getAttacker().equals(player.getUniqueId()) || entry.getVictim().equals(player.getUniqueId()))
                .map(Entry::getInitiationTime)
                .sorted()
                .findFirst();
    }

    /**
     * We want to create two entries one for the attack and one for the victim for combat log information
     * this way we can track time stamps for the attacker and victim along with being able to update
     * the entries with new victims and time stamps
     *
     * @param attacker PlayerA
     * @param victim PlayerB
     */
    public void initiateCombat(Player attacker, Player victim) {

        // Grab entry of the attacker
        Entry eAttacker = log.stream().filter(entry -> entry.getAttacker().equals(attacker.getUniqueId())).findFirst().orElse(null);

        // If the attacker already has an entry then we will update the time stamp and new victim
        if(eAttacker != null) {
            updateEntry(attacker, victim);
        } else {
            log.add(new Entry(attacker.getUniqueId(), victim.getUniqueId(), System.currentTimeMillis()));
            attacker.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            "&8[&4 Enter Combat&8 ]"
                    )
            );
        }

        // Grab entry of the victim ( as attacker )
        Entry eVictim = log.stream().filter(entry -> entry.getAttacker().equals(victim.getUniqueId())).findFirst().orElse(null);

        // If the victim already has an entry then we will update the time stamp and the victim ( with the attacker )
        // if no entry, create entry for victim ( as attacker )
        if(eVictim != null) {
            updateEntry(victim, attacker);
        } else {
            log.add(new Entry(victim.getUniqueId(), attacker.getUniqueId(), System.currentTimeMillis()));
            victim.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            "&8[&4 Enter Combat&8 ]"
                    )
            );
        }
    }

    // update the entry's time stamp and victim
    public void updateEntry(Player playerA, Player playerB) {
        log.stream()
                .filter(entry -> entry.getAttacker().equals(playerA.getUniqueId())).findAny().ifPresent(e -> {
                    e.setInitiationTime(System.currentTimeMillis());
                    e.setVictim(playerB.getUniqueId());
                });

    }

    //
    public void endCombat(Player playerA, Player playerB) {
        log.removeIf(entry -> entry.getAttacker().equals(playerA.getUniqueId()) && entry.getVictim().equals(playerB.getUniqueId()));
        playerA.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&',
                        "&8[&2 Exit Combat&8 ]"
                )
        );
    }

    public boolean isCombat(Player player) {
        return log.stream().anyMatch(entry -> entry.getAttacker().equals(player.getUniqueId()) || entry.getVictim().equals(player.getUniqueId()));
    }

}