package com.outcast.rpgcore.physics;

import com.outcast.rpgcore.util.math.geometry.Vectors;
import com.outcast.rpgcore.RPGCore;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Projectile {

    public static double DEFAULT_SIZE = 0.5;
    private boolean active;
    protected ArmorStand currentAS;
    protected int maxEntities;
    protected LivingEntity owner;
    protected List<Location> locations;
    protected Location location;
    protected BoundingBox hitbox;
    protected Vector velocity = Vectors.zero();

    /**
     * How many sections to divide each location update into.
     * If this is 5 and the projectile's velocity is 10 meters per tick
     * in the positive X direction, all on-tick functions will be called
     * every 10/5 = 2 meters.
     */
    protected int interpolationScale = 1;
    protected long expireTime = 0;
    protected double iterate = 1;
    protected Set<Block> hitBlocks = new HashSet<>();
    protected Set<Entity> hitEntities = new HashSet<>();

    public Projectile(List<Location> locations) {
        this.locations = locations;
        this.location = locations.get(0);
        active = true;
        maxEntities = 1;
        hitbox = BoundingBox.of(location, DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE);
        ProjectileRegistry.registerProjectile(this);
        init();
    }

    public final void destroy() {
        active = false;
    }

    public final LivingEntity getOwner() {
        return owner;
    }

    public final void setOwner(LivingEntity newOwner) {
        owner = newOwner;
    }

    public final boolean hasOwner() {
        return owner != null;
    }

    public final ArmorStand getAS() { return currentAS; }

    public final void setAS(ArmorStand newAS) {
        currentAS = newAS;
    }

    public final void setMaxEntities(int entities) {
        maxEntities = entities;
    }

    /**
     * Sets the current lifetime of the projectile in milliseconds.
     * Use a value of 0 to set an infinite lifetime. This is not recommended.
     */
    public final void setLifetime(long lifetime) {
        if (lifetime == 0)
            expireTime = 0;
        else
            expireTime = System.currentTimeMillis() + lifetime;
    }

    /**
     * Sets the size of the projectile's hitbox.
     */
    public final void setSize(double width, double height, double length) {
        double x = location.getX(),
                y = location.getY(),
                z = location.getZ(),
                wR = width / 2,
                hR = height / 2,
                lR = length / 2;
        hitbox.resize(x - wR, y - hR, z - lR,
                x + wR, y + hR, z + lR);
    }

    public final void setSize(Vector newSize) {
        setSize(newSize.getX(), newSize.getY(), newSize.getZ());
    }

    public final int getInterpolationScale() {
        return interpolationScale;
    }

    /**
     * Sets how many pieces location updates are divided into. Useful for
     * improving precision collision detection.
     *
     * @param newScale
     */
    public final void setInterpolationScale(int newScale) {
        interpolationScale = Math.max(1, newScale);
    }

    public final void setVelocity(Vector newVelocity) {
        velocity = newVelocity;
    }

    /**
     * Lazy implementation that tries to ensure proper collision for projectiles that move more than
     * their width in a single tick.
     */
    public final int autoInterpolationScale() {
        double length = velocity.length();
        double smallest = Math.min(hitbox.getWidthX(), Math.min(hitbox.getWidthZ(), hitbox.getHeight()));
        return (int) Math.floor(length / smallest);
    }

    /**
     * Runs each update tick, independent of interpolation.
     */
    public void onTick() {}

    /**
     *
     * Runs each update and each interpolated update (i.e. the "extra ticks"
     * that occur when {@link #interpolationScale} > 1).
     */
    public void interpolatedTick() {}

    /**
     * Called when the projectile is destroyed, regardless of means.
     */
    public void onDeath() {}

    /**
     * The code to run if {@link #shouldHitEntity(LivingEntity)} returns true.
     * By default, does nothing.
     *
     * @return True if the projectile should be destroyed after this code is run,
     * false otherwise.
     */
    public void onHitEntity(LivingEntity entity) {}

    /**
     * Determines whether or not to hit an entity.
     */
    public boolean shouldHitEntity(LivingEntity entity) {
        return !entity.equals(owner);
    }

    /**
     * The code to run if {@link #shouldHitBlock(Block)} returns true.
     * By default, does nothing.
     *
     * @return True if the projectile should be destroyed after this code is run,
     * false otherwise.
     */
    public boolean onHitBlock(Block block) {
        return true;
    }

    /**
     * Determines whether or not to hit a block.
     */
    public boolean shouldHitBlock(Block block) {
        return block.getType().isSolid();
    }

    protected void init() {
        new BukkitRunnable() {
            private int locationsIndex = 0;

            public void run() {
                if (!active || (expireTime != 0 && System.currentTimeMillis() >= expireTime))
                    cancel();

                // standard tick
                onTick();
                for (int i = 0; i < interpolationScale; i++) {
                    // if it connects with a block it perceives as solid,
                    // reaches its maximum lifetime, travels its maximum range,
                    // or has been destroyed externally, cancel the thread
                    Block block = location.getBlock();
                    if ((shouldHitBlock(block) && onHitBlock(block)) || (locationsIndex >= (locations.size()-1)))
                        cancel();

                    // interpolated tick
                    interpolatedTick();
                    for (LivingEntity entity : location.getWorld().getLivingEntities()) {
                        if (!hitbox.overlaps(entity.getBoundingBox()) || hitEntities.contains(entity)
                                || !shouldHitEntity(entity))
                            continue;

                        onHitEntity(entity);
                        hitEntities.add(entity);

                        if(hitEntities.size() >= maxEntities)
                            cancel();
                    }

                    Location prev = location.clone();

                    if(!(locationsIndex >= (locations.size()-1)))
                        location = locations.get(locationsIndex);

                    velocity = new Vector(location.getX() - prev.getX(),location.getY() - prev.getY(),
                            location.getZ() - prev.getZ());
                    hitbox.shift(velocity);

                    locationsIndex++;
                }
            }

            public void cancel() {
                active = false;
                onDeath();
                ProjectileRegistry.removeProjectile(Projectile.this);
                super.cancel();
            }
        }.runTaskTimer(RPGCore.getInstance(), 0, 1);
    }

}