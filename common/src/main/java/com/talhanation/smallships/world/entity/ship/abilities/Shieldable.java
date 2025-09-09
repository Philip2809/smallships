package com.talhanation.smallships.world.entity.ship.abilities;

import com.mojang.datafixers.types.templates.Tag;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.talhanation.smallships.config.SmallShipsConfig;
import com.talhanation.smallships.world.entity.ship.Ship;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO: The data read/write should need a rewrite
public interface Shieldable extends Ability {

    ShieldPosition getShieldPosition(int index);
    byte getMaxShieldsPerSide();

    default void tickShieldShip() {
    }

    default void readShieldShipSaveData(ValueInput valueInput) {
        valueInput.read("Shields", CompoundTag.CODEC).ifPresent(shields -> {
            self().setData(Ship.SHIELD_DATA, shields);
        });
    }

    default void addShieldShipSaveData(ValueOutput valueOutput) {
        this.getShields();
        CompoundTag tag = self().getData(Ship.SHIELD_DATA);
        valueOutput.store("Shields", CompoundTag.CODEC, tag);
    }

    default List<ItemStack> getShields() {
        List<ItemStack> shields = new ArrayList<>() {
            private <T> T updateDataAndReturn(T out) {
                ListTag shieldItems = new ListTag();
                for (int i = 0; i < this.size(); ++i) {
                    ItemStack itemStack = this.get(i);
                    ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, itemStack).result().ifPresent(shieldItems::add);
                }
                CompoundTag newTag = new CompoundTag();
                newTag.put("Shields", shieldItems);
                self().setData(Ship.SHIELD_DATA, newTag);
                return out;
            }

            @Override
            public boolean add(ItemStack newItemStack) {
                return updateDataAndReturn(super.add(newItemStack));
            }

            @Override
            public ItemStack removeLast() {
                return updateDataAndReturn(super.removeLast());
            }
        };

        CompoundTag tag = self().getData(Ship.SHIELD_DATA);
        tag.getList("Shields").ifPresent(shieldItems -> {
            for (int i = 0; i < shieldItems.size(); ++i) {
                shieldItems.getCompound(i).ifPresent(shieldItem -> {
                    var itemStack = ItemStack.CODEC.parse(self().registryAccess().createSerializationContext(NbtOps.INSTANCE), shieldItem).result().get();
                    if (!itemStack.isEmpty()) shields.add(itemStack);
                });
            }
        });

        return shields;

    }

    default float getDamageModifier() {
        return (float) (1.0F - getShields().size() * SmallShipsConfig.Common.shipGeneralShieldDamageReduction.get()/100F);
    }

   default boolean interactShield(Player player, InteractionHand interactionHand) {
       ItemStack itemStack = player.getItemInHand(interactionHand);
       int shieldCount = this.getShields().size();
       if (itemStack.is(Items.SHIELD)) {
           if (shieldCount >= this.getMaxShieldsPerSide() * 2) {
               return false;
           } else {
               this.getShields().add(itemStack.copy());
               if (!player.isCreative()) itemStack.shrink(1);
               self().level().playSound(player, self().getX(), self().getY() + 4, self().getZ(), SoundEvents.WOOD_HIT, self().getSoundSource(), 15.0F, 1.5F);
               return true;
           }
       } else if (itemStack.getItem() instanceof AxeItem && shieldCount > 0) {
           ItemStack removedShield = this.getShields().removeLast();
           if (self().level() instanceof ServerLevel serverLevel) self().spawnAtLocation(serverLevel, removedShield, 2);
           
           self().level().playSound(player, self().getX(), self().getY() + 4, self().getZ(), SoundEvents.WOOD_HIT, self().getSoundSource(), 15.0F, 1.0F);
           return true;
       }
       return false;
   }

    @SuppressWarnings("ClassCanBeRecord")
    class ShieldPosition {
        public final double x;
        public final double y;
        public final double z;
        public final boolean isRightSided;

        public ShieldPosition(double x, double y, double z, boolean isRightSided) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.isRightSided = isRightSided;
        }
    }
}
