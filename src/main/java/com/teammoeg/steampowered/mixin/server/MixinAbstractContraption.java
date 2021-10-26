/*
 * Copyright (c) 2021 TeamMoeg
 *
 * This file is part of Steam Powered.
 *
 * Steam Powered is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Steam Powered is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Steam Powered. If not, see <https://www.gnu.org/licenses/>.
 */

package com.teammoeg.steampowered.mixin.server;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.mounted.MountedContraption;
import com.teammoeg.steampowered.SPConfig;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContraptionEntity.class)
public abstract class MixinAbstractContraption {
    boolean shoulddisb = false;
    @Shadow(remap = false)
    protected Contraption contraption;

    /**
     * @author khjxiaogu
     * @reason force reset contraptions for mod propose
     */
    @Inject(at = @At("TAIL"), method = "writeAdditional", remap = false)
    protected void writeAdditional(CompoundNBT compound, boolean spawnPacket, CallbackInfo cbi) {
        if (!SPConfig.SERVER.allowUnverifiedContraption.get())
            compound.putInt("spinst", 2);
    }

    @Shadow(remap = false)
    public abstract void disassemble();

    /**
     * @author khjxiaogu
     * @reason force reset contraptions for mod propose
     */
    @Inject(at = @At("TAIL"), method = "readAdditional", remap = false)
    protected void readAdditional(CompoundNBT compound, boolean spawnPacket, CallbackInfo cbi) {
        if (!SPConfig.SERVER.allowUnverifiedContraption.get())
            if (compound.getInt("spinst") != 2) {
                shoulddisb = true;
            }
    }

    /**
     * @author khjxiaogu
     * @reason force reset contraptions for mod propose
     */
    @Inject(at = @At("TAIL"), method = "tick", remap = true)
    protected void tick(CallbackInfo cbi) {
        if (shoulddisb || ((!SPConfig.SERVER.allowCartAssembler.get()) && contraption instanceof MountedContraption))
            this.disassemble();
    }
}