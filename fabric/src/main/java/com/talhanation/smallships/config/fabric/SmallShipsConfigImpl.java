package com.talhanation.smallships.config.fabric;

import com.talhanation.smallships.SmallShipsMod;
import com.talhanation.smallships.config.SmallShipsConfig;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.minecraftforge.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;


@SuppressWarnings("removal")
public class SmallShipsConfigImpl {
    public SmallShipsConfigImpl() {
        ModConfigEvents.loading(SmallShipsMod.MOD_ID).register(config -> {
            boolean updated = SmallShipsConfig.updateConfig(new SmallShipsConfig.ModConfigWrapper(config.getType().toString(), config.getFullPath(), config.getFileName(), config.getLoadedConfig().config()));
            if (updated) config.getFileName();
        });
    }

    public static void registerConfigs(String modId, SmallShipsConfig.ModConfigWrapper.Type type, IConfigSpec<?> spec) {
        ConfigRegistry.INSTANCE.register(modId, ModConfig.Type.valueOf(type.toString()), spec);
    }
}
