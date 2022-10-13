package com.outcast.rpgcore.physics;

import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class ProjectilePreset {

    public String name = "Projectile";
    public Vector size = new Vector(Projectile.DEFAULT_SIZE, Projectile.DEFAULT_SIZE, Projectile.DEFAULT_SIZE);
    public Particle particle = Particle.CRIT;
    public long lifetime = 0;

}