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

public interface Shieldable extends Ability {

    ShieldPosition getShieldPosition(int index);
    byte getMaxShieldsPerSide();

    default void tickShieldShip() {
    }

    default void readShieldShipSaveData(ValueInput valueInput) {
        var shieldItems = new ListTag();

        valueInput.list("Shields", CompoundTag.CODEC).ifPresent(shieldList -> {
            for (CompoundTag compoundTag : shieldList) {
                ItemStack stack = ItemStack.CODEC.parse(self().registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag)
                        .result().orElse(ItemStack.EMPTY);
                if (!stack.isEmpty()) {
                    this.getShields().add(stack);
                    shieldItems.add(compoundTag);

                    /*ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack)
                            .result()
                            .ifPresent(shieldItems::add);*/
                }
            }
        });

        /*
        var shieldItems = valueInput.read("Shields", Codec.list(CompoundTag.CODEC)).get();
        for (CompoundTag compoundTag : shieldItems) {
            DataResult<ItemStack> result = ItemStack.CODEC.parse(
                    self().registryAccess().createSerializationContext(NbtOps.INSTANCE),
                    compoundTag
            );
            result.result().ifPresent(itemStack -> this.getShields().add(itemStack));
        }
         */

        var shieldData = new CompoundTag();
        shieldData.put("Shields", shieldItems);
        self().setData(Ship.SHIELD_DATA, shieldData);
    }

    default void addShieldShipSaveData(ValueOutput valueOutput) {
        /*
        ListTag shieldItems = new ListTag();
        for (int i = 0; i < this.getShields().size(); ++i) {
            ItemStack itemStack = this.getShields().get(i);
            if (!itemStack.isEmpty()) {
                CompoundTag inTag = new CompoundTag();
                inTag.putByte("Shields", (byte) i);
                //Tag itemTag = itemStack.save(self().registryAccess(), inTag);
                //shieldItems.add(itemTag);
            }
        }
        tag.put("Shields", shieldItems);
        valueOutput.store("Shields", CompoundTag.CODEC, shieldItems);
         */
    }

    default List<ItemStack> getShields() {
        return Collections.emptyList();
        /*
        CompoundTag tag = self().getData(Ship.SHIELD_DATA);
        ListTag shieldItems = tag.getList("Shields", 10);

        List<ItemStack> shields = new ArrayList<>() {
            private <T> T updateDataAndReturn(T out) {
                ListTag shieldItems = new ListTag();
                for (int i = 0; i < this.size(); ++i) {
                    ItemStack itemStack = this.get(i);
                    CompoundTag inTag = new CompoundTag();
                    //inTag.putByte("Shields", (byte) i);

                    //var test = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, self().registryAccess());
                    //test.store(ItemStack.MAP_CODEC, )

                    //Tag itemTag = itemStack.save(self().registryAccess(), inTag);
                    shieldItems.add(itemStack);
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

        for (int i = 0; i < shieldItems.size(); ++i) {
            CompoundTag shieldItem = shieldItems.getCompound(i);
            ItemStack itemStack = ItemStack.parse(self().registryAccess(), shieldItem).orElse(ItemStack.EMPTY);
            if (!itemStack.isEmpty()) shields.add(itemStack);
        }
        return shields;
         */

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
