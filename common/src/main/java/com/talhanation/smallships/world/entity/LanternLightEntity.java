package com.talhanation.smallships.world.entity;

import com.talhanation.smallships.world.entity.cannon.GroundCannonEntity;
import com.talhanation.smallships.world.entity.ship.Ship;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LanternLightEntity extends Entity {
    public static final String ID = "lantern_light";
    public LanternLightEntity(EntityType<? extends LanternLightEntity> type, Level level) {
        super(type, level);
    }

    public LanternLightEntity(Level level, Vec3 pos) {
        super(ModEntityTypes.LANTERN_LIGHT, level);
        //this.setPos(pos);
    }

    public static LanternLightEntity factory(EntityType<? extends LanternLightEntity> entityType, Level level) {
        return new LanternLightEntity(entityType, level);
    }

    /*@Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(0.85F, 0.75F); // width, height
    }*/

    public Ship onShip;

    @Override
    public void tick() {
        super.tick();

        Entity vehicle = this.onShip; // optional if using setVehicle
        if (vehicle instanceof Ship ship) {
            Vec3 shipPos = ship.position();

            // Define offsets per lantern type / side
            double offsetX = 0.25; // right-left
            double offsetY = 2.5; // height above deck // Correct :D
            double offsetZ = -3.1; // forward-back

            // Rotate offset with ship yaw
            float yawRad = (float) Math.toRadians(ship.getYRot());
            double rotatedX = offsetX * Math.cos(yawRad) - offsetZ * Math.sin(yawRad);
            double rotatedZ = offsetX * Math.sin(yawRad) + offsetZ * Math.cos(yawRad);

            this.setPos(shipPos.x + rotatedX, shipPos.y + offsetY, shipPos.z + rotatedZ);
            this.setYRot(ship.getYRot());
        } else {
            //this.discard(); // remove lantern if ship despawns
        }
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {}

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {}

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        return false;
    }
}

