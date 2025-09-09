package com.talhanation.smallships.world.entity.ship.abilities;

import com.mojang.serialization.DataResult;
import com.talhanation.smallships.world.entity.ship.Ship;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface Bannerable extends Ability {
    BannerPosition getBannerPosition();//ZP for different angles usefully for wind feature

    default void tickBannerShip() {
        if (!self().getData(Ship.BANNER).isEmpty()) {
            self().prevBannerWaveAngle = self().bannerWaveAngle;
            self().bannerWaveAngle = (float) Math.sin(this.getBannerWaveSpeed() * (float) self().tickCount) * this.getBannerWaveFactor();
        }
    }

    default void readBannerShipSaveData(ValueInput valueInput) {
        valueInput.read("Banner", CompoundTag.CODEC).ifPresent(bannerCompound -> {
            DataResult<ItemStack> result = ItemStack.CODEC.parse(
                    self().registryAccess().createSerializationContext(NbtOps.INSTANCE),
                    bannerCompound
            );

            result.result().ifPresentOrElse(
                    itemStack -> self().setData(Ship.BANNER, itemStack),
                    () -> self().setData(Ship.BANNER, ItemStack.EMPTY)
            );
        });
    }

    default void addBannerShipSaveData(ValueOutput valueOutput) {
        if (!self().getData(Ship.BANNER).isEmpty()) {
            var banner = self().getData(Ship.BANNER);
            ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, banner).result().ifPresent(bannerTag ->{
                valueOutput.store("Banner", CompoundTag.CODEC, (CompoundTag) bannerTag);
            });
        }
    }

    default boolean interactBanner(Player player, InteractionHand interactionHand) {
        ItemStack item = player.getItemInHand(interactionHand);
        ItemStack shipBanner = self().getData(Ship.BANNER);
        shipBanner.setCount(1);
        if (self().level() instanceof ServerLevel serverLevel) {
            if (item.getItem() instanceof BannerItem) {
                self().spawnAtLocation(serverLevel, shipBanner, 4);
                var copy = item.copy();
                copy.setCount(1);
                self().setData(Ship.BANNER, copy);
                if (!player.isCreative()) item.shrink(1);
                serverLevel.playSound(player, self().getX(), self().getY() + 4 , self().getZ(), SoundEvents.WOOL_HIT, self().getSoundSource(), 15.0F, 1.0F);
                return true;
            } else if (item.getItem() instanceof ShearsItem && !shipBanner.isEmpty()) {
                self().spawnAtLocation(serverLevel, shipBanner,4);
                self().setData(Ship.BANNER, ItemStack.EMPTY);
                serverLevel.playSound(player, self().getX(), self().getY() + 4 , self().getZ(), SoundEvents.WOOL_HIT, self().getSoundSource(), 15.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    default float getBannerWaveFactor() {
        return self().level().isRaining() ? 4.5F : 3.0F;
    }

    default float getBannerWaveSpeed() {
        return self().level().isRaining() ? 0.55F : 0.25F;
    }

    default float getBannerWaveAngle(float partialTicks) {
        return Mth.lerp(partialTicks, self().prevBannerWaveAngle, self().bannerWaveAngle);
    }

    @SuppressWarnings("ClassCanBeRecord")
    class BannerPosition {
        public final float yp;
        public final float zp;

        public final double x;
        public final double y;
        public final double z;

        public BannerPosition(float yp, float zp, double x, double y, double z) {
            this.yp = yp;
            this.zp = zp;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
