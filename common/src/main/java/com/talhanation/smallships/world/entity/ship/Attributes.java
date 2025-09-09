package com.talhanation.smallships.world.entity.ship;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueOutput;

public class Attributes {
    public float maxHealth;
    public float maxSpeed;
    public float maxReverseSpeed;
    public float maxRotationSpeed;
    public float acceleration;
    public float rotationAcceleration;
    public float friction;

    public void addSaveData(CompoundTag tag) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putFloat("maxHealth", this.maxHealth);
        compoundtag.putFloat("maxSpeed", this.maxSpeed);
        compoundtag.putFloat("maxReverseSpeed", this.maxReverseSpeed);
        compoundtag.putFloat("acceleration", this.acceleration);
        compoundtag.putFloat("rotationAcceleration", this.rotationAcceleration);
        compoundtag.putFloat("maxRotationSpeed", this.maxRotationSpeed);
        compoundtag.putFloat("friction", this.friction);
        tag.put("Attributes", compoundtag);
    }

    public CompoundTag getSaveData() {
        CompoundTag compoundtag = new CompoundTag();
        this.addSaveData(compoundtag);
        return compoundtag;
    }

    public void loadSaveData(CompoundTag tag) {
        /*this.maxHealth = 200F;
        this.maxSpeed = 40F;
        this.maxReverseSpeed = 5F;
        this.acceleration = 2F;
        this.rotationAcceleration = 5F;
        this.maxRotationSpeed = 50F;
        this.friction = 3F;*/
        // Loads the savedata from the tag
        if (tag.contains("Attributes")) {
            tag.getCompound("Attributes").ifPresent(compoundTag -> {
                this.maxHealth = compoundTag.getFloat("maxHealth").get();
                this.maxSpeed = compoundTag.getFloat("maxSpeed").get();
                this.maxReverseSpeed = compoundTag.getFloat("maxReverseSpeed").get();
                this.acceleration = compoundTag.getFloat("acceleration").get();
                this.rotationAcceleration = compoundTag.getFloat("rotationAcceleration").get();
                this.maxRotationSpeed = compoundTag.getFloat("maxRotationSpeed").get();
                this.friction = compoundTag.getFloat("friction").get();
            });

        }
    }

    public void loadSaveData(CompoundTag tag, Ship shipEntity) { // Workaround because defineSynchedData doesn't work properly (or as I would like it to work: Use the provided 2nd argument as a "default" variable)
        // Checks for attributes, first time goes to else
        if (tag.contains("Attributes")) {
            this.loadSaveData(tag);
        } else {
            // This one create the default attributes, creates a compoundtag and runs addSaveData
            // TLDR; saves the data to a compoundtag and returns it, saved under "Attributes"
            this.loadSaveData(shipEntity.createDefaultAttributes());
        }


    }

    @Override
    public String toString() {
        return "Attributes{" +
                "maxHealth=" + maxHealth +
                ", maxSpeed=" + maxSpeed +
                ", maxReverseSpeed=" + maxReverseSpeed +
                ", acceleration=" + acceleration +
                ", maxRotationSpeed=" + maxRotationSpeed +
                ", friction=" + friction +
                '}';
    }
}
