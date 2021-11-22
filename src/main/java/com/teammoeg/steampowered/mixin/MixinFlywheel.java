package com.teammoeg.steampowered.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelTileEntity;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

@Mixin(FlywheelTileEntity.class)
public abstract class MixinFlywheel extends GeneratingKineticTileEntity{
	public MixinFlywheel(TileEntityType<?> typeIn) {
		super(typeIn);
	}
	@Shadow(remap=false)
	public abstract void setRotation(float speed, float capacity);
	@Inject(at=@At("HEAD"),method="tick")
	public void sp$tick(CallbackInfo cbi) {
		Direction at=FlywheelBlock.getConnection(getBlockState());
		if(at!=null) {
			if(!(this.getWorld().getBlockState(this.getBlockPos().relative(at,2)).getBlock() instanceof EngineBlock)) {
				FlywheelBlock.setConnection(getWorld(),getBlockPos(),getBlockState(),null);
				this.setRotation(0,0);
			}
		}else this.setRotation(0,0);
	}
}
