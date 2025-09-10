package com.talhanation.smallships.forge.events;

import com.talhanation.smallships.world.entity.ship.Ship;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

// This should have been in common/CommonGameBus, instead it is here cluttering the structure of the project
public class PassengerEvents {
    @SubscribeEvent
    public void onPlayerInteractWithPassenger(PlayerInteractEvent.EntityInteract event){
        Player player = event.getEntity();
        Entity entity = event.getTarget();

        if(!player.isCrouching()
                && entity.isPassenger()
                && !(entity instanceof Player)
                && !(entity.getEncodeId() != null && entity.getEncodeId().contains("captain"))
                && entity.getVehicle() != null
                && entity.getVehicle() instanceof Ship){

            entity.stopRiding();
            event.setCancellationResult(InteractionResult.SUCCESS);
            // TODO: This errors; find the replacment 
            event.setCanceled(true);
        }

        if(player.isPassenger() && player.getVehicle() != null && player.getVehicle() instanceof Ship ship){
            if(ship.canAddPassenger(entity) && !(entity instanceof Player)){
                entity.startRiding(ship);
                event.setCancellationResult(InteractionResult.SUCCESS);
                // TODO: This errors; find the replacment 
                event.setCanceled(true);
            }
        }
    }
}
