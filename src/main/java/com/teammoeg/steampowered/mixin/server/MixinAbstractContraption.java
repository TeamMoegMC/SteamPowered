package com.teammoeg.steampowered.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.teammoeg.steampowered.SPConfig;

import net.minecraft.nbt.CompoundNBT;

@Mixin(AbstractContraptionEntity.class)
public abstract class MixinAbstractContraption {
	boolean shoulddisb=false;
	/**
	 * @author khjxiaogu
	 * @reason force reset contraptions for mod propose
	 * */
	@Inject(at = @At("TAIL"), method = "writeAdditional",remap=false)
	protected void writeAdditional(CompoundNBT compound, boolean spawnPacket,CallbackInfo cbi) {
		if(!SPConfig.SERVER.allowUnverifiedContraption.get())
			compound.putInt("spinst",2);
	}
	@Shadow(remap=false)
	public abstract void disassemble();
	/**
	 * @author khjxiaogu
	 * @reason force reset contraptions for mod propose
	 * */
	@Inject(at = @At("TAIL"), method = "readAdditional",remap=false)
	protected void readAdditional(CompoundNBT compound, boolean spawnPacket,CallbackInfo cbi) {
		if(!SPConfig.SERVER.allowUnverifiedContraption.get())
			if(compound.getInt("spinst")!=2) {
				shoulddisb=true;
			}
	}
	/**
	 * @author khjxiaogu
	 * @reason force reset contraptions for mod propose
	 * */
	@Inject(at = @At("TAIL"), method = "tick",remap=true)
	protected void tick(CallbackInfo cbi) {
		if(shoulddisb)
			this.disassemble();
	}
}
