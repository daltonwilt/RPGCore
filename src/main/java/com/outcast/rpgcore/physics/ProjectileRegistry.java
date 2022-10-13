package com.outcast.rpgcore.physics;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

public final class ProjectileRegistry {

    private static int maxProjectiles = 1000;

    private static HashMap<String, ProjectilePreset> projectilePresets = new HashMap<>();

    private static LinkedHashSet<Projectile> projectiles = new LinkedHashSet<>();

    public static Projectile fromPreset(String presetName, List<Location> locations) {
        ProjectilePreset preset = projectilePresets.get(presetName);
        if (preset != null)
            return new Projectile(locations) {
                @Override
                protected void init() {
                    setLifetime(preset.lifetime);
                    setSize(preset.size);
                    super.init();
                }
            };
        return null;
    }

    public static ProjectilePreset fromPresetName(String presetName) {
        return projectilePresets.get(presetName);
    }

    public static void registerProjectile(Projectile proj) {
        projectiles.add(proj);
        if (projectiles.size() >= maxProjectiles) {
            Projectile oldest = projectiles.iterator().next();
            oldest.destroy();
        }
    }

    public static boolean removeProjectile(Projectile proj) {
        return projectiles.remove(proj);
    }

    public static void destroyAll() {
        Projectile[] proj = new Projectile[projectiles.size()];
        projectiles.toArray(proj);
        for (Projectile p : proj)
            p.destroy();
    }

}