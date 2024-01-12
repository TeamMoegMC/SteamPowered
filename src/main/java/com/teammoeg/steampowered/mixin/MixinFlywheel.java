package com.teammoeg.steampowered.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.contraptions.KineticNetwork;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelTileEntity;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineTileEntity;
import com.teammoeg.steampowered.content.engine.SteamEngineTileEntity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

@Mixin(FlywheelTileEntity.class)
public abstract class MixinFlywheel extends GeneratingKineticTileEntity{
	public MixinFlywheel(TileEntityType<?> typeIn) {
		super(typeIn);
	}
	int errortick=0;
	void seemAsError() {
		if(this.getGeneratedSpeed()!=0)
			errortick++;
		if(errortick>=10) {
			errortick=0;
			this.setRotation(0,0);
		}
	}
	@Shadow(remap=false)
	public abstract void setRotation(float speed, float capacity);
	@Inject(at=@At("HEAD"),method="tick")
	public void sp$tick(CallbackInfo cbi) {
		Direction at=FlywheelBlock.getConnection(getBlockState());
		if(at!=null) {
			BlockPos eng=this.getBlockPos().relative(at,2);
			Block b=this.getWorld().getBlockState(eng).getBlock();
			if(!(b instanceof EngineBlock)) {
				FlywheelBlock.setConnection(getWorld(),getBlockPos(),getBlockState(),null);
				seemAsError();
			}else {
				TileEntity te=this.getWorld().getBlockEntity(eng);
				if(te instanceof EngineTileEntity) {
					if(te instanceof SteamEngineTileEntity) {
						SteamEngineTileEntity ete=(SteamEngineTileEntity) te;
						if(ete.getFlywheel()!=this.getBlockState().getBlock())
							seemAsError();
					}
				}else seemAsError();
			}
		}else seemAsError();
	}
	/*@Override
	public void applyNewSpeed(float prevSpeed, float speed) {

		// Speed changed to 0
		if (speed == 0) {
			if (hasSource()) {
				notifyStressCapacityChange(0);
				getOrCreateNetwork().updateStressFor(this, calculateStressApplied());
				return;
			}
			detachKinetics();
			setSpeed(0);
			setNetwork(null);
			return;
		}

		// Now turning - create a new Network
		if (prevSpeed == 0) {
			setSpeed(speed);
			setNetwork(createNetworkId());
			attachKinetics();
			return;
		}

		// Change speed when overpowered by other generator
		if (hasSource()) {

			// Staying below Overpowered speed
			if (Math.abs(prevSpeed) >= Math.abs(speed)) {
				if (Math.signum(prevSpeed) != Math.signum(speed))
					level.destroyBlock(worldPosition, true);
				return;
			}

			// Faster than attached network -> become the new source
			detachKinetics();
			setSpeed(speed);
			source = null;
			setNetwork(createNetworkId());
			attachKinetics();
			return;
		}

		// Reapply source
		detachKinetics();
		setSpeed(speed);
		attachKinetics();
	}*/
	@Override
	public int getFlickerScore(){
		return 0; 
	}
}
