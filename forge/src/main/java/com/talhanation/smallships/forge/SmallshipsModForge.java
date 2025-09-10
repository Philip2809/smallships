package com.talhanation.smallships.forge;

import com.electronwill.nightconfig.toml.TomlFormat;
import com.talhanation.smallships.SmallShipsMod;
import com.talhanation.smallships.forge.events.PassengerEvents;
import com.talhanation.smallships.world.entity.forge.ModEntityTypesImpl;
import com.talhanation.smallships.world.inventory.forge.ModMenuTypesImpl;
import com.talhanation.smallships.world.item.forge.ModItemsImpl;
import com.talhanation.smallships.world.particles.forge.ModParticleTypesImpl;
import com.talhanation.smallships.world.sound.forge.ModSoundTypesImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.nio.file.Path;
import java.util.Arrays;

@Mod(SmallShipsMod.MOD_ID)
public class SmallshipsModForge {
    public static final boolean hasCustomItemGroup = TomlFormat.instance().createParser().parse(Path.of("config", "smallships-client.toml"), (file, configFormat) -> false).getOrElse(Arrays.asList("General", "smallshipsItemGroupEnable"), () -> false); //Forge doesn't do early config initialization. Will have to parse the config ourselves.

    @SuppressWarnings({"InstantiationOfUtilityClass", "removal"})
    public SmallshipsModForge() {
        new SmallShipsMod();

        var modBusGroup = FMLJavaModLoadingContext.get().getModBusGroup();

        ModItemsImpl.ITEMS.register(modBusGroup);
        if (hasCustomItemGroup) ModItemsImpl.CREATIVE_MODE_TABS.register(modBusGroup);
        ModEntityTypesImpl.ENTITY_TYPES.register(modBusGroup);
        ModMenuTypesImpl.MENU_TYPES.register(modBusGroup);
        ModSoundTypesImpl.SOUND_EVENTS.register(modBusGroup);
        ModParticleTypesImpl.PARTICLE_TYPES.register(modBusGroup);

        PlayerInteractEvent.EntityInteract.BUS.addListener(PassengerEvents::onPlayerInteractWithPassenger);

    }
}
