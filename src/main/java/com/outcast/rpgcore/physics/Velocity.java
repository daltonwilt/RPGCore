package com.outcast.rpgcore.physics;

import com.outcast.rpgcore.RPGCore;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class Velocity {

    public static void entityToEntityVelocity(LivingEntity owner, LivingEntity entity, double horizontalDividerX,
                                              double horizontalDividerZ, double startYPosition, double verticalY,
                                              double multiplier) {
        double xDir = horizontalDividerX == 0.0 ? 0.0 :
                (owner.getLocation().getX() - entity.getLocation().getX()) / horizontalDividerX;
        double yDir = verticalY == 0.0 ? 0.0 : verticalY;
        double zDir = horizontalDividerZ == 0.0 ? 0.0 :
                (owner.getLocation().getZ() - entity.getLocation().getZ()) / horizontalDividerZ;
        Vector vec = (new Vector(xDir, yDir, zDir)).multiply(multiplier);
        if(startYPosition != 0.0)
            vec.setY(startYPosition);

        entity.setVelocity(vec);
        RPGCore.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(RPGCore.getInstance(), new Runnable() {
            public void run() {
                owner.setFallDistance(8.0F);
            }
        },  2L);
    }

    public static void locationVelocity(LivingEntity owner, LivingEntity entity, Location targetLocation,
                                        Location eventLocation, double horizontalPower, double verticalPower) {
        double xDir = targetLocation.getX() - eventLocation.getX();
        double zDir = targetLocation.getZ() - eventLocation.getZ();
        double magnitude = Math.sqrt(xDir * xDir + zDir * zDir);
        final double x = xDir / magnitude * horizontalPower;
        final double z = zDir / magnitude * horizontalPower;

        entity.setVelocity(new Vector(x, verticalPower, z));
        RPGCore.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(RPGCore.getInstance(), new Runnable() {
            public void run() {
                owner.setFallDistance(8.0F);
            }
        },  2L);
    }

}