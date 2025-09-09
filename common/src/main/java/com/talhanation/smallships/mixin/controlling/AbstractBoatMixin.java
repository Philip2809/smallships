package com.talhanation.smallships.mixin.controlling;

import com.talhanation.smallships.world.entity.ship.Ship;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
public abstract class AbstractBoatMixin {
    @Shadow protected abstract void controlBoat();

    // Inject right BEFORE the boat's own controlBoat() call in tick()
    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/AbstractBoat;controlBoat()V",
            shift = At.Shift.BEFORE))
    private void tickClientAndServerControlBoat(CallbackInfo ci) {
        if (((AbstractBoat)(Object)this) instanceof Ship) {
            // call the boat control logic for Ship instances too
            this.controlBoat();
        }
    }

    // Redirect the original call so non-Ship boats still call controlBoat() normally
    @Redirect(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/AbstractBoat;controlBoat()V"))
    private void tickCancelControlBoatHereForShip(AbstractBoat instance) {
        // use the redirected 'instance' instead of 'this'
        if (!(instance instanceof Ship)) {
            this.controlBoat();
        }
        // if it is a Ship we skip the original call because our inject already handled it
    }
}
