package com.teammoeg.steampowered.mixin;

import com.simibubi.create.content.contraptions.components.flywheel.FlywheelTileEntity;
import com.simibubi.create.foundation.gui.widgets.InterpolatedChasingValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FlywheelTileEntity.class)
public interface FlywheelTileEntityAccess {
    @Accessor(remap = false)
    InterpolatedChasingValue getVisualSpeed();

    @Accessor(remap = false)
    float getAngle();
}
