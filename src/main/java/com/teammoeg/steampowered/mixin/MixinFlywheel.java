package com.teammoeg.steampowered.mixin;

import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelTileEntity;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineTileEntity;
import com.teammoeg.steampowered.content.engine.SteamEngineTileEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

@Mixin(FlywheelTileEntity.class)
public abstract class MixinFlywheel extends GeneratingKineticTileEntity{
	public MixinFlywheel(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	@Shadow(remap=false)
	public abstract void setRotation(float speed, float capacity);
	@Inject(at=@At("HEAD"),method="tick",remap = false)
	public void sp$tick(CallbackInfo cbi) {
		Direction at=FlywheelBlock.getConnection(getBlockState());
		if(at!=null) {
			BlockPos eng=this.getBlockPos().relative(at,2);
			Block b=this.getLevel().getBlockState(eng).getBlock();
			if(!(b instanceof EngineBlock)) {
				FlywheelBlock.setConnection(getLevel(),getBlockPos(),getBlockState(),null);
				this.setRotation(0,0);
			}else {
				BlockEntity te=this.getLevel().getBlockEntity(eng);
				if(te instanceof EngineTileEntity) {
					if(te instanceof SteamEngineTileEntity) {
						SteamEngineTileEntity ete=(SteamEngineTileEntity) te;
						if(ete.getFlywheel()!=this.getBlockState().getBlock())
							this.setRotation(0,0);
					}
				}else this.setRotation(0,0);
			}
		}else this.setRotation(0,0);
	}
}
